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

import org.eclipse.dsp.generation.jsom.SchemaType;
import org.jetbrains.annotations.NotNull;

/**
 * Transforms a {@link SchemaType} to an output format.
 */
public interface SchemaTypeTransformer<OUTPUT> {

    @NotNull
    OUTPUT transform(SchemaType schemaType);

}
