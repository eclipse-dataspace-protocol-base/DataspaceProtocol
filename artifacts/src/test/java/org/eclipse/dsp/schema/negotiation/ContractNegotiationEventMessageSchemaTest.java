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

public class ContractNegotiationEventMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/negotiation/example/contract-negotiation-event-message.json"));
        assertThat(schema.validate(node)).isEmpty();
        assertThat(schema.validate(ACCEPTED_EVENT_TYPE, JSON)).isEmpty();
        assertThat(schema.validate(FINALIZED_EVENT_TYPE, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-negotiation-event-message-schema.json");
    }

    private static final String ACCEPTED_EVENT_TYPE = """
             {
               "@context": [
                 "https://w3id.org/dspace/2025/1/context.jsonld"
               ],
               "@type": "ContractNegotiationEventMessage",
               "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
               "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
               "eventType": "ACCEPTED"
             }
            """;

    private static final String FINALIZED_EVENT_TYPE = """
             {
               "@context": [
                 "https://w3id.org/dspace/2025/1/context.jsonld"
               ],
               "@type": "ContractNegotiationEventMessage",
               "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
               "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
               "eventType": "FINALIZED"
             }
            """;

}
