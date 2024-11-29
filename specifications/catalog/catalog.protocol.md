# Catalog Protocol

This document outlines the [=Catalog Protocol=]. The used terms are described in section [[[#terminology]]].

## Introduction

The Catalog Protocol defines how a [=Catalog=] is requested from a [=Catalog Service=] by a [=Consumer=] using an
abstract message exchange format. The concrete message exchange wire format is defined in the binding specifications.

### DCAT Vocabulary Mapping

This section describes how the DSP Information Model maps to [DCAT] resources [[vocab-dcat-3]].

#### Dataset

A [=Dataset=] is a [DCAT Dataset](https://www.w3.org/TR/vocab-dcat-3/#Class:Dataset) with the following attributes:

##### odrl:hasPolicy

A [=Dataset=] must have 1..N `hasPolicy` attributes that contain
an [ODRL Offer](https://www.w3.org/TR/odrl-vocab/#term-Offer) defining the [=Usage Policy=] associated with
the [=Catalog=]. Offers must NOT contain any
explicit `target` attributes. The `target` of an [=Offer=] is the associated [=Dataset=]. This is in line with the
semantics of `hasPolicy` as defined in the [ODRL Information Model](https://www.w3.org/TR/odrl-model/#policy-has),
explaining that the subject (here the Dataset) is automatically the `target` of each Rule. To prevent conflicts,
the `target` attribute must not be set explicitely, for example, in the [=Offer=] or Rules.

#### Distributions

A [=Dataset=] may contain 0..N [DCAT Distributions](https://www.w3.org/TR/vocab-dcat-3/#Class:Distribution). Each
distribution must have at least one `DataService` which specifies where the distribution is obtained. Specifically,
a `DataService` specifies the endpoint for initiating a [=Contract Negotiation=] and [=Transfer Process=].

A Distribution may have 0..N `hasPolicy` attributes that contain
an [ODRL Offer](https://www.w3.org/TR/odrl-vocab/#term-Offer) defining the [=Usage Policy=] associated with
the [=Dataset=] and this explicit `Distribution`. [=Offers=] must NOT contain any target attributes. The target of
an [=Offer=] is the [=Dataset=] that contains the distribution.

Support for `hasPolicy` attributes on a `Distribution` is optional. Implementations may choose not to support this
feature, in which case they should return an appropriate error message to clients.

#### Data Service

A Data Service may specify an endpoint supporting the Dataspace Protocol such as a [=Connector=].

##### dspace:dataServiceType

If the Data Service refers to an endpoint that supports the Dataspace Protocol, it must include the
property `dspace:dataServiceType`:

| Category   | Description                                                                |
|------------|----------------------------------------------------------------------------|
| Definition | Specifies the service type                                                 |
| Domain     | [dcat:DataService](https://www.w3.org/TR/vocab-dcat-3/#Class:Data_Service) |
| Type       | xsd:string                                                                 |
| Note       | The value of this field is left intentionally open for future extension.   |

The following table lists well-know endpoint types:

| Value              | Description               |
|--------------------|---------------------------|
| `dspace:connector` | A [=Connector=] endpoint. |
|                    |                           |

##### dcat:servesDataset

Note that the property `dcat:servesDataset` should be omitted from the `DataService`
since [=Datasets=] are included as top-level entries. Clients are not required to process
the contents of `dcat:servesDataset`.

#### Participant Id

The identifier of the participant providing the [=Catalog=] is specified using the `dspace:participantId` attribute on
that [DCAT Catalog](https://www.w3.org/TR/vocab-dcat-3/#Class:Catalog).

### DCAT and ODRL Profiles

The [=Catalog=] is a [DCAT Catalog](https://www.w3.org/TR/vocab-dcat-3/#Class:Catalog) with the following restrictions:

1. Each [ODRL Offer](https://www.w3.org/TR/odrl-vocab/#term-Offer) must be unique to a [=Dataset=] since the target of
   the [=Offer=] is derived from its enclosing context.
2. A [=Catalog=] must not have an `odrl:hasPolicy` attribute, since it is not intended to negotiate on the access
   to [=Catalog=] objects. An implementation might however regulate the visibility and/or the content of its [=Catalog=]
   dependent of the requester.

## Message Types

All messages must be serialized in JSON-LD compact form as specified in
the [JSON-LD 1.1 Processing Algorithms and API](https://www.w3.org/TR/json-ld11-api/#compaction-algorithms).
Further [=Dataspace=] specifications may define additional optional serialization
formats.

### Catalog Request Message

|                     |                                                                              |
|---------------------|------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                 |
| **Resulting state** | `TERMINATED`                                                                 |
| **Response**        | [ACK](#ack-catalog) or [ERROR](#error-catalog-error)                         |
| **Schema**          | [JSON Schema](message/schema/catalog-request-message-schema.json)          |
| **Example**         | [Message](message/example/catalog-request-message.json)                    |
| **Diagram(s)**      | ![](message/diagram/catalog-request-message.png "Catalog Request Message") |

The Catalog Request Message is message sent by a [=Consumer=] to
a [=Catalog Service=].
The [=Catalog Service=] must respond with a [Catalog](#ack-catalog), which
is a valid instance of a [DCAT Catalog](https://www.w3.org/TR/vocab-dcat-3/#Class:Catalog).

- The message may have a `filter` property which contains an implementation-specific query or filter expression type
  supported by the [=Catalog Service=].

- The [=Catalog Service=] may require an authorization token. Details for
  including that token can be found in the protocol binding, e.g., [Catalog HTTPS Binding](#catalog-https-binding).
  Similarly, pagination may be defined in the protocol binding.

### Dataset Request Message

|                     |                                                                     |
|---------------------|---------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                        |
| **Resulting state** | `TERMINATED`                                                        |
| **Response**        | [ACK](#ack-catalog) or [ERROR](#error-catalog-error)                |
| **Schema**          | [JSON Schema](message/schema/dataset-request-message-schema.json) |
| **Example**         | [Message](message/example/dataset-request-message.json)           |
| **Diagram(s)**      | ![](message/diagram/dataset-request-message.png)                  |

The Dataset Request Message is message sent by a [=Consumer=] to
a [=Catalog Service=].
The [=Catalog Service=] must respond with a [Dataset](#ack-dataset), which
is a valid instance of a [DCAT Dataset](https://www.w3.org/TR/vocab-dcat-3/#Class:Dataset).

- The message must have a `dataset` property which contains the id of the [=Dataset=].

- The [=Catalog Service=] may require an authorization token. Details for
  including that token can be found in the protocol binding, e.g., [Catalog HTTPS Binding](#catalog-https-binding).

## Response Types

The `ACK` and `ERROR` response types are mapped onto a protocol such as HTTPS. A description of an error might be
provided in protocol-dependent forms, e.g., for an HTTPS binding in the request or response body.

### ACK - Catalog

|                |                                                     |
|----------------|-----------------------------------------------------|
| **Sent by**    | [=Provider=]                                        |
| **Schema**     | [JSON Schema](message/schema/catalog-schema.json) |
| **Example**    | [Catalog Example](message/example/catalog.json)   |
| **Diagram(s)** | ![](message/diagram/catalog.png)                  |

The [=Catalog=] contains all [Datasets](#dataset) which the requester shall see.

### ACK - Dataset

|                |                                                     |
|----------------|-----------------------------------------------------|
| **Sent by**    | [=Provider=]                                        |
| **Schema**     | [JSON Schema](message/schema/dataset-schema.json) |
| **Example**    | [Dataset Example](message/example/dataset.json)   |
| **Diagram(s)** | ![](message/diagram/dataset.png)                  |

### ERROR - Catalog Error

|                |                                                           |
|----------------|-----------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                |
| **Schema**     | [JSON Schema](message/schema/catalog-error-schema.json) |
| **Example**    | [Error](message/example/catalog-error.json)             |
| **Diagram(s)** | ![](message/diagram/catalog-error.png)                  |

A Catalog Error is used when an error occurred after a [Catalog Request Message](#catalog-request-message) or
a [Dataset Request Message](#dataset-request-message) and the [=Provider=] cannot
provide its [=Catalog=] to the requester.

| Field     | Type          | Description                                                 |
|-----------|---------------|-------------------------------------------------------------|
| `code`    | String        | An optional implementation-specific error code.             |
| `reasons` | Array[object] | An optional array of implementation-specific error objects. |

## Technical Considerations

### Queries and Filter Expressions

A [=Catalog Service=] may support [=Catalog=] queries or filter expressions as an
implementation-specific feature. However, it is expected that query capabilities will be implemented by
the [=Consumer=] against the results of
a [Catalog Request Message](#catalog-request-message), as the latter is an RDF vocabulary. Client-side querying can
be scaled by periodically crawling
the [=Provider=]'s [=Catalog Services=], caching
the results, and executing queries against the locally-stored [=Catalogs=].

### Replication Protocol

The [=Catalog Protocol=] is designed to be used by federated services without the need for a replication protocol.
Each [=Consumer=] is responsible for issuing requests to
1..N [=Catalog Services=], and managing the results. It follows that a specific
replication protocol is not needed, or more precisely, each [=Consumer=] replicates data
from catalog services by issuing [Catalog Request Messages](#catalog-request-message).

The discovery protocol adopted by a particular [=Dataspace=] defines how
a [=Consumer=] discovers [=Catalog Services=].

### Security

It is expected (although not required) that [=Catalog Services=] implement access
control. A [=Catalog=] as well as individual [=Datasets=] may be restricted to trusted
parties. The [=Catalog Service=] may
require [=Consumers=] to include a security token along with
a [Catalog Request Message](#catalog-request-message). The specifics of how this is done can be found in the relevant
protocol binding, e.g., [Catalog HTTPS Binding](#catalog-https-binding). The semantics of such tokens are not part
of this specification.

#### The Proof Metadata Endpoint

When a [=Catalog=] contains protected [=Datasets=]
the [=Provider=] has two options: include
all [=Datasets=] in the [=Catalog=] response and restrict access when a contract is
negotiated; or, require one or more proofs when the [Catalog Request](#catalog-request-message) is made and filter
the [=Datasets=] accordingly. The latter option requires a mechanism for clients to
discover the type of proofs that may be presented at request time. The specifics of proof types and presenting a proof
during a [=Catalog=] request is outside the scope of the Dataspace Protocol.
However, [=Catalog Protocol=] bindings should define a proof data endpoint for
obtaining this information.

### Catalog Brokers

A [=Dataspace=] may include Catalog Brokers. A Catalog Broker is
a [=Consumer=] that has trusted access to 1..N
upstream [=Catalog Services=] and advertises their
respective [=Catalogs=] as a
single [=Catalog Service=]. The broker is expected to honor upstream access
control requirements.
