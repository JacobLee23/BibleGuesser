# Branch Naming Policy

This document defines the naming conventions for all branches in the **BibleGuesser** repository.

## Branch Types

| Type | Prefix | Description |
| :--- | :--- | :--- |
| Main | `master` | Production-ready default branch |
| Development | `develop` | Integration branch for new features |
| Feature | `feat/` | New functionality or improvements |
| Bugfix | `bugfix/` | Bug fixes |
| Hotfix | `hotfix/` | Critical fixes to production |
| Chore | `chore/` | Changes that do not relate to a fix or feature |
| Refactor | `refactor/` | Refactored code that neither fixes a bug nor adds a feature  |
| Documentation | `docs/` | Updates to documentation |
| Style | `style/` | Changes that do not affect the meaning of the code, likely related to code formatting |
| Test | `test/` | Including new or correcting previous tests |
| Performance | `perf/` | Performance improvements |
| Continuous Integration | `ci/` | Changes related to continuous integration |
| Build | `build/` | Changes that affect the build system or external dependencies |
| Revert | `revert/` | Reverts a previous commit |
| Release | `release/` | Release preparation |

## Branch Naming Rules

All branch names must match the following regular expression:

```
^(master|develop|(feat|bugfix|hotfix|chore|refactor|docs|style|test|perf|ci|build|revert|release)\/[-.0-9_a-z]+)$
```

## Enforcement

Branch names are validated by a CI/CD pipeline using regular expression checks. Branches that do not comply will fail automated checks and cannot be merged.
