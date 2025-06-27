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

/**
 * Json Schema keywords.
 */
public interface JsonSchemaKeywords {
    String REF = "$ref";
    String ALL_OF = "allOf";
    String ANY_OF = "anyOf";
    String COMMENT = "$comment";
    String CONST = "const";
    String CONTAINS = "contains";
    String DEFINITIONS = "definitions";
    String ITEMS = "items";
    String NOT = "not";
    String ONE_OF = "oneOf";
    String PROPERTIES = "properties";
    String REQUIRED = "required";
    String TYPE = "type";
    String ENUM = "enum";
}
