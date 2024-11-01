# Transfer Process Protocol

This document outlines the key elements of the [=Transfer Process Protocol=]. The used terms are
described [here](#terminology).

## Introduction

A [=Transfer Process=] (TP) involves two parties, a [=Provider=] that offers one or more [=Datasets=] under
a [=Usage Policy=] and a [=Consumer=] that requests [=Datasets=]. A TP progresses through a series of states, which are
controlled by the [=Provider=] and [=Consumer=] using messages. A TP transitions to another state as a result of an
exchanged message.

### Prerequisites

To put the document into the right context, some non-normative descriptions of the core concepts follow in this
subsection.

#### Control and Data Planes

A TP involves two logical constructs, a control plane and a data plane. Serving as a coordinating layer, services on the
_control plane_ receive messages and manage the local state of the TP (same as for the [=Catalog Protocol=] and
the [=Contract Negotiation Protocol=]). On the _data plane_, the actual transfer of data takes place using a wire
protocol. Both participants in a data sharing scenario run services logically regarded as control and/or data plane
services.

The specification of data plane interfaces and interaction patterns are not in scope of this document.

#### Data Transfer Types

[=Dataset=] transfers are characterized as `push` or `pull` transfers and it's data is either `finite` or `non-finite`.
This section describes the difference between these types.

##### Push Transfer

A push transfer is when the [=Provider=]'s data plane initiates sending data to a [=Consumer=] endpoint. For example,
after the [=Consumer=] has issued a [Transfer Request Message](#transfer-request-message), the [=Provider=] begins data
transmission to an endpoint specified by the [=Consumer=] using an agreed-upon wire protocol.

![](./figures/push-transfer-process.png "Push Transfer Process")

_Note that the illustration of the sequence is only exemplary. The activation of actors is not determined, also,
responses, parameters, possible recursions, and interactions between the components of one participant are not shown._

##### Pull Transfer

A pull transfer is when the [=Consumer=]'s data plane initiates retrieval of data from a [=Provider=] endpoint. For
example, after the [=Provider=] has issued a [Transfer Start Message](#transfer-start-message), the [=Consumer=] can
request the data from the [=Provider=]-specified endpoint.

![](./figures/pull-transfer-process.png "Pull Transfer Process")

_Note that the illustration of the sequence is only exemplary. The activation of actors is not determined, also,
responses, parameters, possible recursions, and interactions between the components of one participant are not shown._

##### Finite and Non-Finite Data

Data may be `finite` or `non-finite`. This applies to either push and pull transfers. Finite data is data that is
defined by a finite set, for example, machine learning data or images. After finite data transmission has finished, the
TP is completed. Non-finite data is data that is defined by an infinite set or has no specified end, for example,
streams or an API endpoint. With non-finite data, a TP will continue indefinitely until either the [=Consumer=]
or [=Provider=] explicitly terminates the transmission.

### States

The TP states are:

- **REQUESTED**: A [=Dataset=] has been requested under an [=Agreement=] by the [=Consumer=] and the [=Provider=] has
  sent an ACK response.
- **STARTED**: The [=Dataset=] is available for access by the [=Consumer=] or the [=Provider=] has begun pushing the
  data to the [=Consumer=] endpoint.
- **COMPLETED**: The transfer has been completed by either the [=Consumer=] or the [=Provider=].
- **SUSPENDED**: The transfer has been suspended by the [=Consumer=] or the [=Provider=].
- **TERMINATED**: The [=Transfer Process=] has been terminated by the [=Consumer=] or the [=Provider=].

### State Machine

The TP state machine is represented in the following diagram:

![](./figures/transfer-process-state-machine.png "Transfer Process State Machine")

Transitions marked with `C` indicate a message sent by the [=Consumer=], transitions marked with `P` indicate
a [=Provider=] message. Terminal states are final; the state machine may not transition to another state.

## Message Types

All messages must be serialized in JSON-LD compact form as specified in
the [JSON-LD 1.1 Processing Algorithms and API](https://www.w3.org/TR/json-ld11-api/#compaction-algorithms) [[json-ld11-api]].
Further Dataspace specifications may define additional optional serialization formats.

### Transfer Request Message

|                     |                                                                                                                                       |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                                                          |
| **Resulting state** | `REQUESTED`                                                                                                                           |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                                                        |
| **Schema**          | [TTL Shape](./message/shape/transfer-request-message-shape.ttl), [JSON Schema](./message/schema/transfer-request-message-schema.json) |
| **Example**         | [Message](./message/example/transfer-request-message.json)                                                                            |
| **Diagram(s)**      | ![](./message/diagram/transfer-request-message.png "Transfer Request Message")                                                        |

The Transfer Request Message is sent by a [=Consumer=] to initiate a TP.

- The `consumerPid` property refers to the transfer id of the [=Consumer=] side.
- The `agreementId` property refers to an existing contract [=Agreement=] between the [=Consumer=] and [=Provider=].
- The `dct:format` property is a format specified by a `Distribution` for the [=Dataset=] associated with
  the [=Agreement=]. This is generally obtained from the [=Provider=]'s [Catalog](../model/terminology.md#catalog).
- The `dataAddress` property must only be provided if the `dct:format` requires a push transfer.
- The `dataAddress` contains a transport-specific endpoint address for pushing the data. It may include a temporary
  authorization via the `endpointProperties` property.
- `callbackAddress` is a URI indicating where messages to the [=Consumer=] should be sent. If the address is not
  understood, the [=Provider=] MUST return an UNRECOVERABLE error.

- The `endpointProperties` may contain the following optional values:
    - `authorization` - An opaque authorization token that clients must present when accessing the transport-specific
      endpoint address.
    - `authType` - The auth token type. For example, the value may be `bearer`. If present, this value may be used in
      conjunction with transport rules to define how the client must present an authorization token.

Note that [=Providers=] should implement idempotent behavior for [Transfer Request Messages](#transfer-request-message)
based on the value of `consumerPid`. [=Providers=] may choose to implement idempotent behavior for a certain period of
time. For example, until a TP has completed and been archived after an implementation-specific expiration period,
repeated sending of [Transfer Request Messages](#transfer-request-message) does not change the state of the TP. If a
request for the given `consumerPid` has already been received *and* the same [=Consumer=] sent the original message
again, the [=Provider=] should respond with an appropriate [Transfer Start Message](#transfer-start-message).

- Once a TP has been created, all associated callback messages must include a `consumerPid` and `providerPid`.
- [=Providers=] must include a `consumerPid` and a `providerPid` property in the object.
- Valid states of a TP are `REQUESTED`, `STARTED`, `TERMINATED`, `COMPLETED`, and `SUSPENDED`.

### Transfer Start Message

|                     |                                                                                                                                   |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Provider=]                                                                                                                      |
| **Resulting state** | `STARTED`                                                                                                                         |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                                                    |
| **Schema**          | [TTL Shape](./message/shape/transfer-start-message-shape.ttl), [JSON Schema](./message/schema/transfer-start-message-schema.json) |
| **Example**         | [Message](./message/example/transfer-start-message.json)                                                                          |
| **Diagram(s)**      | ![](./message/diagram/transfer-start-message.png "Transfer Start Message")                                                        |

The Transfer Start Message is sent by the [=Provider=] to indicate the data transfer has been initiated.

- The `dataAddress` is only provided if the current transfer is a pull transfer and contains a transport-specific
  endpoint address for obtaining the data. It may include a temporary authorization via the `endpointProperties`
  property.
- The `endpointProperties` may contain the following optional values:
    - `authorization` - An opaque authorization token that clients must present when accessing the transport-specific
      endpoint address.
    - `authType` - The auth token type. For example, the value may be `bearer`. If present, this value may be used in
      conjunction with transport rules to define how the client must present an authorization token.

### Transfer Suspension Message

|                     |                                                                                                                                             |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                                                                  |
| **Resulting state** | `SUSPENDED`                                                                                                                                 |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                                                              |
| **Schema**          | [TTL Shape](./message/shape/transfer-suspension-message-shape.ttl), [JSON Schema](./message/schema/transfer-suspension-message-schema.json) |
| **Example**         | [Message](./message/example/transfer-suspension-message.json)                                                                               |
| **Diagram(s)**      | ![](./message/diagram/transfer-suspension-message.png "Transfer Suspension Message")                                                        |

The Transfer Suspension Message is sent by the [=Provider=] or [=Consumer=] when either of them needs to temporarily
suspend the TP.

### Transfer Completion Message

|                     |                                                                                                                                             |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                                                                  |
| **Resulting state** | `COMPLETED`                                                                                                                                 |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                                                              |
| **Schema**          | [TTL Shape](./message/shape/transfer-completion-message-shape.ttl), [JSON Schema](./message/schema/transfer-completion-message-schema.json) |
| **Example**         | [Message](./message/example/transfer-completion-message.json)                                                                               |
| **Diagram(s)**      | ![](./message/diagram/transfer-completion-message.png "Transfer Completion Message")                                                        |

The Transfer Completion Message is sent by the [=Provider=] or [=Consumer=] when a data transfer has completed. Note
that some [=Connector=] implementations may optimize completion
notification by performing it as part of their wire protocol. In those cases, a Transfer Completion Message does not
need to be sent.

### Transfer Termination Message

|                     |                                                                                                                                               |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                                                                    |
| **Resulting state** | `TERMINATED`                                                                                                                                  |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                                                                |
| **Schema**          | [TTL Shape](./message/shape/transfer-termination-message-shape.ttl), [JSON Schema](./message/schema/transfer-termination-message-schema.json) |
| **Example**         | [Message](./message/example/transfer-termination-message.json)                                                                                |
| **Diagram(s)**      | ![](./message/diagram/transfer-termination-message.png "Transfer Termination Message")                                                         |

The Transfer Termination Message is sent by the [=Provider=] or [=Consumer=] at any point except a terminal state to
indicate the TP should stop and be placed in a terminal state. If the termination was due to an error, the sender may
include error information.

## Response Types

The `ACK` and `ERROR` response types are mapped onto a protocol such as HTTPS. A description of an error might be
provided in protocol-dependent forms, e.g., for an HTTPS binding in the request or response body.

### ACK - Transfer Process

|                |                                                                                                                       |
|----------------|-----------------------------------------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                                                            |
| **Schema**     | [TTL Shape](./message/shape/transfer-process-shape.ttl), [JSON Schema](./message/schema/transfer-process-schema.json) |
| **Example**    | [Process](./message/example/transfer-process.json)                                                                    |
| **Diagram(s)** | ![](./message/diagram/transfer-process.png "Transfer Process")                                                         |

The Transfer Process is an object returned by a [=Consumer=] or [=Provider=] indicating a successful state change
happened.

### ERROR - Transfer Error

|                |                                                                                                                   |
|----------------|-------------------------------------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                                                        |
| **Schema**     | [TTL Shape](./message/shape/transfer-error-shape.ttl), [JSON Schema](./message/schema/transfer-error-schema.json) |
| **Example**    | [Process](./message/example/transfer-error.json)                                                                  |
| **Diagram(s)** | ![](./message/diagram/transfer-error.png "Transfer Error")                                                         |

The Transfer Error is an object returned by a [=Consumer=] or [=Provider=] indicating an error has occurred. It does not
cause a state transition.

| Field         | Type          | Description                                                 |
|---------------|---------------|-------------------------------------------------------------|
| `consumerPid` | UUID          | The TF unique id on [=Consumer=] side.                      |
| `providerPid` | UUID          | The TF unique id on [=Provider=] side.                      |
| `code`        | string        | An optional implementation-specific error code.             |
| `reasons`     | Array[object] | An optional array of implementation-specific error objects. |
