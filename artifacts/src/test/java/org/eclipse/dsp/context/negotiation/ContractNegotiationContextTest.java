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

package org.eclipse.dsp.context.negotiation;

import org.eclipse.dsp.context.fixtures.AbstractJsonLdTest;
import org.junit.jupiter.api.Test;

public class ContractNegotiationContextTest extends AbstractJsonLdTest {

    @Test
    void verifyContractAgreementMessage() {
        verifyRoundTrip("/negotiation/example/contract-agreement-message.json",
                "/negotiation/contract-agreement-message-schema.json");
    }

    @Test
    void verifyFullAgreementMessage() {
        verifyRoundTrip("/negotiation/example/contract-agreement-message-full.json",
                "/negotiation/contract-agreement-message-schema.json");
    }

    @Test
    void verifyContractAgreementVerificationMessage() {
        verifyRoundTrip("/negotiation/example/contract-agreement-verification-message.json",
                "/negotiation/contract-agreement-verification-message-schema.json");
    }

    @Test
    void verifyContractNegotiation() {
        verifyRoundTrip("/negotiation/example/contract-negotiation.json",
                "/negotiation/contract-negotiation-schema.json");
    }

    @Test
    void verifyContractNegotiationEventMessage() {
        verifyRoundTrip("/negotiation/example/contract-negotiation-event-message.json",
                "/negotiation/contract-negotiation-event-message-schema.json");
    }

    @Test
    void verifyContractNegotiationError() {
        verifyRoundTrip("/negotiation/example/contract-negotiation-error.json",
                "/negotiation/contract-negotiation-error-schema.json");
    }

    @Test
    void verifyContractNegotiationTerminationMessage() {
        verifyRoundTrip("/negotiation/example/contract-negotiation-termination-message.json",
                "/negotiation/contract-negotiation-termination-message-schema.json");
    }

    @Test
    void verifyContractOfferMessage() {
        verifyRoundTrip("/negotiation/example/contract-offer-message.json",
                "/negotiation/contract-offer-message-schema.json");
    }

    @Test
    void verifyContractRequestMessage() {
        verifyRoundTrip("/negotiation/example/contract-request-message.json",
                "/negotiation/contract-request-message-schema.json");
    }

    @Test
    void verifyContractRequestMessageInitial() {
        verifyRoundTrip("/negotiation/example/contract-request-message_initial.json",
                "/negotiation/contract-request-message-schema.json");
    }


}
