import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Hamcrest

Hamcrest is a framework for writing matcher objects that can be combined to create flexible expressions of intent.
It was popular with JUnit 4, but has several drawbacks compared to modern assertion libraries like AssertJ.
The matcher-based approach requires many static imports, has less discoverable APIs, and can be confusing to use correctly.

## Verbose matcher composition

Hamcrest requires composing matchers with `is()`, `not()`, and `nullValue()` to express simple assertions.
This leads to verbose, nested calls that are harder to read than fluent assertions.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="HamcrestTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class HamcrestTest {

    @Test
    void bundle() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books, is(not(nullValue())));
        assertThat(books, hasSize(3));
        assertThat(books, hasItem(new Book("Effective Java", "Joshua Bloch", 2001)));
        assertThat(books, hasItem(new Book("Java Concurrency in Practice", "Brian Goetz", 2006)));
        assertThat(books, hasItem(new Book("Clean Code", "Robert C. Martin", 2008)));
        assertThat(books, not(hasItem(new Book("Java 8 in Action", "Raoul-Gabriel Urma", 2014))));
    }
}
```

:::warning

Hamcrest requires numerous static imports and verbose matcher composition like `is(not(nullValue()))`.
Each assertion is a separate statement, making the test longer and harder to maintain.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="HamcrestTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HamcrestTest {

    @Test
    void bundle() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .isNotNull()
                .hasSize(3)
                .contains(
                        new Book("Effective Java", "Joshua Bloch", 2001),
                        new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                        new Book("Clean Code", "Robert C. Martin", 2008"))
                .doesNotContain(new Book("Java 8 in Action", "Raoul-Gabriel Urma", 2014));
    }
}
```

:::tip

AssertJ's fluent API chains multiple assertions together, making tests more concise and readable.
IDE autocomplete makes it easy to discover available assertions without memorizing static imports.

:::

</TabItem>
</Tabs>

## Confusing matcher semantics

Hamcrest matchers like `contains()` and `hasItem()` have subtle differences that are easy to get wrong.
`contains()` checks for exact order and completeness, while `hasItem()` checks for presence of a single item.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="HamcrestTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class HamcrestTest {

    @Test
    void failingTest() {
        List<Book> books = new Bundle().getBooks();

        // Wrong: Each contains() checks for the ENTIRE list content in order
        assertThat(books, contains(new Book("Effective Java", "Joshua Bloch", 2001)));
        assertThat(books, contains(new Book("Java Concurrency in Practice", "Brian Goetz", 2006)));
        assertThat(books, contains(new Book("Clean Code", "Robert C. Martin", 2008)));

        // Correct: All items must be in a single contains() call
        assertThat(books, contains(
                new Book("Effective Java", "Joshua Bloch", 2001),
                new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                new Book("Clean Code", "Robert C. Martin", 2008)
        ));
    }
}
```

:::danger

The `contains()` matcher checks that the collection contains exactly the specified items in order.
Using it with a single item per assertion will fail because it expects the entire list to be just that one item.
This is confusing and error-prone.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="HamcrestTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HamcrestTest {

    @Test
    void failingTest() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books).containsExactly(
                new Book("Effective Java", "Joshua Bloch", 2001),
                new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                new Book("Clean Code", "Robert C. Martin", 2008)
        );
    }
}
```

:::tip

AssertJ's method names are more explicit: `containsExactly()` for order-sensitive exact matches, `contains()` for order-insensitive partial matches, and `containsOnly()` for unordered exact matches.
The semantics are clearer and less prone to mistakes.

:::

</TabItem>
</Tabs>

## Poor discoverability

Hamcrest's matcher-based approach requires knowing which static imports to use and how to compose them.
Without IDE autocomplete support, it's difficult to discover what matchers are available and how to use them.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="HamcrestTest.java"
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class HamcrestTest {
    // You need to know which matchers exist and import them manually
    // IDE autocomplete doesn't help much after assertThat(value, ...)
}
```

:::warning

Hamcrest requires memorizing matcher names and manually adding static imports.
The separation between the value and the matcher makes IDE autocomplete less helpful.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="HamcrestTest.java"
import static org.assertj.core.api.Assertions.assertThat;

class HamcrestTest {
    // Single static import, IDE autocomplete shows all available assertions
    // after assertThat(value).
}
```

:::tip

AssertJ requires only one static import for `assertThat()`, and IDE autocomplete immediately shows all available assertions after typing `assertThat(value).`

:::

</TabItem>
</Tabs>
