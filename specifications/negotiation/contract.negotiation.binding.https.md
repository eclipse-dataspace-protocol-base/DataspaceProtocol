# Contract Negotiation HTTPS Binding {#negotiation-http}

## Introduction

This binding defines a RESTful API over HTTPS for the [Contract Negotiation Protocol](#contract-negotiation-protocol).

### Prerequisites

1. The `<base>` notation indicates the base URL for a [=Connector=] endpoint. For example, if the base [=Connector=] URL
   is `connector.example.com`, the URL `https://<base>/negotiations/request` will map
   to `https//connector.example.com/negotiation/request`.
2. All request and response [=Messages=] MUST use the `application/json` media type. Derived media types,
   e.g., `application/ld+json` MAY be exposed in addition.

### Contract Negotiation Error

In the event of a client request error, the [=Connector=] MUST return an appropriate HTTP 4xx client error code. If an
error body is returned, it MUST be a [Contract Negotiation Error](#contract-negotiation-error).

#### State Transition Errors

If a client makes a request that results in an
invalid [state transition as defined by the [=Contract Negotiation Protocol=]](#contract-negotiation-states), it MUST return
an HTTP code 400 (Bad Request) with a [Contract Negotiation Error](#contract-negotiation-error) in the response body.

#### Object Not Found

If the [=Contract Negotiation=] does not exist, the [=Consumer=] or [=Provider=] MUST return an HTTP 404 (Not
Found) response.

#### Unauthorized Access

If the client is not authorized, the [=Consumer=] or [=Provider=] MUST return an HTTP 404 (Not Found) response.

### Authorization

All requests SHOULD use the `Authorization` header to include an authorization token. The semantics of such tokens are
not part of this specification. The `Authorization` HTTP header is OPTIONAL if the [=Connector=] does not require
authorization.

## Provider Path Bindings

| Endpoint                                                    | Method | Path                                                |
|:------------------------------------------------------------|:-------|:----------------------------------------------------|
| [[[#negotiations-get-provider]]]                                     | `GET`  | `/negotiations/:providerPid`                        |
| [[[#negotiations-request-post]]]                            | `POST` | `/negotiations/request`                             |
| [[[#negotiations-providerpid-request-post]]]                | `POST` | `/negotiations/:providerPid/request`                |
| [[[#negotiations-providerpid-events-post]]]                 | `POST` | `/negotiations/:providerPid/events`                 |
| [[[#negotiations-providerpid-agreement-verification-post]]] | `POST` | `/negotiations/:providerPid/agreement/verification` |
| [[[#negotiations-providerpid-termination-post]]]            | `POST` | `/negotiations/:providerPid/termination`            |

### Contract Negotiation Endpoint {#negotiations-get-provider}

**Request**

A [=Contract Negotiation=] can be accessed by a [=Consumer=] sending a GET request to `negotiations/:providerPid`:

<aside class="example" title="Get Negotiation Request">
    <pre class="http">GET https://provider.com/negotiations/:providerPid
Authorization: ...</pre>

</aside>


**Response**

If the [=Contract Negotiation=] is found and the client is authorized, the [=Provider=] MUST return an HTTP 200 (OK) response and a body
containing the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

Predefined states are: `REQUESTED`, `OFFERED`, `ACCEPTED`, `AGREED`, `VERIFIED`, `FINALIZED`, and `TERMINATED` (
see [[[#contract-negotiation-states]]]).

### Contract Request Endpoint (init) {#negotiations-request-post}

**Request**

A [=Contract Negotiation=] is started and placed in the `REQUESTED` state when a [=Consumer=] POSTs an
initiating [Contract Request Message](#contract-request-message) to `negotiations/request`:

<aside class="example" title="Contract Request">
    <pre class="http">POST https://provider.com/negotiations/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-request-message_initial.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives [=Messages=] associated with
  the [=Contract Negotiation=]. The HTTPS scheme MUST be supported. Implementations MAY optionally support other URL schemes.

- Callback [=Messages=] will be sent to paths under the base URL as described by this specification. (_NOTE:
  [=Providers=] SHOULD properly handle the cases where a trailing `/` is included
  with or absent from the `callbackAddress` when resolving full URL._)

**Response**

The [=Provider=] MUST return an HTTP 201 (Created) response with a body containing
the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

### Contract Request Endpoint {#negotiations-providerpid-request-post}

**Request**

A [=Consumer=] MAY make an [=Offer=] by POSTing
a [Contract Request Message](#contract-request-message)
to `negotiations/:providerPid/request`:

<aside class="example" title="Contract Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-request-message.json">
    </pre>
</aside>

**Response**

If the message is successfully processed, the [=Provider=] MUST return an HTTP 200 (OK) response. The response body is
not specified and clients MAY process it.

### Contract Negotiation Event Endpoint {#negotiations-providerpid-events-post}

**Request**

A [=Consumer=] can POST
a [Contract Negotiation Event Message](#contract-negotiation-event-message)
to `negotiations/:providerPid/events` to accept the current [=Provider=]'s [=Offer=].

<aside class="example" title="Contract Negotiation Event Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/events
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-event-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Provider=] MUST return an HTTP code 200 (OK). The response body is
not specified and clients MAY process it.

If the current [=Offer=] was created by the [=Consumer=], the [=Provider=] MUST return an HTTP code 400 (Bad Request)
with a [Contract Negotiation Error](#error-contract-negotiation-error) in the
response body.

### Contract Agreement Verification Endpoint {#negotiations-providerpid-agreement-verification-post}

**Request**

The [=Consumer=] can POST
a [Contract Agreement Verification Message](#contract-agreement-verification-message)
to verify an [=Agreement=].

<aside class="example" title="Contract Agreement Verification Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/agreement/verification
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-agreement-verification-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Provider=] MUST return an HTTP code 200 (OK). The response body is
not specified and clients MAY process it.

### Contract Negotiation Termination Endpoint {#negotiations-providerpid-termination-post}

**Request**

The [=Consumer=] can POST
a [Contract Negotiation Termination Message](#contract-negotiation-termination-message)
to terminate a [=Contract Negotiation=].

<aside class="example" title="Contract Negotiation Termination Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-termination-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Provider=] MUST return HTTP code 200 (OK). The response body is
not specified and clients MAY process it.

## Consumer Path Bindings

| Endpoint                                         | Method | Path                                               |
|:-------------------------------------------------|:-------|:---------------------------------------------------|
| [[[#negotiations-get-consumer]]]                 | `GET`  | `/:callback/negotiations/:consumerPid`             |
| [[[#negotiations-offers-post]]]                  | `POST` | `/negotiations/offers`                             |
| [[[#negotiations-consumerpid-offers-post]]]      | `POST` | `/:callback/negotiations/:consumerPid/offers`      |
| [[[#negotiations-consumerpid-agreement-post]]]   | `POST` | `/:callback/negotiations/:consumerPid/agreement`   |
| [[[#negotiations-consumerpid-events-post]]]      | `POST` | `/:callback/negotiations/:consumerPid/events`      |
| [[[#negotiations-consumerpid-termination-post]]] | `POST` | `/:callback/negotiations/:consumerPid/termination` |

### Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in
the [Contract Request Message](#contract-request-message) that initiated a [=Contract Negotiation=]. For example, if the `callbackAddress` is
specified as `https://consumer.com/:callback` and a callback path binding is `negotiations/:consumerPid/offers`, the
resolved URL will be `https://consumer.com/:callback/negotiations/:consumerPid/offers`.

The `:callback` MAY be chosen freely by the implementations.

### Contract Negotiation Endpoint {#negotiations-get-consumer}

**Request**

A [=Contract Negotiation=] can be accessed by a [=Provider=] sending a GET request to the `negotiations/:consumerPid` callback:

<aside class="example" title="Get Negotiation Request">
    <pre class="http">GET https://consumer.com/:callback/negotiations/:consumerPid
Authorization: ...</pre>

</aside>


**Response**

If the [=Contract Negotiation=] is found and the client is authorized, the [=Consumer=] MUST return an HTTP 200 (OK) response and a body
containing the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

Predefined states are: `REQUESTED`, `OFFERED`, `ACCEPTED`, `AGREED`, `VERIFIED`, `FINALIZED`, and `TERMINATED` (
see [[[#contract-negotiation-states]]]).

### Contract Offer Endpoint (init) {#negotiations-offers-post}

**Request**

A [=Contract Negotiation=] is started and placed in the `OFFERED` state when a [=Provider=] POSTs
a [Contract Offer Message](#contract-offer-message) to `negotiations/offers`:

<aside class="example" title="Contract Offer Request">
    <pre class="http">POST https://consumer.com/negotiations/offers
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-offer-message_initial.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint URL where the client receives messages associated with the
  [=Contract Negotiation=]. The HTTPS scheme MUST be supported. Implementations MAY optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. (_NOTE: [=Consumers=]
  SHOULD properly handle the cases where a trailing / is included with or absent from the `callbackAddress` when
  resolving full URL._)

**Response**

The [=Consumer=] MUST return an HTTP 201 (Created) response with a body containing
the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

### Contract Offer Endpoint {#negotiations-consumerpid-offers-post}

**Request**

A [=Provider=] MAY make an [=Offer=] by POSTing a [Contract Offer Message](#contract-offer-message) to
the `negotiations/:consumerPid/offers` callback:

<aside class="example" title="Contract Offer Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/offers
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-offer-message.json">
    </pre>
</aside>

**Response**

If the message is successfully processed, the [=Consumer=] MUST return an HTTP 200 (OK) response. The response body is
not specified and clients MAY process it.

### Contract Agreement Endpoint {#negotiations-consumerpid-agreement-post}

**Request**

The [=Provider=] can POST a [Contract Agreement Message](#contract-agreement-message) to
the `negotiations/:consumerPid/agreement` callback to create an [=Agreement=].

<aside class="example" title="Contract Agreement Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/agreement
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-agreement-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Consumer=] MUST return an HTTP code 200 (OK). The response body is
not specified and clients MAY process it.

### Contract Negotiation Event Endpoint {#negotiations-consumerpid-events-post}

**Request**

A [=Provider=] can POST a [Contract Negotiation Event Message](#contract-negotiation-event-message) to
the `negotiations/:consumerPid/events` callback with an `eventType` of `FINALIZED` to finalize an [=Agreement=].

<aside class="example" title="Contract Negotiation Event Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/events
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-event-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Consumer=] MUST return HTTP code 200 (OK). The response body is
not specified and clients MAY process it.

### Contract Negotiation Termination Endpoint {#negotiations-consumerpid-termination-post}

**Request**

The [=Provider=] can POST a [Contract Negotiation Termination Message](#contract-negotiation-termination-message) to
terminate a [=Contract Negotiation=].

<aside class="example" title="Contract Negotiation Termination Request">
    <pre class="http">POST https://consumer.com/negotiations/:consumerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-termination-message.json">
    </pre>
</aside>

**Response**

If the [=Contract Negotiation=]'s state is successfully transitioned, the [=Consumer=] MUST return HTTP code 200 (OK). The response body is
not specified and clients MAY process it.
