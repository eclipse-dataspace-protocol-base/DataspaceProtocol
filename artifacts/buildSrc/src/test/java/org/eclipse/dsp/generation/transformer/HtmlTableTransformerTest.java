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

package org.eclipse.dsp.generation.transformer;

import org.eclipse.dsp.generation.jsom.ElementDefinition;
import org.eclipse.dsp.generation.jsom.SchemaProperty;
import org.eclipse.dsp.generation.jsom.SchemaPropertyReference;
import org.eclipse.dsp.generation.jsom.SchemaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dsp.generation.jsom.JsonTypes.ARRAY;
import static org.eclipse.dsp.generation.jsom.JsonTypes.STRING;

class HtmlTableTransformerTest {
    private HtmlTableTransformer transformer;

    @Test
    void verifyTransform() {
        var barType = new SchemaType("Bar", "object", "https://test.com/foo");
        var fooType = new SchemaType("Foo", "object", "https://test.com/foo");

        var prop1 = SchemaProperty.Builder.newInstance()
                .name("prop1")
                .types(Set.of("#/definitions/Bar"))
                .build();
        prop1.resolvedType(barType);

        var prop2 = SchemaProperty.Builder.newInstance()
                .name("prop2")
                .types(Set.of(STRING.getName()))
                .build();
        prop2.resolvedType(STRING);

        var arrayElement = new ElementDefinition(ElementDefinition.Type.REFERENCE, "#/definitions/Bar");
        arrayElement.resolvedType(barType);
        var prop3 = SchemaProperty.Builder.newInstance()
                .name("prop3")
                .types(Set.of(ARRAY.getName()))
                .itemTypes(Set.of(arrayElement))
                .build();
        prop3.resolvedType(ARRAY);

        var propertyReference = new SchemaPropertyReference("prop3");
        propertyReference.resolvedProperty(prop3);

        fooType.properties(List.of(prop1, prop2, prop3));
        fooType.required(Set.of(propertyReference));

        fooType.resolvePropertyReferences();
        barType.resolvePropertyReferences();

        var result = transformer.transform(fooType);

        assertThat(result.contains("<td class=\"message-class\" colspan=\"4\">Foo</td><")).isTrue(); // verify type name
        assertThat(result.contains("<td>string</td>")).isTrue(); // verify property type names are included
        assertThat(result.contains("array[Bar]")).isTrue();  // verify array type names are included
    }

    @BeforeEach
    void setUp() {
        transformer = new HtmlTableTransformer();
    }
}