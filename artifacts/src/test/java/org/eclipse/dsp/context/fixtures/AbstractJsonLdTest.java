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

package org.eclipse.dsp.context.fixtures;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonp.JSONPModule;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.apicatalog.jsonld.JsonLd.compact;
import static com.apicatalog.jsonld.JsonLd.expand;
import static com.networknt.schema.SpecVersion.VersionFlag.V202012;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dsp.DspConstants.DSP_CONTEXT;
import static org.eclipse.dsp.DspConstants.DSP_PREFIX;
import static org.eclipse.dsp.DspConstants.ODRL_CONTEXT;

/**
 * Base class for Json-Ld expansion and compaction tests.
 */
public abstract class AbstractJsonLdTest {
    protected ObjectMapper mapper;
    protected JsonStructure compactionContext;
    protected JsonLdOptions options;

    private static final String CLASSPATH_SCHEMA = "classpath:/";
    private static final String CONTEXT_REFERENCE = format("{\"@context\": [\"%s\"]}", DSP_CONTEXT);

    protected void verifyRoundTrip(String jsonFile, String schemaFile) {
        try {
            var stream = getClass().getResourceAsStream(jsonFile);
            var message = mapper.readValue(stream, JsonObject.class);
            var expanded = expand(JsonDocument.of(message)).options(options).get();
            var compacted = compact(JsonDocument.of(expanded), JsonDocument.of(compactionContext)).options(options).get();

            var schemaFactory = JsonSchemaFactory.getInstance(V202012, builder ->
                    builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix(DSP_PREFIX, CLASSPATH_SCHEMA))
            );

            var schema = schemaFactory.getSchema(SchemaLocation.of(DSP_PREFIX + schemaFile));
            var result = schema.validate(mapper.convertValue(compacted, JsonNode.class));
            assertThat(result.isEmpty()).isTrue();
        } catch (IOException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JSONPModule());

        try (var dspaceStream = getClass().getResourceAsStream("/context/dspace.jsonld");
                var odrlStream = getClass().getResourceAsStream("/context/odrl.jsonld")) {
            var dspaceContext = mapper.readValue(dspaceStream, JsonObject.class);
            @SuppressWarnings("DataFlowIssue")
            Map<String, Document> cache = Map.of(DSP_CONTEXT, JsonDocument.of(dspaceContext),
                    ODRL_CONTEXT, JsonDocument.of(odrlStream)
            );
            var documentLoader = new LocalDocumentLoader(cache);
            compactionContext = mapper.readValue(CONTEXT_REFERENCE, JsonStructure.class);
            options = new JsonLdOptions();
            options.setDocumentLoader(documentLoader);
        } catch (IOException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private static class LocalDocumentLoader implements DocumentLoader {
        private final Map<String, Document> contexts = new HashMap<>();

        LocalDocumentLoader(Map<String, Document> contexts) {
            this.contexts.putAll(contexts);
        }

        @Override
        public Document loadDocument(URI url, DocumentLoaderOptions options) {
            return contexts.get(url.toString());
        }
    }
}
