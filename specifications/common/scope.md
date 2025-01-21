# Scope

Sharing data between autonomous entities requires the provision of metadata to facilitate the transfer of [=Datasets=]
by making use of a data transfer (or application layer) protocol.
The __Dataspace Protocol__ defines how this metadata is provisioned:

1. How [=Datasets=] are advertised via [=Catalogs=] (reusing terminology from [[?vocab-dcat-3]] and usage control is expressed as [=Policies=] (reusing terminology from [[?odrl-model]] vocabulary).
2. How [=Agreements=] that govern data usage are syntactically expressed and electronically negotiated.
3. How [=Datasets=] are accessed using [=Transfer Process Protocols=].

These specifications build on protocols located in the [ISO OSI model (ISO/IEC 7498-1:1994)](https://www.iso.org/standard/20269.html) layers, like HTTPS.
The purpose of this specification is to define interactions between systems independent of such protocols, but
describing how to implement it in an unambiguous and extensible way.
To do so, the messages that are exchanged during the process are described in this specification and the states and
their transitions are specified as state machines, based on the key terms and concepts of
a [Dataspace](../model/terminology.md#dataspace).
On this foundation the bindings to data transfer protocols, like HTTPS, are described.

The specifications are organized into the following documents:

* [__*Dataspace Model*__](#dataspace-information-model) and [__*Dataspace Terminology*__](#terminology) documents that
  define key terms.
* [__*Common Functionalities*__](#general-common-protocol-requirements) declares cross-cutting functions as, e.g., the declaration of
  supported versions of this Dataspace Protocol and common data processing rules.
* [__*Catalog Protocol*__](#catalog-protocol) and [__*Catalog HTTPS Binding*__](#catalog-https-binding) documents that
  define how [=Catalogs=] are published and accessed as HTTPS endpoints respectively.
* [__*Contract Negotiation Protocol*__](#contract-negotiation-protocol) and [__*Contract Negotiation HTTPS Binding*
  __](#contract-negotiation-https-binding) documents that define how [=Contract Negotiations=] are conducted and
  requested via HTTPS endpoints.
* [__*Transfer Process Protocol*__](#transfer-process-protocol) and [__*Transfer Process HTTPS Binding*
  __](#transfer-process-https-binding) documents that define how [=Transfer Processes=] using a given data transfer
  protocol are governed via HTTPS.
  endpoints.

> **This specification does not cover the data transfer process as such.**
>
> While the data transfer is controlled by the __*Transfer Process Protocol*__ mentioned above, e.g. the initation of
> the transfer channels or their decomissioning, the data transfer itself and especially the handling of technical
> exceptions is an obligation to the Transport Protocol.
>
> As an implication, the data transfer can be conducted in a separated process if required, as long as this process is
> to the specified extend controlled by the __*Transfer Process Protocol*__.
>
> Nevertheless, illustrative message examples are provided in the [__*Transfer Process Protocol section*
__](#message-types-1). The best practices section may contain further non-normative examples and explanations.
