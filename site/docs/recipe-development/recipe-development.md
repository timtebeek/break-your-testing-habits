# Recipe development

Automated code migrations are implemented using a combination of **Error Prone Refaster** templates and **OpenRewrite** recipes.
This hybrid approach provides both the expressiveness of pattern matching and the powerful refactoring capabilities of OpenRewrite.

## Architecture

The migration framework consists of three main components:

1. **Refaster Templates** - Define before/after code patterns using Google's Error Prone Refaster
2. **OpenRewrite Recipes** - Auto-generated from Refaster templates to perform actual transformations
3. **Recipe Tests** - Verify transformations work correctly on real code examples

## Project Structure

The recipes are developed in the `recipes/` module:

```
recipes/
├── pom.xml                                    # Maven configuration
├── src/main/java/
│   └── com/github/timtebeek/recipes/
│       └── AssertToAssertThat.java           # Refaster templates
├── src/main/resources/META-INF/rewrite/
│   ├── rewrite.yml                           # Recipe composition
│   └── classpath.tsv.gz                      # Type information
└── src/test/java/
    └── com/github/timtebeek/recipes/
        └── AssertToAssertThatTest.java       # Recipe tests
```

## How Refaster Templates Work

Refaster templates define code transformations using a simple before/after pattern:

```java
@RecipeDescriptor(
    name = "Convert assert to AssertJ",
    description = "Convert assert statements to AssertJ assertions."
)
static class AssertThatIsNull {
    @BeforeTemplate
    void before(Object actual) {
        assert actual == null;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual) {
        assertThat(actual).isNull();
    }
}
```

### Key Annotations

- **`@RecipeDescriptor`** - Provides recipe name and description for documentation
- **`@BeforeTemplate`** - Defines the pattern to match in existing code
- **`@AfterTemplate`** - Defines the replacement code
- **`@UseImportPolicy`** - Controls how imports are added (`STATIC_IMPORT_ALWAYS`, `IMPORT_CLASS_DIRECTLY`, etc.)

### Template Compilation

When you compile the project, the `rewrite-templating` annotation processor automatically generates OpenRewrite recipes from your Refaster templates:

```bash
mvn clean compile
# Generates: recipes/target/generated-sources/annotations/com/github/timtebeek/recipes/AssertToAssertThatRecipes.java
```

The generated class contains one `Recipe` for each Refaster template.

## Writing Refaster Templates

### Basic Pattern Matching

Templates can match various code patterns:

```java
static class AssertThatIsNullWithMessage {
    @BeforeTemplate
    void before(Object actual, String message) {
        assert actual == null : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, String message) {
        assertThat(actual).as(message).isNull();
    }
}
```

### Type Constraints

You can constrain templates to specific types:

```java
static class AssertThatStringIsEmpty {
    @BeforeTemplate
    void before(String str) {
        assert str.isEmpty();
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(String str) {
        assertThat(str).isEmpty();
    }
}
```

### Multiple Overloads

Create separate templates for different method signatures:

```java
// Without message
static class AssertThatIsNotNull {
    @BeforeTemplate
    void before(Object actual) {
        assert actual != null;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual) {
        assertThat(actual).isNotNull();
    }
}

// With message
static class AssertThatIsNotNullWithMessage {
    @BeforeTemplate
    void before(Object actual, String message) {
        assert actual != null : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, String message) {
        assertThat(actual).as(message).isNotNull();
    }
}
```

## Writing Recipe Tests

Tests use OpenRewrite's testing framework to verify transformations:

```java
class AssertToAssertThatTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new AssertToAssertThatRecipes());
    }

    @DocumentExample  // Shows this test in recipe documentation
    @Test
    void assertNull() {
        rewriteRun(
          java(
            """
            class Test {
                void test(Object obj) {
                    assert obj == null;
                }
            }
            """,
            """
            import org.assertj.core.api.Assertions;

            class Test {
                void test(Object obj) {
                    Assertions.assertThat(obj).isNull();
                }
            }
            """
          )
        );
    }
}
```

### Test Structure

1. **Before code** - First string argument shows the original code
2. **After code** - Second string argument shows the expected result
3. **Imports** - OpenRewrite automatically adds necessary imports
4. **Formatting** - Indentation and whitespace are preserved

### Best Practices for Tests

- Include one test per template to verify it works
- Use `@DocumentExample` on the most representative test
- Test edge cases (null messages, complex expressions, etc.)
- Verify imports are added correctly
- Test that the transformation doesn't break valid code

