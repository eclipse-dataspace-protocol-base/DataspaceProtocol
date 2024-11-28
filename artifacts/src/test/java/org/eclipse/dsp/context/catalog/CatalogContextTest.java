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

package org.eclipse.dsp.context.catalog;

import org.eclipse.dsp.context.fixtures.AbstractJsonLdTest;
import org.junit.jupiter.api.Test;

public class CatalogContextTest extends AbstractJsonLdTest {

    @Test
    void verifyCatalogError() {
        verifyRoundTrip("/catalog/example/catalog-error.json",
                "/catalog/catalog-error-schema.json");
    }

    @Test
    void verifyCatalog() {
        verifyRoundTrip("/catalog/example/catalog.json",
                "/catalog/catalog-schema.json");
    }

    @Test
    void verifyDatasetRequestMessage() {
        verifyRoundTrip("/catalog/example/dataset-request-message.json",
                "/catalog/dataset-request-message-schema.json");
    }

    @Test
    void verifyDataset() {
        verifyRoundTrip("/catalog/example/dataset.json",
                "/catalog/dataset-schema.json");
    }

    @Test
    void verifyNestedCatalog() {
        verifyRoundTrip("/catalog/example/nested-catalog.json",
                "/catalog/catalog-schema.json");
    }
}
