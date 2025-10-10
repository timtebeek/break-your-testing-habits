---
sidebar_position: 6
---

# Practice: Migrate to JUnit 5

Ready to practice migrating from JUnit 4 to JUnit 5? We've prepared a test class with common JUnit 4 patterns that you can improve.

## Exercise: OrderValidatorTest

The [`OrderValidatorTest`](https://github.com/timtebeek/break-your-testing-habits/blob/main/orders/src/test/java/com/github/timtebeek/orders/OrderValidatorTest.java) class in the `orders` module demonstrates several JUnit 4 patterns that need migration:

### What you'll find

- ❌ **JUnit 4 annotations**: `@Before`, `@Test` from `org.junit`.
- ❌ **Try-catch-fail pattern**: Awkward exception testing with try-catch blocks.
- ❌ **Verbose assertions**: Manual loops and checks.
- ❌ **Old assertion style**: JUnit 4's `assertTrue`, `assertEquals`, `assertFalse`.

### Your task

Migrate this test to JUnit 5 and improve the test patterns.

**Hints:**
- Look at the [Migrate to JUnit 5](migrate-to-junit5.md) page for guidance on package and annotation changes.
- JUnit 5 uses different package names (`org.junit.jupiter.api.*` instead of `org.junit.*`).
- Lifecycle annotations have new names that better express their purpose.
- Exception testing can be done more elegantly without try-catch blocks.
- JUnit 5 relaxes visibility requirements for test classes and methods.

### Running the test

You can run the test in your IDE or use the following commands: 

```bash
# Run the test
mvn test -pl orders -Dtest=OrderValidatorTest

# Or run all tests in the orders module
mvn test -pl orders
```

### Automated migration

You can also use OpenRewrite to automatically migrate the test:

```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit4to5Migration
```

See the [Migrate to JUnit 5](../junit-jupiter/migrate-to-junit5.md) page for more migration options.

## What you'll learn

By completing this exercise, you'll gain hands-on experience with:

- Converting JUnit 4 lifecycle annotations to JUnit 5.
- Modernizing exception testing patterns.
- Improving test readability and maintainability.
- Understanding the benefits of JUnit 5 over JUnit 4.
