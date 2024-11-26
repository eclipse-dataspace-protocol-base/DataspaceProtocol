# General Common Protocol Requirements

## Exposure of Dataspace Protocol Versions

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

This data object must comply to the [JSON Schema](schema/version-schema.json) and
the [SHACL Shape](shape/version-shape.ttl).

The requesting [=Connector=] may select from the endpoints in the response. If the [=Connector=] can't identify a
matching Dataspace Protocol Version, it must terminate the communication. 