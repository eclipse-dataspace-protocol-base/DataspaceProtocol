# Transfer Process Protocol {#transfer-protocol}

## Introduction

A [=Transfer Process=] involves two parties, a [=Provider=] that offers one or more [=Datasets=] along with
a [=Policy=] and a [=Consumer=] that requests [=Datasets=]. A [=Transfer Process=] progresses through a series of states, which are
controlled by the [=Provider=] and [=Consumer=] using [=Messages=]. A [=Transfer Process=] transitions to another state as a result of an
exchanged Message.

### Prerequisites

To put the document into the right context, some **non-normative** descriptions of the core concepts follow in this
subsection.

#### Control and Data Planes

A [=Transfer Process=] involves two logical constructs, a control plane and a data plane. Serving as a coordinating layer, services on the
_control plane_ receive [=Messages=] and manage the local state of the [=Transfer Process=] (same as for the [=Catalog Protocol=] and
the [=Contract Negotiation Protocol=]). On the _data plane_, the actual transfer of data takes place using a wire
protocol. Both [=Participants=] in a data sharing scenario run services logically regarded as control and/or data plane
services.

The specification of data plane interfaces and interaction patterns are not in scope of this document.

#### Data Transfer Types

[=Dataset=] transfers are characterized as `push` or `pull` transfers and it's data is either `finite` or `non-finite`.
This section describes the difference between these types.

##### Push Transfer

