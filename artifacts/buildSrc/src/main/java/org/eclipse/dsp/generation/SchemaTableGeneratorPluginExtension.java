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

package org.eclipse.dsp.generation;

/**
 * Defines the plugin configuration.
 */
public class SchemaTableGeneratorPluginExtension {
    private String schemaPrefix;
    private String schemaFileSuffix = "-schema.json";

    public SchemaTableGeneratorPluginExtension() {
    }

    public String getSchemaPrefix() {
        return schemaPrefix;
    }

    public void setSchemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
    }

    public String getSchemaFileSuffix() {
        return schemaFileSuffix;
    }

    public void setSchemaFileSuffix(String schemaFileSuffix) {
        this.schemaFileSuffix = schemaFileSuffix;
    }
}
