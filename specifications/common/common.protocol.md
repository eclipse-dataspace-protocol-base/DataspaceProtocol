# Common Requirements {#requirements}

## Authorization

All requests to HTTPS endpoints should use the `Authorization` header to include an authorization token. The semantics
of such tokens are not part of these specifications. The `Authorization` HTTP header is optional if the [=Connector=]
does not require authorization.

## Schemas & Contexts

All protocol [=Messages=] are normatively defined by a [[json-schema]]. This specification also uses JSON-LD 1.1 and
provides a JSON-LD context to serialize data structures and [=Message=] types as it facilitates extensibility. The
JSON-LD context is designed to produce [=Message=] serializations using compaction that validate against the Json Schema
for the given [=Message=] type. This allows implementations to choose whether to process [=Messages=] as plain JSON or
as JSON-LD and maintain interoperability between those approaches. Extensions that use JSON-LD are encouraged to provide
similar contexts that facilitate this approach to interoperability.

## Exposure of Versions {#exposure-of-dataspace-protocol-versions}

[=Connectors=] implementing the [=Dataspace Protocol=] may operate on different versions and bindings. Therefore, it is
necessary that they can discover such information reliably and unambiguously. Each [=Connector=]
must provide a version metadata endpoint `<host>/.well-known/dspace-version`. The location of this endpoint should 
adhere to the [[rfc8615]]. 

A [=Connector=] must respond to a respective HTTPS request by returning a [`VersionResponse`](#VersionResponse-table)
with at least one item. The item connects the version tag (`version` attribute) with a path to the endpoint.
The semantics of the `path` property are specified by each protocol binding.

This data object must comply to the [JSON Schema](message/schema/protocol-version-schema.json). The requesting
[=Connector=] may select from the endpoints in the response. If the [=Connector=] can't identify a matching Dataspace
Protocol version, it must terminate the communication. The version endpoint MUST be unversioned and unauthenticated.

### HTTPS Binding

When using the DSP HTTPS binding, the `path` property is an absolute URL path segment to be appended to the domain for
all endpoints of this version.

The following example demonstrates that a [=Connector=] offers the HTTPS binding from version `2024-1` at
`<host>/some/path/2024-1`, the `2025-1` endpoints at `<host>/some/path/2025-1` and another [=Connector=] on the same host
under `<host>/<path-to-root>/different/path/2025-1` - some of which signal the relevant authentication protocol overlay, 
determined by `protocol`, `version` and the `profile` array.

<aside class="example" title="Well-known Version Endpoint (HTTPS) at different root path">
    <pre class="http">GET https://provider.com/.well-known/dspace-version
    </pre>
    OR
    <pre class="http">GET https://provider.com/path-to-root/.well-known/dspace-version</pre>
    <pre class="json" data-include="message/example/protocol-version.json">
    </pre>
</aside>

## Discovery of Service Endpoints

A Participant may publicize their [=Data Services=], i.e., [=Connectors=], or [=Catalog Services=] via a DID document, 
see [[?did-core]]. In that case, the Participant MUST add at least one entry to the DID document's `service` array 
adhering to the corresponding [JSON schema](message/schema/did-service-schema.json).

<aside class="example" title="Catalog Service Did Service Example">
    <pre class="json" data-include="message/example/catalogservice-did-service.json">
    </pre>
</aside>

<aside class="example" title="Data Service Did Service Example">
    <pre class="json" data-include="message/example/dataservice-did-service.json">
    </pre>
</aside>