# Exposure of Versions {#exposure-of-dataspace-protocol-versions}

The adoption of versioning to implementations that comply with this specification is optional.

## Introduction 

[=Connectors=] implementing the [=Dataspace Protocol=] may operate on different versions and bindings. Such information should be discoverable reliably and unambiguously. If a [=Dataspace=] requires [=Connectors=] to expose supported versions, each [=Connector=] _MUST_ provide the version metadata endpoint using the `dspace-version` Well-Known Uniform Resource Identifier [[rfc8615]] at the top of the path hierarchy. Example: `<host>/.well-known/dspace-version`

A [=Connector=] _MUST_ respond to a respective HTTPS request by returning a [`VersionResponse`](#VersionResponse-table)
with at least one item. The item connects the version tag (`version` attribute) with a path to the endpoint.
The semantics of the `path` property are specified by each protocol binding.

This data object _MUST_ comply to the [JSON Schema](message/schema/protocol-version-schema.json). The requesting [=Connector=] _MAY_ select from the endpoints in the response. If the [=Connector=] cannot identify a matching [=Dataspace Protocol=] version, it _MUST_ terminate the communication. The version endpoint _MUST_ be unversioned and unauthenticated.

## HTTPS Binding

When using the DSP HTTPS binding, the `path` property is an absolute URL path segment to be appended to the domain for all endpoints of this version.

The following example demonstrates that a [=Connector=] offers the HTTPS binding from version `2024-1` at
`<host>/some/path/2024-1`, the `2025-1` endpoints at`<host>/some/path/2025-1` and another [=Connector=] on the same host under `<host>/different/path/2025-1` - some of which signal the relevant authentication protocol overlay, determined by `protocol`, `version`, and the `profile` array.

<aside class="example" title="well-known version endpoint (HTTPS)">
    <pre class="http">GET https://provider.com/.well-known/dspace-version
    </pre>
    <pre class="json" data-include="message/example/protocol-version.json">
    </pre>
</aside>
