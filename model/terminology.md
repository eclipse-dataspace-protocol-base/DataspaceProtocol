# Terminology

This and the following sections define the core concepts, entities, and relationships that underpin a __dataspace__ and its protocol.

### Agreement

A concrete [Policy](#policy) associated with a specific [Dataset](#dataset) that has been signed by both the [Provider](#provider) and consumer [Participants](#participant). An Agreement is a result of a [Contract Negotiation](../negotiation/contract.negotiation.protocol.md) and is associated with _exactly one_ [Dataset](#dataset).

### Catalog

A collection of entries representing [Datasets](#dataset) and their [Offers](#offer) that is advertised by a [Provider](#provider) [Participant](#participant).

### Catalog Protocol

A set of allowable [Message Types](#message-type) that are used to request a [Catalog](#catalog) from a [Catalog Service](#catalog-service).

### Catalog Service

A [Participant Agent](#participant-agent) that makes a [Catalog](#catalog) accessible to [Participants](#participant).

### Connector (Data Service)

A [Participant Agent](#participant-agent) that produces [Agreements](#agreement) and manages [Dataset](#dataset) sharing.

### Consumer

A [Participant Agent](#participant-agent) that requests access to an offered [Dataset](#dataset).

### Contract Negotiation

A set of interactions between a [Provider](#provider) and [Consumer](#consumer) that establish an [Agreement](#agreement). It is an instantiation of the state machine of a [Contract Negotiation Protocol](#contract-negotiation-protocol).

### Contract Negotiation Protocol

A set of allowable [Message Type](#message-type) sequences defined as a state machine.

## Credential Issuer

A Credential Issuer is a trusted technology system that issues verifiable credentials for a [Participant](#participant) and [Participant Agents](#participant-agent).

### Dataset

Data or a technical service that can be shared by a [Participant](#participant).

### Dataspace

A set of technical services that facilitate interoperable [Dataset](#dataset) sharing between entities.

### Dataspace Authority

An entity that manages a [Dataspace](#dataspace). The form and capabilities of a Dataspace Authority are not covered in these specifications.

### Dataspace Registration Service (Dataspace Registry)

A technology system that maintains the state of [Participants](#participant) in a [Dataspace](#dataspace).  The form and capabilities of a Dataspace Registration Service are not covered in these specifications.

### Identity Provider

A trusted technology system that creates, maintains, and manages identity information for a [Participant](#participant) and [Participant Agents](#participant-agent).

### Message

An instantiation of a [Message Type](#message-type).

### Message Type

A definition of the structure of a [Message](#message).

### Offer

A concrete [Policy](#policy) associated with a specific [Dataset](#dataset).

### Participant

A [Dataspace](#dataspace) member that provides and/or consumes [Datasets](#dataset).

### Participant Agent

A technology system that performs operations on behalf of a [Participant](#participant) that offers a [Dataset](#dataset).

### Policy

A set of rules, duties, and obligations that define the terms of use for a [Dataset](#dataset). Also referred to as "Usage Policy".

### Provider

A [Participant Agent](#participant-agent) that offers a [Dataset](#dataset).

### Transfer Process

A set of interactions between a [Provider](#provider) and [Consumer](#consumer) that give access to a [Dataset](#dataset) under the terms of an [Agreement](#agreement). It is an instantiation of the state machine of a [Transfer Process Protocol](#transfer-process-protocol).

### Transfer Process Protocol

A set of allowable [Message Type](#message-type) sequences defined as a state machine.
