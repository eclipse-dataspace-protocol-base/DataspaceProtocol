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

public class AgreementSchemaTest extends AbstractSchemaTest {

    @Test
    void verifySchema() {
        assertThat(schema.validate(AGREEMENT, JSON)).isEmpty();
        assertThat(schema.validate(MINIMAL_AGREEMENT, JSON)).isEmpty();
        assertThat(schema.validate(INVALID_AGREEMENT_NO_ASSIGNER, JSON)).isNotEmpty();
        assertThat(schema.validate(INVALID_AGREEMENT_NO_ASSIGNEE, JSON)).isNotEmpty();
        assertThat(schema.validate(INVALID_AGREEMENT_NO_TARGET, JSON)).isNotEmpty();
    }

    @BeforeEach
    void setUp() {
        setUp("/negotiation/contract-schema.json");
    }

    private final static String AGREEMENT = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Agreement",
              "target": "asset:1",
              "assigner": "did:web:provider",
              "assignee": "did:web:consumer",
              "permission": [
                {
                  "action": "use",
                  "constraint": [{
                      "leftOperand": "partner",
                      "operator": "eq",
                      "rightOperand": "gold"
                  }]
                }
              ]
            }""";

    private final static String MINIMAL_AGREEMENT = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Agreement",
              "target": "asset:1",
              "assigner": "did:web:provider",
              "assignee": "did:web:consumer",
              "permission": [
                {
                  "action": "use"
                }
              ]
            }""";

    private final static String INVALID_AGREEMENT_NO_TARGET = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Agreement",
              "assigner": "did:web:provider",
              "assignee": "did:web:consumer",
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

    private final static String INVALID_AGREEMENT_NO_ASSIGNEE = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Agreement",
              "target": "asset:1",
              "assigner": "did:web:provider",
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

    private final static String INVALID_AGREEMENT_NO_ASSIGNER = """
            {
              "@id": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
              "@type": "Agreement",
              "target": "asset:1",
              "assignee": "did:web:consumer",
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


}
