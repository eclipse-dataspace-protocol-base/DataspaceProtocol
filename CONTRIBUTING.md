# Contributing to the Project

Thank you for being interested in contributing to the [Dataspace Protocol](https://projects.eclipse.org/projects/technology.dataspace-protocol-base)!

* [Eclipse Contributor Agreement](#eclipse-contributor-agreement)
* [How to Contribute](#how-to-contribute)
  * [Discuss](#discuss)
  * [Create an Issue](#create-an-issue)
  * [Submit a Pull Request](#submit-a-pull-request)
* [EF Project Roles](#ef-project-roles)
  * [Contributor](#contributor)
    * [Privileges & Responsibilities](#privileges--responsibilities)
    * [Becoming a Contributor](#becoming-a-contributor)
    * [Retirement of Contributors](#retirement-of-contributors)
  * [Committer](#committer)
    * [Privileges & Responsibilities](#privileges--responsibilities-1)
    * [Special Role: Normative Editor](#special-role-normative-editor)
    * [Becoming a Committer](#becoming-a-committer)
    * [Retirement of Committers](#retirement-of-committers)
* [Contact Us](#contact-us)

## Eclipse Contributor Agreement

Before your contribution can be accepted by the project, you need to create and electronically sign an [Eclipse Contributor Agreement (ECA)](http://www.eclipse.org/legal/ecafaq.php):

1. Log in to the [Eclipse Foundation website](https://accounts.eclipse.org/user/login/). If you still need to, create an account within the Eclipse Foundation.
2. Click on "Eclipse ECA" and complete the form.

_Note: Be sure to use the same email address in your Eclipse Account that you intend to use when you commit to GitHub._

## How to Contribute

### Discuss

If you want to share an idea to further enhance the project or discuss potential use cases, please feel free to create a [GitHub discussion](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/discussions).

If you feel there is a bug or issue, contribute to the discussions in **existing issues**; otherwise, [create a new issue](#create-an-issue).

### Create an Issue

If you have identified a bug or want to formulate a working item that you want to concentrate on, feel free to create a new issue at the repository's corresponding [GitHub Issues page](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/issues).

**Before doing so, please consider searching for potentially suitable [existing issues](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/issues?q=is%3Aissue+is%3Aopen).**

We also use [GitHub's default label set](https://docs.github.com/en/issues/using-labels-and-milestones-to-track-work/managing-labels) extended by custom ones to classify issues and improve findability.

If an issue appears to cover changes that will have a (huge) impact on the specification and need to be discussed first, or if you have a question regarding the usage of the protocols, please create a discussion _before_ raising an issue.

Please note that if an issue covers a topic or the response to a question that may be interesting for other contributors or for further discussions, it should be converted to a discussion and not be closed.

### Submit a Pull Request

In addition to the contribution guideline in the [Eclipse project handbook](https://www.eclipse.org/projects/handbook/#contributing), we would appreciate it if your pull request (PR) addressed the following points.

* Conform to the [PR Etiquette](PR_ETIQUETTE.md).

* Always apply the following copyright header to specific files in your work, replacing the fields enclosed by curly brackets "{}" with your identifying information. (Don't include the curly brackets!) Enclose the text using the appropriate comment syntax for the file format.

  "`text
  Copyright (c) {year} {owner}[ and others]

  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0, which is available at
  https://www.apache.org/licenses/LICENSE-2.0

  SPDX-License-Identifier: Apache-2.0

  Contributors:
  {name} - {description}
    ```

* The git commit messages should comply with the following format:
    ```
    <prefix>(<scope>): <description>
    ```

  Use the [imperative mood](https://github.com/git/git/blob/master/Documentation/SubmittingPatches)  as in "fix bug" or "add feature" (lowercase at the beginning) rather than "fixed bug" or "added feature" and [mention the GitHub issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue).

  All Committers and all commits are bound to the [Developer Certificate of Origin](https://www.eclipse.org/legal/DCO.php). As such, all parties involved in a contribution must have valid ECAs. Additionally, commits can include a ["Signed-off-by" entry](https://wiki.eclipse.org/Development_Resources/Contributing_via_Git).

* PR descriptions should use the current [PR template](.github/PULL_REQUEST_TEMPLATE.md)

* Submit a draft PR early and add people who have previously worked on the same content as the reviewer. Make sure automatic checks pass before marking it as "ready for review":

  * _Intellectual Property Validation_ verifying the [Eclipse CLA](#eclipse-contributor-agreement) has been signed, and commits have been signed-off.
  * _Prevent Changes to Release Files_ checks if old release documents have been changed unintentionally.

## EF Project Roles

The [Eclipse Foundation Project Handbook](https://www.eclipse.org/projects/handbook/#roles) defines roles for projects such as _Committer_ and _Contributor_.

### Contributor

Anybody can be a Contributor. To be a Contributor, you just need to contribute. Contributions typically take the form of content or documentation submitted to the project's repository, but may also take the form of answering questions in project and community forums, and more.

#### Privileges & Responsibilities
- Ability to commit code and documentation.
- Participation in project meetings and discussions.
- Recognition in project credits and contributions.
- Adhere to project guidelines and coding standards.
- Review and provide feedback on contributions from others.
- Maintain clear communication with the Committer team.
- Submit high-quality code and documentation.
- Participate actively in community discussions.

#### Becoming a Contributor

Everyone who contributes to the project is a Contributor. However, official EF Contributors are mentioned on the [project's page](https://projects.eclipse.org/projects/technology.dataspace-protocol-base/who). They have additional rights on GitHub, i.e., to label issues, add assignees, and ask for specific reviewers of PRs.

#### Retirement of Contributors

After one year of inactivity, Contributors are removed from the [project's page](https://projects.eclipse.org/projects/technology.dataspace-protocol-base/who). Although this changes their technical role on GitHub, they remain in the [list of contributors on GitHub](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/graphs/contributors).

### Committer

For Eclipse projects, Committers hold the keys. They decide on contributions and ultimately decide what is delivered to the community.

#### Privileges & Responsibilities

* Full access to commit code directly to the project's repository.
* Ability to manage and approve contributions from other contributors.
* Voting rights in project governance and decision-making.
* Access to advanced project resources and tools.
* Ensure the quality and integrity of the specification.
* Mentor and support new contributors.
* Facilitate project meetings and discussions.
* Maintain documentation and project standards.
* Actively engage with the community and promote collaboration.

#### Special Role: Normative Editor

Every specification project has some appointed editors who are responsible for the following:
* Ensuring that the specification meets established standards and guidelines.
* Reviewing and revising the content for clarity, precision, and consistency.
* Managing the formal aspects of the specification, including structure and formatting.
* Collaborating with authors and contributors to align the document with project goals.
* Ensuring that the specification is technically accurate.

_As of 17.10.2024, this role will be assumed by every active Committer._

#### Becoming a Committer

Committers are appointed at the time of project creation or elected by the existing project team. To become a Committer, you need to have a proven track record covering significant contributions like:

* Demonstrated expertise in the project
* History of quality contributions
* Community involvement in the GitHub Issues or Discussions section

The Committer election process will always be initiated by existing Committers. They will contact you and ask your willingness to fulfil the Committer's rights and obligations. The project lead then starts the official EF Committer election process, which follows a democratic voting process. A list of active Committers is available on the [project's page](https://projects.eclipse.org/projects/technology.dataspace-protocol-base/who).

#### Retirement of Committers

Committers can retire themselves from a project at any point in time. In case of inactivity of a Committer for six months, the project lead is asked to put the retirement up for discussion. Before retiring a Committer, the project's community should be informed of the change, and the Committer must be given a chance to defend retaining their status.

Retired Committers cannot appoint a person (e.g., from the same organisation) as a stand-in. New Committers **must** pass the official process of [becoming a Committer](#becoming-a-committer). Retired Committers are listed as "Historic Committers" on a [project's page](https://projects.eclipse.org/projects/technology.dataspace-protocol-base/who). To restore a Historic Committer to Committer status, they must be re-elected.


## Contact Us

If you have questions or suggestions, do not hesitate to contact the project developers via the [project's "dev" list](https://accounts.eclipse.org/mailing-list/dataspace-protocol-base-dev).

We have bi-weekly Committer, and community calls on Thursdays. Please find the meeting information [in our discussion section](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/discussions/11).

_If you have a "Committer" status, you will be automatically included in the meeting invite for the Committers meeting._