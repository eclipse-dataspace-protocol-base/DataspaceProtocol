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

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidContractRequestMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_REQUEST_NO_OFFER, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_ID, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_REQUEST_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_CALLBACK, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-request-message-schema.json");
    }

    private static final String INVALID_REQUEST_NO_OFFER = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_CONSUMER_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_CALLBACK = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "ContractRequestMessage",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                }
            }
            """;

    private static final String INVALID_REQUEST_NO_TYPE = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_CONTEXT = """
            {
                "@type": "ContractRequestMessage",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
                "offer": {
                  "@id": "urn:uuid:d526561f-528e-4d5a-ae12-9a9dd9b7a815"
                },
                "callbackAddress": "https://example.com/callback"
            }
            """;
}
