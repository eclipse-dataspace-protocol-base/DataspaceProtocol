# Terms and definitions {#terminology}

The following terms and definitions introduce the core concepts, entities, and relationships that underpin a [=Dataspace=] and its communication protocols. Not all [=Dataspace=] entities have a concrete _technical_ materialization; some entities may exist as purely logical constructs.

<dfn>Agreement</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=] that has been agreed between the [=Provider=] and [=Consumer=]. It is a result of a [=Contract Negotiation=] defining the [=Policy=] agreed to for a [=Dataset=].

<dfn>Catalog</dfn>

A collection of entries representing [=Offers=] that are advertised by a [=Provider=].

<dfn>Catalog Protocol</dfn>

A set of allowable [=Message Types=] that are used to request a [=Catalog=] from a [=Catalog Service=].

<dfn>Catalog Service</dfn>

A [=Participant Agent=] that makes a [=Catalog=] available and accessible to [=Participants=].

<dfn data-lt="Data Service">Connector</dfn> (Data Service)

A [=Participant Agent=] that performs [=Contract Negotiation=] and [=Transfer Process=] operations with other [=Connectors=], by implementing [=Dataspace Protocols=]. It produces [=Agreements=] and manages [=Dataset=] sharing. 

<dfn>Consumer</dfn>

A [=Participant=] that requests access to an offered [=Dataset=].

<dfn>Contract Negotiation</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that establish an [=Agreement=]. It is an instantiation of the state machine of a [=Contract Negotiation Protocol=]. An outcome of a Contract Negotiation may be the production of an [=Agreement=].

<dfn>Contract Negotiation Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.

<dfn>Dataset</dfn>

Data or a technical service that can be shared by a [=Participant=].

<dfn>Dataspace</dfn>

A set of technical services that facilitate interoperable [=Dataset=] sharing between entities.

<dfn>Dataspace Protocol</dfn>

A set of [=Messages=] and [=Message=] sequences that enables the interaction between [=Participant Agents=] in a [=Dataspace=]. This may require additional concepts, which are not in the scope of the specifications themselves.

<dfn>Message</dfn>

An instantiation of a [=Message Type=].

<dfn>Message Type</dfn>

A definition of the structure of a [=Message=].

<dfn>Offer</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=].

<dfn>Participant</dfn>

A member of one or more [=Dataspaces=] that provides and/or consumes [=Datasets=]. It registers [=Participant Agents=] that perform tasks on its behalf.

<dfn>Participant Agent</dfn>

A technology system that performs operations and interactions in a [=Dataspace=] on behalf of a [=Participant=], such as publishing a [=Catalog=] or engaging in a [=Transfer Process=]. It is a logical construct and does not necessarily correspond to a single runtime process. While most interactions take place between so-called [=Connectors=], some interactions with other systems are required.

<dfn>Policy</dfn>

A set of rules, duties, and obligations that define the terms of use for a [=Dataset=].

<dfn>Profile</dfn>

A profile is a restriction or subset of a specification that enforces every occurrence of an externally defined class to be conformant with the original definition.

<dfn>Provider</dfn>

A [=Participant=] that offers a [=Dataset=].

<dfn>Transfer Process</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that give access to a [=Dataset=] under the terms of an [=Agreement=]. It is an instantiation of the state machine of a [=Transfer Process Protocol=].

<dfn>Transfer Process Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.
