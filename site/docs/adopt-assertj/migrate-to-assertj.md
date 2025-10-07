---
sidebar_position: 5
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Migrate to AssertJ

You can easily convert existing assertions to use [AssertJ](https://assertj.github.io/doc/) using OpenRewrite's [AssertJ recipe](https://docs.openrewrite.org/recipes/java/testing/assertj/assertj-best-practices).
This recipe contains many Refaster rules from [Error Prone Support](https://error-prone.picnic.tech/refasterrules/AssertJStringRules/).
There are multiple ways to run OpenRewrite recipes on your project, depending on your build tool and preferences.

We recommend the Moderne CLI when interested in running recipes across multiple projects, or when running multiple recipes in succession.
If you want to try out the recipe on a single project, you can also use the OSS Maven and Gradle plugins.

:::note

Building the Lossless Semantic Tree (LST) of your project will take some time, proportional to the size of your project.
With the Moderne CLI this only needs to be done once, after which subsequent recipe runs will be much faster.

:::

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
mod run ~/workspace/ --recipe org.openrewrite.java.testing.assertj.Assert
```

</TabItem>
<TabItem value="maven-command-line" label="Maven Command Line">

You will need to have [Maven](https://maven.apache.org/download.cgi) installed on your machine before you can run the following command.

```shell title="shell"
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-testing-frameworks:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.testing.assertj.Assert -Drewrite.exportDatatables=true
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
            <recipe>org.openrewrite.java.testing.assertj.Assert</recipe>
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
        activeRecipe("org.openrewrite.java.testing.assertj.Assert")
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
    activeRecipe("org.openrewrite.java.testing.assertj.Assert")
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
name: com.github.timtebeek.AdoptAssertJ
displayName: Adopt AssertJ
description: Adopt AssertJ and apply best practices to assertions.
recipeList:
  - org.openrewrite.java.testing.assertj.Assert
```

After adding the file, you should see a run icon in the left margin offering to run the recipe.

</TabItem>
</Tabs>

## Exercise A

Run the above command on the `books` project in this repository, and check the changes made to the test files.

:::note

Notice how nearly all the bad and outdated practices have been replaced with AssertJ's fluent assertions.
Existing use of AssertJ has been improved by for instance chaining assertions and removing redundant calls.

:::

## Exercise B

Run the above command on your own project, and check the changes made to the test files.

:::info

At this point you can choose whether you want to keep the changes or revert them.
If you'd prefer a smaller set of changes, you can also try out individual recipes from the [AssertJ recipe family](https://docs.openrewrite.org/recipes/java/testing/assertj).

:::