A push transfer is when the [=Provider=]'s data plane initiates sending data to a [=Consumer=] endpoint. For example,
after the [=Consumer=] has issued a [Transfer Request Message](#transfer-request-message), the [=Provider=] begins data
transmission to an endpoint specified by the [=Consumer=] using an agreed-upon wire protocol.

![](figures/push-transfer-process.png "Push Transfer Process")

_Note that the illustration of the sequence is only exemplary. The activation of actors is not determined, also,
responses, parameters, possible recursions, and interactions between the components of one [=Participant=] are not shown._

##### Pull Transfer

A pull transfer is when the [=Consumer=]'s data plane initiates retrieval of data from a [=Provider=] endpoint. For
example, after the [=Provider=] has issued a [Transfer Start Message](#transfer-start-message), the [=Consumer=] can
request the data from the [=Provider=]-specified endpoint.

![](figures/pull-transfer-process.png "Pull Transfer Process")

_Note that the illustration of the sequence is only exemplary. The activation of actors is not determined, also,
responses, parameters, possible recursions, and interactions between the components of one [=Participant=] are not shown._

##### Finite and Non-Finite Data

Data MAY be `finite` or `non-finite`. This applies to either push and pull transfers. Finite data is data that is
defined by a finite set, for example, machine learning data or images. After finite data transmission has finished, the
[=Transfer Process=] is completed. Non-finite data is data that is defined by an infinite set or has no specified end, for example,
streams or an API endpoint. With non-finite data, a [=Transfer Process=] will continue indefinitely until either the [=Consumer=]
or [=Provider=] explicitly terminates the transmission.

### States {#transfer-process-states}

The [=Transfer Process=] states are:

- **REQUESTED**: A [=Dataset=] has been requested under an [=Agreement=] by the [=Consumer=] and the [=Provider=] has
  sent an ACK response.
- **STARTED**: The [=Dataset=] is available for access by the [=Consumer=] or the [=Provider=] has begun pushing the
  data to the [=Consumer=] endpoint.
- **COMPLETED**: The transfer has been completed by either the [=Consumer=] or the [=Provider=].
- **SUSPENDED**: The transfer has been suspended by the [=Consumer=] or the [=Provider=].
- **TERMINATED**: The [=Transfer Process=] has been terminated by the [=Consumer=] or the [=Provider=].

### State Machine

The [=Transfer Process=] state machine is represented in the following diagram:

![](figures/transfer-process-state-machine.png "Transfer Process State Machine")

Transitions marked with `C` indicate a message sent by the [=Consumer=], transitions marked with `P` indicate
a [=Provider=] message. Terminal states are final; the state machine MUST NOT transition to another state.

## Message Types

### Transfer Request Message

|                     |                                                                                              |
|---------------------|----------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                 |
| **Resulting state** | `REQUESTED`                                                                                  |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                               |
| **Schema**          | [JSON Schema](message/schema/transfer-request-message-schema.json)                           |
| **Example**         | [Message](message/example/transfer-request-message.json)                                     |
| **Properties**      | <p data-include="message/table/transferrequestmessage.html" data-include-format="html"></p> |

The Transfer Request Message is sent by a [=Consumer=] to initiate a [=Transfer Process=].

- The `consumerPid` property MUST refer to the transfer identifier of the [=Consumer=] side.

- The `agreementId` property MUST refer to an existing [=Agreement=] between the [=Consumer=] and [=Provider=].

- The `format` property is a format specified by a `Distribution` for the [=Dataset=] associated with
  the [=Agreement=]. This is generally obtained from the [=Provider=]'s [=Catalog=].

- The `dataAddress` property MUST only be provided if the `format` REQUIRES a push transfer.

- The `dataAddress` MUST contain a transport-specific set of properties for pushing the data. It MAY include an `endpoint`, 
  a temporary authorization via the `endpointProperties` property - depending on the `endpointType`.

- `callbackAddress` MUST be a URI indicating where messages to the [=Consumer=] SHOULD be sent. If the address is not
  understood, the [=Provider=] MUST return an **unrecoverable** error.

- The `endpointProperties` MAY (among others) contain the following OPTIONAL values:
    - `authorization` - An opaque authorization token that clients MAY present when accessing the transport-specific
      endpoint address.
    - `authType` - The auth token type. For example, the value MAY be `bearer`. If present, this value MAY be used in
      conjunction with transport rules to define how the client MAY present an authorization token.

Note that [=Providers=] SHOULD implement idempotent behavior for [Transfer Request Messages](#transfer-request-message)
based on the value of `consumerPid`. [=Providers=] MAY choose to implement idempotent behavior for a certain period of
time. For example, until a [=Transfer Process=] has completed and been archived after an implementation-specific expiration period,
repeated sending of [Transfer Request Messages](#transfer-request-message) does not change the state of the [=Transfer Process=]. If a
request for the given `consumerPid` has already been received *and* the same [=Consumer=] sent the original message
again, the [=Provider=] SHOULD respond with an appropriate [Transfer Start Message](#transfer-start-message).

- Once a [=Transfer Process=] has been created, all associated callback messages MUST include a `consumerPid` and `providerPid`.

- [=Providers=] MUST include a `consumerPid` and a `providerPid` property in the object.

- Valid states of a [=Transfer Process=] are `REQUESTED`, `STARTED`, `TERMINATED`, `COMPLETED`, and `SUSPENDED`.

### Transfer Start Message

|                     |                                                                                            |
|---------------------|--------------------------------------------------------------------------------------------|
| **Sent by**         | [=Provider=]                                                                               |
| **Resulting state** | `STARTED`                                                                                  |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                             |
| **Schema**          | [JSON Schema](message/schema/transfer-start-message-schema.json)                           |
| **Example**         | [Message](message/example/transfer-start-message.json)                                     |
| **Properties**      | <p data-include="message/table/transferstartmessage.html" data-include-format="html"></p> |

The Transfer Start Message is sent by the [=Provider=] to indicate the data transfer has been initiated.

- The `dataAddress` MUST be provided if the current transfer is a pull transfer and contains a transport-specific
  endpoint address for obtaining the data. The kind of transport is signaled by the `endpointType` property which
  determines a set of REQUIRED `endpointProperties` in a [=Profile=] separate from this specification.

- The `endpointProperties` MAY (among others) contain the following OPTIONAL values:
  - `authorization` - An opaque authorization token that clients MAY present when accessing the transport-specific
    endpoint address.
  - `authType` - The auth token type. For example, the value MAY be `bearer`. If present, this value MAY be used in
    conjunction with transport rules to define how the client MAY present an authorization token.

### Transfer Suspension Message

|                     |                                                                                            |
|---------------------|--------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                 |
| **Resulting state** | `SUSPENDED`                                                                                |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                             |
| **Schema**          | [JSON Schema](message/schema/transfer-suspension-message-schema.json)                      |
| **Example**         | [Message](message/example/transfer-suspension-message.json)                                |
| **Properties**      | <p data-include="message/table/transferstartmessage.html" data-include-format="html"></p> |

The Transfer Suspension Message is sent by the [=Provider=] or [=Consumer=] when either of them needs to temporarily
suspend the [=Transfer Process=].

### Transfer Completion Message

|                     |                                                                                                 |
|---------------------|-------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                      |
| **Resulting state** | `COMPLETED`                                                                                     |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                  |
| **Schema**          | [JSON Schema](message/schema/transfer-completion-message-schema.json)                           |
| **Example**         | [Message](message/example/transfer-completion-message.json)                                     |
| **Properties**      | <p data-include="message/table/transfercompletionmessage.html" data-include-format="html"></p> |

The Transfer Completion Message is sent by the [=Provider=] or [=Consumer=] when a data transfer has completed. Note
that some [=Connector=] implementations MAY optimize completion
notification by performing it as part of their wire protocol. In those cases, a [Transfer Completion Message](transfer-completion-message) does not
need to be sent.

### Transfer Termination Message

|                     |                                                                                                  |
|---------------------|--------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                       |
| **Resulting state** | `TERMINATED`                                                                                     |
| **Response**        | [ACK](#ack-transfer-process) or [ERROR](#error-transfer-error)                                   |
| **Schema**          | [JSON Schema](message/schema/transfer-termination-message-schema.json)                           |
| **Example**         | [Message](message/example/transfer-termination-message.json)                                     |
| **Properties**      | <p data-include="message/table/transferterminationmessage.html" data-include-format="html"></p> |

The Transfer Termination Message is sent by the [=Provider=] or [=Consumer=] at any point except a terminal state to
indicate the [=Transfer Process=] SHOULD stop and be placed in a terminal state. If the termination was due to an error, the sender MAY
include error information.

## Response Types

The `ACK` and `ERROR` response types are mapped onto a protocol such as HTTPS. A description of an error MAY be
provided in protocol-dependent forms, e.g., for an HTTPS binding in the request or response body.

### ACK - Transfer Process

|                |                                                                                       |
|----------------|---------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                            |
| **Schema**     | [JSON Schema](message/schema/transfer-process-schema.json)                            |
| **Example**    | [Process](message/example/transfer-process.json)                                      |
| **Properties** | <p data-include="message/table/transferprocess.html" data-include-format="html"></p> |

The Transfer Process is an object returned by a [=Consumer=] or [=Provider=] indicating a successful state change
happened.

### ERROR - Transfer Error

|                |                                                                                     |
|----------------|-------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                          |
| **Schema**     | [JSON Schema](message/schema/transfer-error-schema.json)                            |
| **Example**    | [Process](message/example/transfer-error.json)                                      |
| **Properties** | <p data-include="message/table/transfererror.html" data-include-format="html"></p> |

The Transfer Error is an object returned by a [=Consumer=] or [=Provider=] indicating an error has occurred. It does not
cause a state transition.
