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

import java.util.HashMap;
import java.util.Map;

import static org.eclipse.dsp.generation.jsom.JsonTypes.ANY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ARRAY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.BOOLEAN;
import static org.eclipse.dsp.generation.jsom.JsonTypes.INTEGER;
import static org.eclipse.dsp.generation.jsom.JsonTypes.NULL;
import static org.eclipse.dsp.generation.jsom.JsonTypes.NUMBER;
import static org.eclipse.dsp.generation.jsom.JsonTypes.OBJECT;
import static org.eclipse.dsp.generation.jsom.JsonTypes.STRING;

/**
 * Maps Json base types to their name.
 */
public class JsonTypeMappings {

    public static final Map<String, SchemaType> TYPE_MAPPINGS = new HashMap<>();

    static {
        TYPE_MAPPINGS.put(STRING.getBaseType(), STRING);
        TYPE_MAPPINGS.put(NUMBER.getBaseType(), NUMBER);
        TYPE_MAPPINGS.put(INTEGER.getBaseType(), INTEGER);
        TYPE_MAPPINGS.put(OBJECT.getBaseType(), OBJECT);
        TYPE_MAPPINGS.put(ARRAY.getBaseType(), ARRAY);
        TYPE_MAPPINGS.put(BOOLEAN.getBaseType(), BOOLEAN);
        TYPE_MAPPINGS.put(NULL.getBaseType(), NULL);
        TYPE_MAPPINGS.put(ANY.getBaseType(), ANY);
    }

    private JsonTypeMappings() {
    }
}
