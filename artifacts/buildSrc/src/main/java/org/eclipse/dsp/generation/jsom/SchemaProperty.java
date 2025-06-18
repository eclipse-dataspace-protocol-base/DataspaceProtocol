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
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * A property defined in a schema type.
 */
public class SchemaProperty implements Comparable<SchemaProperty> {
    private String name;
    private String description = "";
    private String constantValue;
    private Set<String> types = new TreeSet<>();
    private final Set<SchemaType> resolvedTypes = new TreeSet<>();
    private final Set<ElementDefinition> itemTypes = new TreeSet<>();
    private final  Set<Object> enumValues = new TreeSet<>();

    public String getName() {
        return name;
    }

    public Set<String> getTypes() {
        return types;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public String getDescription() {
        return description;
    }

    public Set<Object> getEnumValues() {
        return enumValues;
    }

    public Set<SchemaType> getResolvedTypes() {
        return resolvedTypes;
    }

    public void resolvedType(SchemaType resolvedType) {
        this.resolvedTypes.add(resolvedType);
    }

    public Set<ElementDefinition> getItemTypes() {
        return itemTypes;
    }

    @Override
    public int compareTo(@NotNull SchemaProperty o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        var b = new StringBuilder(name)
                .append(": ")
                .append(resolvedTypes.stream().map(SchemaType::getName).collect(joining(",")));
        if (constantValue != null) {
            b.append(" [").append(constantValue).append("]");
        }
        return b.toString();
    }

    private SchemaProperty() {
    }

    public static final class Builder {
        private final SchemaProperty property;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder name(String name) {
            property.name = name;
            return this;
        }

        public Builder description(String description) {
            property.description = description;
            return this;
        }

        public Builder types(Set<String> types) {
            property.types = types;
            return this;
        }

        public Builder itemTypes(Collection<ElementDefinition> elementTypes) {
            property.itemTypes.addAll(elementTypes);
            return this;
        }

        public Builder constantValue(String value) {
            property.constantValue = value;
            return this;
        }

        public Builder enumValues(Collection<Object> enumValues) {
            property.enumValues.addAll(enumValues);
            return this;
        }

        public SchemaProperty build() {
            requireNonNull(property.name);
            requireNonNull(property.description);
            if (property.types.isEmpty()) {
                property.types.add("any");
            }
            return property;
        }

        private Builder() {
            property = new SchemaProperty();
        }

    }
}
