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


import org.eclipse.dsp.generation.jsom.JsomParser;
import org.eclipse.dsp.generation.transformer.HtmlTableTransformer;
import org.eclipse.dsp.generation.transformer.SchemaTypeTransformer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Generates a table of schema properties to be included in the specification text.
 */
public class SchemaTableGeneratorPlugin implements Plugin<Project> {
    private static final String TASK_NAME = "generateTablesFromSchemas";
    public static final String CONFIG_NAME = "schemaTableGenerator";

    private final SchemaTypeTransformer<String> htmlTransformer = new HtmlTableTransformer();

    @Override
    public void apply(@NotNull Project project) {
        var extension = project.getExtensions().create(CONFIG_NAME, SchemaTableGeneratorPluginExtension.class);

        project.task(TASK_NAME).doLast(task -> {
            var tablesDir = task.getProject().getLayout().getBuildDirectory().dir("generated").get().dir("tables").getAsFile();
            //noinspection ResultOfMethodCallIgnored
            tablesDir.mkdirs();
            var sourceSet = requireNonNull(project.getExtensions()
                    .findByType(JavaPluginExtension.class)).getSourceSets().getByName("main");

            var prefix = extension.getSchemaPrefix();
            String resolvePath = getResolutionPath(sourceSet);

            // parse the schema object model
            var parser = new JsomParser(prefix, resolvePath);
            var stream = sourceSet.getResources().getFiles().stream()
                    .filter(f -> f.getName().endsWith(extension.getSchemaFileSuffix()));
            var schemaModel = parser.parseFiles(stream);

            schemaModel.getSchemaTypes().stream()
                    .filter(type -> !type.isRootDefinition() && !type.isJsonBaseType())  // do not process built-in Json types and root schema types
                    .forEach(type -> {
                        var content = htmlTransformer.transform(type);
                        var destination = new File(tablesDir, type.getName().toLowerCase() + ".html");
                        try (var writer = new FileWriter(destination)) {
                            writer.write(content);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            task.getLogger().info("Completed generation");
        });

    }

    private String getResolutionPath(SourceSet sourceSet) {
        var files = sourceSet.getResources().getSourceDirectories().getFiles();
        if (files.isEmpty()) {
            throw new IllegalStateException("No schema resource directories found");
        }
        var path = files.iterator().next();
        return path.getAbsolutePath().endsWith("/") ? path.getAbsolutePath() : path.getAbsolutePath() + "/";
    }


}
