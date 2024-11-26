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

import java.io.IOException;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidVersionSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() throws IOException {
        assertThat(schema.validate(INVALID_NO_VERSIONS, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_EMPTY_VERSIONS, JSON).iterator().next().getType()).isEqualTo(MIN_ITEMS);
        assertThat(schema.validate(INVALID_NO_VERSION, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_PATH, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/common/protocol-version-schema.json");
    }

    private static final String INVALID_NO_VERSIONS = """
            {}
            """;

    private static final String INVALID_EMPTY_VERSIONS = """
            {
              "protocolVersions": []
            }
            """;

    private static final String INVALID_NO_VERSION = """
            {
              "protocolVersions": [
                {
                  "path": "/some/path/v1"
                }
              ]
            }
            """;

    private static final String INVALID_NO_PATH = """
            {
              "protocolVersions": [
                {
                  "version": "1.0"
                }
              ]
            }
            """;
}
