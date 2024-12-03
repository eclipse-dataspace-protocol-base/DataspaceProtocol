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
 * The Json Schema type system.
 */
public interface JsonTypes {

    SchemaType STRING = new SchemaType("string");

    SchemaType NUMBER = new SchemaType("number");

    SchemaType INTEGER = new SchemaType("integer");

    SchemaType OBJECT = new SchemaType("object");

    SchemaType ARRAY = new SchemaType("array");

    SchemaType BOOLEAN = new SchemaType("boolean");

    SchemaType NULL = new SchemaType("null");

    SchemaType ANY = new SchemaType("any");

}
