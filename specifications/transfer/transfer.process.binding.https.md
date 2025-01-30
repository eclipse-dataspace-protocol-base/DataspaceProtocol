# Transfer Process HTTPS Binding

This specification defines a RESTful API over HTTPS for the [Transfer Process Protocol](#transfer-process-protocol).

## Introduction

### Prerequisites

1. The `<base>` notation indicates the base URL for a [=Connector=] endpoint. For example, if the scheme is `https` and
   the full hostname is `connector.example.com`, the URL `<base>/transfers/request` will map
   to `https://connector.example.com/transfers/request`.
2. All request and response messages must use the `application/json` media type. Derived media types,
   e.g., `application/ld+json` may be exposed in addition.

### Transfer Error

In the event of a client request error, the [=Connector=] must return an appropriate HTTP 4xx client error code. If an
error body is returned it must be a [Transfer Error](#error-transfer-error).

#### State Transition Errors

If a client or [=Provider=] makes a request that results in an invalid [=Transfer Process=] (TP) state transition as
defined by the Transfer Process Protocol, it must return an HTTP code 400 (Bad Request) with
a [Transfer Error](#error-transfer-error) in the response body.

#### Object Not Found

If the TP does not exist, the [=Consumer=] or [=Provider=] must return an HTTP 404 (Not Found) response.

#### Unauthorized Access

If the client is not authorized, the [=Consumer=] or [=Provider=] must return an HTTP 404 (Not Found) response.

## Provider Path Bindings

| Endpoint                                                | Method | Description                                   |
|:--------------------------------------------------------|:-------|:----------------------------------------------|
| https://provider.com/transfers/:providerPid             | `GET`  | [[[#transfers-get]]]                          |
| https://provider.com/transfers/request                  | `POST` | [[[#transfers-request-post]]]                 |
| https://provider.com/transfers/:providerPid/start       | `POST` | [[[#transfers-providerpid-start-post]]]       |
| https://provider.com/transfers/:providerPid/completion  | `POST` | [[[#transfers-providerpid-completion-post]]]  |
| https://provider.com/transfers/:providerPid/termination | `POST` | [[[#transfers-providerpid-termination-post]]] |
| https://provider.com/transfers/:providerPid/suspension  | `POST` | [[[#transfers-providerpid-suspension-post]]]  |

### The `transfers` Endpoint _(Provider-side)_

#### GET {#transfers-get}

##### Request

A CN can be accessed by a [=Consumer=] or [=Provider=] sending a GET request to `transfers/:providerPid`:

<aside class="example" title="Get Transfers Request">
    <pre class="http">GET https://provider.com/transfers/:providerPid
Authorization: ...</pre>
</aside>

##### Response

If the TP is found and the client is authorized, the [=Provider=] must return an HTTP 200 (OK) response and a body
containing the [Transfer Process](#ack-transfer-process):

<aside class="example" title="Transfer Process Response">
    <pre class="http">GET https://provider.com/transfers/:providerPid
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-process.json">
    </pre>
</aside>

Predefined states are: `REQUESTED`, `STARTED`, `SUSPENDED`, `REQUESTED`, `COMPLETED`, and `TERMINATED`.

### The `transfers/request` Endpoint _(Provider-side)_

#### POST {#transfers-request-post}

##### Request

A TP is started and placed in the `REQUESTED` state when a [=Consumer=] POSTs
a [Transfer Request Message](#transfer-request-message) to `transfers/request`:

<aside class="example" title="Transfer Request">
    <pre class="http">POST https://provider.com/transfers/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-request-message.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives messages associated with
  the TP. Support for the `HTTPS` scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note
  that [=Providers=] should properly handle the cases where a trailing `/` is included
  with or absent from the `callbackAddress` when resolving full URL.

##### Response

The [=Provider=] must return an HTTP 201 (Created) response with a body containing
the [Transfer Process](#ack-transfer-process):

<aside class="example" title="Transfer Process Response">
    <pre class="json" data-include="message/example/transfer-process.json">
    </pre>
</aside>

### The `transfers/:providerPid/start` Endpoint _(Provider-side)_

#### POST {#transfers-providerpid-start-post}

##### Request

The [=Consumer=] can POST a [Transfer Start Message](#transfer-start-message) to
attempt to start a TP after it has been suspended:

<aside class="example" title="Transfer Start Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/start
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-start-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Provider=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:providerPid/completion` Endpoint _(Provider-side)_

#### POST {#transfers-providerpid-completion-post}

##### Request

The [=Consumer=] can POST a [Transfer Completion Message](#transfer-completion-message)
to complete a TP:

<aside class="example" title="Transfer Completion Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/completion
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-completion-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Provider=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:providerPid/termination` Endpoint _(Provider-side)_

#### POST {#transfers-providerpid-termination-post}

##### Request

The [=Consumer=] can POST
a [Transfer Termination Message](#transfer-termination-message) to terminate a TP:

<aside class="example" title="Transfer Termination Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-termination-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Provider=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:providerPid/suspension` Endpoint _(Provider-side)_

#### POST {#transfers-providerpid-suspension-post}

##### Request

The [=Consumer=] can POST a [Transfer Suspension Message](#transfer-suspension-message)
to suspend a TP:

<aside class="example" title="Transfer Suspension Request">
    <pre class="http">POST https://provider.com/transfers/:providerPid/suspension
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-suspension-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Provider=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

## Consumer Callback Path Bindings

| Endpoint                                                          | Method | Description                                   |
|:------------------------------------------------------------------|:-------|:----------------------------------------------|
| https://consumer.com/:callback/transfers/:consumerPid/start       | `POST` | [[[#transfers-consumerpid-start-post]]]       |
| https://consumer.com/:callback/transfers/:consumerPid/completion  | `POST` | [[[#transfers-consumerpid-completion-post]]]  |
| https://consumer.com/:callback/transfers/:consumerPid/termination | `POST` | [[[#transfers-consumerpid-termination-post]]] |
| https://consumer.com/:callback/transfers/:consumerPid/suspension  | `POST` | [[[#transfers-consumerpid-suspension-post]]] |

### Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in
the [Transfer Request Message](#transfer-request-message) that initiated a TP. For
example, if the `callbackAddress` is specified as `https://consumer.com/callback` and a callback path binding
is `transfers/:consumerPid/start`, the resolved URL will
be `https://consumer.com/callback/transfers/:consumerPid/start`.

### The `transfers/:consumerPid/start` Endpoint _(Consumer-side)_

#### POST {#transfers-consumerpid-start-post}

##### Request

The [=Provider=] can POST a [Transfer Start Message](#transfer-start-message) to
indicate the start of a TP:

<aside class="example" title="Transfer Start Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/start
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-start-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:consumerPid/completion` Endpoint _(Consumer-side)_

#### POST {#transfers-consumerpid-completion-post}

##### Request

The [=Provider=] can POST a [Transfer Completion Message](#transfer-completion-message)
to complete a TP:

<aside class="example" title="Transfer Completion Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/completion
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-completion-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:consumerPid/termination` Endpoint _(Consumer-side)_

#### POST {#transfers-consumerpid-termination-post}

##### Request

The [=Provider=] can POST
a [Transfer Termination Message](#transfer-termination-message) to terminate a TP:

<aside class="example" title="Transfer Termination Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-termination-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `transfers/:consumerPid/suspension` Endpoint _(Consumer-side)_

#### POST {#transfers-consumerpid-suspension-post}

##### Request

The [=Provider=] can POST a [Transfer Suspension Message](#transfer-suspension-message)
to suspend a TP:

<aside class="example" title="Transfer Suspension Request">
    <pre class="http">POST https://consumer.com/:callback/transfers/:consumerPid/suspension
Authorization: ...</pre>
    <pre class="json" data-include="message/example/transfer-suspension-message.json">
    </pre>
</aside>

##### Response

If the TP's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it. 
