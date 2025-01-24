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

public class InvalidContractNegotiationTerminationMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() {
        assertThat(schema.validate(MESSAGE_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(MESSAGE_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(MESSAGE_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(MESSAGE_NO_PROVIDER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-negotiation-termination-message-schema.json");
    }

    private static final String MESSAGE_NO_CONTEXT = """
            {
              "@type": "ContractNegotiationTerminationMessage",
              "consumerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "code": "99",
              "reason": [
                "License model does not fit."
              ]
            }
            """;

    private static final String MESSAGE_NO_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.json"
              ],
              "consumerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "code": "99",
              "reason": [
                "License model does not fit."
              ]
            }
            """;

    private static final String MESSAGE_NO_CONSUMER_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.json"
              ],
              "@type": "ContractNegotiationTerminationMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "code": "99",
              "reason": [
                "License model does not fit."
              ]
            }
            """;

    private static final String MESSAGE_NO_PROVIDER_ID = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.json"
              ],
              "@type": "ContractNegotiationTerminationMessage",
              "consumerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "code": "99",
              "reason": [
                "License model does not fit."
              ]
            }
            """;
}

