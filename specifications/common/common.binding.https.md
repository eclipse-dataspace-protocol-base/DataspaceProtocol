# General Common Protocol Requirement Bindings

## The Well-Known Version Metadata Endpoint

Each implementation must provide the version metadata endpoint, which must use the `dspace-version` Well-Known Uniform Resource Identifier [[rfc8615]] at the top of the path hierarchy:

```
/.well-known/dspace-version
```

The contents of the response is a JSON object defined in section [[[#exposure-of-dataspace-protocol-versions]]].

Note that if multiple [=Connectors=] are hosted under the same base URL, a path segment appended to the base well-known URL can be used, for example, `https://example.com/.well-known/dspace-version/connector1.`

## Authorization

All requests to HTTPS endpoints should use the `Authorization` header to include an authorization token. The semantics of such tokens are not part of these specifications. The `Authorization` HTTP header is optional if the [=Connector=] does not require authorization.
