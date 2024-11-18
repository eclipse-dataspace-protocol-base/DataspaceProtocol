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

public class InvalidTransferSuspensionMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_PROVIDER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-suspension-message-schema.json");
    }

    private static final String INVALID_NO_TYPE = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;
    private static final String INVALID_NO_PROVIDER_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "TransferSuspensionMessage",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;
    private static final String INVALID_NO_CONSUMER_ID = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "TransferSuspensionMessage",
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab"
            }
            """;

}