## Recipe Composition

The `rewrite.yml` file allows you to compose recipes and add preconditions:

```yaml
---
type: specs.openrewrite.org/v1beta/recipe
name: com.github.timtebeek.AssertToAssertThatRecipesForTests
displayName: Convert `assert` to `assertThat` in tests
description: Convert `assert` to `assertThat` in test methods only.
preconditions:
  - org.openrewrite.java.search.IsLikelyTest
recipeList:
  - com.github.timtebeek.recipes.AssertToAssertThatRecipes
```

### Preconditions

Preconditions ensure recipes only run in appropriate contexts:

- `org.openrewrite.java.search.IsLikelyTest` - Only transform test files
- `org.openrewrite.java.search.FindMethods` - Only transform specific methods
- Custom preconditions - Create your own filters

## Development Workflow

### 1. Generate Type Table

When starting a new recipe project, generate type information for dependencies:

```bash
mvn generate-resources -Ptypetable
```

This creates `META-INF/rewrite/classpath.tsv.gz` with type information for libraries like AssertJ.

### 2. Write Refaster Templates

Add templates to your Java file:

```java
@RecipeDescriptor(
    name = "Your Recipe Name",
    description = "What this recipe does"
)
static class YourRecipeName {
    @BeforeTemplate
    void before(/* parameters */) {
        // Pattern to match
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(/* same parameters */) {
        // Replacement code
    }
}
```

### 3. Compile to Generate Recipes

```bash
mvn clean compile
```

The annotation processor generates OpenRewrite recipes in `target/generated-sources/annotations/`.

### 4. Write Tests

Add tests to verify your transformations:

```java
@Test
void yourRecipeTest() {
    rewriteRun(
      java(
        """
        // Before code
        """,
        """
        // After code
        """
      )
    );
}
```

### 5. Run Tests

```bash
mvn test
```

### 6. Apply Recipes

Use the OpenRewrite Maven plugin to apply recipes to a codebase:

```bash
cd /path/to/target/codebase
mvn org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=com.github.timtebeek:recipes:1.0-SNAPSHOT \
  -Drewrite.activeRecipes=com.github.timtebeek.AssertToAssertThatRecipesForTests
```

## Debugging Tips

### View Generated Recipes

Check the generated recipe code:

```bash
cat target/generated-sources/annotations/com/github/timtebeek/recipes/AssertToAssertThatRecipes.java
```

### Test Individual Templates

Create focused tests for specific transformations:

```java
@Test
void specificCase() {
    rewriteRun(
      spec -> spec.recipe(new AssertToAssertThatRecipes.AssertThatIsNullRecipe()),
      java(
        "assert obj == null;",
        "assertThat(obj).isNull();"
      )
    );
}
```

## Exercises

Now it's your turn! Complete the following exercises to practice writing Refaster templates.

### Exercise 1: Assert Not Null

Complete the `AssertThatIsNotNull` template in `AssertToAssertThat.java`:

**Goal:** Convert `assert actual != null;` to `assertThat(actual).isNotNull();`

**Steps:**
1. Add the `@BeforeTemplate` annotation and method
2. Add the `@AfterTemplate` annotation and method
3. Add appropriate import policy
4. Write a test in `AssertToAssertThatTest.java`

<details>
<summary>Solution</summary>

```java
static class AssertThatIsNotNull {
    @BeforeTemplate
    void before(Object actual) {
        assert actual != null;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual) {
        assertThat(actual).isNotNull();
    }
}
```

Test:
```java
@Test
void assertNotNull() {
    rewriteRun(
      java(
        """
        class Test {
            void test(Object obj) {
                assert obj != null;
            }
        }
        """,
        """
        import org.assertj.core.api.Assertions;

        class Test {
            void test(Object obj) {
                Assertions.assertThat(obj).isNotNull();
            }
        }
        """
      )
    );
}
```
</details>

### Exercise 2: Assert Not Null with Message

Complete the `AssertThatIsNotNullWithMessage` template:

**Goal:** Convert `assert actual != null : "message";` to `assertThat(actual).as("message").isNotNull();`

<details>
<summary>Solution</summary>

```java
static class AssertThatIsNotNullWithMessage {
    @BeforeTemplate
    void before(Object actual, String message) {
        assert actual != null : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, String message) {
        assertThat(actual).as(message).isNotNull();
    }
}
```
</details>

