---
sidebar_position: 1
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# JUnit 3

JUnit 3 was the dominant testing framework for Java for many years, but it has several limitations compared to modern alternatives.
Tests must extend `TestCase`, method names must start with `test`, and the assertion library is limited and not very expressive.

## Limited expressiveness

JUnit 3 only provides basic assertions like `assertTrue`, `assertFalse`, `assertEquals`, and `assertNotNull`.
This makes tests verbose and harder to understand, especially when they fail.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitThreeTest.java"
import java.util.List;

public class JUnitThreeTest extends junit.framework.TestCase {

    public void testBundle() {
        List<Book> books = new Bundle().getBooks();

        assertNotNull(books);
        assertEquals(3, books.size());
        assertTrue(books.contains(new Book("Effective Java", "Joshua Bloch", 2001)));
        assertTrue(books.contains(new Book("Java Concurrency in Practice", "Brian Goetz", 2006)));
        assertTrue(books.contains(new Book("Clean Code", "Robert C. Martin", 2008)));
        assertFalse(books.contains(new Book("Java 8 in Action", "Raoul-Gabriel Urma", 2014)));

        String expectedTitle = books.get(0).getTitle();
        assertNotNull("Title not null", expectedTitle);
        assertEquals("Title should match", "Effective Java", expectedTitle);
    }
}
```

:::warning

JUnit 3 tests require extending `TestCase`, limiting inheritance, and test methods must be public and start with `test`.
The limited assertion library makes tests verbose and less readable.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitThreeTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitThreeTest {

    @Test
    void testBundle() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .hasSize(3)
                .contains(
                        new Book("Effective Java", "Joshua Bloch", 2001),
                        new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                        new Book("Clean Code", "Robert C. Martin", 2008))
                .doesNotContain(new Book("Java 8 in Action", "Raoul-Gabriel Urma", 2014));

        assertThat(books.get(0).getTitle())
                .as("Title should match")
                .isEqualTo("Effective Java");
    }
}
```

:::tip

Modern testing frameworks like JUnit 5 use annotations instead of naming conventions, don't require extending base classes, and pair well with AssertJ for more expressive assertions.

:::

</TabItem>
</Tabs>

## Confusing boolean assertions

Overuse of `assertTrue` and `assertFalse` with complex conditions makes it hard to understand what is being tested.
When tests fail, the error messages are not helpful.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitThreeTest.java"
public class JUnitThreeTest extends junit.framework.TestCase {

    public void testThatFails() {
        List<Book> books = new Bundle().getBooks();

        assertTrue(books.size() == 2);
        assertTrue(books.size() <= 2);
        assertTrue(books.size() < 2);

        assertFalse(books.size() == 3);
        assertFalse(books.size() <= 3);
        assertFalse(books.size() >= 3);
    }
}
```

:::danger

Using `assertTrue` and `assertFalse` with complex conditions produces unhelpful failure messages.
When `assertTrue(books.size() == 2)` fails, you only see "expected: true but was: false" instead of the actual size.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitThreeTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitThreeTest {

    @Test
    void testThatFails() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .hasSize(2)
                .hasSizeLessThanOrEqualTo(2)
                .hasSizeLessThan(2);

        assertThat(books).doesNotHaveSize(3);
    }
}
```

:::tip

AssertJ provides dedicated assertion methods that produce clear, informative failure messages showing both expected and actual values.

:::

</TabItem>
</Tabs>

## Backwards argument order

JUnit 3 uses the opposite argument order compared to modern frameworks, with the expected value first and actual value second.
This can lead to confusing error messages and incorrectly passing tests.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitThreeTest.java"
public class JUnitThreeTest extends junit.framework.TestCase {

    public void testIncorrectArgumentOrder() {
        String actualTitle = new Bundle().getBooks().get(0).getTitle();
        assertNotNull(actualTitle, "Title not null");
        assertEquals("Title should match", actualTitle, "Effective Java");
    }
}
```

:::danger

In JUnit 3, the message comes first in `assertNotNull`, but in `assertEquals` the expected value comes first.
This inconsistency is confusing and error-prone. The test above will incorrectly pass because it's comparing the message to the actual title.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitThreeTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitThreeTest {

    @Test
    void testIncorrectArgumentOrder() {
        String actualTitle = new Bundle().getBooks().get(0).getTitle();
        assertThat(actualTitle)
                .as("Title should match")
                .isNotNull()
                .isEqualTo("Effective Java");
    }
}
```

:::tip

AssertJ's fluent API eliminates argument order confusion by always starting with the actual value and chaining assertions.

:::

</TabItem>
</Tabs>
