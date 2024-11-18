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

public class PolicySchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() {
        assertThat(schema.validate(POLICY, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_STRING_PROFILE, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_ARRAY_PROFILE, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_PERMISSION_DUTY, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_OR_CONSTRAINT, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_AND_CONSTRAINT, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_AND_SEQUENCE_CONSTRAINT, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_XONE_SEQUENCE_CONSTRAINT, JSON)).isEmpty();
        assertThat(schema.validate(POLICY_NESTED_MULTIPLICITY, JSON)).isEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-schema.json");
    }

    private final static String POLICY = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                    "leftOperand": "partner",
                    "operator": "eq",
                    "rightOperand": "gold"
                  }],
                  "duty": {
                    "action": "report"
                  }
                }
              ]
            }""";

    private final static String POLICY_STRING_PROFILE = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1",
              "profile": "https://test.com/profile",
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                  }],
                  "duty": {
                    "action": "report"
                  }
                }
              ]
            }""";

    private final static String POLICY_ARRAY_PROFILE = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1",
              "profile": ["https://test.com/profile"],
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                  }],
                  "duty": {
                    "action": "report"
                  }
                }
              ]
            }""";

    private final static String POLICY_PERMISSION_DUTY = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1",
              "profile": ["https://test.com/profile"],
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                  }],
                  "duty": {
                    "action": "report",
                    "constraint": [{
                          "leftOperand": "event",
                          "operator": "gt",
                          "rightOperand": "use"
                    }]
                  }
                }
              ]
            }""";


    private final static String POLICY_OR_CONSTRAINT = """
            {
                 "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                 "@type": "Offer",
                 "target": "asset:1",
                 "permission": [
                   {
                     "action": "use",
                     "constraint": [{
                       "or": [
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
                     }]
                   }
                 ]
               }
            """;

    private final static String POLICY_AND_CONSTRAINT = """
            {
                 "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                 "@type": "Offer",
                 "target": "asset:1",
                 "permission": [
                   {
                     "action": "use",
                     "constraint": [{
                       "and": [
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
                     }]
                   }
                 ]
               }
            """;

    private final static String POLICY_AND_SEQUENCE_CONSTRAINT = """
            {
                 "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                 "@type": "Offer",
                 "target": "asset:1",
                 "permission": [
                   {
                     "action": "use",
                     "constraint": [{
                       "andSequence": [
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
                     }]
                   }
                 ]
               }
            """;

    private final static String POLICY_XONE_SEQUENCE_CONSTRAINT = """
            {
                 "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
                 "@type": "Offer",
                 "target": "asset:1",
                 "permission": [
                   {
                     "action": "use",
                     "constraint": [{
                       "xone": [
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
                     }]
                   }
                 ]
               }
            """;
    private static final String POLICY_NESTED_MULTIPLICITY = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Offer",
              "target": "asset:1",
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                    "or": [
                      {
                        "and": [
                          {
                            "leftOperand": "constraint1",
                            "operator": "eq",
                            "rightOperand": "value1"
                          },
                          {
                            "leftOperand": "constraint2",
                            "operator": "eq",
                            "rightOperand": "value2"
                          }
                        ]
                      },
                      {
                        "leftOperand": "partner",
                        "operator": "eq",
                        "rightOperand": "silver"
                      }
                    ]
                  }]
                }
              ]
            }
            """;

}
