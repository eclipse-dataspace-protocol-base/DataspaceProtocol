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

package org.eclipse.dsp.schema.negotiation;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidContractOfferMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_MESSAGE_NO_TARGET, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_MESSAGE_NO_CALLBACK, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-offer-message-schema.json");
    }

    private static final String INVALID_MESSAGE_NO_TARGET = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractOfferMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "offer": {
                "@type": "Offer",
                "@id": "urn:uuid:6bcea82e-c509-443d-ba8c-8eef25984c07",
                "permission": [
                  {
                    "action": "use"
                  }
                ]
              },
              "callbackAddress": "https://example.com/callback"
            }
            """;

    private static final String INVALID_MESSAGE_NO_CALLBACK = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractOfferMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "offer": {
                "@type": "Offer",
                "@id": "urn:uuid:6bcea82e-c509-443d-ba8c-8eef25984c07",
                "target": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                "permission": [
                  {
                    "action": "use"
                  }
                ]
              }
            }
            """;
}
