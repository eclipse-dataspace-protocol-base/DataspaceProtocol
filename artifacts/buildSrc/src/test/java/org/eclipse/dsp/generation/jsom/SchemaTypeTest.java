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

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaTypeTest {

    @Test
    void verifyResolvePropertyReferences() {
        var fooSchema = new SchemaType("Bar", "SchemaFile");

        var requiredProperty = SchemaProperty.Builder.newInstance()
                .name("requiredProperty")
                .types(Set.of("string"))
                .build();

        var optionalProperty = SchemaProperty.Builder.newInstance()
                .name("optionalProperty")
                .types(Set.of("string"))
                .build();

        fooSchema.properties(List.of(requiredProperty, optionalProperty));
        fooSchema.required(Set.of(new SchemaPropertyReference("requiredProperty")));

        fooSchema.resolvePropertyReferences();

        assertThat(fooSchema.getTransitiveRequiredProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveRequiredProperties().iterator().next().getResolvedProperty()).isEqualTo(requiredProperty);

        assertThat(fooSchema.getTransitiveOptionalProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveOptionalProperties().iterator().next().getResolvedProperty()).isEqualTo(optionalProperty);

    }

    @Test
    void verifyTransitiveProperties() {
        var abstractFooSchema = new SchemaType("AbstractFoo", "SchemaFile");

        var abstractProperty = SchemaProperty.Builder.newInstance()
                .name("abstractProperty")
                .types(Set.of("string"))
                .build();

        var abstractRequiredProperty = SchemaProperty.Builder.newInstance()
                .name("abstractRequiredProperty")
                .types(Set.of("string"))
                .build();

        abstractFooSchema.properties(List.of(abstractProperty, abstractRequiredProperty));

        var fooSchema = new SchemaType("Foo", "SchemaFile");
        var abstractRequiredReference = new SchemaPropertyReference("abstractRequiredProperty");
        fooSchema.required(Set.of(abstractRequiredReference));
        fooSchema.resolvedAllOfType(abstractFooSchema);

        abstractFooSchema.resolvePropertyReferences();
        fooSchema.resolvePropertyReferences();

        assertThat(fooSchema.getTransitiveRequiredProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveRequiredProperties().iterator().next()).isSameAs(abstractRequiredReference);

        assertThat(fooSchema.getTransitiveOptionalProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveOptionalProperties().iterator().next().getName()).isSameAs(abstractProperty.getName());

    }
}