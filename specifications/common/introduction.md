# Introduction

The __Dataspace Protocol__ is used in the context of [=Dataspaces=] as described and defined in the subsequent sections with the purpose to support _interoperability_. In this context, the specification provides fundamental technical interoperability for [=Participants=] in [=Dataspaces=]. Beyond the technical interoperability measures described in this specification, semantic interoperability should be addressed by the [=Participants=]. On the perspective of the [=Dataspace=], interoperability needs to be addressed also on the level of trust, on organizational levels, and on legal levels. The aspect of cross-dataspace communication is not subject of this document, as this is addressed by the [=Dataspaces=]' organizational and legal agreements.

This specification is organized into the following documents:

* [[[#terminology]]] documents define key terms.
* [[[#requirements]]] declares cross-cutting functions as, e.g., the declaration of supported versions of this protocol and common data processing rules.
* [[[#catalog-protocol]]] and [[[#catalog-http]]] define how [=Catalogs=] are published and accessed as HTTPS endpoints respectively.
* [[[#negotiation-protocol]]] and [[[#negotiation-http]]] documents that define how [=Contract Negotiations=] are conducted and requested via HTTPS endpoints.
* [[[#transfer-protocol]]] and [[[#transfer-http]]] documents that define how [=Transfer Processes=] using a given data transfer protocol are governed via HTTPS endpoints.