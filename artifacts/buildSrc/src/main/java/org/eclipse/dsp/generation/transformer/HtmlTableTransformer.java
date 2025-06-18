/*
 *  Copyright (c) 2024 Metaform Systems, Inc.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Metaform Systems, Inc. - initial API and implementation
 *
 */

package org.eclipse.dsp.generation.transformer;

import org.eclipse.dsp.generation.jsom.ElementDefinition;
import org.eclipse.dsp.generation.jsom.SchemaProperty;
import org.eclipse.dsp.generation.jsom.SchemaPropertyReference;
import org.eclipse.dsp.generation.jsom.SchemaType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;
import static org.eclipse.dsp.generation.jsom.ElementDefinition.Type.CONSTANT;
import static org.eclipse.dsp.generation.jsom.JsonTypes.OBJECT;

/**
 * Transforms a {@link SchemaType} into an HTML table representation.
 */
public class HtmlTableTransformer implements SchemaTypeTransformer<String> {

    @Override
    @NotNull
    public String transform(SchemaType schemaType) {
        var builder = new StringBuilder(CSS).append("<table class=\"message-table\">");
        builder.append(format("<tr><td class=\"message-class\" colspan=\"4\" id=\"%s-table\">%s</td></tr>", schemaType.getName(), schemaType.getName()));
        transformProperties(schemaType.getTransitiveRequiredProperties(), true, builder);
        transformProperties(schemaType.getTransitiveOptionalProperties(), false, builder);
        return builder.append("</table>").toString();
    }

    private void transformProperties(Set<SchemaPropertyReference> references, boolean required, StringBuilder builder) {
        if (!references.isEmpty()) {
            references.forEach(propertyReference -> transformProperty(propertyReference, required, builder));
        }
    }

    private void transformProperty(SchemaPropertyReference propertyReference, boolean required, StringBuilder builder) {
        builder.append("<tr>");
        builder.append(format("<td class=\"code\">%s</td>", propertyReference.getName()));
        builder.append("<td>").append(required ? "required" : "optional").append("</td>");
        var resolvedProperty = propertyReference.getResolvedProperty();
        if (resolvedProperty != null) {
            String resolvedTypes = "";
            if (!resolvedProperty.getItemTypes().isEmpty()) {
                resolvedTypes = getArrayTypeName(resolvedProperty);
            } else {
                resolvedTypes = parseResolvedTypes(resolvedProperty);
            }
            builder.append(format("<td>%s</td>", resolvedTypes));
            if (resolvedProperty.getConstantValue() != null) {
                builder.append(format("<td>Value must be <span class=\"code\">%s</span></td>", resolvedProperty.getConstantValue()));
            }  else if (!resolvedProperty.getEnumValues().isEmpty()){
                var values = resolvedProperty.getEnumValues().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                builder.append(format("<td>Must be of the following:<br><span class=\"code\">%s</span></td>",values));
            } else {
                var constants = resolvedProperty.getResolvedTypes().stream()
                        .flatMap(t -> concat(Stream.of(t), t.getResolvedAllOf().stream()))   // search the contains of the current type and any references 'allOf' types
                        .flatMap(t -> t.getContains().stream())
                        .filter(cd -> cd.getType() == CONSTANT)
                        .map(ElementDefinition::getValue)
                        .collect(Collectors.joining("<br> "));
                if (constants.isEmpty()) {
                    builder.append(format("<td>%s</td>", resolvedProperty.getDescription()));
                } else {
                    builder.append(format("<td>Must contain the following:<br><span class=\"code\">%s</span></td>", constants));
                }
            }
        }
        builder.append("</tr>");
    }

    private String parseResolvedTypes(SchemaProperty property) {
        String resolvedTypes = "";
        if (property.getItemTypes().isEmpty()) {
            resolvedTypes = property
                    .getResolvedTypes().stream().map(schemaType -> {
                        if (schemaType.getItemType() != null) {
                            return "array[" + schemaType.getItemType() + "]";
                        }
                        return getTypeName(schemaType);
                    }).collect(joining(", "));
        }
        return resolvedTypes;
    }

    private @NotNull String getArrayTypeName(SchemaProperty resolvedProperty) {
        var itemTypes = resolvedProperty.getItemTypes().stream()
                .flatMap(t -> t.getResolvedTypes().stream())
                .map(e -> {
                    if (e.isJsonBaseType()) {
                        return String.format("%s", getTypeName(e));
                    }
                    return String.format("<a href=#%s-table>%s</a>", getTypeName(e), getTypeName(e));
                })
                .collect(joining(", "));
        if (itemTypes.isEmpty()) {
            itemTypes = resolvedProperty.getResolvedTypes().stream()
                    .filter(e->getTypeName(e)!=null)
                    .map(e->{
                        if (e.isJsonBaseType()) {
                            return String.format("%s", getTypeName(e));
                        }
                        return String.format("<a href=#%s-table>%s</a>", getTypeName(e), getTypeName(e));
                    })
                    .collect(joining(", "));
            if (itemTypes.isEmpty()) {
                return "array";
            }
        }
        return "array[" + itemTypes + "]";
    }

    private String getTypeName(SchemaType schemaType) {
        if (schemaType.isRootDefinition()) {
            // root definition, check to see if it has an allOf, and if not, fallback to the Json base type
            if (!schemaType.getResolvedAllOf().isEmpty()) {
                // ue the allOf types and return the type name if it is a Json base object type; otherwise use the base type name
                return schemaType.getResolvedAllOf().stream()
                        .map(t -> OBJECT.getName().equals(t.getBaseType()) ? t.getName() : t.getBaseType())
                        .collect(joining(", "));
            }
            return schemaType.getBaseType();
        }
        return schemaType.getName();
    }

    private static final String CSS = """
            <style type="text/css">
                .code {
                    font-family: 'Courier New', monospace
                }
            
                .message-class {
                    font-weight: 600;
                    font-size: 1.1rem;
                    background-color: #FAFAFA;
                }
            
                .message-properties-heading {
                    font-weight: 600;
                }
            
                .message-table {
                    border-collapse: collapse;
            
                    tr {
                        vertical-align: top !important;
                    }
            
                    td {
                        padding: 8px;
                        border: 1px solid #DADADA;
                    }
                }
            </style>
            """;
}
