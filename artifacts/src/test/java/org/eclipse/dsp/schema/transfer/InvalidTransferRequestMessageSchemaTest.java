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

public class InvalidTransferRequestMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_REQUEST_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_AGREEMENT_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_FORMAT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_REQUEST_NO_CALLBACK, JSON).iterator().next().getType()).isEqualTo(REQUIRED);

        assertThat(schema.validate(INVALID_NO_DA_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_DA_ENDPOINT_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_DA_ENDPOINT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-request-message-schema.json");
    }

    private static final String INVALID_REQUEST_NO_CONTEXT = """
            {
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_CONSUMER_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_AGREEMENT_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "format": "example:HTTP_PUSH",
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_FORMAT = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_REQUEST_NO_CALLBACK = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH"
            }
            """;

    private static final String INVALID_NO_DA_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "dataAddress": {
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
                "endpoint": "http://example.com"
              },
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_NO_DA_ENDPOINT_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "dataAddress": {
                "@type": "DataAddress",
                "endpoint": "http://example.com"
              },
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_NO_DA_ENDPOINT = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferRequestMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "format": "example:HTTP_PUSH",
              "dataAddress": {
                "@type": "DataAddress",
                "endpointType": "https://w3id.org/idsa/v4.1/HTTP"
              },
              "callbackAddress": "https://example.com/callback"
            }
            """;

}
