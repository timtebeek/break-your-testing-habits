---
sidebar_position: 2
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Chained assertions

There are many different ways to write AssertJ assertions.
Having many assertions on the same object can decrease the readability.
Chained assertions allow you to group multiple assertions on the same object into a single statement, making it much easier to read.

## Limited expressiveness

JUnit 3 only provides basic assertions like `assertTrue`, `assertFalse`, `assertEquals`, and `assertNotNull`.
This makes tests verbose and harder to understand, especially when they fail.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitThreeTest.java"
import java.util.List;

class ChainedAssertJTest {

    @Test
    void bundle() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(3);
        assertThat(books).contains(new Book("Effective Java", "Joshua Bloch", 2001));
        assertThat(books).contains(new Book("Java Concurrency in Practice", "Brian Goetz", 2006));
        assertThat(books).contains(new Book("Clean Code", "Robert C. Martin", 2008));
        assertThat(books).doesNotContain(new Book("Java 8 in Action", "Raoul-Gabriel Urma", 2014));
    }
}
```

:::warning

JUnit 3 tests require extending `TestCase`, limiting inheritance, and test methods must be public and start with `test`.
The limited assertion library makes tests verbose and less readable.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ChainedAssertJTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChainedAssertJTest {

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
    }
}
```

:::tip

Modern testing frameworks like JUnit 5 use annotations instead of naming conventions, don't require extending base classes, and pair well with AssertJ for more expressive assertions.

:::

</TabItem>
</Tabs>
