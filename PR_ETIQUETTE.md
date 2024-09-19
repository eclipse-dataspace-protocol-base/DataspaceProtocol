# Etiquette for Pull Requests

## As a contributor

Submitting pull requests should be done while adhering to a couple of simple rules.

- Familiarize yourself with our [contribution guidelines](CONTRIBUTING.md).
- Before you submit a PR, open a [discussion](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/discussions/new) or an [issue](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol/issues/new) outlining your planned work and give people time to comment. It may even be advisable to contact committers using the `@mention` feature. Unsolicited PRs may get ignored or rejected.
- Create focused PRs: your work should be focused on one particular feature or bugfix. Do not create broad-scoped PRs that solve multiple issues as reviewers may reject those PR bombs outright.
- Provide a clear description and motivation in the PR description in GitHub. This makes the reviewer's life much easier. It is also helpful to outline the broad changes that were made.
- All tests should be green, especially when your PR is in `"Ready for review"`
- Mark PRs as `"Ready for review"` only when you're prepared to defend your work. By that time you have completed your work and shouldn't need to push any more commits other than to incorporate review comments.
- Merge conflicts should be resolved by rebasing onto `main` and force-pushing. Do this when your PR is ready to review.
- If you require a reviewer's input while it's still in draft, please contact the designated reviewer using the `@mention` feature and let them know what you'd like them to look at.
- Request a review from one of the [project's committers](https://projects.eclipse.org/projects/technology.dataspace-protocol-base/who). Requesting a review from anyone else is still possible, and sometimes may be advisable, but only committers can merge PRs, so be sure to include them early on.
- Re-request reviews after all remarks have been adopted. This helps reviewers track their work in GitHub.
- If you disagree with a committer's remarks, feel free to object and argue, but if no agreement is reached, you'll have to either accept the decision or withdraw your PR.
- Be civil and objective. No foul language, insulting or otherwise abusive language will be tolerated.
- The PR titles must follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
    - The title must follow the format as `<type>(<optional scope>): <description>`.
      `build`, `chore`, `ci`, `docs`, `feat`, `fix`, `perf`, `refactor`, `revert`, `style`, `test` are allowed for the `<type>`.
    - The length must be kept under 80 characters.

## As a reviewer

- Please complete reviews within five business days or delegate to another committer, removing yourself as a reviewer.
- If you have been requested as reviewer, but cannot do the review for any reason (time, lack of knowledge in particular area, etc.) please comment that in the PR and remove yourself as a reviewer, suggesting a stand-in. 
- Don't be overly pedantic.
- Don't argue basic principles (e.g., protocol architecture).
- Don't just wave through any PR. Please take the time to look at them carefully.
- Be civil and objective. No foul language, insulting or otherwise abusive language will be tolerated. The goal is to _encourage_ contributions.