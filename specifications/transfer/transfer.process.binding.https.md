# Transfer Process HTTPS Binding {#transfer-http}

## Introduction

This binding defines a RESTful API over HTTPS for the [Transfer Process Protocol](#transfer-process-protocol).

### Prerequisites

1. The `<base>` notation indicates the base URL for a [=Connector=] endpoint. For example, if the scheme is `https` and
   the full hostname is `connector.example.com`, the URL `<base>/transfers/request` will map
   to `https://connector.example.com/transfers/request`.
2. All request and response [=Messages=] _MUST_ use the `application/json` media type. Derived media types,
   e.g., `application/ld+json` _MAY_ be exposed in addition.

### Transfer Error

In the event of a client request error, the [=Connector=] _MUST_ return an appropriate HTTP 4xx client error code. If an
error body is returned it _MUST_ be a [Transfer Error](#error-transfer-error).

#### State Transition Errors

If a client or [=Provider=] makes a request that results in an invalid [=Transfer Process=] state transition as
defined by the [=Transfer Process Protocol=], it _MUST_ return an HTTP code 400 (Bad Request) with
a [Transfer Error](#error-transfer-error) in the response body.

#### Object Not Found

If the [=Transfer Process=] does not exist, the [=Consumer=] or [=Provider=] _MUST_ return an HTTP 404 (Not Found) response.

#### Unauthorized Access

If the client is not authorized, the [=Consumer=] or [=Provider=] _MUST_ return an HTTP 404 (Not Found) response.

## Provider Path Bindings

| Endpoint                                      | Method | Path                                  |
|:----------------------------------------------|:-------|:--------------------------------------|
| [[[#transfers-get]]]                          | `GET`  | `/transfers/:providerPid`             |
| [[[#transfers-request-post]]]                 | `POST` | `/transfers/request`                  |
| [[[#transfers-providerpid-start-post]]]       | `POST` | `/transfers/:providerPid/start`       |
| [[[#transfers-providerpid-completion-post]]]  | `POST` | `/transfers/:providerPid/completion`  |
| [[[#transfers-providerpid-termination-post]]] | `POST` | `/transfers/:providerPid/termination` |
| [[[#transfers-providerpid-suspension-post]]]  | `POST` | `/transfers/:providerPid/suspension`  |

### Transfer Process Endpoint {#transfers-get}

**Request**

A [=Contract Negotiation=] can be accessed by a [=Consumer=] or [=Provider=] sending a GET request to `transfers/:providerPid`:

<aside class="example" title="Get Transfers Request">
    <pre class="http">GET https://provider.com/transfers/:providerPid
Authorization: ...</pre>
</aside>

**Response**

If the [=Transfer Process=] is found and the client is authorized, the [=Provider=] _MUST_ return an HTTP 200 (OK) response and a body
containing the [Transfer Process](#ack-transfer-process):

<aside class="example" title="Transfer Process Response">
    <pre class="http">GET https://provider.com/transfers/:providerPid
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-process.json">
    </pre>
</aside>

Predefined states are: `REQUESTED`, `STARTED`, `SUSPENDED`, `REQUESTED`, `COMPLETED`, and `TERMINATED` (
see [[[#transfer-process-states]]]).

### Transfer Request Endpoint {#transfers-request-post}

**Request**

A [=Transfer Process=] is started and placed in the `REQUESTED` state when a [=Consumer=] POSTs
a [Transfer Request Message](#transfer-request-message) to `transfers/request`:

<aside class="example" title="Transfer Request">
    <pre class="http">POST https://provider.com/transfers/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-request-message.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives messages associated with
  the [=Transfer Process=]. The HTTPS scheme _MUST_ be supported. Implementations _MAY_ optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note
  that [=Providers=] _SHOULD_ properly handle the cases where a trailing `/` is included
  with or absent from the `callbackAddress` when resolving full URL.

**Response**

The [=Provider=] _MUST_ return an HTTP 201 (Created) response with a body containing
the [Transfer Process](#ack-transfer-process):

<aside class="example" title="Transfer Process Response">
    <pre class="json" data-include="message/example/transfer-process.json">
    </pre>
</aside>

### Transfer Start Endpoint {#transfers-providerpid-start-post}

**Request**

The [=Consumer=] can POST a [Transfer Start Message](#transfer-start-message) to
attempt to start a [=Transfer Process=] after it has been suspended:

<aside class="example" title="Transfer Start Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/start
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-start-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Provider=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Completion Endpoint {#transfers-providerpid-completion-post}

**Request**

The [=Consumer=] can POST a [Transfer Completion Message](#transfer-completion-message)
to complete a [=Transfer Process=]:

<aside class="example" title="Transfer Completion Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/completion
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-completion-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Provider=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Termination Endpoint {#transfers-providerpid-termination-post}

**Request**

The [=Consumer=] can POST
a [Transfer Termination Message](#transfer-termination-message) to terminate a [=Transfer Process=]:

<aside class="example" title="Transfer Termination Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-termination-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Provider=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Suspension Endpoint {#transfers-providerpid-suspension-post}

**Request**

The [=Consumer=] can POST a [Transfer Suspension Message](#transfer-suspension-message)
to suspend a [=Transfer Process=]:

<aside class="example" title="Transfer Suspension Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/suspension
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-suspension-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Provider=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

## Consumer Callback Path Bindings

| Endpoint                                      | Method | Path                                            |
|:----------------------------------------------|:-------|:------------------------------------------------|
| [[[#transfers-consumerpid-start-post]]]       | `POST` | `/:callback/transfers/:consumerPid/start`       |
| [[[#transfers-consumerpid-completion-post]]]  | `POST` | `/:callback/transfers/:consumerPid/completion`  |
| [[[#transfers-consumerpid-termination-post]]] | `POST` | `/:callback/transfers/:consumerPid/termination` |
| [[[#transfers-consumerpid-suspension-post]]]  | `POST` | `/:callback/transfers/:consumerPid/suspension`  |

### Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in
the [Transfer Request Message](#transfer-request-message) that initiated a [=Transfer Process=]. For
example, if the `callbackAddress` is specified as `https://consumer.com/:callback` and a callback path binding
is `transfers/:consumerPid/start`, the resolved URL will
be `https://consumer.com/:callback/transfers/:consumerPid/start`.

The `:callback` _MAY_ be chosen freely by the implementations.

### Transfer Start Endpoint {#transfers-consumerpid-start-post}

**Request**

The [=Provider=] can POST a [Transfer Start Message](#transfer-start-message) to
indicate the start of a [=Transfer Process=]:

<aside class="example" title="Transfer Start Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/start
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-start-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Consumer=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Completion Endpoint {#transfers-consumerpid-completion-post}

**Request**

The [=Provider=] can POST a [Transfer Completion Message](#transfer-completion-message)
to complete a [=Transfer Process=]:

<aside class="example" title="Transfer Completion Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/completion
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-completion-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Consumer=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Termination Endpoint {#transfers-consumerpid-termination-post}

**Request**

The [=Provider=] can POST
a [Transfer Termination Message](#transfer-termination-message) to terminate a [=Transfer Process=]:

<aside class="example" title="Transfer Termination Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-termination-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Consumer=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it.

### Transfer Suspension Endpoint {#transfers-consumerpid-suspension-post}

**Request**

The [=Provider=] can POST a [Transfer Suspension Message](#transfer-suspension-message)
to suspend a [=Transfer Process=]:

<aside class="example" title="Transfer Suspension Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/suspension
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-suspension-message.json">
    </pre>
</aside>

**Response**

If the [=Transfer Process=]'s state is successfully transitioned, the [=Consumer=] _MUST_ return HTTP code 200 (OK). The response body is
not specified and clients _MUST NOT_ process it. 
