# Terms and definitions {#terminology}

This and the following sections define the core concepts, entities, and relationships that underpin a __dataspace__ and its protocol.

<dfn>Agreement</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=] that has been signed by both the [=Provider=] and consumer [=Participants=]. An Agreement is a result of a [=Contract Negotiation=] and is associated with _exactly one_ [=Dataset=].

<dfn>Catalog</dfn>

A collection of entries representing [=Datasets=] and their [=Offers=] that is advertised by a [=Provider=] [=Participant=].

<dfn>Catalog Protocol</dfn>

A set of allowable [=Message Types=] that are used to request a [=Catalog=] from a [=Catalog Service=].

<dfn>Catalog Service</dfn>

A [=Participant Agent=] that makes a [=Catalog=] accessible to [=Participants=].

<dfn data-lt="Data Service">Connector</dfn> (Data Service)

A [=Participant Agent=] that produces [=Agreements=] and manages [=Dataset=] sharing.

<dfn>Consumer</dfn>

A [=Participant Agent=] that requests access to an offered [=Dataset=].

<dfn>Contract Negotiation</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that establish an [=Agreement=]. It is an instantiation of the state machine of a [=Contract Negotiation Protocol=].

<dfn>Contract Negotiation Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.

<dfn>Credential Issuer</dfn>

A Credential Issuer is a trusted technology system that issues verifiable credentials for a [=Participant=] and [=Participant Agents=].

<dfn>Dataset</dfn>

Data or a technical service that can be shared by a [=Participant=].

<dfn>Dataspace</dfn>

A set of technical services that facilitate interoperable [=Dataset=] sharing between entities.

<dfn>Dataspace Governance Authority</dfn>

An entity that manages a [=Dataspace=]. The form and capabilities of a Dataspace Governance Authority are not covered in these specifications.

<dfn data-lt="Dataspace Registry" >Dataspace Registration Service</dfn> (Dataspace Registry)

A technology system that maintains the state of [=Participants=] in a [=Dataspace=].  The form and capabilities of a Dataspace Registration Service are not covered in these specifications.

<dfn>Identity Provider</dfn>

A trusted technology system that creates, maintains, and manages identity information for a [=Participant=] and [=Participant Agents=].

<dfn>Message</dfn>

An instantiation of a [=Message Type=].

<dfn>Message Type</dfn>

A definition of the structure of a [=Message=].

<dfn>Offer</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=].

<dfn>Participant</dfn>

A [=Dataspace=] member that provides and/or consumes [=Datasets=].

<dfn>Participant Agent</dfn>

A technology system that performs operations on behalf of a [=Participant=] that offers a [=Dataset=].

<dfn data-lt="Usage Policy">Policy</dfn>

A set of rules, duties, and obligations that define the terms of use for a [=Dataset=]. Also referred to as "Usage Policy".

<dfn>Provider</dfn>

A [=Participant Agent=] that offers a [=Dataset=].

<dfn>Transfer Process</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that give access to a [=Dataset=] under the terms of an [=Agreement=]. It is an instantiation of the state machine of a [=Transfer Process Protocol=].

<dfn>Transfer Process Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.
