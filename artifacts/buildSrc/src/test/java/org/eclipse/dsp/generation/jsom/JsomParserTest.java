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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dsp.generation.jsom.TestSchema.TEST_SCHEMA;

class JsomParserTest {
    private static final String LOCAL = "/local/";
    private static final String SCHEMA_PREFIX = "http://foo.com/schema/";
    private ObjectMapper mapper;
    private JsomParser parser;

    @Test
    @SuppressWarnings("unchecked")
    void verifyParse() throws JsonProcessingException {
        var schema = (Map<String, Object>) mapper.readValue(TEST_SCHEMA, Map.class);
        var schemaModel = parser.parseInstances(Stream.of(new JsomParser.SchemaInstance(LOCAL + "foo.json", schema)));

        assertThat(schemaModel.getSchemaTypes()).isNotEmpty();

        var policyClass = schemaModel.resolveType("PolicyClass", LOCAL);

        assertThat(policyClass.getProperties().size()).isEqualTo(4);    // PolicyClass should have 4 properties
        //noinspection OptionalGetWithoutIsPresent
        assertThat(policyClass.getProperties().stream()
                .filter(p -> p.getName().equals("profile"))
                .findFirst().get()
                .getItemTypes().iterator().next().getValue())
                .isEqualTo(JsonTypes.STRING.getBaseType());

        var agreement = schemaModel.resolveType("Agreement", LOCAL);
        assertThat(agreement.getResolvedAllOf()).contains(policyClass);
        assertThat(agreement.getProperties().size()).isEqualTo(5);    // Agreement should have 5 properties
        assertThat(agreement.getTransitiveRequiredProperties().size()).isEqualTo(5);
        assertThat(agreement.getTransitiveOptionalProperties().size()).isEqualTo(4);

        assertThat(agreement.getRequiredProperties())
                .filteredOn(p -> p.getName().equals("assignee"))
                .first()
                .extracting(p -> p.getResolvedProperty().getDescription())
                .isEqualTo("The dataset consumer");

    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        parser = new JsomParser(SCHEMA_PREFIX, LOCAL, mapper);
    }
}