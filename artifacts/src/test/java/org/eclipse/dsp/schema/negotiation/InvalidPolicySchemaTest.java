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

package org.eclipse.dsp.schema.negotiation;

import org.eclipse.dsp.schema.fixtures.AbstractSchemaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.networknt.schema.InputFormat.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidPolicySchemaTest extends AbstractSchemaTest {

    @Test
    void verifyInvalidCases() {
        assertThat(schema.validate(INVALID_NO_RULES, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_NO_ID, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_NO_ACTION, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_NO_LEFT_OPERAND, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_NO_OPERATOR, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_NO_RIGHT_OPERAND, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
        assertThat(schema.validate(INVALID_MULTIPLICITY_CONSTRAINT, JSON).iterator().next().getType()).isEqualTo(ONE_OF);
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-schema.json");
    }

    private static final String INVALID_NO_RULES = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1"
            }""";

    private static final String INVALID_NO_ID = """
            {
              "@type": "Offer",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [
                    {
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                    }
                  ]
                }
              ]
            }""";

    private static final String INVALID_NO_ACTION = """
            {
              "@type": "Offer",
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "target": "asset:1",
              "permission": [
                {
                  "constraint": [
                    {
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                    }
                  ]
                }
              ]
            }""";

    private static final String INVALID_NO_LEFT_OPERAND = """
            {
              "@type": "Offer",
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [
                    {
                      "operator": "eq",
                      "rightOperand": "gold"
                    }
                  ]
                }
              ]
            }""";
    private static final String INVALID_NO_OPERATOR = """
            {
              "@type": "Offer",
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [
                    {
                      "leftOperand": "partner",
                      "rightOperand": "gold"
                    }
                  ]
                }
              ]
            }""";

    private static final String INVALID_NO_RIGHT_OPERAND = """
            {
              "@type": "Offer",
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [
                    {
                      "leftOperand": "partner",
                      "operator": "eq"
                    }
                  ]
                }
              ]
            }""";

    private final static String INVALID_MULTIPLICITY_CONSTRAINT = """
            {
                 "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                 "@type": "Offer",
                 "target": "asset:1",
                 "permission": [
                   {
                     "action": "use",
                     "constraint": {
                       "invalid": [
                         {
                           "leftOperand": "partner",
                           "operator": "eq",
                           "rightOperand": "gold"
                         },
                         {
                           "leftOperand": "partner",
                           "operator": "eq",
                           "rightOperand": "silver"
                         }
                       ]
                     }
                   }
                 ]
               }
            """;
}
