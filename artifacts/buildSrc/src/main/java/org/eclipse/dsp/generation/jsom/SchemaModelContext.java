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

package org.eclipse.dsp.generation.jsom;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.eclipse.dsp.generation.jsom.ElementDefinition.Type.REFERENCE;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ANY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ARRAY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.BOOLEAN;
import static org.eclipse.dsp.generation.jsom.JsonTypes.INTEGER;
import static org.eclipse.dsp.generation.jsom.JsonTypes.NULL;
import static org.eclipse.dsp.generation.jsom.JsonTypes.NUMBER;
import static org.eclipse.dsp.generation.jsom.JsonTypes.OBJECT;
import static org.eclipse.dsp.generation.jsom.JsonTypes.STRING;

/**
 * Contains a Json Schema object model and its types.
 * <p>
 * Types must first be added to the context. After all types have been added, {@link #resolve()} will bind all type references in the model.
 * For example, {@code $ref} entries will be linked to their actual parsed type instances.
 */
public class SchemaModelContext implements SchemaModel {
    private static final String RELATIVE_REFERENCE = "#/definitions/";
    private static final String POINTER_REFERENCE = "#definitions/";
    private static final String POINTER_REFERENCE_SEPARATOR = "#/definitions/";

    private final Map<String, SchemaType> schemaTypes = new HashMap<>();
    private final Map<String, Map<String, SchemaType>> typesByContext = new HashMap<>();

    public SchemaModelContext() {
        // load built-in Json types
        schemaTypes.put(STRING.getName(), STRING);
        schemaTypes.put(NUMBER.getName(), NUMBER);
        schemaTypes.put(INTEGER.getName(), INTEGER);
        schemaTypes.put(OBJECT.getName(), OBJECT);
        schemaTypes.put(ARRAY.getName(), ARRAY);
        schemaTypes.put(BOOLEAN.getName(), BOOLEAN);
        schemaTypes.put(NULL.getName(), NULL);
        schemaTypes.put(ANY.getName(), ANY);
    }

    @Override
    public SchemaType resolveType(String reference, String typeContext) {
        if (reference.startsWith(RELATIVE_REFERENCE)) {
            // resolve references relative to the schema definition, e.g. in "definitions"
            var typesForContext = typesByContext.get(typeContext);
            if (typesForContext != null) {
                return typesForContext.get(reference.substring(RELATIVE_REFERENCE.length()));
            }
            return null;
        } else if (reference.contains(POINTER_REFERENCE)) {
            // reference of type https://<uri>>#definitions/SomeType
            var tokens = reference.split(POINTER_REFERENCE);
            if (tokens.length != 2) {
                throw new UnsupportedOperationException("Unsupported reference type: " + reference);
            }
            return resolveType(RELATIVE_REFERENCE + tokens[1], tokens[0]);
        } else if (reference.contains(POINTER_REFERENCE_SEPARATOR)) {
            var tokens = reference.split(POINTER_REFERENCE_SEPARATOR);
            if (tokens.length != 2) {
                throw new UnsupportedOperationException("Unsupported reference type: " + reference);
            }
            return resolveType(RELATIVE_REFERENCE + tokens[1], tokens[0]);
        } else {
            // resolve a reference to an external schema
            return schemaTypes.get(reference);
        }
    }

    @NotNull
    public Collection<SchemaType> getSchemaTypes() {
        return schemaTypes.values();
    }

    public void addType(SchemaType type) {
        schemaTypes.put(type.getName(), type);
        typesByContext.computeIfAbsent(type.getSchemaUri(), k -> new HashMap<>()).put(type.getName(), type);
    }

    public void resolve() {
        // resolve all schema type references and link them
        var types = schemaTypes.values();
        types.forEach(type -> type.getAllOf().stream()
                .map(ref -> resolveType(ref, type.getSchemaUri()))
                .filter(Objects::nonNull)
                .forEach(type::resolvedAllOfType));

        types.forEach(type -> type.getOneOf().stream()
                .map(ref -> resolveType(ref, type.getSchemaUri()))
                .filter(Objects::nonNull)
                .forEach(type::resolvedOneOfType));

        // resolve all property type references and link them
        types.forEach(type -> type.getProperties()
                .forEach(property -> property.getTypes().stream()
                        .map((ref -> resolveType(ref, type.getSchemaUri())))
                        .filter(Objects::nonNull)
                        .forEach(property::resolvedType)));

        // resolve required properties that do not reference properties explicitly defined on the type, e.g.
        // required properties from types referenced in `allOf`
        types.forEach(type -> type.getRequiredProperties()
                .stream()
                .filter(ref -> ref.getResolvedProperty() == null)
                .forEach(ref -> {
                    // check in allOf
                    var resolved = type.getResolvedAllOf().stream()
                            .flatMap(t -> t.getProperties().stream()
                                    .map(p -> p.getName().equals(ref.getName()) ? p : null)
                                    .filter(Objects::nonNull)).findFirst().orElse(null);

                    ref.resolvedProperty(resolved);

                    resolved = type.getResolvedOneOf().stream()
                            .flatMap(t -> t.getProperties().stream()
                                    .map(p -> p.getName().equals(ref.getName()) ? p : null)
                                    .filter(Objects::nonNull)).findFirst().orElse(null);

                    ref.resolvedProperty(resolved);
                }));


        // resolve contains references
        types.forEach(type -> type.getContains().stream().filter(cd -> cd.getType() == REFERENCE)
                .forEach(cd -> {
                    var resolved = resolveType(cd.getValue(), type.getSchemaUri());
                    if (resolved != null) {
                        cd.resolvedType(resolved);
                    }
                }));

        // resolve array item references
        types.forEach(type -> {
            type.getProperties().stream()
                    .flatMap(property -> property.getItemTypes().stream())
                    .filter(item -> item.getType() == REFERENCE)
                    .forEach(item -> {
                        var resolved = resolveType(item.getValue(), type.getSchemaUri());
                        if (resolved != null) {
                            item.resolvedType(resolved);
                        }
                    });
        });

        // resolve required properties to the schema property definition
        types.forEach(SchemaType::resolvePropertyReferences);

    }
}
