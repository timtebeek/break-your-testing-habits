---
sidebar_position: 3
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# JUnit Jupiter best practices

After migrating to JUnit 5, there are several common patterns that should be cleaned up to make tests more modern and maintainable.

## Remove test prefix

In JUnit 3, test methods had to start with `test` because the framework used reflection to discover tests by name.
JUnit 4 and 5 use annotations, so this prefix is no longer necessary and just adds noise.

<Tabs>
<TabItem value="before" label="Before">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void testSubtraction() {
        assertEquals(0, 2 - 2);
    }

    @Test
    void testMultiplication() {
        assertEquals(4, 2 * 2);
    }
}
```

:::warning

The `test` prefix is a remnant from JUnit 3 and adds unnecessary noise to test method names.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void addition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void subtraction() {
        assertEquals(0, 2 - 2);
    }

    @Test
    void multiplication() {
        assertEquals(4, 2 * 2);
    }
}
```

:::tip

Removing the `test` prefix makes method names cleaner and more readable. The `@Test` annotation already indicates it's a test method.

:::

</TabItem>
</Tabs>

## Remove public modifier

JUnit 4 required test classes and methods to be public, but JUnit 5 removed this requirement.
Package-private (default) visibility is sufficient and reduces boilerplate.

<Tabs>
<TabItem value="before" label="Before">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void addition() {
        assertEquals(4, calculator.add(2, 2));
    }

    @Test
    public void subtraction() {
        assertEquals(0, calculator.subtract(2, 2));
    }
}
```

:::warning

The `public` modifiers on the class and test methods are unnecessary in JUnit 5 and add visual clutter.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void addition() {
        assertEquals(4, calculator.add(2, 2));
    }

    @Test
    void subtraction() {
        assertEquals(0, calculator.subtract(2, 2));
    }
}
```

:::tip

Package-private visibility is cleaner and sufficient for JUnit 5 test classes and methods.
Only keep `public` if you need the test class to be accessible from other packages.

:::

</TabItem>
</Tabs>

## Apply best practices

Beyond basic cleanup, there are many other best practices to apply when modernizing tests.

<Tabs>
<TabItem value="before" label="Before">

```java title="UserServiceTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    @Test
    void testUserCreation() {
        UserService service = new UserService();
        User user = service.createUser("John", "Doe");

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getId() > 0);
    }
}
```

:::info

While this test works, it could be improved with better naming, display names, and more expressive assertions.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="UserServiceTest.java"
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Service")
class UserServiceTest {

    @Test
    @DisplayName("creates user with first and last name")
    void createUser() {
        UserService service = new UserService();
        User user = service.createUser("John", "Doe");

        assertThat(user)
                .isNotNull()
                .extracting(User::getFirstName, User::getLastName, User::getId)
                .containsExactly("John", "Doe", 1L);
    }
}
```

:::tip

Modern tests should:
- Use descriptive method names without `test` prefix
- Add `@DisplayName` for human-readable test reports
- Use AssertJ for more expressive assertions
- Chain related assertions together
- Follow the Arrange-Act-Assert pattern

:::

</TabItem>
</Tabs>

## Automated modernization

OpenRewrite provides recipes to automatically apply these modernizations.

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
mod run ~/workspace/ --recipe org.openrewrite.java.testing.junit5.JUnit5BestPractices
```

</TabItem>
<TabItem value="maven-command-line" label="Maven Command Line">

You will need to have [Maven](https://maven.apache.org/download.cgi) installed on your machine before you can run the following command.

```shell title="shell"
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit5BestPractices -Drewrite.exportDatatables=true
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
            <recipe>org.openrewrite.java.testing.junit5.JUnit5BestPractices</recipe>
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
        activeRecipe("org.openrewrite.java.testing.junit5.JUnit5BestPractices")
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
    activeRecipe("org.openrewrite.java.testing.junit5.JUnit5BestPractices")
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
displayName: JUnit Jupiter best practices
description: Apply JUnit Jupiter best practices to assertions.
recipeList:
  - org.openrewrite.java.testing.junit5.JUnit5BestPractices
```

After adding the file, you should see a run icon in the left margin offering to run the recipe.

</TabItem>
</Tabs>

:::tip

OpenRewrite recipes automatically handle:
- [RemoveTestPrefix](https://docs.openrewrite.org/recipes/java/testing/cleanup/removetestprefix) - Removes `test` prefix from method names
- [TestsShouldNotBePublic](https://docs.openrewrite.org/recipes/java/testing/cleanup/testsshouldnotbepublic) - Removes unnecessary `public` modifiers
- [JUnit5BestPractices](https://docs.openrewrite.org/recipes/java/testing/junit5/junit5bestpractices) - Applies various JUnit 5 best practices

:::
