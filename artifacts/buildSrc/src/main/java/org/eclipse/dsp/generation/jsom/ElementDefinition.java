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

import java.util.Set;
import java.util.TreeSet;

/**
 * Models an element such as {@code contains} object or an {@code items} object.
 */
public class ElementDefinition implements Comparable<ElementDefinition> {

    public enum Type {
        REFERENCE, CONSTANT, JSON
    }

    private final Type type;
    private final String value;
    private final Set<SchemaType> resolvedTypes = new TreeSet<>();

    public ElementDefinition(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Set<SchemaType> getResolvedTypes() {
        return resolvedTypes;
    }

    public void resolvedType(SchemaType resolvedType) {
        this.resolvedTypes.add(resolvedType);
    }

    @Override
    public int compareTo(@NotNull ElementDefinition o) {
        return value.compareTo(o.value);
    }

}
