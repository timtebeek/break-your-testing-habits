---
sidebar_position: 9
---

# Practice: Improve Test Expressiveness

Ready to practice improving test expressiveness with AssertJ? We've prepared a test class with verbose JUnit assertions that you can refactor.

## Exercise: DiscountCalculatorTest

The [`DiscountCalculatorTest`](https://github.com/timtebeek/break-your-testing-habits/blob/main/orders/src/test/java/com/github/timtebeek/orders/DiscountCalculatorTest.java) class in the `orders` module demonstrates common assertion anti-patterns:

### What you'll find

- ❌ **Verbose JUnit assertions**: `assertEquals`, `assertTrue`, `assertNotNull`
- ❌ **Manual BigDecimal comparisons**: Using `compareTo()` in assertions

### Your task

Refactor the tests to use AssertJ for better expressiveness.

**Hints:**
- AssertJ uses a fluent API that starts with `assertThat(actualValue)`.
- Look at the [Poor expressiveness](../bad-habits/poor-expressiveness.md) examples for patterns.
- BigDecimal comparisons have special assertion methods that are more readable than `compareTo()`.
- Multiple assertions on the same object can be chained together.
- The `.as()` method can add descriptive messages to your assertions.
- Check the reference table below for common patterns.

### Running the test

You can run the test in your IDE or use the following commands: 

```bash
# Run the test
mvn test -pl orders -Dtest=DiscountCalculatorTest

# Or run all tests in the orders module
mvn test -pl orders
```

### Automated refactoring

You can use OpenRewrite to automatically migrate JUnit assertions to AssertJ:

```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.java.testing.assertj.Assertj
```

## What you'll learn

By completing this exercise, you'll gain hands-on experience with:

- Converting verbose JUnit assertions to expressive AssertJ assertions.
- Using AssertJ's fluent API for better readability.
- Handling BigDecimal comparisons elegantly.
- Writing assertions that clearly convey test intent.
- Creating better failure messages.
