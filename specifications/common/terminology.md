# Terms and definitions {#terminology}

The following terms and definitions introduce the core concepts, entities, and relationships that underpin a [=Dataspace=] and its communication protocols. Not all [=Dataspace=] entities have a concrete _technical_ materialization; some entities MAY exist as purely logical constructs.

<dfn>Agreement</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=] that has been agreed between the [=Provider=] and [=Consumer=]. It is a result of a [=Contract Negotiation=] defining the [=Policy=] agreed to for a [=Dataset=].

Note 1 to entry: The policies agreed upon typically include machine-processable access and usage policies.

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

A set of interactions between a [=Provider=] and [=Consumer=] that establish an [=Agreement=]. It is an instantiation of the state machine of a [=Contract Negotiation Protocol=]. An outcome of a Contract Negotiation MAY be the production of an [=Agreement=].

<dfn>Contract Negotiation Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.

<dfn>Data Sharing</dfn>

access to the same data by more than one authorized entity.

Note 1 to entry: Use of the data can be synchronous or asynchronous.

Note 2 to entry: Data can be shared, for example, (i) by allowing access to, or the execution of operations over, the original dataset, or (ii) by giving a copy of the data to the interested entity.

Note 3 to entry: The way in which data is shared fundamentally influences the available controls and the statements needed in a data sharing agreement.

[SOURCE: [[?iso-20151-1]]]

<dfn>Dataset</dfn>

Data or a technical service that can be shared by a [=Participant=].

<dfn>Dataspace</dfn>

[=Environment=] enabling trusted [=Data Sharing=] between participating parties, based on an agreed [=Governance Framework=], along with an agreed set of [=Policies=], semantic models, standardized protocols, processes, and facilitating services.

[SOURCE: [[?iso-20151-1]]]

Note 1 to entry: This specification covers only protocols to facilitate interoperable [=Dataset=] sharing between entities based on technical services. The overarching concepts and characteristics of Dataspaces are described in [ISO/IEC FDIS 20151-1](#bib-iso-20151-1).

<dfn>Dataspace Protocol</dfn>

A set of Messages and Message sequences that enables the interaction between [=Participant Agents=] in a [=Dataspace=]. This may require additional concepts, which are not in the scope of the specification itself.

<dfn>Data Transfer Protocol</dfn>

A set of rules and conventions that dictate how data is transmitted over a network by defining the format, error handling, and flow control. Examples include HTTP, FTP, MQTT, and AMQP.

<dfn>Environment</dfn>

context determining the setting and circumstances of all influences upon a system.

[SOURCE: [[?iso-20151-1]]]

<dfn>Governance</dfn>

human-based system comprising directing, overseeing and accountability.

[SOURCE: ISO/IEC 38500:2024, 3.3]

<dfn>Governance Framework</dfn>

strategies, policies, decision-making structures and accountabilities through which the organization's [=Governance=] arrangements operate.

[SOURCE: [[?iso-20151-1]]]

<dfn>Message Type</dfn>

A definition of the structure of a Message.

<dfn>Offer</dfn>

A concrete [=Policy=] associated with a specific [=Dataset=].

<dfn>Participant</dfn>

A member of one or more [=Dataspaces=] that provides and/or consumes [=Datasets=]. It registers [=Participant Agents=] that perform tasks on its behalf.

<dfn>Participant Agent</dfn>

A technology system that performs operations and interactions in a [=Dataspace=] on behalf of a [=Participant=], such as publishing a [=Catalog=] or engaging in a [=Transfer Process=]. It is a logical construct and does not necessarily correspond to a single runtime process. While most interactions take place between so-called [=Connectors=], some interactions with other systems are required.

<dfn>Policy</dfn>

A set of rules, duties, and obligations that define the terms of use for a [=Dataset=].

<dfn>Profile</dfn>

A restriction or subset of a specification that enforces every occurrence of an externally defined class to be conformant with the original definition.

<dfn>Provider</dfn>

A [=Participant=] that offers a [=Dataset=].

<dfn>Transfer Process</dfn>

A set of interactions between a [=Provider=] and [=Consumer=] that give access to a [=Dataset=] under the terms of an [=Agreement=]. It is an instantiation of the state machine of a [=Transfer Process Protocol=].

<dfn>Transfer Process Protocol</dfn>

A set of allowable [=Message Type=] sequences defined as a state machine.
