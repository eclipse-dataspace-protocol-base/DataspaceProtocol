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