---
sidebar_position: 1
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Migrate to JUnit 5

JUnit 5 (also known as JUnit Jupiter) was released in 2017 and introduced significant improvements over JUnit 4.
Migrating from JUnit 4 to JUnit 5 involves updating imports, annotations, and taking advantage of new features.

## Package changes

JUnit 5 uses different package names and imports compared to JUnit 4.

<Tabs>
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

<Tabs>
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

<Tabs groupId="projectType">
<TabItem value="moderne-cli" label="Moderne CLI">

The Moderne CLI allows you to run OpenRewrite recipes on your project without needing to modify your build files,
against serialized Lossless Semantic Tree (LST) of your project for a considerable performance boost & across projects.

You will need to have configured the [Moderne CLI](https://docs.moderne.io/user-documentation/moderne-cli/getting-started/cli-intro) on your machine before you can run the following command.

1. If project serialized Lossless Semantic Tree is not yet available locally, then build the LST.
   This is only needed the first time, or after extensive changes:
```bash title="shell"
mod build ~/workspace/
```

2. If the recipe is not available locally yet, then you can install it once using:
```shell title="shell"
mod config recipes jar install org.openrewrite.recipe:rewrite-testing-frameworks:LATEST
```

3. Run the recipe.
```shell title="shell"
mod run ~/workspace/ --recipe org.openrewrite.java.testing.junit5.JUnit4to5Migration
```

</TabItem>
<TabItem value="maven-command-line" label="Maven Command Line">

You will need to have [Maven](https://maven.apache.org/download.cgi) installed on your machine before you can run the following command.

```shell title="shell"
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit4to5Migration -Drewrite.exportDatatables=true
```

</TabItem>
<TabItem value="maven" label="Maven POM">

You may add the plugin to your `pom.xml` file, so that it is available for all developers and CI/CD pipelines.

1. Add the following to your `pom.xml` file:

```xml title="pom.xml"
<project>
  <build>
    <plugins>
      <plugin>
        <groupId>org.openrewrite.maven</groupId>
        <artifactId>rewrite-maven-plugin</artifactId>
        <version>LATEST</version>
        <configuration>
          <exportDatatables>true</exportDatatables>
          <activeRecipes>
            <recipe>org.openrewrite.java.testing.junit5.JUnit4to5Migration</recipe>
          </activeRecipes>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-testing-frameworks</artifactId>
            <version>LATEST</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
</project>
```

2. Run the recipe.
```shell title="shell"
mvn rewrite:run
```

</TabItem>
<TabItem value="gradle-init-script" label="Gradle init script">

Gradle init scripts are a good way to try out a recipe without modifying your `build.gradle` file.

1. Create a file named `init.gradle` in the root of your project.

```groovy title="init.gradle"
initscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2" }
    }
    dependencies { classpath("org.openrewrite:plugin:latest.release") }
}
rootProject {
    plugins.apply(org.openrewrite.gradle.RewritePlugin)
    dependencies {
        rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:latest.release")
    }
    rewrite {
        activeRecipe("org.openrewrite.java.testing.junit5.JUnit4to5Migration")
        setExportDatatables(true)
    }
    afterEvaluate {
        if (repositories.isEmpty()) {
            repositories {
                mavenCentral()
            }
        }
    }
}
```

2. Run the recipe.

```shell title="shell"
gradle --init-script init.gradle rewriteRun
```

</TabItem>
<TabItem value="gradle" label="Gradle">

You can add the plugin to your `build.gradle` file, so that it is available for all developers and CI/CD pipelines.

1. Add the following to your `build.gradle` file:

```groovy title="build.gradle"
plugins {
    id("org.openrewrite.rewrite") version("latest.release")
}

rewrite {
    activeRecipe("org.openrewrite.java.testing.junit5.JUnit4to5Migration")
    setExportDatatables(true)
}

repositories {
    mavenCentral()
}

dependencies {
    rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:latest.release")
}
```

2. Run `gradle rewriteRun` to run the recipe.

</TabItem>
<TabItem value="intelliJ" label="IntelliJ IDEA Ultimate">

You can run OpenRewrite recipes directly from IntelliJ IDEA Ultimate, by adding a `rewrite.yml` file to your project.

```yaml title="rewrite.yml"
---
type: specs.openrewrite.org/v1beta/recipe
name: com.github.timtebeek.AdoptJUnitJupiter
displayName: Adopt JUnit Jupiter
description: Adopt JUnit Jupiter and apply best practices to assertions.
recipeList:
  - org.openrewrite.java.testing.junit5.JUnit4to5Migration
```

After adding the file, you should see a run icon in the left margin offering to run the recipe.

</TabItem>
</Tabs>

:::tip

OpenRewrite's [JUnit4to5Migration](https://docs.openrewrite.org/recipes/java/testing/junit5/junit4to5migration) recipe automatically handles:
- Updating imports and packages
- Converting annotations (`@Before` → `@BeforeEach`, etc.)
- Migrating exception testing patterns
- Updating assertions to JUnit 5 equivalents
- Converting JUnit 4 rules to JUnit 5 extensions

:::
