import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Migrate to JUnit 5

JUnit 5 (also known as JUnit Jupiter) was released in 2017 and introduced significant improvements over JUnit 4.
Migrating from JUnit 4 to JUnit 5 involves updating imports, annotations, and taking advantage of new features.

## Package changes

JUnit 5 uses different package names and imports compared to JUnit 4.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="CalculatorTest.java"
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class CalculatorTest {

    @BeforeClass
    public static void setUpClass() {
        // Runs once before all tests
    }

    @Before
    public void setUp() {
        // Runs before each test
    }

    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Ignore("Not implemented yet")
    @Test
    public void testSubtraction() {
        assertTrue(true);
    }

    @After
    public void tearDown() {
        // Runs after each test
    }

    @AfterClass
    public static void tearDownClass() {
        // Runs once after all tests
    }
}
```

:::info

JUnit 4 uses `org.junit.*` packages and requires public visibility for test classes and methods.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CalculatorTest {

    @BeforeAll
    static void setUpClass() {
        // Runs once before all tests
    }

    @BeforeEach
    void setUp() {
        // Runs before each test
    }

    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Disabled("Not implemented yet")
    @Test
    void testSubtraction() {
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        // Runs after each test
    }

    @AfterAll
    static void tearDownClass() {
        // Runs once after all tests
    }
}
```

:::tip

JUnit 5 uses `org.junit.jupiter.api.*` packages, allows package-private visibility, and has more consistent naming:
- `@Before` → `@BeforeEach`
- `@After` → `@AfterEach`
- `@BeforeClass` → `@BeforeAll`
- `@AfterClass` → `@AfterAll`
- `@Ignore` → `@Disabled`

:::

</TabItem>
</Tabs>

## Exception testing

JUnit 5 introduces `assertThrows()` to replace the awkward `@Test(expected=...)` pattern from JUnit 4.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="ExceptionTest.java"
import org.junit.Test;

public class ExceptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException() {
        throw new IllegalArgumentException("Invalid argument");
    }
}
```

:::warning

The `@Test(expected=...)` pattern doesn't allow you to verify the exception message or other properties.
Any code in the test method can throw the exception, making it unclear which statement is expected to fail.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ExceptionTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionTest {

    @Test
    void shouldThrowException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Invalid argument");
        });
        assertEquals("Invalid argument", ex.getMessage());
    }
}
```

:::tip

JUnit 5's `assertThrows()` makes it clear which code is expected to throw the exception and allows you to verify exception properties.

:::

</TabItem>
</Tabs>

## Automated migration

Manual migration can be tedious and error-prone. OpenRewrite provides automated migration recipes.

```xml title="pom.xml"
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.43.0</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.testing.junit5.JUnit4to5Migration</recipe>
        </activeRecipes>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-testing-frameworks</artifactId>
            <version>2.21.0</version>
        </dependency>
    </dependencies>
</plugin>
```

```bash
# Run the migration
mvn rewrite:run
```

:::tip

OpenRewrite's [JUnit4to5Migration](https://docs.openrewrite.org/recipes/java/testing/junit5/junit4to5migration) recipe automatically handles:
- Updating imports and packages
- Converting annotations (`@Before` → `@BeforeEach`, etc.)
- Migrating exception testing patterns
- Updating assertions to JUnit 5 equivalents
- Converting JUnit 4 rules to JUnit 5 extensions

:::
