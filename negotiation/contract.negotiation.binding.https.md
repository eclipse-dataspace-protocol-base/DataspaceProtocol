# Contract Negotiation HTTPS Binding

This specification defines a RESTful API over HTTPS for the [Contract Negotiation Protocol](./contract.negotiation.protocol.md).

- [Contract Negotiation HTTPS Binding](#contract-negotiation-https-binding)
  - [1 Introduction](#1-introduction)
    - [1.1 Prerequisites](#11-prerequisites)
    - [1.2 Contract Negotiation Error](#12-contract-negotiation-error)
      - [1.2.1 State Transition Errors](#121-state-transition-errors)
      - [1.2.2 Object Not Found](#122-object-not-found)
      - [1.2.3 Unauthorized Access](#123-unauthorized-access)
  - [2 Provider Path Bindings](#2-provider-path-bindings)
    - [2.1 The `negotiations` Endpoint _(Provider-side)_](#21-the-negotiations-endpoint-provider-side)
      - [2.1.1 GET](#211-get)
        - [Request](#request)
        - [Response](#response)
    - [2.2 The `negotiations/request` Endpoint _(Provider-side)_](#22-the-negotiationsrequest-endpoint-provider-side)
      - [2.2.1 POST](#221-post)
        - [Request](#request-1)
        - [Response](#response-1)
    - [2.3 The `negotiations/:providerPid/request` Endpoint _(Provider-side)_](#23-the-negotiationsproviderpidrequest-endpoint-provider-side)
      - [2.3.1 POST](#231-post)
        - [Request](#request-2)
        - [Response](#response-2)
    - [2.4 The `negotiations/:providerPid/events` Endpoint _(Provider-side)_](#24-the-negotiationsproviderpidevents-endpoint-provider-side)
      - [2.4.1 POST](#241-post)
        - [Request](#request-3)
        - [Response](#response-3)
    - [2.5 The `negotiations/:providerPid/agreement/verification` Endpoint  _(Provider-side)_](#25-the-negotiationsproviderpidagreementverification-endpoint--provider-side)
      - [2.5.1 POST](#251-post)
        - [Request](#request-4)
        - [Response](#response-4)
    - [2.6 The `negotiations/:providerPid/termination` Endpoint _(Provider-side)_](#26-the-negotiationsproviderpidtermination-endpoint-provider-side)
      - [2.6.1 POST](#261-post)
        - [Request](#request-5)
        - [Response](#response-5)
  - [3 Consumer Callback Path Bindings](#3-consumer-callback-path-bindings)
    - [3.1 Prerequisites](#31-prerequisites)
    - [3.2 The `negotiations/offers` Endpoint _(Consumer-side)_](#32-the-negotiationsoffers-endpoint-consumer-side)
      - [3.2.1 POST](#321-post)
        - [Request](#request-6)
        - [Response](#response-6)
    - [3.3 The `negotiations/:consumerPid/offers` Endpoint _(Consumer-side)_](#33-the-negotiationsconsumerpidoffers-endpoint-consumer-side)
      - [3.3.1 POST](#331-post)
        - [Request](#request-7)
        - [Response](#response-7)
    - [3.4 The `negotiations/:consumerPid/agreement` Endpoint _(Consumer-side)_](#34-the-negotiationsconsumerpidagreement-endpoint-consumer-side)
      - [3.4.1 POST](#341-post)
        - [Request](#request-8)
        - [Response](#response-8)
    - [3.5 The `negotiations/:consumerPid/events` Endpoint _(Consumer-side)_](#35-the-negotiationsconsumerpidevents-endpoint-consumer-side)
      - [3.5.1 POST](#351-post)
        - [Request](#request-9)
        - [Response](#response-9)
    - [3.6 The `negotiations/:consumerPid/termination` Endpoint _(Consumer-side)_](#36-the-negotiationsconsumerpidtermination-endpoint-consumer-side)
      - [3.6.1 POST](#361-post)
        - [Request](#request-10)
        - [Response](#response-10)

## 1 Introduction

### 1.1 Prerequisites

1. The `<base>` notation indicates the base URL for a [Connector](../model/terminology.md#connector--data-service-) endpoint. For example, if the base [Connector](../model/terminology.md#connector--data-service-) URL is `connector.example.com`, the URL `https://<base>/negotiations/request` will map to `https//connector.example.com/negotiation/request`.

2. All request and response messages must use the `application/json` media type. Derived media types, e.g., `application/ld+json` may be exposed in addition.

### 1.2 Contract Negotiation Error

In the event of a client request error, the [Connector](../model/terminology.md#connector--data-service-) must return an appropriate HTTP 4xx client error code. If an error body is returned it must be a [Contract Negotiation Error](./contract.negotiation.protocol.md#32-error---contract-negotiation-error).

#### 1.2.1 State Transition Errors

If a client makes a request that results in an invalid [state transition as defined by the Contract Negotiation Protocol](./contract.negotiation.protocol.md#11-states), it must return an HTTP code 400 (Bad Request) with a [Contract Negotiation Error](./contract.negotiation.protocol.md#32-error---contract-negotiation-error) in the response body.

#### 1.2.2 Object Not Found

If the [Contract Negotiation](../model/terminology.md#contract-negotiation) (CN) does not exist, the [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) must return an HTTP 404 (Not Found) response.

#### 1.2.3 Unauthorized Access

If the client is not authorized, the [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) must return an HTTP 404 (Not Found) response.

### 1.3 Authorization

All requests should use the `Authorization` header to include an authorization token. The semantics of such tokens are not part of this specification. The `Authorization` HTTP header is optional if the [Connector](../model/terminology.md#connector--data-service-) does not require authorization.

## 2 Provider Path Bindings

| Endpoint                                                              | Method | Description                |
|:----------------------------------------------------------------------|:-------|:---------------------------|
| https://provider.com/negotiations/:providerPid                        | `GET`  | Section [2.1.1](#211-get)  |
| https://provider.com/negotiations/request                             | `POST` | Section [2.2.1](#221-post) |
| https://provider.com/negotiations/:providerPid/request                | `POST` | Section [2.3.1](#231-post) |
| https://provider.com/negotiations/:providerPid/events                 | `POST` | Section [2.4.1](#241-post) |
| https://provider.com/negotiations/:providerPid/agreement/verification | `POST` | Section [2.5.1](#251-post) |
| https://provider.com/negotiations/:providerPid/termination            | `POST` | Section [2.6.1](#261-post) |

### 2.1 The `negotiations` Endpoint _(Provider-side)_

#### 2.1.1 GET

##### Request

A CN can be accessed by a [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) sending a GET request to `negotiations/:providerPid`:

```http request
GET https://provider.com/negotiations/:providerPid

Authorization: ...

```

##### Response

If the CN is found and the client is authorized, the [Provider](../model/terminology.md#provider) must return an HTTP 200 (OK) response and a body containing the [Contract Negotiation](./contract.negotiation.protocol.md#31-ack---contract-negotiation):

```json
{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiation",
  "dspace:providerPid": "urn:uuid:dcbf434c-eacf-4582-9a02-f8dd50120fd3",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:state" :"REQUESTED"
}  
```

Predefined states are: `REQUESTED`, `OFFERED`, `ACCEPTED`, `AGREED`, `VERIFIED`, `FINALIZED`, and `TERMINATED` (see [here](./contract.negotiation.protocol.md#11-states).

### 2.2 The `negotiations/request` Endpoint _(Provider-side)_

#### 2.2.1 POST

##### Request

A CN is started and placed in the `REQUESTED` state when a [Consumer](../model/terminology.md#consumer) POSTs an initiating [Contract Request Message](./contract.negotiation.protocol.md#21-contract-request-message) to `negotiations/request`:

```http request
POST https://provider.com/negotiations/request

Authorization: ...

{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractRequestMessage",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:offer": {
    "@type": "odrl:Offer",
    "@id": "...",
    "target": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88"
  },
  "dspace:callbackAddress": "https://..."
}
```

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives messages associated with the CN. Support for the `HTTPS` scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note that [Providers](../model/terminology.md#provider) should properly handle the cases where a trailing `/` is included with or absent from the `callbackAddress` when resolving full URL.

##### Response

The [Provider](../model/terminology.md#provider) must return an HTTP 201 (Created) response with a body containing the [Contract Negotiation](./contract.negotiation.protocol.md#31-ack---contract-negotiation):

```json
{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiation",
  "dspace:providerPid": "urn:uuid:dcbf434c-eacf-4582-9a02-f8dd50120fd3",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:state" :"REQUESTED"
}
```

### 2.3 The `negotiations/:providerPid/request` Endpoint _(Provider-side)_

#### 2.3.1 POST

##### Request

A [Consumer](../model/terminology.md#consumer) may make an [Offer](../model/terminology.md#offer) by POSTing a [Contract Request Message](./contract.negotiation.protocol.md#21-contract-request-message) to `negotiations/:providerPid/request`:

```http request
POST https://provider.com/negotiations/:providerPid/request

Authorization: ...

{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractRequestMessage",
  "dspace:providerPid": "urn:uuid:dcbf434c-eacf-4582-9a02-f8dd50120fd3",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:offer": {
    "@type": "odrl:Offer",
    "@id": "...",
    "target": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88"
  }
}
```

##### Response

If the message is successfully processed, the [Provider](../model/terminology.md#provider) must return an HTTP 200 (OK) response. The response body is not specified and clients are not required to process it.

### 2.4 The `negotiations/:providerPid/events` Endpoint _(Provider-side)_

#### 2.4.1 POST

##### Request

A [Consumer](../model/terminology.md#consumer) can POST a [Contract Negotiation Event Message](./contract.negotiation.protocol.md#25-contract-negotiation-event-message) to `negotiations/:providerPid/events` to accept the current [Provider's](../model/terminology.md#provider) [Offer](../model/terminology.md#offer).


```http request
POST https://provider.com/negotiations/:providerPid/events

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiationEventMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:eventType": "dspace:ACCEPTED"
}
```

##### Response

If the CN's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return an HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

If the current [Offer](../model/terminology.md#offer) was created by the [Consumer](../model/terminology.md#consumer), the [Provider](../model/terminology.md#provider) must return an HTTP code 400 (Bad Request) with a [Contract Negotiation Error](./contract.negotiation.protocol.md#32-error---contract-negotiation-error) in the response body.

### 2.5 The `negotiations/:providerPid/agreement/verification` Endpoint  _(Provider-side)_

#### 2.5.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Contract Agreement Verification Message](./contract.negotiation.protocol.md#24-contract-agreement-verification-message) to verify an [Agreement](../model/terminology.md#agreement). 

```http request
POST https://provider.com/negotiations/:providerPid/agreement/verification

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractAgreementVerificationMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
}
```

##### Response

If the CN's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return an HTTP code 200 (OK). The response body is not specified and clients are not required to process it.


### 2.6 The `negotiations/:providerPid/termination` Endpoint _(Provider-side)_

#### 2.6.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Contract Negotiation Termination Message](./contract.negotiation.protocol.md#26-contract-negotiation-termination-message) to terminate a CN. 

```http request
POST https://provider.com/negotiations/:providerPid/termination

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiationTerminationMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
```

##### Response

If the CN's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

## 3 Consumer Callback Path Bindings

| Endpoint                                                             | Method | Description                |
|:---------------------------------------------------------------------|:-------|:---------------------------|
| https://consumer.com/negotiations/offers                             | `POST` | Section [3.2.1](#321-post) |
| https://consumer.com/:callback/negotiations/:consumerPid/offers      | `POST` | Section [3.3.1](#331-post) |
| https://consumer.com/:callback/negotiations/:consumerPid/agreement   | `POST` | Section [3.4.1](#341-post) |
| https://consumer.com/:callback/negotiations/:consumerPid/events      | `POST` | Section [3.5.1](#351-post) |
| https://consumer.com/:callback/negotiations/:consumerPid/termination | `POST` | Section [3.6.1](#361-post) |

**_Note:_** The `:callback` can be chosen freely by the implementations.

### 3.1 Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in the [Contract Request Message](./contract.negotiation.protocol.md#21-contract-request-message) that initiated a CN. For example, if the `callbackAddress` is specified as `https://consumer.com/callback` and a callback path binding is `negotiations/:consumerPid/offers`, the resolved URL will be `https://consumer.com/callback/negotiations/:consumerPid/offers`.

### 3.2 The `negotiations/offers` Endpoint _(Consumer-side)_

#### 3.2.1 POST

##### Request

A CN is started and placed in the `OFFERED` state when a [Provider](../model/terminology.md#provider) POSTs a [Contract Offer Message](./contract.negotiation.protocol.md#22-contract-offer-message) to `negotiations/offers`:

```http request
POST https://consumer.com/negotiations/offers

Authorization: ...

{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractOfferMessage",
  "dspace:providerPid": "urn:uuid:dcbf434c-eacf-4582-9a02-f8dd50120fd3",
  "dspace:offer": {
    "@type": "odrl:Offer",
    "@id": "...",
    "target": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88"
  },
  "dspace:callbackAddress": "https://..."
}
```

- The `callbackAddress` property specifies the base endpoint URL where the client receives messages associated with the CN. Support for the HTTPS scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note that [Consumers](../model/terminology.md#consumer) should properly handle the cases where a trailing / is included with or absent from the `callbackAddress` when resolving full URL.

##### Response

The [Consumer](../model/terminology.md#consumer) must return an HTTP 201 (Created) response with a body containing the [Contract Negotiation](./contract.negotiation.protocol.md#31-ack---contract-negotiation):

```json
{
  "@context": "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiation",
  "dspace:providerPid": "urn:uuid:dcbf434c-eacf-4582-9a02-f8dd50120fd3",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:state" :"OFFERED"
}
```

### 3.3 The `negotiations/:consumerPid/offers` Endpoint _(Consumer-side)_

#### 3.3.1 POST

##### Request

A [Provider](../model/terminology.md#provider) may make an [Offer](../model/terminology.md#offer) by POSTing a [Contract Offer Message](./contract.negotiation.protocol.md#22-contract-offer-message) to the `negotiations/:consumerPid/offers` callback:

```http request
POST https://consumer.com/:callback/negotiations/:consumerPid/offers

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractOfferMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:offer": {
    "@type": "odrl:Offer",
    "@id": "urn:uuid:6bcea82e-c509-443d-ba8c-8eef25984c07",
    "odrl:target": "urn:uuid:3dd1add8-4d2d-569e-d634-8394a8836a88",
    "dspace:providerId": "urn:tsdshhs636378",
    "dspace:consumerId": "urn:jashd766",
    ...
  },
  "dspace:callbackAddress": "https://......"
}
```

##### Response

If the message is successfully processed, the [Consumer](../model/terminology.md#consumer) must return an HTTP 200 (OK) response. The response body is not specified and clients are not required to process it.

### 3.4 The `negotiations/:consumerPid/agreement` Endpoint _(Consumer-side)_

#### 3.4.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Contract Agreement Message](./contract.negotiation.protocol.md#23-contract-agreement-message) to the `negotiations/:consumerPid/agreement` callback to create an [Agreement](../model/terminology.md#agreement). 

```http request
POST https://consumer.com/:callback/negotiations/:consumerPid/agreement

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractAgreementMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:agreement": {
    "@id": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
    "@type": "odrl:Agreement",
    "odrl:target": "urn:uuid:3dd1add4-4d2d-569e-d634-8394a8836d23",
    "dspace:timestamp": "2023-01-01T01:00:00Z",
    "dspace:providerId": "urn:tsdshhs636378",
    "dspace:consumerId": "urn:jashd766",
    ...
  },
  "dspace:callbackAddress": "https://......"
}
```

##### Response

If the CN's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return an HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 3.5 The `negotiations/:consumerPid/events` Endpoint _(Consumer-side)_

#### 3.5.1 POST

##### Request

A [Provider](../model/terminology.md#provider) can POST a [Contract Negotiation Event Message](./contract.negotiation.protocol.md#25-contract-negotiation-event-message) to the `negotiations/:consumerPid/events` callback with an `eventType` of `FINALIZED` to finalize an [Agreement](../model/terminology.md#agreement).

```http request
POST https://consumer.com/:callback/negotiations/:consumerPid/events

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiationEventMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:eventType": "dspace:FINALIZED"
}
```

##### Response

If the CN's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it. 

### 3.6 The `negotiations/:consumerPid/termination` Endpoint _(Consumer-side)_

#### 3.6.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Contract Negotiation Termination Message](./contract.negotiation.protocol.md#26-contract-negotiation-termination-message) to terminate a CN.

```http request
POST https://consumer.com/negotiations/:consumerPid/termination

Authorization: ...

{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:ContractNegotiationTerminationMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
```

##### Response

If the CN's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.
