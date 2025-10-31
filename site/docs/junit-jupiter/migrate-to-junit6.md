---
sidebar_position: 2
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Migrate to JUnit 6

JUnit 6 was released in 2025. The main change is a baseline requirement of Java 17, which affects tests using conditional execution annotations like `@EnabledOnJre` and `@DisabledOnJre`.

## Java version baseline

JUnit 6 requires Java 17 as a minimum, dropping support for Java 8, 11, and earlier versions.
This impacts conditional test execution based on Java versions.

<Tabs>
<TabItem value="before" label="Before">

```java title="ConditionalTest.java"
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalTest {

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void onlyOnJava8() {
        assertTrue(true);
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void onlyOnJava11() {
        assertTrue(true);
    }

    @Test
    @DisabledOnJre(JRE.JAVA_8)
    void notOnJava8() {
        assertTrue(true);
    }
}
```

:::warning

With JUnit 6's Java 17 baseline, tests conditioned on Java 8-16 will never run on supported JDK versions.
These annotations become dead code that should be cleaned up.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ConditionalTest.java"
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalTest {

    // Tests conditioned on Java 8-16 are removed since they can never run

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void onlyOnJava17() {
        assertTrue(true);
    }

    @Test
    @EnabledOnJre(JRE.JAVA_21)
    void onlyOnJava21() {
        assertTrue(true);
    }

    @Test
    @DisabledOnJre(JRE.JAVA_17)
    void notOnJava17() {
        assertTrue(true);
    }
}
```

:::tip

After migrating to JUnit 6:
- Remove tests with `@EnabledOnJre` for Java 8-16 (they'll never run)
- Remove `@DisabledOnJre` for Java 8-16 (they're always enabled now)
- Update remaining conditional annotations to use Java 17+

:::

</TabItem>
</Tabs>

## Cleaning up obsolete conditions

Tests that were disabled on old Java versions can now be unconditionally enabled.

<Tabs>
<TabItem value="before" label="Before">

```java title="ModernFeatureTest.java"
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModernFeatureTest {

    @Test
    @DisabledOnJre({JRE.JAVA_8, JRE.JAVA_11})
    void usesTextBlocks() {
        String text = """
                This is a
                multi-line
                string
                """;
        assertEquals(3, text.lines().count());
    }

    @Test
    @DisabledOnJre(JRE.JAVA_8)
    void usesVar() {
        var list = List.of(1, 2, 3);
        assertEquals(3, list.size());
    }
}
```

:::info

These tests were disabled on older Java versions but can now run unconditionally since JUnit 6 requires Java 17.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ModernFeatureTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModernFeatureTest {

    @Test
    void usesTextBlocks() {
        String text = """
                This is a
                multi-line
                string
                """;
        assertEquals(3, text.lines().count());
    }

    @Test
    void usesVar() {
        var list = List.of(1, 2, 3);
        assertEquals(3, list.size());
    }
}
```

:::tip

Remove obsolete `@DisabledOnJre` annotations for Java versions below 17, as these tests will always run on supported platforms.

:::

</TabItem>
</Tabs>

## Automated migration

OpenRewrite provides a recipe to automatically migrate from JUnit 5 to JUnit 6.

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
mod run ~/workspace/ --recipe org.openrewrite.java.testing.junit6.JUnit5to6Migration
```

</TabItem>
<TabItem value="maven-command-line" label="Maven Command Line">

You will need to have [Maven](https://maven.apache.org/download.cgi) installed on your machine before you can run the following command.

```shell title="shell"
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.testing.junit6.JUnit5to6Migration -Drewrite.exportDatatables=true
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
            <recipe>org.openrewrite.java.testing.junit6.JUnit5to6Migration</recipe>
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
        activeRecipe("org.openrewrite.java.testing.junit6.JUnit5to6Migration")
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
    activeRecipe("org.openrewrite.java.testing.junit6.JUnit5to6Migration")
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
displayName: Adopt JUnit 6
description: Adopt JUnit 6 and apply best practices to assertions.
recipeList:
  - org.openrewrite.java.testing.junit6.JUnit5to6Migration
```

After adding the file, you should see a run icon in the left margin offering to run the recipe.

</TabItem>
</Tabs>

:::tip

OpenRewrite's [JUnit5to6Migration](https://docs.openrewrite.org/recipes/java/testing/junit6/junit5to6migration) recipe automatically:
- Removes tests with `@EnabledOnJre` for Java 8-16
- Removes `@DisabledOnJre` annotations for Java 8-16
- Cleans up obsolete conditional execution logic
- Updates JUnit dependencies to version 6.x

:::

## What hasn't changed

Despite the version number change, JUnit 6 is not a breaking change in terms of API.

:::info

JUnit 6 maintains API compatibility with JUnit 5:
- All JUnit 5 APIs continue to work
- Package names remain `org.junit.jupiter.api.*`
- Annotation names and behavior are unchanged
- The main changes are internal improvements and the Java 17 baseline

The version bump to 6 primarily reflects the breaking change in minimum Java version requirements rather than API changes.

:::
