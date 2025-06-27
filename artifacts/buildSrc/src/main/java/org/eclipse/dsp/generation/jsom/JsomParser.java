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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.dsp.generation.jsom.SchemaProperty.ConstraintType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Stream.concat;
import static org.eclipse.dsp.generation.jsom.ElementDefinition.Type.CONSTANT;
import static org.eclipse.dsp.generation.jsom.ElementDefinition.Type.JSON;
import static org.eclipse.dsp.generation.jsom.ElementDefinition.Type.REFERENCE;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.ALL_OF;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.COMMENT;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.CONST;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.CONTAINS;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.DEFINITIONS;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.ENUM;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.ITEMS;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.ONE_OF;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.PROPERTIES;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.REF;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.REQUIRED;
import static org.eclipse.dsp.generation.jsom.JsonSchemaKeywords.TYPE;
import static org.eclipse.dsp.generation.jsom.JsonTypeMappings.TYPE_MAPPINGS;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ANY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ARRAY;

/**
 * Parses JSON Schemas into a JSON Schema Object Model (JSOM).
 */
public class JsomParser {
    private final ObjectMapper mapper;
    private final String prefix;
    private final String resolutionPath;

    public record SchemaInstance(String schemaPath, Map<String, Object> schema) {
    }

    /**
     * Ctor
     *
     * @param prefix         the schema prefix used when referencing type definitions
     * @param resolutionPath the local path that maps to the schema prefix
     */
    public JsomParser(String prefix, String resolutionPath) {
        this(prefix, resolutionPath, new ObjectMapper());
    }

    /**
     * Ctor
     *
     * @param prefix         the schema prefix used when referencing type definitions
     * @param resolutionPath the local path that maps to the schema prefix
     * @param mapper         an object mapper used for schema deserialization
     */
    public JsomParser(String prefix, String resolutionPath, ObjectMapper mapper) {
        this.prefix = prefix;
        this.resolutionPath = resolutionPath;
        this.mapper = mapper;
    }

