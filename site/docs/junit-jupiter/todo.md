# TODO

## JUnit 5
- Migrate to JUnit 5
- https://docs.openrewrite.org/recipes/java/testing/junit5/junit4to5migration

## Modernize
Clear out common but noisy patterns in tests first, before applying further best practices.

- Remove `test` prefix from test method names
- https://docs.openrewrite.org/recipes/java/testing/cleanup/removetestprefix

- Remove `public` modifier from test methods and classes
- https://docs.openrewrite.org/recipes/java/testing/cleanup/testsshouldnotbepublic


- Apply further best practices
- https://docs.openrewrite.org/recipes/java/testing/junit5/junit5bestpractices

## JUnit 6
- Explain Java 17 baseline, and effect on `@Enabled/DisabledForJre`
- https://docs.openrewrite.org/recipes/java/testing/junit6/junit5to6migration
