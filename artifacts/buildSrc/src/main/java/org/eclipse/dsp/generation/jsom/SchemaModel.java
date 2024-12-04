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

/**
 * Represents a JSON Schema Object Model (JSOM).
 */
public interface SchemaModel {

    /**
     * Resolves a reference to a schema type for a given context or null if not found.
     */
    SchemaType resolveType(String reference, String typeContext);

    /**
     * Returns all schema types defined in the model.
     */
    @NotNull
    Collection<SchemaType> getSchemaTypes();
}
