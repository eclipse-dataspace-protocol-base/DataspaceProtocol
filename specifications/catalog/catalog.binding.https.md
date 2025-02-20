# Catalog HTTPS Binding {#catalog-http}

This specification defines a RESTful API over HTTPS for the [=Catalog Protocol=].

## Introduction

### Prerequisites

1. The `<base>` notation indicates the base URL for a [=Catalog Service=] endpoint. For example, if the base [=Catalog=]
   URL is `api.example.com`, the URL `https://<base>/catalog/request` will map
   to `https//api.example.com/catalog/request`.

2. All request and response [=Messages=] must use the `application/json` media type.

### Catalog Error

In the event of a request error, the [=Catalog Service=] must return an appropriate HTTP code and
a [Catalog Error](#error-catalog-error) in the response body.

## Path Bindings

| Endpoint                                  | Method | Description                 |
|:------------------------------------------|:-------|:----------------------------|
| https://provider.com/catalog/request      | `POST` | [[[#catalog-request-post]]] |
| https://provider.com/catalog/datasets/:id | `GET`  | [[[#catalog-datasets-get]]] |

### Catalog Request Endpoint {#catalog-request-post}

**Request**

The [Catalog Request Message](#catalog-request-message) corresponds to `POST https://<base>/catalog/request`:

<aside class="example" title="Catalog Request Message Request">
    <pre class="http">POST https://provider.com/catalog/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/catalog-request-message.json">
    </pre>
</aside>

- The `Authorization` header is optional if the [=Catalog Service=] does not require authorization. If present, the
  contents of the `Authorization` header are detailed in
  the [Authorization section](../common/common.binding.https.md#2-authorization).

- The `filter` property is optional. If present, the `filter` property can contain an implementation-specific filter
  expression or query to be executed as part of the [=Catalog=] request. If a filter expression is not supported by an
  implementation, it must return a HTTP 400 (Bad Request) response.

**Response**

If the request is successful, the [=Catalog Service=] must return an HTTP 200 (OK) with a response body containing
a [Catalog](#ack-catalog).

<aside class="example" title="Catalog Response">
    <pre class="json" data-include="message/example/catalog.json">
    </pre>
</aside>

### Dataset Request Endpoint {#catalog-datasets-get}

**Request**

The [Dataset Request Message](#dataset-request-message) corresponds
to `GET https://<base>/catalog/datasets/:id}`:

<aside class="example" title="Dataset Request Message Request">
    <pre class="http">GET https://provider.com/catalog/datasets/{id}
Authorization: ...</pre>
    </pre>
</aside>

- The `Authorization` header is optional if the [=Catalog Service=] does not require authorization. If present, the
  contents of the `Authorization` header are detailed in
  the [[[#authorization]]].

**Response**

If the request is successful, the [=Catalog Service=] must return an HTTP 200 (OK) with a response body containing
a [Dataset](#ack-dataset).

<aside class="example" title="Dataset Response">
    <pre class="json" data-include="message/example/dataset.json">
    </pre>
</aside>

## Technical Considerations

### Pagination

A [=Catalog Service=] may paginate the results of a [Catalog Request Message](#catalog-request-message). Pagination data
must be specified using [Web Linking](https://datatracker.ietf.org/doc/html/rfc5988) and the HTTP `Link` header.
The `Link` header will contain URLs for navigating to previous and subsequent results. Only the `next` and `previous`
link relation types must be supported.
Note that the content and structure of the link query parameters is not defined by the current specification.

The following request sequence demonstrates pagination:

```http request
Link: <https://provider.com/catalog?continuationToken=f59892315ac44de8ab4bdc9014502d52>; rel="next"

{
  "@context":  "https://w3id.org/dspace/2025/1/context.jsonld",
  "@type": "Catalog",
  ...
}
```

Second page response:

```http request
Link: <https://provider.com/catalog?continuationToken=a59779015bn44de8ab4bfc9014502d53>; rel="previous"
Link: <https://provider.com/catalog?continuationToken=f59892315ac44de8ab4bdc9014502d52>; rel="next"

{
   "@type": "Catalog",
   ...
}
```

Last page response:

```http request
Link: <https://provider.com/catalog?continuationToken=bn9556075bn44de8ab4bfc9014582t76>; rel="previous"

{
   "@type": "Catalog",
   ...
}
```

### Compression

[=Catalog Services=] MAY compress responses to
a [Catalog Request](#catalog-request-message) by setting the `Content-Encoding` header to `gzip` as described in
the [HTTP 1.1 Specification](https://www.rfc-editor.org/rfc/rfc9110.html#name-gzip-coding).

