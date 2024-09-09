# Introduction

The __Dataspace Protocol__ is used in the context of [Dataspaces](./model/terminology.md#dataspace) as described and defined in the subsequent sections with the purpose to support interoperability.
In this context, the specification provides fundamental technical interoperability for [Participants](./model/terminology.md#participant) in [Dataspaces](./model/terminology.md#dataspace).
Beyond the technical interoperability measures described in this specification, semantic interoperability should also be addressed by the [Participants](./model/terminology.md#participant). On the perspective of the [Dataspace](./model/terminology.md#dataspace), interoperability needs to be addressed also on the level of trust, on organizational levels, and on legal levels.
The aspect of cross-dataspace communication is not subject of this document, as this is addressed by the [Dataspaces'](./model/terminology.md#dataspace) organizational and legal agreements.

The interaction of [Participants](./model/terminology.md#participant) in a [Dataspace](./model/terminology.md#dataspace) is conducted by the [Participant Agents](./model/terminology.md#participant-agent), so-called [Connectors](./model/terminology.md#connector--data-service-), which implement the protocols described above.
While most interactions take place between [Connectors](./model/terminology.md#connector--data-service-), some interactions with other systems are required.
The figure below provides an overview on the context of this specification.

An [Identity Provider](./model/terminology.md#identity-provider) realizes the required interfaces and provides required information to implement the Trust Framework of a [Dataspace](./model/terminology.md#dataspace).
The validation of the identity of a given [Participant Agent](./model/terminology.md#participant-agent) and the validation of additional claims is a fundamental mechanism. The structure and content of such claims and identities may, however, vary between different [Dataspaces](./model/terminology.md#dataspace), as well as the structure of such an [Identity Provider](./model/terminology.md#identity-provider), e.g. a centralized system, a decentralized system or a federated system. Other specifications, like the Identity and Trust Protocol ([IATP](https://github.com/eclipse-tractusx/identity-trust)), define the respective functions.

A [Connector](./model/terminology.md#connector--data-service-) will implement additional internal functionalities, like monitoring or policy engines, as appropriate. It is not covered by this specification if a [Connector](./model/terminology.md#connector--data-service-) implements such or how.

The same applies for the actual data that is transferred between the systems. While this document does not define the transport protocol, the structure, syntax or semantics of the data, a specification for those aspects is required and subject to the agreements of the [Participants](./model/terminology.md#participant) or the [Dataspace](./model/terminology.md#dataspace).

![Overview on protocol and context](./resources/figures/ProtocolOverview.png)
