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
 *       SAP SE - fixes
 *
 */

package org.eclipse.dsp.schema.fixtures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.io.IOException;

import static com.networknt.schema.SchemaId.V201909;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dsp.DspConstants.DSP_PREFIX;

/**
 * Base test class.
 */
public abstract class AbstractSchemaTest {
    protected static final String MIN_CONTAINS = "minContains";
    protected static final String MIN_ITEMS = "minItems";
    protected static final String REQUIRED = "required";
    protected static final String ONE_OF = "oneOf";
    protected static final String TYPE = "type";
    protected static final String ENUM = "enum";

    private static final String CLASSPATH_SCHEMA = "classpath:/";
    private static final String MAIN_RESOURCES = "src/main/resources";


    protected ObjectMapper mapper = new ObjectMapper();
    protected JsonSchema schema;
    protected JsonSchema metaSchema;

    protected void setUp(String schemaFile) {
        var schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909, builder ->
                builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix(DSP_PREFIX, CLASSPATH_SCHEMA))
        );

        metaSchema = schemaFactory.getSchema(SchemaLocation.of(V201909));

        JsonNode schemaAsNode;
        try {
            schemaAsNode = mapper.readTree(new File(MAIN_RESOURCES + schemaFile));
        } catch (IOException e) {
            throw new RuntimeException();
        }

        assertThat(metaSchema.validate(schemaAsNode)).isEmpty();

        schema = schemaFactory.getSchema(SchemaLocation.of(DSP_PREFIX + schemaFile));
    }

    protected SchemaError errorExtractor(ValidationMessage validationMessage) {
        return new SchemaError(validationMessage.getProperty(), validationMessage.getType());
    }

    protected SchemaError error(String property, String type) {
        return new SchemaError(property, type);
    }

    public record SchemaError(String property, String type) {

    }
}
