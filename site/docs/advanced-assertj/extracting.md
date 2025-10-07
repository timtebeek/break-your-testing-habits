---
sidebar_position: 1
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Extracting properties

When testing collections of complex objects, you often need to verify properties of the contained elements.
AssertJ's `extracting()` method provides a clean way to pull out properties and assert on them.

## Single property extraction

Extract a single property from all elements in a collection for verification.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyBookTitles() {
        List<Book> books = new Bundle().getBooks();

        List<String> titles = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        assertThat(titles).containsExactly(
                "Effective Java",
                "Java Concurrency in Practice",
                "Clean Code");
    }
}
```

:::warning

Using Java streams to extract properties adds boilerplate and separates the extraction from the assertion.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyBookTitles() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .extracting(Book::getTitle)
                .containsExactly(
                        "Effective Java",
                        "Java Concurrency in Practice",
                        "Clean Code");
    }
}
```

:::tip

AssertJ's `extracting()` is more concise and keeps the extraction and assertion together in a fluent chain.

:::

</TabItem>
</Tabs>

## Multiple property extraction

Extract multiple properties from all elements for comprehensive verification.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyFirstBook() {
        List<Book> books = new Bundle().getBooks();
        Book first = books.get(0);

        assertThat(first.getTitle()).isEqualTo("Effective Java");
        assertThat(first.getAuthor()).isEqualTo("Joshua Bloch");
        assertThat(first.getYear()).isEqualTo(2001);
    }
}
```

:::info

While this works, it requires multiple separate assertions and explicit element access.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyFirstBook() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .first()
                .extracting(Book::getTitle, Book::getAuthor, Book::getYear)
                .containsExactly("Effective Java", "Joshua Bloch", 2001);
    }
}
```

:::tip

Extracting multiple properties creates a tuple that can be verified with a single assertion.
Use `first()`, `last()`, or `element(index)` to focus on specific elements.

:::

</TabItem>
</Tabs>

## Extracting from all elements

When verifying multiple properties across all collection elements, extraction helps create clear assertions.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class BundleTest {

    @Test
    void verifyAllBooks() {
        List<Book> books = new Bundle().getBooks();

        for (Book book : books) {
            assertThat(book.getTitle()).isNotNull();
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getYear()).isBetween(1999, 2010);
        }
    }
}
```

:::warning

Using a loop makes the test more imperative and stops at the first failure, hiding subsequent issues.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class BundleTest {

    @Test
    void verifyAllBooks() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .extracting(Book::getTitle, Book::getAuthor, Book::getYear)
                .containsExactly(
                        tuple("Effective Java", "Joshua Bloch", 2001),
                        tuple("Java Concurrency in Practice", "Brian Goetz", 2006),
                        tuple("Clean Code", "Robert C. Martin", 2008));
    }
}
```

:::tip

Extracting properties from all elements and using `tuple()` creates a clear, declarative assertion that:
- Verifies all elements at once.
- Shows all failures together if there are multiple issues.
- Is more concise and readable than loops.

:::

</TabItem>
</Tabs>

## Flat extracting

When properties themselves are collections, use `flatExtracting()` to flatten nested collections.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyAllAuthors() {
        List<String> authors = new Bundle().getAuthors();

        assertThat(authors).containsExactlyInAnyOrder(
                "Joshua Bloch",
                "Brian Goetz",
                "Robert C. Martin");
    }
}
```

:::info

When the class already provides a flattened collection, this works fine.
But what if you need to extract and flatten from multiple bundles?

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void verifyAuthorsFromMultipleBundles() {
        List<Bundle> bundles = List.of(new Bundle(), new Bundle());

        assertThat(bundles)
                .flatExtracting(Bundle::getAuthors)
                .contains(
                        "Joshua Bloch",
                        "Brian Goetz",
                        "Robert C. Martin");
    }
}
```

:::tip

`flatExtracting()` is like `extracting()` followed by flattening nested collections.
It's useful when each element contains a collection, and you want to assert on all values across all elements.

:::

</TabItem>
</Tabs>

## Extracting and chaining

When you need to make assertions on a property of an object, you can use `extracting` to extract the property and then chain assertions on it.
Sometimes, you may need to specify the type of the extracted property to use the appropriate assertions.

```java title="ExtractThenChainTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

class ExtractThenChainTest {
    @Test
    void extractingThenChaining() {
        assertThat(new Bundle())
                .isNotNull()
                .extracting(Bundle::getBooks)
                .asInstanceOf(LIST)
                .containsExactly(
                        new Book("Effective Java", "Joshua Bloch", 2001),
                        new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                        new Book("Clean Code", "Robert C. Martin", 2008));
    }
}
```

:::tip

The `asInstanceOf` method helps to specify the type of the extracted property, enabling you to use the appropriate assertions for that type.

:::
