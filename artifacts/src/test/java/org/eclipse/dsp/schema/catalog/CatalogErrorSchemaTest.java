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

package org.eclipse.dsp.schema.catalog;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class CatalogErrorSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyExamples() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/catalog/example/catalog-error.json"));
        assertThat(schema.validate(node)).isEmpty();
    }

    @Test
    void verifyNodeCode() {
        assertThat(schema.validate(VALID_NO_CODE, JSON)).isEmpty();
    }

    @Test
    void verifyMissingContext() {
        assertThat(schema.validate(INVALID_MISSING_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @Test
    void verifyInvalidReason() {
        assertThat(schema.validate(INVALID_REASON, JSON).iterator().next().getType()).isEqualTo(TYPE);
    }

    @Test
    void verifyInvalidCode() {
        assertThat(schema.validate(INVALID_CODE_TYPE, JSON).iterator().next().getType()).isEqualTo(TYPE);
    }

    @BeforeEach
    void setUp() {
        setUp("/catalog/catalog-error-schema.json");
    }

    private static final String INVALID_MISSING_CONTEXT = """
            {
              "@type": "CatalogError",
              "code": "123-A",
              "reason": [
                "Catalog not provisioned for this requester."
              ]
            }""";

    private static final String VALID_NO_CODE = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "CatalogError"
            }""";

    private static final String INVALID_CODE_TYPE = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "CatalogError",
              "code": 123,
              "reason": [
                "Catalog not provisioned for this requester."
              ]
            }""";

    private static final String INVALID_REASON = """
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "CatalogError",
              "code": "123-A",
              "reason": "Catalog not provisioned for this requester."
            }""";
}
