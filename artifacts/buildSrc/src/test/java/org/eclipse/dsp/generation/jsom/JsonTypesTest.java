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

package org.eclipse.dsp.generation.jsom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonTypesTest {

    @Test
    void verifyTypes() {
        assertThat(JsonTypes.ANY.getBaseType()).isEqualTo("any");
        assertThat(JsonTypes.ARRAY.getBaseType()).isEqualTo("array");
        assertThat(JsonTypes.BOOLEAN.getBaseType()).isEqualTo("boolean");
        assertThat(JsonTypes.INTEGER.getBaseType()).isEqualTo("integer");
        assertThat(JsonTypes.NULL.getBaseType()).isEqualTo("null");
        assertThat(JsonTypes.NUMBER.getBaseType()).isEqualTo("number");
        assertThat(JsonTypes.OBJECT.getBaseType()).isEqualTo("object");
        assertThat(JsonTypes.STRING.getBaseType()).isEqualTo("string");
    }
}