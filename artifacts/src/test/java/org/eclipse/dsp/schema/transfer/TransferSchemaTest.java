/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.dsp.schema.transfer;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() {
        assertThat(schema.validate(PROPERTIES, JSON)).isEmpty();
        assertThat(schema.validate(NO_PROPERTIES, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/data-address-schema.json");
    }


    private static final String PROPERTIES = """
            {
                "@type": "DataAddress",
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
                "endpoint": "http://example.com",
                "endpointProperties": [
                    {
                      "@type": "EndpointProperty",
                      "name": "authorization",
                      "value": "TOKEN-ABCDEFG"
                    },
                    {
                      "@type": "EndpointProperty",
                      "name": "authType",
                      "value": "bearer"
                    }
              ]
            }
            """;

    private static final String NO_PROPERTIES = """
            {
                "@type": "DataAddress",
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
                "endpoint": "http://example.com"
            }
            """;


}
