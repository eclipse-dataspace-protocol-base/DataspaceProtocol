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

package org.eclipse.dsp.schema.negotiation;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class ContractRequestMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyRequestSchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/negotiation/example/contract-request-message.json"));
        assertThat(schema.validate(node)).isEmpty();
    }

    @Test
    void verifyInitialRequestSchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/negotiation/example/contract-request-message_initial.json"));
        assertThat(schema.validate(node)).isEmpty();
    }

    @Test
    void verifyIdRequest() {
        assertThat(schema.validate(REQUEST_ID, JSON)).isEmpty();
        assertThat(schema.validate(REQUEST_INITIAL_ID, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-request-message-schema.json");
    }

    private static final String REQUEST_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String REQUEST_INITIAL_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;
}
