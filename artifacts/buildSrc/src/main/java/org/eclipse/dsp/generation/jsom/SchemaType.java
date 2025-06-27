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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Stream.concat;

/**
 * A Json Schema type.
 * <p>
 * {@link #getBaseType()} returns the JSON type, e.g. {@code object}, {@code array}, etc. of the type. {@link #getName()} returns
 * the type name which is either the property name of its {@code definitions} entry in the schema, or if the type is defined
 * in the root level of the schema, the latter's URI.
 */
public class SchemaType implements Comparable<SchemaType> {
    private static final String JSON_BASE_URI = "urn:json";

    private final String name;
    private final String baseType;
    private final String itemType;
    private final boolean rootDefinition;
    private final String schemaUri;
    private final Set<String> allOf = new HashSet<>();
    private final Set<SchemaType> resolvedAllOf = new HashSet<>();
    private final Set<String> oneOf = new HashSet<>();
    private final Set<SchemaType> resolvedOneOf = new HashSet<>();
    private final Set<SchemaProperty> properties = new TreeSet<>();
    private final Set<ElementDefinition> contains = new TreeSet<>();
    private final Map<String, SchemaProperty> propertiesMap = new HashMap<>();
    private final Map<String, SchemaPropertyReference> requiredProperties = new HashMap<>();
    private final Map<String, SchemaPropertyReference> optionalProperties = new HashMap<>();

    private boolean jsonBaseType; // denotes if this type represents a base Json type, e.g. string, object, array

    /**
     * Ctor for base Json types.
     */
    public SchemaType(String name) {
        this(name, name, JSON_BASE_URI);
        this.jsonBaseType = true;
    }

    /**
     * Creates a type which a base type "any".
     */
    public SchemaType(String name, String schemaUri) {
        this(name, "any", schemaUri);
    }

    public SchemaType(String name, String baseType, String schemaUri) {
        this(name, baseType, null, false, schemaUri);
    }

    public SchemaType(String name, String baseType, String itemType, String schemaUri) {
        this(name, baseType, itemType, false, schemaUri);
    }

    public SchemaType(String name, String baseType, String itemType, boolean rootDefinition, String schemaUri) {
        this.name = name;
        this.baseType = baseType;
        this.itemType = itemType;
        this.rootDefinition = rootDefinition;
        this.schemaUri = schemaUri;
    }

    public String getName() {
        return name;
    }

    public boolean isJsonBaseType() {
        return jsonBaseType;
    }

    public String getBaseType() {
        return baseType;
    }

    public String getItemType() {
        return itemType;
    }

    public boolean isRootDefinition() {
        return rootDefinition;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    @NotNull
    public Set<SchemaProperty> getProperties() {
        return properties;
    }

    public Collection<SchemaPropertyReference> getRequiredProperties() {
        return requiredProperties.values();
    }

    @NotNull
    public Set<SchemaPropertyReference> getTransitiveRequiredProperties() {
        return concat(requiredProperties.values().stream(),
                concat(resolvedAllOf.stream().flatMap(type -> type.getTransitiveRequiredProperties().stream()),
                resolvedOneOf.stream().flatMap(type -> type.getTransitiveRequiredProperties().stream())))
                .collect((toCollection(TreeSet::new)));
    }

    @NotNull
    public Set<SchemaPropertyReference> getTransitiveOptionalProperties() {
        // a type may include multiple other types (allOf) where a property is optional in one but mandatory in another - filter it
        var required = getRequiredProperties().stream().map(SchemaPropertyReference::getName).collect(Collectors.toSet());
        return concat(optionalProperties.values().stream(),
                concat(resolvedAllOf.stream().flatMap(type -> type.getTransitiveOptionalProperties().stream()),
                resolvedOneOf.stream().flatMap(type -> type.getTransitiveOptionalProperties().stream())))
                .filter(prop -> !required.contains(prop.getName()))  // filter required
                .collect((toCollection(TreeSet::new)));
    }

    public void properties(List<SchemaProperty> schemaProperties) {
        properties.addAll(schemaProperties);
        schemaProperties.forEach(p -> propertiesMap.put(p.getName(), p));
    }

    public Set<ElementDefinition> getContains() {
        return contains;
    }

    public void contains(Collection<ElementDefinition> collection) {
        contains.addAll(collection);
    }

    @NotNull
    public Set<String> getAllOf() {
        return allOf;
    }

    public Set<SchemaType> getResolvedAllOf() {
        return resolvedAllOf;
    }

    public void allOf(Collection<String> allOf) {
        this.allOf.addAll(allOf);
    }

    public void resolvedAllOfType(SchemaType type) {
        this.resolvedAllOf.add(type);
    }

    @NotNull
    public Set<String> getOneOf() {
        return oneOf;
    }

    public Set<SchemaType> getResolvedOneOf() {
        return resolvedOneOf;
    }

    public void oneOf(Collection<String> oneOf) {
        this.oneOf.addAll(oneOf);
    }

    public void resolvedOneOfType(SchemaType type) {
        this.resolvedOneOf.add(type);
    }

    public void required(Collection<SchemaPropertyReference> required) {
        required.forEach(ref -> requiredProperties.put(ref.getName(), ref));
    }

    public void resolvePropertyReferences() {
        // resolve required properties that have not yet been previously resolved, e.g. those directly defined on the type
        requiredProperties.values()
                .stream()
                .filter(ref -> ref.getResolvedProperty() == null)
                .forEach(ref -> ref.resolvedProperty(propertiesMap.get(ref.getName())));

        // populate optional properties
        properties.stream()
                .filter(p -> !requiredProperties.containsKey(p.getName()))
                .forEach(property -> optionalProperties.put(property.getName(), new SchemaPropertyReference(property.getName(), property)));
    }

    @Override
    public int compareTo(@NotNull SchemaType o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "SchemaType{" +
               "name='" + name + '\'' +
               ", baseType='" + baseType + '\'' +
               '}';
    }
}
