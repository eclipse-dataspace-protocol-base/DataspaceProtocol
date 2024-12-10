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

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasetSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() {
        assertThat(schema.validate(DATASET, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/catalog/dataset-schema.json");
    }

    private static final String DATASET = """
            {
              "@context": [
                "https://w3id.org/dspace/2024/1/context.json"
              ],
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "hasPolicy": [
                {
                  "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                  "@type": "Offer",
                  "permission": [
                    {
                      "action": "use",
                      "constraint": [{
                        "leftOperand": "spatial",
                        "operator": "eq",
                        "rightOperand": "http://example.org/EU"
                      }]
                    }
                  ]
                }
              ],
              "distribution": [
                {
                  "@type": "Distribution",
                  "format": "HttpData-PULL",
                  "accessService": "urn:uuid:4aa2dcc8-4d2d-569e-d634-8394a8834d77"
                }
              ]
            }
            """;

}
