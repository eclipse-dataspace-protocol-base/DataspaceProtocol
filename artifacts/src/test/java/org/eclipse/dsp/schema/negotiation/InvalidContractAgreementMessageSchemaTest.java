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
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class InvalidContractAgreementMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_TYPE, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_PROVIDER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_CONSUMER_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_CALLBACK, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(NO_AGREEMENT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-agreement-message-schema.json");
    }

    private static final String AGREEMENT = """
            "agreement": {
              "@id": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
              "@type": "Agreement",
              "target": "urn:uuid:3dd1add4-4d2d-569e-d634-8394a8836d23",
              "timestamp": "2023-01-01T01:00:00Z",
              "assigner": "http://example.com/Provider",
              "assignee": "http://example.com/Consumer",
              "permission": [
                {
                  "action": "use",
                  "constraint": [
                    {
                      "leftOperand": "dateTime",
                      "operator": "lteq",
                      "rightOperand": "2023-12-31T06:00Z"
                    }
                  ]
                }
              ]
            }
            """;

    private static final String INVALID_NO_CONTEXT = format("""
            {
              "@type": "ContractAgreementMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              %s,
              "callbackAddress": "https://example.com/callback"
            }
            """, AGREEMENT);

    private static final String INVALID_NO_TYPE = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              %s,
              "callbackAddress": "https://example.com/callback"
            }
            """, AGREEMENT);

    private static final String INVALID_NO_PROVIDER_ID = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractAgreementMessage",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              %s,
              "callbackAddress": "https://example.com/callback"
            }
            """, AGREEMENT);

    private static final String INVALID_NO_CONSUMER_ID = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractAgreementMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              %s,
              "callbackAddress": "https://example.com/callback"
            }
            """, AGREEMENT);

    private static final String INVALID_NO_CALLBACK = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractAgreementMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              %s
            }
            """, AGREEMENT);

    private static final String NO_AGREEMENT = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "ContractAgreementMessage",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
              "callbackAddress": "https://example.com/callback"
            }
            """;

}
