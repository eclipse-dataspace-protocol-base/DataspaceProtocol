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

package org.eclipse.dsp.schema.fixtures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;

import static com.networknt.schema.SpecVersion.VersionFlag.V202012;
import static org.eclipse.dsp.DspConstants.DSP_PREFIX;

/**
 * Base test class.
 */
public abstract class AbstractSchemaTest {
    protected static final String MIN_CONTAINS = "minContains";
    protected static final String REQUIRED = "required";
    protected static final String ONE_OF = "oneOf";
    protected static final String TYPE = "type";
    protected static final String ENUM = "enum";

    private static final String CLASSPATH_SCHEMA = "classpath:/";

    protected ObjectMapper mapper = new ObjectMapper();
    protected JsonSchema schema;

    protected void setUp(String schemaFile) {
        var schemaFactory = JsonSchemaFactory.getInstance(V202012, builder ->
                builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix(DSP_PREFIX, CLASSPATH_SCHEMA))
        );

        schema = schemaFactory.getSchema(SchemaLocation.of(DSP_PREFIX + schemaFile));
    }
}
