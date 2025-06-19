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
as JSON-LD and maintain interoperability between those approaches. Profiles that use JSON-LD are encouraged to provide
similar contexts that facilitate this approach to interoperability. As this specification's JSON-LD objects are 
`@protected`, profile authors are advised to define their custom terms as protected to spot term redefinition early.

## Exposure of Versions {#exposure-of-dataspace-protocol-versions}

[=Connectors=] implementing the [=Dataspace Protocol=] may operate on different versions and bindings. Therefore, it is
necessary that they can discover such information reliably and unambiguously. Each [=Connector=]
must provide a version metadata endpoint ending with URI segments `/.well-known/dspace-version`. The location of this 
endpoint should adhere to [[rfc8615]].

A [=Connector=] must respond to a respective HTTPS request by returning a [`VersionResponse`](#VersionResponse-table)
with at least one item. 

<p data-include="message/table/versionresponse.html" data-include-format="html">
</p>

The item connects the version tag (`version` attribute) with a path to the endpoint.
The semantics of the `path` property are specified by each protocol binding. The `serviceId` is a unique id for 
a [=Data Service=] and allows to group DSP-endpoints exposed by different [=Data Service=]s across versions. `binding`
describes the DSP protocol binding such as HTTPS. `identification` describes the type of identifier used to refer to
participants in the protocol communication.

<p data-include="message/table/version.html" data-include-format="html">
</p>

`auth` describes how a DSP endpoint is secured by means of the `protocol`, `version` strings and the `profile` array.


<p data-include="message/table/auth.html" data-include-format="html">
</p>


This data object must comply to the [JSON Schema](message/schema/protocol-version-schema.json). The requesting
[=Connector=] may select from the endpoints in the response. If the [=Connector=] can't identify a matching Dataspace
Protocol version, it must terminate the communication. The version endpoint MUST be unversioned and unauthenticated.

### HTTPS Binding

When using the DSP HTTPS binding, the `path` property is an absolute URL path segment to be appended to `<root>` for
discovery of all endpoints of this version. The concatenation of `<root>` and `path` yields `<base>`.

The following example demonstrates that a [=Connector=] offers the HTTPS binding from version `2024-1` at
`<root>/some/path/2024-1`, the `2025-1` endpoints at `<root>/some/path/2025-1` and another [=Connector=] on the same 
root URL under `<root>/different/path/2025-1` - some of which signal the relevant authentication protocol overlay. 
`<root>` in the examples below is _https://provider.com_ or _https://provider.com/path-to-root/_ respectively.

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
see [[?did-core]]. In this case, the Participant MUST add at least one entry to the DID document's `service` array 
adhering to the corresponding [JSON schema](message/schema/did-service-schema.json).

<aside class="example" title="Catalog Service Did Service Example">
    <pre class="json" data-include="message/example/catalogservice-did-service.json">
    </pre>
</aside>

<aside class="example" title="Data Service Did Service Example">
    <pre class="json" data-include="message/example/dataservice-did-service.json">
    </pre>
</aside>