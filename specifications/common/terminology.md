# Terms and definitions {#terminology}

The following terms and definitions introduce the core concepts, entities, and relationships that underpin a __dataspace__ and its communication protocols. Not all [=Dataspace=] entities have a concrete _technical_ materialization; some entities may exist as purely logical constructs.

<dfn>Agreement</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=] that has been signed by both the [=Provider=] and [=Consumer=]. It is a result of a [=Contract Negotiation=] defining the [=Policy=] agreed to for a [=Dataset=]. 

* An Agreement _MUST_ be associated with exactly one [=Dataset=]
* An Agreement _MUST_ include exactly one `target` attribute that is the identifier of the [=Dataset=] the [=Agreement=] is associated with.

<dfn>Catalog</dfn>

A collection of entries representing [=Offers=] that are advertised by a [=Provider=].

* A Catalog _MUST_ have zero to many [=Datasets=]. (_NOTE: Since a Catalog may be dynamically generated for a request based on the requesting [=Participant=]'s credentials, it is possible for it to contain 0 matching [=Datasets=]._)
* A Catalog _MUST_ have one to many [=Data Services=] that reference a [=Connector=] where [=Datasets=] may be obtained.

<dfn>Catalog Protocol</dfn>

A set of allowable [=Message Types=] that are used to request a [=Catalog=] from a [=Catalog Service=].

<dfn>Catalog Service</dfn>

A [=Participant Agent=] that makes a [=Catalog=] available and accessible to [=Participants=].

<dfn data-lt="Data Service">Connector</dfn> (Data Service)

A [=Participant Agent=] that performs [=Contract Negotiation=] and [=Transfer Process=] operations with other Connectors, by implementing [=Dataspace Protocols=]. It produces [=Agreements=] and manages [=Dataset=] sharing. 

<dfn>Consumer</dfn>

A [=Participant=] that requests access to an offered [=Dataset=].

<dfn>Contract Negotiation</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that establish an [=Agreement=]. It is an instantiation of the state machine of a [=Contract Negotiation Protocol=]. An outcome of a Contract Negotiation may be the production of an [=Agreement=].

<dfn>Contract Negotiation Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.

<dfn>Dataset</dfn>

Data or a technical service that can be shared by a [=Participant=].

* A Dataset _MUST_ have at least one `hasPolicy` attribute that contain an [=Offer=] defining the [=Policy=] associated with the [=Dataset=].
* A Dataset _MUST_ have at least one `Distributions` attribute.
* Each `DataService` object _MUST HAVE_ at least one `DataService` which specifies where the distribution is obtained. Specifically, a `DataService` specifies the endpoint for initiating a [=Contract Negotiation=] and [=Transfer Process=].

<dfn>Dataspace</dfn>

A set of technical services that facilitate interoperable [=Dataset=] sharing between entities.

<dfn>Dataspace Protocol</dfn>

A set of messages and message sequences that enables the interaction between [=Participant Agents=] in a [=Dataspace=]. This may require additional concepts, which are not in the scope of the specifications themselves.

<dfn>Message</dfn>

An instantiation of a [=Message Type=].

<dfn>Message Type</dfn>

A definition of the structure of a [=Message=].

<dfn>Offer</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=]. 

* An Offer _MUST_ have an `@id` that is a unique identifier.
* An Offer _MUST_ be unique to a [=Dataset=] since the target of the [=Offer=] is derived from its enclosing context.
* Offers _MUST NOT_ contain any `target` attributes. The value of the `target` attribute _MUST_ be the [=Dataset=] ID. (_Note: If the Offer is used in an enclosing [=Catalog=] or [=Dataset=], there must not be any `target` attribute set._)

<dfn>Participant</dfn>

A member of one or more [=Dataspaces=] that provides and/or consumes [=Datasets=]. It registers [=Participant Agents=] that perform tasks on its behalf.

<dfn>Participant Agent</dfn>

A technology system that performs operations and interactions in a [=Dataspace=] on behalf of a [=Participant=], such as publishing a [=Catalog=] or engaging in a [=Transfer Process=]. It is a logical construct and does not necessarily correspond to a single runtime process. While most interactions take place between so-called [=Connectors=], some interactions with other systems are required.

<dfn>Policy</dfn>

A set of rules, duties, and obligations that define the terms of use for a [=Dataset=].

<dfn>Provider</dfn>

A [=Participant=] that offers a [=Dataset=].

<dfn>Transfer Process</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that give access to a [=Dataset=] under the terms of an [=Agreement=]. It is an instantiation of the state machine of a [=Transfer Process Protocol=].

<dfn>Transfer Process Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.
