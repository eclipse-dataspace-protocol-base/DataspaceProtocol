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

package org.eclipse.dsp.schema.common;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyContext() {
        var baseInput = """
                ["https://w3id.org/dspace/2025/1/context.json"]
                """;
        assertThat(schema.validate(baseInput, JSON)).isEmpty();

        var multiValueInput = """
                [
                    "https://w3id.org/dspace/2025/1/context.json",
                    "https://test.com/context.json"
                ]
                """;
        assertThat(schema.validate(multiValueInput, JSON)).isEmpty();

        var missingDspValueInput = """
                [
                    "https://test.com/context.json"
                ]
                """;

        assertThat(schema.validate(missingDspValueInput, JSON).iterator().next().getType()).isEqualTo(MIN_CONTAINS);

        var emptyInput = "[]";
        assertThat(schema.validate(emptyInput, JSON).iterator().next().getType()).isEqualTo(MIN_CONTAINS);
    }

    @BeforeEach
    void setUp() {
        setUp("/common/context-schema.json");
    }
}
