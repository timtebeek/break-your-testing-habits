---
sidebar_position: 5
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Assertion generator

Writing custom assertion classes manually can be time-consuming, especially for large domain models.
AssertJ provides a generator that can automatically create custom assertion classes from your domain objects.

## Manual custom assertions

Creating custom assertions by hand requires significant boilerplate code.

<Tabs>
<TabItem value="before" label="Before">

```java title="BookAssert.java"
import com.github.timtebeek.books.Book;
import org.assertj.core.api.*;

public class BookAssert extends AbstractObjectAssert<BookAssert, Book>
        implements AssertProvider<BookAssert> {

    private final Book book;

    private BookAssert(Book book) {
        super(book, BookAssert.class);
        this.book = book;
    }

    public static BookAssert assertThat(Book book) {
        return new BookAssert(book);
    }

    @Override
    public BookAssert assertThat() {
        return new BookAssert(book);
    }

    public AbstractStringAssert<?> title() {
        return Assertions.assertThat(book.getTitle()).describedAs("title");
    }

    public AbstractStringAssert<?> author() {
        return Assertions.assertThat(book.getAuthor()).describedAs("author");
    }

    public AbstractIntegerAssert<?> year() {
        return Assertions.assertThat(book.getYear()).describedAs("year");
    }

    public BookAssert hasTitle(String title) {
        assertThat().isNotNull().title().isEqualTo(title);
        return myself;
    }

    public BookAssert hasAuthor(String author) {
        assertThat().isNotNull().author().isEqualTo(author);
        return myself;
    }

    public BookAssert hasYear(int year) {
        assertThat().isNotNull().year().isEqualTo(year);
        return myself;
    }
}
```

:::warning

Writing custom assertions manually is repetitive and error-prone:
- Lots of boilerplate code
- Easy to make mistakes with method signatures
- Time-consuming for large domain models
- Needs manual updates when domain classes change

:::

</TabItem>
<TabItem value="after" label="After">

AssertJ provides a Maven plugin that generates custom assertions automatically:

```xml title="pom.xml"
<plugin>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
    <version>2.2.0</version>
    <executions>
        <execution>
            <goals>
                <goal>generate-assertions</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <packages>
            <package>com.github.timtebeek.books</package>
        </packages>
        <classes>
            <param>com.github.timtebeek.books.Book</param>
        </classes>
    </configuration>
</plugin>
```

```bash
# Generate custom assertions
mvn assertj:generate-assertions
```

The plugin generates a complete `BookAssert` class with all the boilerplate handled automatically.

:::tip

The assertion generator:
- Creates fluent assertion methods for all properties
- Generates both property accessors and `has*()` methods
- Updates automatically when you regenerate
- Saves time and reduces errors
- Works with complex domain models

:::

</TabItem>
</Tabs>

## Using generated assertions

Generated assertions work exactly like handwritten ones but with less effort.

<Tabs>
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book.getTitle()).isEqualTo("Effective Java");
        assertThat(book.getAuthor()).isEqualTo("Joshua Bloch");
        assertThat(book.getYear()).isEqualTo(2001);
    }
}
```

:::info

Using basic AssertJ assertions works but lacks domain-specific expressiveness.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static com.github.timtebeek.books.BookAssert.assertThat;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book)
                .hasTitle("Effective Java")
                .hasAuthor("Joshua Bloch")
                .hasYear(2001);
    }
}
```

:::tip

Generated assertions provide:
- Domain-specific fluent API.
- Better readability.
- Automatic updates when domain changes.
- All the benefits of custom assertions with none of the manual work.

:::

</TabItem>
</Tabs>

## Configuration options

The generator supports various configuration options to customize the generated assertions.

```xml title="pom.xml"
<plugin>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
    <version>2.2.0</version>
    <configuration>
        <!-- Generate for entire packages -->
        <packages>
            <package>com.github.timtebeek.books</package>
        </packages>

        <!-- Or specific classes -->
        <classes>
            <param>com.github.timtebeek.books.Book</param>
            <param>com.github.timtebeek.books.Bundle</param>
        </classes>

        <!-- Output directory -->
        <targetDir>target/generated-test-sources/assertj-assertions</targetDir>

        <!-- Generate entry point class for all assertions -->
        <entryPointClassPackage>com.github.timtebeek.assertions</entryPointClassPackage>

        <!-- Skip certain fields or properties -->
        <excludes>
            <exclude>.*password.*</exclude>
            <exclude>.*secret.*</exclude>
        </excludes>
    </configuration>
</plugin>
```

:::info

Configuration options:
- `packages` - Generate for all classes in specified packages.
- `classes` - Generate for specific classes only.
- `targetDir` - Where to output generated files.
- `entryPointClassPackage` - Generate a single entry point with all `assertThat()` methods.
- `excludes` - Regex patterns for fields/methods to skip.

:::

## Online generator

For quick experimentation or one-off generation, use the online generator.

:::tip

AssertJ provides an [online assertion generator](https://joel-costigliola.github.io/assertj/assertj-assertions-generator.html) where you can:
- Paste your class code.
- Generate custom assertions in your browser.
- Copy the generated code to your project.
- Useful for quick prototyping or when you can't use the Maven plugin.

:::

## Best practices

<Tabs>
<TabItem value="before" label="Before">

```java title="BookTest.java"
// Mixing generated and manual assertions
import static org.assertj.core.api.Assertions.assertThat;
import static com.github.timtebeek.books.BookAssert.assertThat;  // Conflict!

class BookTest {
    @Test
    void test() {
        // Which assertThat() is being used?
        assertThat(book).hasTitle("Effective Java");
    }
}
```

:::warning

Importing both standard and generated `assertThat()` methods can cause confusion and compilation errors.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTest.java"
// Use entry point class to avoid conflicts
import static com.github.timtebeek.assertions.Assertions.assertThat;

class BookTest {
    @Test
    void test() {
        // Clear which assertThat() is used - the generated entry point
        assertThat(book).hasTitle("Effective Java");
        assertThat(list).hasSize(3);  // Works for all types
    }
}
```

:::tip

Best practices for generated assertions:
- Configure an entry point class package to provide a single import.
- Keep generated code in `target/` and regenerate as needed.
- Run generation as part of your build process.
- Use for frequently-tested domain objects.
- Combine with soft assertions for comprehensive testing.

:::

</TabItem>
</Tabs>

## Exercise A

Open the `orders` project and explore the `Order` and `Customer` domain classes.
Run the AssertJ assertion generator Maven plugin to create custom assertions for these classes.
Then, use the assertion classes in a few test cases to verify their properties.