### Exercise 3: Assert Equals

Complete the `AssertThatIsEqualTo` template:

**Goal:** Convert `assert actual.equals(expected);` to `assertThat(actual).isEqualTo(expected);`

**Hint:** You need two parameters in the template methods.

<details>
<summary>Solution</summary>

```java
static class AssertThatIsEqualTo {
    @BeforeTemplate
    void before(Object actual, Object expected) {
        assert actual.equals(expected);
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
```
</details>

### Exercise 4: Assert Equals with Message

Complete the `AssertThatIsEqualToWithMessage` template:

**Goal:** Convert `assert actual.equals(expected) : "message";` to `assertThat(actual).as("message").isEqualTo(expected);`

<details>
<summary>Solution</summary>

```java
static class AssertThatIsEqualToWithMessage {
    @BeforeTemplate
    void before(Object actual, Object expected, String message) {
        assert actual.equals(expected) : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected, String message) {
        assertThat(actual).as(message).isEqualTo(expected);
    }
}
```
</details>

### Exercise 5: Assert Not Equals

Complete the `AssertThatIsNotEqualTo` and `AssertThatIsNotEqualToWithMessage` templates:

**Goal:**
- Convert `assert !actual.equals(expected);` to `assertThat(actual).isNotEqualTo(expected);`
- Convert `assert !actual.equals(expected) : "message";` to `assertThat(actual).as("message").isNotEqualTo(expected);`

<details>
<summary>Solution</summary>

```java
static class AssertThatIsNotEqualTo {
    @BeforeTemplate
    void before(Object actual, Object expected) {
        assert !actual.equals(expected);
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected) {
        assertThat(actual).isNotEqualTo(expected);
    }
}

static class AssertThatIsNotEqualToWithMessage {
    @BeforeTemplate
    void before(Object actual, Object expected, String message) {
        assert !actual.equals(expected) : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected, String message) {
        assertThat(actual).as(message).isNotEqualTo(expected);
    }
}
```
</details>

### Exercise 6: Assert Same As

Complete the remaining templates for reference equality:

**Goal:**
- Convert `assert actual == expected;` (reference equality) to `assertThat(actual).isSameAs(expected);`
- Convert `assert actual != expected;` (reference inequality) to `assertThat(actual).isNotSameAs(expected);`
- Include versions with messages

**Note:** In Java, `==` checks reference equality for objects. For this exercise, assume the context where reference equality is intended.

<details>
<summary>Solution</summary>

```java
static class AssertThatIsSameAs {
    @BeforeTemplate
    void before(Object actual, Object expected) {
        assert actual == expected;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected) {
        assertThat(actual).isSameAs(expected);
    }
}

static class AssertThatIsSameAsWithMessage {
    @BeforeTemplate
    void before(Object actual, Object expected, String message) {
        assert actual == expected : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected, String message) {
        assertThat(actual).as(message).isSameAs(expected);
    }
}

static class AssertThatIsNotSameAs {
    @BeforeTemplate
    void before(Object actual, Object expected) {
        assert actual != expected;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected) {
        assertThat(actual).isNotSameAs(expected);
    }
}

static class AssertThatIsNotSameAsWithMessage {
    @BeforeTemplate
    void before(Object actual, Object expected, String message) {
        assert actual != expected : message;
    }

    @AfterTemplate
    @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
    void after(Object actual, Object expected, String message) {
        assertThat(actual).as(message).isNotSameAs(expected);
    }
}
```
</details>

### Bonus Exercise: Complex Conditions

**Challenge:** Create a template to convert complex assertions:

Convert:
```java
assertTrue(str != null && str.equals("Foo"))
```

To:
```java
assertThat(str).isNotNull().isEqualTo("Foo")
```

**Hint:** This is more challenging because you need to match compound boolean expressions. Consider starting with simpler patterns first.

## Resources

- [OpenRewrite Documentation](https://docs.openrewrite.org/)
- [Refaster User Guide](https://errorprone.info/docs/refaster)
- [OpenRewrite Recipe Development](https://docs.openrewrite.org/authoring-recipes)
- [AssertJ Documentation](https://assertj.github.io/doc/)

## Next Steps

After completing the exercises:

1. Run `mvn clean compile` to generate recipes
2. Run `mvn test` to verify all tests pass
3. Try applying your recipes to the `books` module
4. Experiment with more complex transformation patterns
5. Share your recipes with the community!
