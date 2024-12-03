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

import static java.util.Objects.requireNonNull;

/**
 * A reference to a schema property.
 */
public class SchemaPropertyReference implements Comparable<SchemaPropertyReference> {
    private final String name;
    private SchemaProperty resolvedProperty;

    public SchemaPropertyReference(String name) {
        this.name = requireNonNull(name);
    }

    public SchemaPropertyReference(String name, SchemaProperty resolvedProperty) {
        this.name = name;
        this.resolvedProperty = resolvedProperty;
    }

    public String getName() {
        return name;
    }

    public SchemaProperty getResolvedProperty() {
        return resolvedProperty;
    }

    public void resolvedProperty(SchemaProperty property) {
        this.resolvedProperty = property;
    }

    @Override
    public int compareTo(@NotNull SchemaPropertyReference o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
