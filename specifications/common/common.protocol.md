# General Common Protocol Requirements

## Exposure of Dataspace Protocol Versions

### Generic Definition

[=Connectors=] implementing the Dataspace Protocol may operate on different versions. Therefore, it is necessary that
they can discover the supported versions of each other reliably and unambiguously. Each [=Connector=] must expose
information of at least one Dataspace Protocol Version it supports. The specifics of how this information is obtained
its defined by specific protocol bindings.

A [=Connector=] must respond to a respective request by providing a JSON object containing an array of supported
versions with at least one item. The item connects the version tag (`version` attribute) with the absolute URL path
segment of the root path for all endpoints of this version. The following example specifies that this [=Connector=]
offers version `1.0` endpoints at `<host>/some/path/v1`.

```json
{
  "protocolVersions": [
    {
      "version": "1.0",
      "path": "/some/path/v1"
    }
  ]
}
```

This data object must comply to the [JSON Schema](schema/version-schema.json). The requesting [=Connector=] may select
from the endpoints in the response. If the [=Connector=] can't identify a matching Dataspace Protocol Version, it must
terminate the communication.

### HTTP Binding

#### The Well-Known Version Metadata Endpoint

Each implementation must provide the version metadata endpoint, which must use the `dspace-version` Well-Known Uniform
Resource Identifier [[rfc8615]] at the top of the path hierarchy:

```
/.well-known/dspace-version
```

The contents of the response is a JSON object defined in section [[[#exposure-of-dataspace-protocol-versions]]].

Note that if multiple [=Connectors=] are hosted under the same base URL, a path segment appended to the base well-known
URL can be used, for example, `https://example.com/.well-known/dspace-version/connector1.`

#### Authorization

All requests to HTTPS endpoints should use the `Authorization` header to include an authorization token. The semantics
of such tokens are not part of these specifications. The `Authorization` HTTP header is optional if the [=Connector=]
does not require authorization.

## Schemas, Contexts, and Message Processing

All protocol messages are normatively defined by a [json-schema]. This specification also uses JSON-LD 1.1 and provides
a JSON-LD context to serialize data structures and message types as it facilitates extensibility. The JSON-LD context is
designed to produce message serializations using compaction that validate against the Json Schema for the given message
type. This allows implementations to choose whether to process messages as plain Json or as JSON-LD and maintain
interoperability between those approaches. Extensions that use JSON-LD are encouraged to provide similar contexts that
facilitate this approach to interoperability.
