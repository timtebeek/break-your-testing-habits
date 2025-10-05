---
sidebar_position: 4
---

# TODO

## Migrate
- Show migration steps for Maven and Gradle
- Explain mapping of old assertions to AssertJ

## Create recipe
- Explain how migration is implemented with mix of Error Prone Support Refaster and OpenRewrite recipes
- Exercise for new refaster rule converting `assert object != null, "message"` to `assertThat(object).as("message").isNotNull()`
- Exercise for new refaster rule converting `assertTrue(str != null && str.equals("Foo"))` to `assertThat(obj).isNotNull().isEqualTo("Foo")`

## Enforce
- Explain how to enforce AssertJ usage with Error Prone custom check
- Explain how to run recipes on pull requests with GitHub Actions
