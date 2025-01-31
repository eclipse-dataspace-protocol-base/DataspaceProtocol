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

public class InvalidTransferCompletionMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_MESSAGE_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_MESSAGE_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_MESSAGE_NO_PROVIDER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_MESSAGE_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-completion-message-schema.json");
    }

    private static final String INVALID_MESSAGE_NO_CONTEXT = """
            {
              "@type": "TransferCompletionMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;

    private static final String INVALID_MESSAGE_NO_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;

    private static final String INVALID_MESSAGE_NO_PROVIDER_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "TransferCompletionMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;

    private static final String INVALID_MESSAGE_NO_CONSUMER_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "TransferCompletionMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab"
            }
            """;

}
