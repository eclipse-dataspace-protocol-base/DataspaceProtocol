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

public class CatalogSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() throws IOException {
        var node = mapper.readTree(getClass().getResourceAsStream("/catalog/example/catalog.json"));
        assertThat(schema.validate(node)).isEmpty();
        assertThat(schema.validate(MINIMAL_CATALOG, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/catalog/catalog-schema.json");
    }

    private static final String MINIMAL_CATALOG = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@id": "urn:uuid:3afeadd8-ed2d-569e-d634-8394a8836d57",
              "@type": "Catalog",
              "participantId": "urn:example:DataProviderA"
            }
            """;
}
