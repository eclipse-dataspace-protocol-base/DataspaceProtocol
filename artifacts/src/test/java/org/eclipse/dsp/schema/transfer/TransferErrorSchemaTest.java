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

public class TransferErrorSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/transfer/example/transfer-error.json"));
        assertThat(schema.validate(node)).isEmpty();
    }

    @Test
    void verifyMinimal() {
        assertThat(schema.validate(MINIMAL_ERROR, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/transfer/transfer-error-schema.json");
    }

    private static final String MINIMAL_ERROR = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@type": "TransferError",
              "providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
              "consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
            }
            """;
}
