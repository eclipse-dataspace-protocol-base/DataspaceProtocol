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

package org.eclipse.dsp.context.transfer;

import org.eclipse.dsp.context.fixtures.AbstractJsonLdTest;
import org.junit.jupiter.api.Test;

public class TransferContextTest extends AbstractJsonLdTest {

    @Test
    void verifyTransferCompletionMessage() {
        verifyRoundTrip("/transfer/example/transfer-completion-message.json",
                "/transfer/transfer-completion-message-schema.json");
    }

    @Test
    void verifyTransferError() {
        verifyRoundTrip("/transfer/example/transfer-error.json",
                "/transfer/transfer-error-schema.json");
    }

    @Test
    void verifyTransferProcess() {
        verifyRoundTrip("/transfer/example/transfer-process.json",
                "/transfer/transfer-process-schema.json");
    }

    @Test
    void verifyTransferRequestMessage() {
        verifyRoundTrip("/transfer/example/transfer-request-message.json",
                "/transfer/transfer-request-message-schema.json");
    }

    @Test
    void verifyTransferStartMessage() {
        verifyRoundTrip("/transfer/example/transfer-start-message.json",
                "/transfer/transfer-start-message-schema.json");
    }

    @Test
    void verifyTransferSuspensionMessage() {
        verifyRoundTrip("/transfer/example/transfer-suspension-message.json",
                "/transfer/transfer-suspension-message-schema.json");
    }

    @Test
    void verifyTransferTerminationMessage() {
        verifyRoundTrip("/transfer/example/transfer-termination-message.json",
                "/transfer/transfer-termination-message-schema.json");
    }


}
