# Contract Negotiation HTTPS Binding {#negotiation-http}

This specification defines a RESTful API over HTTPS for
the [Contract Negotiation Protocol](#contract-negotiation-protocol).

## Introduction

### Prerequisites

1. The `<base>` notation indicates the base URL for a [=Connector=] endpoint. For example, if the base [=Connector=] URL
   is `connector.example.com`, the URL `https://<base>/negotiations/request` will map
   to `https//connector.example.com/negotiation/request`.
2. All request and response messages must use the `application/json` media type. Derived media types,
   e.g., `application/ld+json` may be exposed in addition.

### Contract Negotiation Error

In the event of a client request error, the [=Connector=] must return an appropriate HTTP 4xx client error code. If an
error body is returned it must be a [Contract Negotiation Error](#contract-negotiation-error).

#### State Transition Errors

If a client makes a request that results in an
invalid [state transition as defined by the Contract Negotiation Protocol](#contract-negotiation-states), it must return
an HTTP code 400 (Bad Request) with a [Contract Negotiation Error](#contract-negotiation-error) in the response body.

#### Object Not Found

If the [=Contract Negotiation=] (CN) does not exist, the [=Consumer=] or [=Provider=] must return an HTTP 404 (Not
Found) response.

#### Unauthorized Access

If the client is not authorized, the [=Consumer=] or [=Provider=] must return an HTTP 404 (Not Found) response.

### Authorization

All requests should use the `Authorization` header to include an authorization token. The semantics of such tokens are
not part of this specification. The `Authorization` HTTP header is optional if the [=Connector=] does not require
authorization.

## Provider Path Bindings

| Endpoint                                                              | Method | Description                                                 |
|:----------------------------------------------------------------------|:-------|:------------------------------------------------------------|
| https://provider.com/negotiations/:providerPid                        | `GET`  | [[[#negotiations-get]]]                                     |
| https://provider.com/negotiations/request                             | `POST` | [[[#negotiations-request-post]]]                            |
| https://provider.com/negotiations/:providerPid/request                | `POST` | [[[#negotiations-providerpid-request-post]]]                |
| https://provider.com/negotiations/:providerPid/events                 | `POST` | [[[#negotiations-providerpid-events-post]]]                 |
| https://provider.com/negotiations/:providerPid/agreement/verification | `POST` | [[[#negotiations-providerpid-agreement-verification-post]]] |
| https://provider.com/negotiations/:providerPid/termination            | `POST` | [[[#negotiations-providerpid-termination-post]]]            |

### The `negotiations` Endpoint _(Provider-side)_

#### GET {#negotiations-get}

##### Request

A CN can be accessed by a [=Consumer=] or [=Provider=] sending a GET request to `negotiations/:providerPid`:

<aside class="example" title="Get Negotiation Request">
    <pre class="http">GET https://provider.com/negotiations/:providerPid
Authorization: ...</pre>

</aside>


##### Response

If the CN is found and the client is authorized, the [=Provider=] must return an HTTP 200 (OK) response and a body
containing the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>



Predefined states are: `REQUESTED`, `OFFERED`, `ACCEPTED`, `AGREED`, `VERIFIED`, `FINALIZED`, and `TERMINATED` (
see [[[#contract-negotiation-states]]]).

### The `negotiations/request` Endpoint _(Provider-side)_

#### POST {#negotiations-request-post}

##### Request

A CN is started and placed in the `REQUESTED` state when a [=Consumer=] POSTs an
initiating [Contract Request Message](#contract-request-message)
to `negotiations/request`:

<aside class="example" title="Contract Request">
    <pre class="http">POST https://provider.com/negotiations/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-request-message.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives messages associated with
  the CN. Support for the `HTTPS` scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note
  that [=Providers=] should properly handle the cases where a trailing `/` is included
  with or absent from the `callbackAddress` when resolving full URL.

##### Response

The [=Provider=] must return an HTTP 201 (Created) response with a body containing
the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

### The `negotiations/:providerPid/request` Endpoint _(Provider-side)_

#### POST {#negotiations-providerpid-request-post}

##### Request

A [=Consumer=] may make an [=Offer=] by POSTing
a [Contract Request Message](#contract-request-message)
to `negotiations/:providerPid/request`:

<aside class="example" title="Contract Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/request
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-request-message.json">
    </pre>
</aside>

##### Response

If the message is successfully processed, the [=Provider=] must return an HTTP 200 (OK) response. The response body is
not specified and clients are not required to process it.

### The `negotiations/:providerPid/events` Endpoint _(Provider-side)_

#### POST {#negotiations-providerpid-events-post}

##### Request

A [=Consumer=] can POST
a [Contract Negotiation Event Message](#contract-negotiation-event-message)
to `negotiations/:providerPid/events` to accept the current [=Provider=]'s [=Offer=].

<aside class="example" title="Contract Negotiation Event Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/events
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-event-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Provider=] must return an HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

If the current [=Offer=] was created by the [=Consumer=], the [=Provider=] must return an HTTP code 400 (Bad Request)
with a [Contract Negotiation Error](#error-contract-negotiation-error) in the
response body.

### The `negotiations/:providerPid/agreement/verification` Endpoint  _(Provider-side)_

#### POST {#negotiations-providerpid-agreement-verification-post}

##### Request

The [=Consumer=] can POST
a [Contract Agreement Verification Message](#contract-agreement-verification-message)
to verify an [=Agreement=].

<aside class="example" title="Contract Agreement Verification Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/agreement/verification
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-agreement-verification-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Provider=] must return an HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `negotiations/:providerPid/termination` Endpoint _(Provider-side)_

#### POST {#negotiations-providerpid-termination-post}

##### Request

The [=Consumer=] can POST
a [Contract Negotiation Termination Message](#contract-negotiation-termination-message)
to terminate a CN.

<aside class="example" title="Contract Negotiation Termination Request">
    <pre class="http">POST https://provider.com/negotiations/:providerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-termination-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Provider=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

## Consumer Callback Path Bindings

| Endpoint                                                             | Method | Description                                    |
|:---------------------------------------------------------------------|:-------|:-----------------------------------------------|
| https://consumer.com/negotiations/offers                             | `POST` | [[[#negotiations-offers-post]]]                |
| https://consumer.com/:callback/negotiations/:consumerPid/offers      | `POST` | [[[#negotiations-consumerpid-offers-post]]]    |
| https://consumer.com/:callback/negotiations/:consumerPid/agreement   | `POST` | [[[#negotiations-consumerpid-agreement-post]]] |
| https://consumer.com/:callback/negotiations/:consumerPid/events      | `POST` | [[[#negotiations-consumerpid-events-post]]]    |
| https://consumer.com/:callback/negotiations/:consumerPid/termination | `POST` | [[[#negotiations-consumerpid-termination-post]]]                                         |

**_Note:_** The `:callback` can be chosen freely by the implementations.

### Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in
the [Contract Request Message](#contract-request-message) that initiated a CN. For example, if the `callbackAddress` is
specified as `https://consumer.com/callback` and a callback path binding is `negotiations/:consumerPid/offers`, the
resolved URL will be `https://consumer.com/callback/negotiations/:consumerPid/offers`.

### The `negotiations/offers` Endpoint _(Consumer-side)_

#### POST {#negotiations-offers-post}

##### Request

A CN is started and placed in the `OFFERED` state when a [=Provider=] POSTs
a [Contract Offer Message](#contract-offer-message) to `negotiations/offers`:

<aside class="example" title="Contract Offer Request">
    <pre class="http">POST https://consumer.com/negotiations/offers
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-offer-message.json">
    </pre>
</aside>

- The `callbackAddress` property specifies the base endpoint URL where the client receives messages associated with the
  CN. Support for the HTTPS scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note that [=Consumers=]
  should properly handle the cases where a trailing / is included with or absent from the `callbackAddress` when
  resolving full URL.

##### Response

The [=Consumer=] must return an HTTP 201 (Created) response with a body containing
the [Contract Negotiation](#ack-contract-negotiation):

<aside class="example" title="Contract Negotiation Response">
    <pre class="json" data-include="message/example/contract-negotiation.json">
    </pre>
</aside>

### The `negotiations/:consumerPid/offers` Endpoint _(Consumer-side)_

#### POST {#negotiations-consumerpid-offers-post}

##### Request

A [=Provider=] may make an [=Offer=] by POSTing a [Contract Offer Message](#contract-offer-message) to
the `negotiations/:consumerPid/offers` callback:

<aside class="example" title="Contract Offer Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/offers
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-offer-message.json">
    </pre>
</aside>

##### Response

If the message is successfully processed, the [=Consumer=] must return an HTTP 200 (OK) response. The response body is
not specified and clients are not required to process it.

### The `negotiations/:consumerPid/agreement` Endpoint _(Consumer-side)_

#### POST {#negotiations-consumerpid-agreement-post}

##### Request

The [=Provider=] can POST a [Contract Agreement Message](#contract-agreement-message) to
the `negotiations/:consumerPid/agreement` callback to create an [=Agreement=].

<aside class="example" title="Contract Agreement Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/agreement
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-agreement-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Consumer=] must return an HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `negotiations/:consumerPid/events` Endpoint _(Consumer-side)_ 

#### POST {#negotiations-consumerpid-events-post}

##### Request

A [=Provider=] can POST a [Contract Negotiation Event Message](#contract-negotiation-event-message) to
the `negotiations/:consumerPid/events` callback with an `eventType` of `FINALIZED` to finalize an [=Agreement=].

<aside class="example" title="Contract Negotiation Event Request">
    <pre class="http">POST https://consumer.com/:callback/negotiations/:consumerPid/events
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-event-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.

### The `negotiations/:consumerPid/termination` Endpoint _(Consumer-side)_

#### POST {#negotiations-consumerpid-termination-post}

##### Request

The [=Provider=] can POST a [Contract Negotiation Termination Message](#contract-negotiation-termination-message) to
terminate a CN.

<aside class="example" title="Contract Negotiation Termination Request">
    <pre class="http">POST https://consumer.com/negotiations/:consumerPid/termination
Authorization: ...</pre>
    <pre class="json" data-include="message/example/contract-negotiation-termination-message.json">
    </pre>
</aside>

##### Response

If the CN's state is successfully transitioned, the [=Consumer=] must return HTTP code 200 (OK). The response body is
not specified and clients are not required to process it.
