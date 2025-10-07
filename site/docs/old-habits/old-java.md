---
sidebar_position: 5
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Old Java versions

Java 13 introduced text blocks (multi-line strings) as a preview feature, finalized in Java 15.
They make working with multi-line strings much more readable, but traditional string concatenation is still commonly found in older tests.

## String concatenation

Traditional string concatenation with `+` operators for multi-line strings is hard to read and maintain.
Each line needs explicit `\n` newline characters and quote escaping.

<Tabs>
<TabItem value="before" label="Before">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("Books:\n" +
                        "Effective Java by Joshua Bloch (2001)\n" +
                        "Java Concurrency in Practice by Brian Goetz (2006)\n" +
                        "Clean Code by Robert C. Martin (2008)\n" +
                        "Authors:\n" +
                        "Joshua Bloch\n" +
                        "Brian Goetz\n" +
                        "Robert C. Martin\n" +
                        "Total books: 3\n" +
                        "Total authors: 3\n",
                summary);
    }
}
```

:::warning

String concatenation with `+` is verbose and error-prone. Missing newlines, extra spaces, and quote escaping make it hard to see the actual content.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """,
                summary);
    }
}
```

:::tip

Text blocks make multi-line strings much more readable. The content is exactly as it appears, without escape sequences or concatenation operators.

:::

</TabItem>
</Tabs>

## Even better with AssertJ

While text blocks improve readability, AssertJ can make multi-line string comparisons even clearer with better diff output.

<Tabs>
<TabItem value="before" label="Before">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """,
                summary);
    }
}
```

:::info

JUnit's `assertEquals()` works with text blocks, but the diff output for multi-line strings can be hard to read when tests fail.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertThat(summary).isEqualToIgnoringWhitespace("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """);
    }
}
```

:::tip

AssertJ provides better diff output for multi-line strings and offers flexible comparison methods like `isEqualToIgnoringWhitespace()` for when exact whitespace doesn't matter.

:::

</TabItem>
</Tabs>

## Automated migration

The OpenRewrite [UpgradeToJava21](https://docs.openrewrite.org/reference/recipes/java/migrate-to-java-21) recipe can automatically convert string concatenation to text blocks in your Java code, as well as upgrade other parts of your application to align with Java 21 best practices, like using `getFirst()` and `getLast()` on sequenced collections.

In this case we're going to look at upgrading just the tests to Java 21; not yet upgrading the main source code.
This split in the Java version used for `src/main` and `src/test` is possible with both Maven and Gradle.
With Maven, you can set the `maven.compiler.testRelease` property to 21 in the `maven-compiler-plugin` configuration.

We find starting out with newer Java versions in tests only is often a good way to start adoption.
Developers can start writing new tests and updating existing tests using the latest Java features,
while the main application code can be upgraded at a more leisurely pace as it continues to target the version you're on.
You'll be able to adapt your build pipelines already, and prove to management that the newer Java version work well for your team.

We will first create a custom recipe file in the root of your project, that applies the `UpgradeToJava21` recipe only to test code,
by using a dedicated precondition that matches test code only.

```yaml title="rewrite.yml"
---
type: specs.openrewrite.org/v1beta/recipe
name: com.github.timtebeek.Java21ForTests
displayName: Adopt Java 21 for tests
description: Upgrade your tests to Java 21.
preconditions:
  - org.openrewrite.java.search.IsLikelyTest
recipeList:
  - org.openrewrite.java.migrate.UpgradeToJava21
```

You can run OpenRewrite recipes directly from IntelliJ IDEA Ultimate; after adding the file to your repository,
you should see a run icon in the left margin offering to run the recipe.

If you're not using IntelliJ IDEA Ultimate, you can run the above recipe using one of the following methods.

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
mod config recipes jar install org.openrewrite.recipe:rewrite-migrate-java:LATEST
```

3. Run the recipe.
```shell title="shell"
mod run ~/workspace/ --recipe com.github.timtebeek.Java21ForTests
```

</TabItem>
<TabItem value="maven-command-line" label="Maven Command Line">

You will need to have [Maven](https://maven.apache.org/download.cgi) installed on your machine before you can run the following command.

```shell title="shell"
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE -Drewrite.activeRecipes=com.github.timtebeek.Java21ForTests -Drewrite.exportDatatables=true
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
            <recipe>com.github.timtebeek.Java21ForTests</recipe>
          </activeRecipes>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-migrate-java</artifactId>
            <version>LATEST</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
</project>
```

2. Run run the recipe.
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
        rewrite("org.openrewrite.recipe:rewrite-migrate-java:latest.release")
    }
    rewrite {
        activeRecipe("com.github.timtebeek.Java21ForTests")
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
    activeRecipe("com.github.timtebeek.Java21ForTests")
    setExportDatatables(true)
}

repositories {
    mavenCentral()
}

dependencies {
    rewrite("org.openrewrite.recipe:rewrite-migrate-java:latest.release")
}
```

2. Run `gradle rewriteRun` to run the recipe.

</TabItem>
</Tabs>