    /**
     * Parses a set of JSON schemas from the file locations and returns an object model representing the type system.
     */
    @SuppressWarnings("unchecked")
    public SchemaModel parseFiles(Stream<File> files) {
        var schemaContext = new SchemaModelContext();
        files.flatMap(schemaFile -> {
            try {
                var root = mapper.readValue(schemaFile, Map.class);
                return parseTypes(schemaFile.getAbsolutePath(), (Map<String, Object>) root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).forEach(schemaContext::addType);
        schemaContext.resolve();
        return schemaContext;
    }

    /**
     * Parses a set of JSON schemas instances and returns an object model representing the type system.
     */
    public SchemaModel parseInstances(Stream<SchemaInstance> instances) {
        var schemaContext = new SchemaModelContext();
        instances.flatMap(instance -> parseTypes(instance.schemaPath(), instance.schema())).forEach(schemaContext::addType);
        schemaContext.resolve();
        return schemaContext;
    }

    @SuppressWarnings("unchecked")
    private Stream<SchemaType> parseTypes(String schemaPath, Map<String, Object> parsedSchema) {
        var definitions = (Map<String, Object>) parsedSchema.getOrDefault(DEFINITIONS, Map.of());
        var definitionsStream = definitions.entrySet().stream()
                .map(entry -> parseTypeDefinition(entry.getKey(), schemaPath, (Map<String, Object>) entry.getValue()));

        var rootAllOf = parsedSchema.get(ALL_OF);
        if (rootAllOf != null) {
            var rootType = parseRootType(schemaPath, parsedSchema);
            return concat(Stream.of(rootType), definitionsStream);
        }
        return definitionsStream;

    }

    /**
     * Parses a type definition at the schema root, i.e. its type name is the schema URI.
     */
    private @NotNull SchemaType parseRootType(String schemaPath, Map<String, Object> root) {
        var typeName = prefix + schemaPath.substring(resolutionPath.length());
        var baseType = root.getOrDefault(TYPE, ANY.getName()).toString();
        var itemType = parseItemType(root, baseType);
        var rootType = new SchemaType(typeName, baseType, itemType, true, typeName);
        parseAttributes(root, rootType);
        return rootType;
    }

    /**
     * Parses a type definition at in the {@code definitions} schema property.
     */
    private @NotNull SchemaType parseTypeDefinition(String type, String schemaPath, Map<String, Object> definition) {
        var baseType = definition.getOrDefault(TYPE, ANY.getName()).toString();
        var itemType = parseItemType(definition, baseType);
        var context = prefix + schemaPath.substring(resolutionPath.length());
        var schemaType = new SchemaType(type, baseType, itemType, context);
        parseAttributes(definition, schemaType);
        return schemaType;
    }

    private @Nullable String parseItemType(Map<String, Object> definition, String baseType) {
        String itemType = null;
        if (baseType.equals(ARRAY.getBaseType())) {
            var items = definition.get(ITEMS);
            if (items instanceof Map itemsMap) {
                itemType = (String) itemsMap.get(TYPE);
            }
        }
        return itemType;
    }

    @SuppressWarnings("unchecked")
    private void parseAttributes(Map<String, Object> definition, SchemaType schemaType) {
        parseRequired(definition, schemaType);

        // parse properties
        var properties = (Map<String, Object>) definition.get(PROPERTIES);
        if (properties != null) {
            var schemaProperties = properties.entrySet().stream()
                    .map(e -> parseProperty(e.getKey(), (Map<String, Object>) e.getValue()))
                    .filter(Objects::nonNull)
                    .toList();
            schemaType.properties(schemaProperties);
        }

        parseReferences(definition, ConstraintType.ALL_OF, schemaType);
        parseReferences(definition, ConstraintType.ONE_OF, schemaType);

        // parse contains
        var contains = (Map<String, Object>) definition.get(CONTAINS);
        if (contains != null) {
            schemaType.contains(parseElementDefinition(contains));
        }
    }

    private List<ElementDefinition> parseElementDefinition(Map<String, Object> container) {
        var constantValue = (String) container.get(CONST);
        if (constantValue != null) {
            return List.of(new ElementDefinition(CONSTANT, constantValue));
        } else {
            var refValue = (String) container.get(REF);
            if (refValue != null) {
                return List.of(new ElementDefinition(REFERENCE, refValue));
            }
        }
        return emptyList();
    }

    @SuppressWarnings("unchecked")
    private void parseReferences(Map<String, Object> definition, ConstraintType constraintType, SchemaType schemaType) {
        var constraintDef = (List<Map<String, Object>>) definition.getOrDefault(constraintType.key(), emptyList());
        var properties = constraintDef.stream()
                .map(e -> (Map<String, Object>) e.get(PROPERTIES))
                .filter(Objects::nonNull)
                .flatMap(e -> e.entrySet().stream())
                .map(e -> parseProperty(e.getKey(), (Map<String, Object>) e.getValue()))
                .toList();
        schemaType.properties(properties);

        // parse references
        var references = constraintDef
                .stream()
                .map(e -> e.get(REF))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
        switch (constraintType) {
            case ONE_OF -> {
                schemaType.oneOf(references);
            }
            case ALL_OF -> {
                schemaType.allOf(references);
            }
            default -> {
                throw new UnsupportedOperationException("Unsupported constraint type: " + constraintType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseRequired(Map<String, Object> definition, SchemaType schemaType) {
        // parse required
        var required = (List<String>) definition.get(REQUIRED);
        if (required != null) {
            var requiredFields = required.stream().map(SchemaPropertyReference::new).toList();
            schemaType.required(requiredFields);
        }
    }

    @SuppressWarnings("unchecked")
    private SchemaProperty parseProperty(String name, Map<String, Object> value) {
        var type = value.get(TYPE);
        var comment = value.containsKey(COMMENT) ? value.get(COMMENT).toString() : "";
        if (type == null) {
            var ref = value.get(REF);
            if (ref != null) {
                return SchemaProperty.Builder.newInstance()
                        .name(name)
                        .types(Set.of((String) ref))
                        .description(comment)
                        .build();
            }
            var oneOf = value.get(ONE_OF);
            //noinspection rawtypes
            if (oneOf instanceof List oneOfList) {
                return parseListProperty(name, oneOfList, comment, ConstraintType.ONE_OF);
            }
            return SchemaProperty.Builder.newInstance()
                    .name(name)
                    .types(Set.of(ANY.getName()))
                    .description(comment)
                    .build();
        } else if (ARRAY.getBaseType().equals(type)) {
            return parseArrayProperty(name, value, (String) type, comment);
        } else {
            var builder = SchemaProperty.Builder.newInstance()
                    .name(name)
                    .types(Set.of((String) type))
                    .description(comment);
            var constantValue = value.get(CONST);
            if (constantValue != null) {
                builder.constantValue(constantValue.toString());
            }
            var enumValues = (List<Object>) value.get(ENUM);
            if (enumValues != null) {
                builder.enumValues(enumValues);
            }
            return builder.build();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private SchemaProperty parseListProperty(String name, List list, String comment, ConstraintType type) {
        var types = new HashSet<String>();
        var itemTypes = new HashSet<ElementDefinition>();
        for (var entry : list) {
            if (entry instanceof Map<?, ?> oneOfMap) {
                var subtype = oneOfMap.get(TYPE);
                if (ARRAY.getBaseType().equals(subtype)) {
                    types.add(ARRAY.getBaseType());
                    var items = oneOfMap.get(ITEMS);
                    if (items instanceof Map) {
                        itemTypes.addAll(parseElementDefinition((Map<String, Object>) items));
                    } else if (items instanceof String itemString) {
                        parseArrayOfTypeElementDefinition(itemString, itemTypes);
                    }
                } else if (subtype != null) {
                    itemTypes.add(parseJsonElementDefinition(subtype.toString()));
                } else {
                    var ref = oneOfMap.get(REF);
                    if (ref instanceof String refString) {
                        itemTypes.add(new ElementDefinition(REFERENCE, refString));
                    }
                }
            }
        }
        return SchemaProperty.Builder.newInstance()
                .name(name)
                .constraintType(type)
                .types(types)
                .itemTypes(itemTypes)
                .description(comment)
                .build();
    }

    private void parseArrayOfTypeElementDefinition(String itemString, HashSet<ElementDefinition> itemTypes) {
        var elementDefinition = new ElementDefinition(JSON, ARRAY.getBaseType());
        var itemType = new SchemaType("array[" + itemString + "]", ARRAY.getBaseType());
        elementDefinition.resolvedType(itemType);
        itemTypes.add(elementDefinition);
    }

    private @NotNull ElementDefinition parseJsonElementDefinition(String type) {
        var elementDefinition = new ElementDefinition(JSON, TYPE_MAPPINGS.getOrDefault(type, ANY).getBaseType());
        elementDefinition.resolvedType(JsonTypes.STRING);
        return elementDefinition;
    }

    private SchemaProperty parseArrayProperty(String name, Map<String, Object> value, String type, String comment) {
        var property = SchemaProperty.Builder.newInstance()
                .name(name)
                .types(Set.of(type))
                .description(comment);
        var items = value.get(ITEMS);
        if (items instanceof Map) {
            //noinspection unchecked
            property.itemTypes(parseElementDefinition((Map<String, Object>) items));
        }
        return property.build();
    }
}
