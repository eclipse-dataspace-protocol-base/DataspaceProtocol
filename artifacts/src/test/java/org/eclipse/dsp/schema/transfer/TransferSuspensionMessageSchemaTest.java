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

import java.io.IOException;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferSuspensionMessageSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyRequestSchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/transfer/example/transfer-suspension-message.json"));
        assertThat(schema.validate(node)).isEmpty();
    }

    @Test
    void verifyMinimalRequestSchema() {
        assertThat(schema.validate(MINIMAL_REQUEST, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-suspension-message-schema.json");
    }

    private static final String MINIMAL_REQUEST = """
            {
                "@context": [
                  "https://w3id.org/dspace/2024/1/context.json"
                ],
                "@type": "TransferSuspensionMessage",
                "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
                "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;
}
