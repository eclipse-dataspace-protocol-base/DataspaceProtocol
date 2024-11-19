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

public class InvalidDatasetSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_NO_ID_DATASET, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_ID_DATASET_NO_POLICY, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_DISTRIBUTION, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_DISTRIBUTION_FORMAT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/catalog/dataset-schema.json");
    }

    private static final String INVALID_NO_ID_DATASET = """
            {
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
                    "accessService" : "urn:uuid:fdb94161-14b4-4319-9f45-09dda9f3ce83",
                    "format": "HttpData-PULL"
                  }
              ]
            }
            """;

    private static final String INVALID_NO_ID_DATASET_NO_POLICY = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "distribution": [
                  {
                    "accessService" : "urn:uuid:fdb94161-14b4-4319-9f45-09dda9f3ce83",
                    "format": "HttpData-PULL"
                  }
              ]
            }
            """;

    private static final String INVALID_NO_DISTRIBUTION = """
            {
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
              ]
            }
            """;

    private static final String INVALID_NO_DISTRIBUTION_FORMAT = """
            {
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
                    "accessService" : "urn:uuid:fdb94161-14b4-4319-9f45-09dda9f3ce83"
                  }
              ]
            }
            """;

}
