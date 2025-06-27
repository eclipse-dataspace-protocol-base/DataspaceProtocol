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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaModelContextTest {
    private SchemaModelContext modelContext;

    @Test
    public void verifyResolveRelativeReference() {
        modelContext.addType(new SchemaType("Foo", "SchemaFile"));
        assertThat(modelContext.resolveType("Foo", "SchemaFile")).isNotNull();
        assertThat(modelContext.resolveType("#/definitions/Foo", "SchemaFile")).isNotNull();
    }

    @Test
    void verifyResolveReferenceToAnotherFile() {
        modelContext.addType(new SchemaType("Foo", "SchemaFile"));
        assertThat(modelContext.resolveType("SchemaFile#/definitions/Foo", "AnotherContext")).isNotNull();
        assertThat(modelContext.resolveType("SchemaFile#definitions/Foo", "AnotherContext")).isNotNull();
    }

    @Test
    void verifyPropertyTypeResolution() {
        var fooSchema = new SchemaType("Foo", "SchemaFile");
        var barSchema = new SchemaType("Bar", "SchemaFile");

        var fooProperty = SchemaProperty.Builder.newInstance()
                .name("foo")
                .types(Set.of("#/definitions/Foo"))
                .build();

        var fooArrayProperty = SchemaProperty.Builder.newInstance()
                .name("fooArray")
                .types(Set.of("array"))
                .itemTypes(Set.of(new ElementDefinition(ElementDefinition.Type.REFERENCE, "#/definitions/Foo")))
                .build();

        var stringProperty = SchemaProperty.Builder.newInstance()
                .name("stringProperty")
                .types(Set.of("string"))
                .build();


        barSchema.properties(List.of(fooProperty, fooArrayProperty, stringProperty));
        modelContext.addType(fooSchema);
        modelContext.addType(barSchema);
        modelContext.resolve();

        assertThat(fooProperty.getResolvedTypes().size()).isEqualTo(1);
        assertThat(fooProperty.getResolvedTypes()).contains(fooSchema);

        assertThat(fooArrayProperty.getItemTypes().iterator().next().getResolvedTypes()).contains(fooSchema);
        assertThat(stringProperty.getResolvedTypes()).contains(JsonTypes.STRING);
    }

    @Test
    void verifyAllOfTypeResolution() {
        var abstractFooSchema = new SchemaType("AbstractFoo", "SchemaFile");

        var abstractProperty = SchemaProperty.Builder.newInstance()
                .name("abstractProperty")
                .types(Set.of("#/definitions/Foo"))
                .build();

        var abstractRequiredProperty = SchemaProperty.Builder.newInstance()
                .name("abstractRequiredProperty")
                .types(Set.of("string"))
                .build();

        abstractFooSchema.properties(List.of(abstractProperty, abstractRequiredProperty));

        var fooSchema = new SchemaType("Foo", "SchemaFile");
        fooSchema.allOf(Set.of("#/definitions/AbstractFoo"));
        fooSchema.required(Set.of(new SchemaPropertyReference("abstractRequiredProperty")));

        modelContext.addType(abstractFooSchema);
        modelContext.addType(fooSchema);
        modelContext.resolve();

        assertThat(fooSchema.getTransitiveOptionalProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveOptionalProperties().iterator().next().getResolvedProperty()).isSameAs(abstractProperty);

        assertThat(fooSchema.getTransitiveRequiredProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveRequiredProperties().iterator().next().getResolvedProperty()).isSameAs(abstractRequiredProperty);
    }

    @Test
    void verifyOneOfTypeResolution() {
        var abstractFooSchema = new SchemaType("AbstractFoo", "SchemaFile");

        var abstractProperty = SchemaProperty.Builder.newInstance()
                .name("abstractProperty")
                .types(Set.of("#/definitions/Foo"))
                .build();

        var abstractRequiredProperty = SchemaProperty.Builder.newInstance()
                .name("abstractRequiredProperty")
                .types(Set.of("string"))
                .build();

        abstractFooSchema.properties(List.of(abstractProperty, abstractRequiredProperty));

        var fooSchema = new SchemaType("Foo", "SchemaFile");
        fooSchema.oneOf(Set.of("#/definitions/AbstractFoo"));
        fooSchema.required(Set.of(new SchemaPropertyReference("abstractRequiredProperty")));

        modelContext.addType(abstractFooSchema);
        modelContext.addType(fooSchema);
        modelContext.resolve();

        assertThat(fooSchema.getTransitiveOptionalProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveOptionalProperties().iterator().next().getResolvedProperty()).isSameAs(abstractProperty);

        assertThat(fooSchema.getTransitiveRequiredProperties().size()).isEqualTo(1);
        assertThat(fooSchema.getTransitiveRequiredProperties().iterator().next().getResolvedProperty()).isSameAs(abstractRequiredProperty);
    }

    @Test
    void verifyContainsTypeResolution() {
        var fooSchema = new SchemaType("Foo", "SchemaFile");
        var barSchema = new SchemaType("Bar", "SchemaFile");

        barSchema.contains(Set.of(new ElementDefinition(ElementDefinition.Type.REFERENCE, "#/definitions/Foo")));

        modelContext.addType(fooSchema);
        modelContext.addType(barSchema);
        modelContext.resolve();

        assertThat(barSchema.getContains().iterator().next().getResolvedTypes().iterator().next()).isEqualTo(fooSchema);
    }

    @BeforeEach
    void setUp() {
        modelContext = new SchemaModelContext();
    }
}