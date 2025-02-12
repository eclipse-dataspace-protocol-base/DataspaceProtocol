# Introduction

The __Dataspace Protocol__ is used in the context of [=Dataspaces=] as described and defined in the subsequent sections with the purpose to support _interoperability_. In this context, the specification provides fundamental technical interoperability for [=Participants=] in [=Dataspaces=]. 

This specification builds on protocols located in the [ISO OSI model (ISO/IEC 7498-1:1994)](https://www.iso.org/standard/20269.html) layers, like HTTPS. The purpose of this specification is to define interactions between systems independent of such protocols, but describing how to implement it in an unambiguous and extensible way. To do so, the [=Messages=] that are exchanged during the process are described in this specification and the states and their transitions are specified as state machines, based on the key terms and concepts of a [=Dataspace=]. On this foundation the bindings to data transfer protocols, like HTTPS, are described.

_Note: This specification does not cover the data transfer as such. While this is controlled by the [=Transfer Process Protocol=], e.g., the initiation of the transfer channels or their decomissioning, the data transfer itself and especially the handling of technical exceptions is an obligation to the transport protocol._

The classes and definitions used in this specification are reused from different standards and specifications as much as possible, in particular, DCAT [[?vocab-dcat-3]] and ODRL [[?odrl-model]]. As, however, the external definitions allow different interpretations or provide more attributes than required, this specification is leveraging _profiles_ of the original definitions rather than the complete original expressiveness. A _profile_ in this sense is a restriction or subset of an external definition, enforcing that every occurrence of an externally defined class is always conformant with the original definition. However, not every standard-compliant class might be compliant to the [=Dataspace=] profile. The profiles are not separate artifacts but implicitly contained in the JSON schemas for the [=Message Types=] of this specification.

This specification is organized into the following documents:

* [[[#terminology]]] documents define key terms.
* [[[#requirements]]] declares cross-cutting functions as, e.g., the declaration of supported versions of this protocol and common data processing rules.
* [[[#catalog-protocol]]] and [[[#catalog-http]]] define how [=Catalogs=] are published and accessed as HTTPS endpoints respectively.
* [[[#negotiation-protocol]]] and [[[#negotiation-http]]] documents that define how [=Contract Negotiations=] are conducted and requested via HTTPS endpoints.
* [[[#transfer-protocol]]] and [[[#transfer-http]]] documents that define how [=Transfer Processes=] using a given data transfer protocol are governed via HTTPS endpoints.