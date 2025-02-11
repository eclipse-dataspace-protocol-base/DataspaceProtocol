# Scope

Sharing data between autonomous entities requires the provision of metadata to facilitate the transfer of [=Datasets=] by making use of a data transfer (or application layer) protocol. The [=Dataspace Protocol=] defines how this metadata is provisioned:

1. How [=Datasets=] are advertised via [=Catalogs=] (reusing terminology from [[?vocab-dcat-3]] and usage control is expressed as [=Policies=] (reusing terminology from [[?odrl-model]] vocabulary).
2. How [=Agreements=] that govern data usage are syntactically expressed and electronically negotiated.
3. How [=Datasets=] are accessed using [=Transfer Process Protocols=].

These specifications build on protocols located in the [ISO OSI model (ISO/IEC 7498-1:1994)](https://www.iso.org/standard/20269.html) layers, like HTTPS. The purpose of this specification is to define interactions between systems independent of such protocols, but describing how to implement it in an unambiguous and extensible way. To do so, the messages that are exchanged during the process are described in this specification and the states and their transitions are specified as state machines, based on the key terms and concepts of a [=Dataspace=]. On this foundation the bindings to data transfer protocols, like HTTPS, are described.

**_Note 1:_**
**This specification does not cover the data transfer process as such.** While the data transfer is controlled by the [=Transfer Process Protocol=], e.g., the initiation of the transfer channels or their decomissioning, the data transfer itself and especially the handling of technical exceptions is an obligation to the transport protocol. As an implication, the data transfer can be conducted in a separated process if required, as long as this process is to the specified extend controlled by the [=Transfer Process Protocol=]. While this document does not define the transport protocol, the structure, syntax or semantics of the data, a specification for those aspects is required and subject to the agreements of the [=Participants=] or the [=Dataspace=].

**_Note 2:_**
The classes and definitions used in this specification are reused from different standards and specifications as much as possible, in particular, DCAT [[?vocab-dcat-3]] and ODRL [[?odrl-model]]. As, however, the external definitions allow different interpretations or provide more attributes than required, this specification is leveraging _profiles_ of the original definitions rather than the complete original expressiveness. A _profile_ in this sense is a restriction or subset of an external definition, enforcing that every occurrence of an externally defined class is always conformant with the original definition. However, not every standard-compliant class might be compliant to the [=Dataspace=] profile. The profiles are not separate artifacts but implicitly contained in the JSON schemas for the [=Message Types=] of this specification.

**_Note 3:_**
A [=Participant Agents=], e.g., in the form of a [=Connector=], may implement additional features that leverage this specification, e.g., policy engines or metadata processing, as appropriate. The definitions of such are not covered by this specification. The same applies for the actual data that is transferred between the systems. 