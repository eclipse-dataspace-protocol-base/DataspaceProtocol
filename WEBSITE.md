## Static Rendering and Web Deployment

This repository contains the set of artifacts that make up the normative
and non-normative sections of the Dataspace Protocol. All artifacts are
bundled by the [respec framework](https://www.respec.org) which takes care
of rendering a static website. 

### Conventions

The following extensions to the basic markdown syntax are used in this 
specification project. Keep them handy and navigating the document will 
be easy.

- Referencing an external specification document. [Respec Docs](https://respec.org/docs/#references-0)
    - with identifier inline `[[foreign-spec-id]]`
    - with the foreign spec's display name inline `[[[foreign-spec-id]]]`
    - referencing a particular section in a remote document works via ordinary markdown links. The reference has to be added to the `References` section manually (if it's not already there).
- Defining terminology: A term is defined by wrapping it in `<dfn>Defineable</dfn>`. [Respec Docs](https://respec.org/docs/#definitions-and-linking)
- Custom section IDs: If various sections have the same heading, they must be given a unique id manually via `{#my-custom-section-id}` that can then be used for referencing it. [Respec Docs](https://respec.org/docs/#example-specifying-a-custom-id-for-a-heading)
- Referencing within the document. Please note that despite separation in multiple markdown files, there is only one html document. References to sections must be flat `(#section)` instead of path-based `../catalog/catalog.protocol.md#response-types`.
    - with the sections number and display name inline `[[[#my-section-id]]]`
    - If that's not desired, ordinary links work as well. `[my custom link](#my-section-id)`
    - referencing terminology: `[=Defineable=]`. This will work out of the box with Plurals such that `[=Definables=]` refers to the definition of `<dfn>Defineable</dfn>`.
- Code blocks work natively like in markdown.

### Rendering in your IDE

1. Locally execute the commands from the [autopublish](.github/workflows/autopublish.yaml) workflow's "Copy files for correct web access" step. All resulting folders and files are duplicates, gitignored and don't break anything.
2. Open the `index.html` file.
3. You IDE should have a feature to display html documents (either in your browser of choice or inline). Use that and you should always see the updated webpage when saving.