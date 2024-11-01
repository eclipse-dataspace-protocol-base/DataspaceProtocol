# Contract Negotiation Protocol

This document outlines the key elements of the [Contract Negotiation Protocol](#contract-negotiation-protocol). The used
terms are described [here](#terminology).

## Introduction

A [=Contract Negotiation=] (CN) involves two parties, a [=Provider=] that offers one or more [=Datasets=] under a usage
contract and [=Consumer=] that requests [=Datasets=]. A CN is uniquely identified through an IRI [[rfc3987]]. Each CN
requires a newly generated IRI, which may not be used in a CN after a terminal state has been reached. A CN progresses
through a series of states, which are tracked by the [=Provider=] and [=Consumer=] using messages. A CN transitions to a
state in response to an acknowledged message from the counter-party. Both parties have the same state of the CN. In case
the states differ, the CN is terminated and a new CN has to be initiated.

### States {#contract-negotiation-states}

The CN states are:

- **REQUESTED**: A contract for a [=Dataset=] has been requested by the [=Consumer=] based on an [=Offer=] and
  the [=Provider=] has sent an ACK response.
- **OFFERED**: The [=Provider=] has sent an [=Offer=] to the [=Consumer=] and the [=Consumer=] has sent an ACK response.
- **ACCEPTED**: The [=Consumer=] has accepted the latest [=Offer=] and the [=Provider=] has sent an ACK response.
- **AGREED**: The [=Provider=] has accepted the latest [=Offer=], sent an [=Agreement=] to the [=Consumer=], and
  the [=Consumer=] has sent an ACK response.
- **VERIFIED**: The [=Consumer=] has sent an [=Agreement=] verification to the [=Provider=] and the [=Provider=] has
  sent an ACK response.
- **FINALIZED**: The [=Provider=] has sent a finalization message including his own [=Agreement=] verification to
  the [=Consumer=] and the [=Consumer=] has sent an ACK response. Data is now available to the [=Consumer=].
- **TERMINATED**: The [=Provider=] or [=Consumer=] has placed the CN in a terminated state. A termination message has
  been sent by either of the [=Participants=] and the other has sent an ACK response. This is a terminal state.

### State Machine

The CN state machine is represented in the following diagram:

!["Contract Negotiation State Machine"](./figures/contract.negotiation.state.machine.png "Contract Negotiation State Machine")

Transitions marked with `C` indicate a message sent by the [=Consumer=], transitions marked with `P` indicate
a [=Provider=] message. Terminal states are final; the state machine may not transition to another state. A new CN may
be initiated if, for instance, the CN entered the `TERMINATED` state due to a network issue.

## Message Types

The CN state machine is transitioned upon receipt and acknowledgement of a message. This section details those messages
as abstract message types.

- Concrete wire formats are defined by the protocol binding, e.g., [Contract Negotiation HTTPS Binding](#contract-negotiation-https-binding).
- All [=Policy=] types ([=Offer=], [=Agreement=]) must contain an unique identifier in the form of a URI. GUIDs can also
  be used in the form of URNs, for instance following the pattern <urn:uuid:{GUID}>.
- An [ODRL Agreement](https://www.w3.org/TR/odrl-vocab/#term-Agreement) must have a target property containing
  the [=Dataset=] id.

### Contract Request Message

|                     |                                                                                                                                         |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                                                            |
| **Resulting state** | `REQUESTED`, `TERMINATED`                                                                                                               |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                          |
| **Schema**          | [TTL Shape](./message/shape/contract-request-message-shape.ttl), [JSON Schema](./message/schema/contract-request-message-schema.json)   |
| **Example**         | Initiating [Message](./message/example/contract-request-message_initial.json), [Message](./message/example/contract-offer-message.json) |
| **Diagram(s)**      | ![](./message/diagram/contract-request-message.png "Contract Request Message")                                                          |

The Contract Request Message is sent by a [=Consumer=] to initiate a CN or to respond to
a [Contract Offer Message](#contract-offer-message) sent by a [=Provider=].

- The [=Consumer=] must include an `offer` property, which itself must have a `@id` property. If the message includes
  a `providerPid` property, the request will be associated with an existing CN and a [=Consumer=] [=Offer=] will be
  created using either the `offer` or `offer.@id` properties. If the message does not include a `providerPid`, a new CN
  will be created on [=Provider=] side using either the `offer` or `offer.@id` properties and the [=Provider=] selects
  an appropriate `providerPid`.
- An `offer.@id` will generally refer to an [=Offer=] contained in a [=Catalog=]. If the [=Provider=] is not aware of
  the `offer.@id` value, it must respond with an error message.
- The `callbackAddress` is a URL indicating where messages to the [=Consumer=] should be sent in asynchronous settings.
  If the address is not understood, the [=Provider=] MUST return an UNRECOVERABLE error.
- Different to a [=Catalog=] or [=Dataset=], the [=Offer=] inside
  a [Contract Request Message](#contract-request-message) must have an `odrl:target` attribute. However, it's contained
  Rules must not have any `odrl:target` attributes to prevent inconsistencies with
  the [ODRL inferencing rules for compact policies](https://www.w3.org/TR/odrl-model/#composition-compact).

### Contract Offer Message

|                     |                                                                                                                                                                                                                                                         |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Provider=]                                                                                                                                                                                                                                            |
| **Resulting state** | `OFFERED`, `TERMINATED`                                                                                                                                                                                                                                 |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                                                                                                                                          |
| **Schema**          | [TTL Shape](./message/shape/contract-offer-message-shape.ttl), [JSON Schema](./message/schema/contract-offer-message-schema.json)                                                                                                                       |
| **Example**         | [Example Initial Message](./message/example/contract-offer-message_initial.json), [Example Message](./message/example/contract-offer-message.json)                                                                                                      |
| **Diagram(s)**      | ![](./message/diagram/contract-offer-message_initial.png "Initial Contract Offer message (note the missing `consumerPid`)") <p/> ![](./message/diagram/contract-offer-message.png "Contract Offer Message following a [[[#contract-request-message]]]") |

The Contract Offer Message is sent by a [=Provider=] to initiate a CN or to respond to
a [Contract Request Message](#contract-request-message) sent by a [=Consumer=].

- If the message includes a `consumerPid` property, the request will be associated with an existing CN. If the message
  does not include a `consumerPid`, a new CN will be created on [=Consumer=] side and the [=Consumer=] selects an
  appropriate `consumerPid`.
- The [=Dataset=] id is not required but can be included when the [=Provider=] initiates a CN.
- Different to a [=Dataset=] (see [DCAT Vocabulary Mapping](#dcat-vocabulary-mapping)),
  the Offer inside a ContractOfferMessage must have an `odrl:target` attribute. However, it's contained Rules must not
  have any `odrl:target` attributes to prevent inconsistencies with
  the [ODRL inferencing rules for compact policies](https://www.w3.org/TR/odrl-model/#composition-compact).

### Contract Agreement Message

|                     |                                                                                                                                           |
|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Provider=]                                                                                                                              |
| **Resulting state** | `AGREED`, `TERMINATED`                                                                                                                    |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                            |
| **Schema**          | [TTL Shape](./message/shape/contract-agreement-message-shape.ttl), [JSON Schema](./message/schema/contract-agreement-message-schema.json) |
| **Example**         | [Message](./message/example/contract-agreement-message.json)                                                                              |
| **Diagram(s)**      | ![](./message/diagram/contract-agreement-message.png "Contract Agreement Message")                                                        |

The Contract Agreement Message is sent by a [=Provider=] when it agrees to a contract. It contains the
complete [=Agreement=].

- The message must contain a `consumerPid` and a `providerPid`.
- The message must contain an [ODRL Agreement](https://www.w3.org/TR/odrl-vocab/#term-Agreement).
- An [=Agreement=] must contain a `timestamp` property defined as
  an [XSD DateTime](https://www.w3schools.com/XML/schema_dtypes_date.asp) type.
- An [=Agreement=] must contain an `assigner` and `assignee`. The contents of these properties are a dataspace-specific
  unique identifier of the [=Agreement=] parties. Note that these identifiers are not necessarily the same as the
  identifiers of the [=Participant Agents=] negotiating the contract (
  e.g., [=Connectors=]).
- An [=Agreement=] must contain a `odrl:target` property. None of its Rules, however, must have any `odrl:target`
  attributes to prevent inconsistencies with
  the [ODRL inferencing rules for compact policies](https://www.w3.org/TR/odrl-model/#composition-compact).

### Contract Agreement Verification Message

|                     |                                                                                                                                                                     |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                                                                                        |
| **Resulting state** | `VERIFIED`, `TERMINATED`                                                                                                                                            |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                                                      |
| **Schema**          | [TTL Shape](./message/shape/contract-agreement-verification-message-shape.ttl), [JSON Schema](./message/schema/contract-agreement-verification-message-schema.json) |
| **Example**         | [Message](./message/example/contract-agreement-verification-message.json)                                                                                           |
| **Diagram(s)**      | ![](./message/diagram/contract-agreement-verification-message.png "Contract Agreement Verification Message")                                                        |

The Contract Agreement Verification Message is sent by a [=Consumer=] to verify the acceptance of an [=Agreement=].

- A [=Provider=] responds with an error if the contract cannot be validated or is incorrect.
- The message must contain a `consumerPid` and a `providerPid`.

### Contract Negotiation Event Message

|                     |                                                                                                                                                           |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                                                                                |
| **Resulting state** | `FINALIZED`, `ACCEPTED`, `TERMINATED`                                                                                                                     |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                                            |
| **Schema**          | [TTL Shape](./message/shape/contract-negotiation-event-message-shape.ttl), [JSON Schema](./message/schema/contract-negotiation-event-message-schema.json) |
| **Example**         | [Message](./message/example/contract-negotiation-event-message.json)                                                                                      |
| **Diagram(s)**      | ![](./message/diagram/contract-negotiation-event-message.png "Contract Negotiation Event Message")                                                        |

When the Contract Negotiation Event Message is sent by a [=Provider=] with an `eventType` property set to `FINALIZED`,
an [=Agreement=] has been finalized and the associated [=Dataset=] is accessible. The state machine is transitioned to
the `FINALIZED` state.

- Other event types may be defined in the future.
- A [=Consumer=] responds with an error if the contract cannot be validated or is incorrect.
- The message must contain a `consumerPid` and a `providerPid`.
- When the message is sent by a [=Consumer=] with an `eventType` set to `ACCEPTED`, the state machine is placed in
  the `ACCEPTED` state.
- It is an error for a [=Consumer=] to send the message with an event type `FINALIZED` to the [=Provider=].
- It is an error for a [=Provider=] to send the message with an event type `ACCEPTED` to the [=Consumer=].

Note that CN events are not intended for propagation of an [=Agreement=] state after a CN has entered a terminal state.
It is considered an error for a [=Consumer=] or [=Provider=] to send an event after the CN state machine has entered a
terminal state.

### Contract Negotiation Termination Message

|                     |                                                                                                                                                                       |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=], [=Provider=]                                                                                                                                            |
| **Resulting state** | `TERMINATED`                                                                                                                                                          |
| **Response**        | [ACK](#ack-contract-negotiation) or [ERROR](#error-contract-negotiation-error)                                                                                        |
| **Schema**          | [TTL Shape](./message/shape/contract-negotiation-termination-message-shape.ttl), [JSON Schema](./message/schema/contract-negotiation-termination-message-schema.json) |
| **Example**         | [Message](./message/example/contract-negotiation-termination-message.json)                                                                                            |
| **Diagram(s)**      | ![](./message/diagram/contract-negotiation-termination-message.png "Contract Negotiation Termination message")                                                        |

The Contract Negotiation Termination Message is sent by a [=Consumer=] or [=Provider=] indicating it has cancelled the
CN sequence. The message can be sent at any state of a CN without providing an explanation. Nevertheless, the sender may
provide a description to help the receiver.

- The message must contain a `consumerPid` and a `providerPid`.
- If an error is received in response to the message, the sending party may choose to ignore the error.

Note that a CN may be terminated for a variety of reasons, for example, an unrecoverable error was encountered or one of
the parties no longer wishes to continue. A [=Connector=]'s operator may
remove terminated CN resources after it has reached the terminated state.

## Response Types

The `ACK` and `ERROR` response types are mapped onto a protocol such as HTTPS. A description of an error might be
provided in protocol-dependent forms, e.g., for an HTTPS binding in the request or response body.

### ACK - Contract Negotiation

|                |                                                                                                                               |
|----------------|-------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                                                                    |
| **Schema**     | [TTL Shape](./message/shape/contract-negotiation-shape.ttl), [JSON Schema](./message/schema/contract-negotiation-schema.json) |
| **Example**    | [Process](./message/example/contract-negotiation.json)                                                                        |
| **Diagram(s)** | ![](./message/diagram/contract-negotiation.png "Contract Negotiation ACK")                                                    |

The Contract Negotiation is an object returned by a [=Consumer=] or [=Provider=] indicating a successful state change
happened.

### ERROR - Contract Negotiation Error

|                |                                                                                                                                           |
|----------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                                                                                |
| **Schema**     | [TTL Shape](./message/shape/contract-negotiation-error-shape.ttl), [JSON Schema](./message/schema/contract-negotiation-error-schema.json) |
| **Example**    | [Error](./message/example/contract-negotiation-error.json)                                                                                |
| **Diagram(s)** | ![](./message/diagram/contract-negotiation-error.png "Contract Negotiation Error")                                                        |

The Contract Negotiation Error is an object returned by a [=Consumer=] or [=Provider=] indicating an error has occurred.
It does not cause a state transition.

| Field         | Type          | Description                                                 |
|---------------|---------------|-------------------------------------------------------------|
| `consumerPid` | UUID          | The CN unique id on [=Consumer=] side.                      |
| `providerPid` | UUID          | The CN unique id on [=Provider=] side.                      |
| `code`        | String        | An optional implementation-specific error code.             |
| `reason`      | Array[object] | An optional array of implementation-specific error objects. |
