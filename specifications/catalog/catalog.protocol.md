# Catalog Protocol {#catalog-protocol}

This document outlines the [=Catalog Protocol=]. The used terms are described in section [[[#terminology]]].

## Introduction

The Catalog Protocol defines how a [=Catalog=] is requested from a [=Catalog Service=] by a [=Consumer=] using an
abstract message exchange format. The concrete message exchange wire format is defined in the binding specifications.

The [=Catalog Protocol=] reuses properties from the DCAT and ODRL vocabularies with restrictions defined in this
specification. This is done implicitly by the use of the JSON schemas and JSON-LD-contexts that are part of the DSP.
Servers have no obligation to process properties that are not part of the schemas.

## Message Types

### Catalog Request Message

|                     |                                                                                             |
|---------------------|---------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                |
| **Resulting state** | `TERMINATED`                                                                                |
| **Response**        | [ACK](#ack-catalog) or [ERROR](#error-catalog-error)                                        |
| **Schema**          | [JSON Schema](message/schema/catalog-request-message-schema.json)                           |
| **Example**         | [Message](message/example/catalog-request-message.json)                                     |
| **Properties**      | <p data-include="message/table/catalogrequestmessage.html" data-include-format="html"></p> |

The Catalog Request Message is message sent by a [=Consumer=] to
a [=Catalog Service=].
The [=Catalog Service=] must respond with a [Catalog](#ack-catalog) that adheres to the schema linked above.

- The message may have a `filter` property which contains an implementation-specific query or filter expression type
  supported by the [=Catalog Service=].

- The [=Catalog Service=] may require an authorization token. Details for
  including that token can be found in the protocol binding, e.g., [Catalog HTTPS Binding](#catalog-https-binding).
  Similarly, pagination may be defined in the protocol binding.

### Dataset Request Message

|                     |                                                                                             |
|---------------------|---------------------------------------------------------------------------------------------|
| **Sent by**         | [=Consumer=]                                                                                |
| **Resulting state** | `TERMINATED`                                                                                |
| **Response**        | [ACK](#ack-catalog) or [ERROR](#error-catalog-error)                                        |
| **Schema**          | [JSON Schema](message/schema/dataset-request-message-schema.json)                           |
| **Example**         | [Message](message/example/dataset-request-message.json)                                     |
| **Properties**      | <p data-include="message/table/datasetrequestmessage.html" data-include-format="html"></p> |

The Dataset Request Message is message sent by a [=Consumer=] to
a [=Catalog Service=].
The [=Catalog Service=] must respond with a [Dataset](#ack-dataset) that adheres to the schema linked above.

- The message must have a `dataset` property which contains the id of the [=Dataset=].

- The [=Catalog Service=] may require an authorization token. Details for
  including that token can be found in the protocol binding, e.g., [Catalog HTTPS Binding](#catalog-https-binding).

## Response Types

The `ACK` and `ERROR` response types are mapped onto a protocol such as HTTPS. A description of an error might be
provided in protocol-dependent forms, e.g., for an HTTPS binding in the request or response body.

### ACK - Catalog

|                |                                                                               |
|----------------|-------------------------------------------------------------------------------|
| **Sent by**    | [=Provider=]                                                                  |
| **Schema**     | [JSON Schema](message/schema/catalog-schema.json)                             |
| **Example**    | [Catalog Example](message/example/catalog.json)                               |
| **Properties**      | <p data-include="message/table/catalog.html" data-include-format="html"></p> |

* A [=Catalog=] _MUST_ have zero to many [=Datasets=]. (_NOTE: Since a Catalog may be dynamically generated for a request based on the requesting [=Participant=]'s credentials, it is possible for it to contain 0 matching [=Datasets=]._)
* A [=Catalog=] _MUST_ have one to many [=Data Services=] that reference a [=Connector=] where [=Datasets=] may be obtained.

### ACK - Dataset

|                |                                                                               |
|----------------|-------------------------------------------------------------------------------|
| **Sent by**    | [=Provider=]                                                                  |
| **Schema**     | [JSON Schema](message/schema/dataset-schema.json)                             |
| **Example**    | [Dataset Example](message/example/dataset.json)                               |
| **Properties**      | <p data-include="message/table/dataset.html" data-include-format="html"></p> |

* A [=Dataset=] _MUST_ have at least one `hasPolicy` attribute that contain an [=Offer=] defining the [=Policy=] associated with the [=Dataset=].
* A [=Dataset=] _MUST_ have at least one `Distributions` attribute.
* Each `DataService` object _MUST HAVE_ at least one `DataService` which specifies where the distribution is obtained. Specifically, a `DataService` specifies the endpoint for initiating a [=Contract Negotiation=] and [=Transfer Process=].

An [=Offer=] contains the following attributes:

* An [=Offer=] _MUST_ have an `@id` that is a unique identifier.
* An [=Offer=] _MUST_ be unique to a [=Dataset=] since the target of the [=Offer=] is derived from its enclosing context.
* [=Offers=] _MUST NOT_ contain any `target` attributes. The value of the `target` attribute _MUST_ be the [=Dataset=] ID. (_Note: If the [=Offer=] is used in an enclosing [=Catalog=] or [=Dataset=], there must not be any `target` attribute set._)

### ERROR - Catalog Error

|                |                                                                                    |
|----------------|------------------------------------------------------------------------------------|
| **Sent by**    | [=Consumer=], [=Provider=]                                                         |
| **Schema**     | [JSON Schema](message/schema/catalog-error-schema.json)                            |
| **Example**    | [Error](message/example/catalog-error.json)                                        |
| **Properties**      | <p data-include="message/table/catalogerror.html" data-include-format="html"></p> |

A Catalog Error is used when an error occurred after a [Catalog Request Message](#catalog-request-message) or
a [Dataset Request Message](#dataset-request-message) and the [=Provider=] cannot
provide its [=Catalog=] to the requester.

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
