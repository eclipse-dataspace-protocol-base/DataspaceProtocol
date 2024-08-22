# Transfer Process HTTPS Binding

This specification defines a RESTful API over HTTPS for the [Transfer Process Protocol](./transfer.process.protocol.md).

* [1 Introduction](#1-introduction)
  * [1.1 Prerequisites](#11-prerequisites)
  * [1.2 Transfer Error](#12-transfer-error)
    * [1.2.1 State Transition Errors](#121-state-transition-errors)
    * [1.2.2 Object Not Found](#122-object-not-found)
    * [1.2.3 Unauthorized Access](#123-unauthorized-access)
* [2 Provider Path Bindings](#2-provider-path-bindings)
  * [2.1 The `transfers` Endpoint _(Provider-side)_](#21-the-transfers-endpoint-provider-side)
  * [2.2 The `transfers/request` Endpoint _(Provider-side)_](#22-the-transfersrequest-endpoint-provider-side)
  * [2.3 The `transfers/:providerPid/start` Endpoint _(Provider-side)_](#23-the-transfersproviderpidstart-endpoint-provider-side)
  * [2.4 The `transfers/:providerPid/completion` Endpoint _(Provider-side)_](#24-the-transfersproviderpidcompletion-endpoint-provider-side)
  * [2.5 The `transfers/:providerPid/termination` Endpoint _(Provider-side)_](#25-the-transfersproviderpidtermination-endpoint-provider-side)
  * [2.6 The `transfers/:providerPid/suspension` Endpoint _(Provider-side)_](#26-the-transfersproviderpidsuspension-endpoint-provider-side)
* [3 Consumer Callback Path Bindings](#3-consumer-callback-path-bindings)
  * [3.1 Prerequisites](#31-prerequisites)
  * [3.2 The `transfers/:consumerPid/start` Endpoint _(Consumer-side)_](#32-the-transfersconsumerpidstart-endpoint-consumer-side)
  * [3.3 The `transfers/:consumerPid/completion` Endpoint _(Consumer-side)_](#33-the-transfersconsumerpidcompletion-endpoint-consumer-side)
  * [3.4 The `transfers/:consumerPid/termination` Endpoint _(Consumer-side)_](#34-the-transfersconsumerpidtermination-endpoint-consumer-side)
  * [3.5 The `transfers/:consumerPid/suspension` Endpoint _(Consumer-side)_](#35-the-transfersconsumerpidsuspension-endpoint-consumer-side)

## 1 Introduction

### 1.1 Prerequisites

1. The `<base>` notation indicates the base URL for a [Connector](../model/terminology.md#connector--data-service-) endpoint. For example, if the scheme is `https` and the full hostname is `connector.example.com`, the URL `<base>/transfers/request` will map to `https://connector.example.com/transfers/request`.

2. All request and response messages must use the `application/json` media type. Derived media types, e.g., `application/ld+json` may be exposed in addition.

### 1.2 Transfer Error

In the event of a client request error, the [Connector](../model/terminology.md#connector--data-service-) must return an appropriate HTTP 4xx client error code. If an error body is returned it must be a [Transfer Error](./transfer.process.protocol.md#32-error---transfer-error).

#### 1.2.1 State Transition Errors

If a client or [Provider](../model/terminology.md#provider) makes a request that results in an invalid [Transfer Process](../model/terminology.md#transfer-process) (TP) state transition as defined by the Transfer Process Protocol, it must return an HTTP code 400 (Bad Request) with a [Transfer Error](./transfer.process.protocol.md#32-error---transfer-error) in the response body.

#### 1.2.2 Object Not Found

If the TP does not exist, the [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) must return an HTTP 404 (Not Found) response.

#### 1.2.3 Unauthorized Access

If the client is not authorized, the [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) must return an HTTP 404 (Not Found) response.

## 2 Provider Path Bindings

| Endpoint                                                | Method | Description                |
|:--------------------------------------------------------|:-------|:---------------------------|
| https://provider.com/transfers/:providerPid             | `GET`  | Section [2.1.1](#211-get)  |
| https://provider.com/transfers/request                  | `POST` | Section [2.2.1](#221-post) |
| https://provider.com/transfers/:providerPid/start       | `POST` | Section [2.3.1](#231-post) |
| https://provider.com/transfers/:providerPid/completion  | `POST` | Section [2.4.1](#241-post) |
| https://provider.com/transfers/:providerPid/termination | `POST` | Section [2.5.1](#251-post) |
| https://provider.com/transfers/:providerPid/suspension  | `POST` | Section [2.6.1](#261-post) |

### 2.1 The `transfers` Endpoint _(Provider-side)_

#### 2.1.1 GET

##### Request

A CN can be accessed by a [Consumer](../model/terminology.md#consumer) or [Provider](../model/terminology.md#provider) sending a GET request to `transfers/:providerPid`:

```http request
GET https://provider.com/transfers/:providerPid

Authorization: ...
```

##### Response

If the TP is found and the client is authorized, the [Provider](../model/terminology.md#provider) must return an HTTP 200 (OK) response and a body containing the [Transfer Process](./transfer.process.protocol.md#31-ack---transfer-process):

```json
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferProcess",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:state": "dspace:REQUESTED"
} 
```

Predefined states are: `REQUESTED`, `STARTED`, `SUSPENDED`, `REQUESTED`, `COMPLETED`, and `TERMINATED`.

### 2.2 The `transfers/request` Endpoint _(Provider-side)_

#### 2.2.1 POST

##### Request

A TP is started and placed in the `REQUESTED` state when a [Consumer](../model/terminology.md#consumer) POSTs a [Transfer Request Message](./transfer.process.protocol.md#21-transfer-request-message) to `transfers/request`:

 ```http request
POST https://provider.com/transfers/request
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferRequestMessage",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:agreementId": "urn:uuid:e8dc8655-44c2-46ef-b701-4cffdc2faa44",
  "dct:format": "example:HTTP_PUSH",
  "dspace:dataAddress": {
    "@type": "dspace:DataAddress",
    "dspace:endpointType": "https://w3id.org/idsa/v4.1/HTTP",
    "dspace:endpoint": "http://example.com",
    "dspace:endpointProperties": [
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authorization",
        "dspace:value": "TOKEN-ABCDEFG"
      },
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authType",
        "dspace:value": "bearer"
      }
    ]
  },
  "dspace:callbackAddress": "https://......"
}
 ```

- The `callbackAddress` property specifies the base endpoint `URL` where the client receives messages associated with the TP. Support for the `HTTPS` scheme is required. Implementations may optionally support other URL schemes.

- Callback messages will be sent to paths under the base URL as described by this specification. Note that [Providers](../model/terminology.md#provider) should properly handle the cases where a trailing `/` is included with or absent from the `callbackAddress` when resolving full URL.

##### Response

The [Provider](../model/terminology.md#provider) must return an HTTP 201 (Created) response with a body containing the [Transfer Process](./transfer.process.protocol.md#31-ack---transfer-process):

```json
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferProcess",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:state": "dspace:REQUESTED"
}
```

### 2.3 The `transfers/:providerPid/start` Endpoint _(Provider-side)_

#### 2.3.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Transfer Start Message](./transfer.process.protocol.md#22-transfer-start-message) to attempt to start a TP after it has been suspended:

 ```http request
POST https://provider.com/transfers/:providerPid/start
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferStartMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:dataAddress": {
    "@type": "dspace:DataAddress",
    "dspace:endpointType": "https://w3id.org/idsa/v4.1/HTTP",
    "dspace:endpoint": "http://example.com",
    "dspace:endpointProperties": [
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authorization",
        "dspace:value": "TOKEN-ABCDEFG"
      },
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authType",
        "dspace:value": "bearer"
      }
    ]
  }
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 2.4 The `transfers/:providerPid/completion` Endpoint _(Provider-side)_

#### 2.4.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Transfer Completion Message](./transfer.process.protocol.md#24-transfer-completion-message) to complete a TP:

 ```http request
POST https://provider.com/transfers/:providerPid/completion
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferCompletionMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 2.5 The `transfers/:providerPid/termination` Endpoint _(Provider-side)_

#### 2.5.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Transfer Termination Message](./transfer.process.protocol.md#25-transfer-termination-message) to terminate a TP:

 ```http request
POST https://provider.com/transfers/:providerPid/termination
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferTerminationMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 2.6 The `transfers/:providerPid/suspension` Endpoint _(Provider-side)_

#### 2.6.1 POST

##### Request

The [Consumer](../model/terminology.md#consumer) can POST a [Transfer Suspension Message](./transfer.process.protocol.md#23-transfer-suspension-message) to suspend a TP:

 ```http request
POST https://provider.com/transfers/:providerPid/suspension
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferSuspensionMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Provider](../model/terminology.md#provider) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

## 3 Consumer Callback Path Bindings

| Endpoint                                                          | Method | Description                |
|:------------------------------------------------------------------|:-------|:---------------------------|
| https://consumer.com/:callback/transfers/:consumerPid/start       | `POST` | Section [3.2.1](#321-post) |
| https://consumer.com/:callback/transfers/:consumerPid/completion  | `POST` | Section [3.3.1](#331-post) |
| https://consumer.com/:callback/transfers/:consumerPid/termination | `POST` | Section [3.4.1](#341-post) |
| https://consumer.com/:callback/transfers/:consumerPid/suspension  | `POST` | Section [3.5.1](#351-post) |

### 3.1 Prerequisites

All callback paths are relative to the `callbackAddress` base URL specified in the [Transfer Request Message](./transfer.process.protocol.md#21-transfer-request-message) that initiated a TP. For example, if the `callbackAddress` is specified as `https://consumer.com/callback` and a callback path binding is `transfers/:consumerPid/start`, the resolved URL will be `https://consumer.com/callback/transfers/:consumerPid/start`.

### 3.2 The `transfers/:consumerPid/start` Endpoint _(Consumer-side)_

#### 3.2.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Transfer Start Message](./transfer.process.protocol.md#22-transfer-start-message) to indicate the start of a TP:

 ```http request
POST https://consumer.com/:callback/transfers/:consumerPid/start
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferStartMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:dataAddress": {
    "@type": "dspace:DataAddress",
    "dspace:endpointType": "https://w3id.org/idsa/v4.1/HTTP",
    "dspace:endpoint": "http://example.com",
    "dspace:endpointProperties": [
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authorization",
        "dspace:value": "TOKEN-ABCDEFG"
      },
      {
        "@type": "dspace:EndpointProperty",
        "dspace:name": "authType",
        "dspace:value": "bearer"
      }
    ]
  }
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 3.3 The `transfers/:consumerPid/completion` Endpoint _(Consumer-side)_

#### 3.3.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Transfer Completion Message](./transfer.process.protocol.md#24-transfer-completion-message) to complete a TP:

 ```http request
POST https://consumer.com/:callback/transfers/:consumerPid/completion
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferCompletionMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833"
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 3.4 The `transfers/:consumerPid/termination` Endpoint _(Consumer-side)_

#### 3.4.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Transfer Termination Message](./transfer.process.protocol.md#25-transfer-termination-message) to terminate a TP:

 ```http request
POST https://consumer.com/:callback/transfers/:consumerPid/termination
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferTerminationMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it.

### 3.5 The `transfers/:consumerPid/suspension` Endpoint _(Consumer-side)_

#### 3.5.1 POST

##### Request

The [Provider](../model/terminology.md#provider) can POST a [Transfer Suspension Message](./transfer.process.protocol.md#23-transfer-suspension-message) to suspend a TP:

 ```http request
POST https://consumer.com/:callback/transfers/:consumerPid/suspension
 
Authorization: ...
 
{
  "@context":  "https://w3id.org/dspace/2024/1/context.json",
  "@type": "dspace:TransferSuspensionMessage",
  "dspace:providerPid": "urn:uuid:a343fcbf-99fc-4ce8-8e9b-148c97605aab",
  "dspace:consumerPid": "urn:uuid:32541fe6-c580-409e-85a8-8a9a32fbe833",
  "dspace:code": "...",
  "dspace:reason": [
    ...
  ]
}
 ```

##### Response

If the TP's state is successfully transitioned, the [Consumer](../model/terminology.md#consumer) must return HTTP code 200 (OK). The response body is not specified and clients are not required to process it. 
