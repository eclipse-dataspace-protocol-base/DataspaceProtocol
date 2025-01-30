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
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidCatalogSchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() throws IOException {
        assertThat(schema.validate(INVALID_CATALOG_NO_CONTEXT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_CATALOG_NO_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_CATALOG_NO_PARTICIPANT_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_SERVICE_ID, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
        assertThat(schema.validate(INVALID_NO_SERVICE_ENDPOINT, JSON).iterator().next().getType()).isEqualTo(REQUIRED);
    }

    @BeforeEach
    void setUp() {
        setUp("/catalog/catalog-schema.json");
    }

    private static final String DATASET = """
            "dataset": [
              {
                "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                "@type": "Dataset",
                "hasPolicy": [
                  {
                    "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                    "@type": "Offer",
                    "permission": [
                      {
                        "action": "use"
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
            ]
            """;

    private static final String INVALID_CATALOG_NO_CONTEXT = format("""
            {
              "@id": "urn:uuid:3afeadd8-ed2d-569e-d634-8394a8836d57",
              "@type": "Catalog",
              "participantId": "urn:example:DataProviderA",
              "service": [
                {
                  "@id": "urn:uuid:4aa2dcc8-4d2d-569e-d634-8394a8834d77",
                  "@type": "DataService",
                  "endpointURL": "https://provider-a.com/connector"
                }
              ],
              %s
            }
            """, DATASET);

    private static final String INVALID_CATALOG_NO_ID = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@type": "Catalog",
              "participantId": "urn:example:DataProviderA",
              "service": [
                {
                  "@id": "urn:uuid:4aa2dcc8-4d2d-569e-d634-8394a8834d77",
                  "@type": "DataService",
                  "endpointURL": "https://provider-a.com/connector"
                }
              ],
              %s
            }
            """, DATASET);

    private static final String INVALID_CATALOG_NO_PARTICIPANT_ID = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@id": "urn:uuid:3afeadd8-ed2d-569e-d634-8394a8836d57",
              "@type": "Catalog",
              "service": [
                {
                  "@id": "urn:uuid:4aa2dcc8-4d2d-569e-d634-8394a8834d77",
                  "@type": "DataService",
                  "endpointURL": "https://provider-a.com/connector"
                }
              ],
              %s
            }
            """, DATASET);

    private static final String INVALID_NO_SERVICE_ID = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@id": "urn:uuid:3afeadd8-ed2d-569e-d634-8394a8836d57",
              "@type": "Catalog",
              "participantId": "urn:example:DataProviderA",
              "service": [
                {
                  "@type": "DataService",
                  "endpointURL": "https://provider-a.com/connector"
                }
              ],
              %s
            }
            """, DATASET);

    private static final String INVALID_NO_SERVICE_ENDPOINT = format("""
            {
              "@context": [
                "https://w3id.org/dspace/2025/1/context.jsonld"
              ],
              "@id": "urn:uuid:3afeadd8-ed2d-569e-d634-8394a8836d57",
              "@type": "Catalog",
              "participantId": "urn:example:DataProviderA",
              "service": [
                {
                  "@id": "urn:uuid:4aa2dcc8-4d2d-569e-d634-8394a8834d77",
                  "@type": "DataService"
                }
              ],
              %s
            }
            """, DATASET);

}
