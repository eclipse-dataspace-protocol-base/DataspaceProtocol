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

package org.eclipse.dsp.schema.transfer;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidTransferSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_ENDPOINT_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_ENDPOINT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-schema.json");
    }


    private static final String INVALID_NO_TYPE = """
            {
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
                "endpoint": "http://example.com"
            }
            """;

    private static final String INVALID_NO_ENDPOINT_TYPE = """
            {
                "@type": "DataAddress",
                "endpoint": "http://example.com"
            }
            """;

    private static final String INVALID_NO_ENDPOINT = """
            {
                "@type": "DataAddress",
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP"
            }
            """;

}
