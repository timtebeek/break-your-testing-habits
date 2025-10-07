---
sidebar_position: 4
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Exact assertions

While `contains()` verifies that specific elements are present in a collection, it doesn't ensure that *only* those elements exist.
AssertJ provides exact assertions that verify the complete contents of a collection, ensuring no unexpected elements are present.

## From partial to exact verification

The `contains()` assertion is useful when you only care about certain elements being present.
However, when you need to verify the complete contents of a collection, exact assertions provide stronger guarantees.

<Tabs>
<TabItem value="before" label="Before">

```java title="AssertListExact.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertListExact {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertMultipleElements() {
        assertThat(list).contains("a", "b", "c");
    }
}
```

:::warning

`contains()` only verifies the specified elements are present. The collection could contain additional unexpected elements and the test would still pass.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="AssertListExact.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertListExact {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertMultipleElements() {
        assertThat(list).containsExactly("a", "b", "c");
        // Or if order doesn't matter:
        assertThat(list).containsExactlyInAnyOrder("a", "b", "c");
    }
}
```

:::tip

`containsExactly()` verifies the collection contains exactly these elements in the specified order.
`containsExactlyInAnyOrder()` verifies the collection contains exactly these elements—no more, no less—making your test more precise.

:::

</TabItem>
</Tabs>

## From chained to exact assertions

When you find yourself chaining `hasSize()`, `contains()`, and `doesNotContain()` together, this is often a sign that an exact assertion would better express your intent.

<Tabs>
<TabItem value="before" label="Before">

```java title="ExactAssertJTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExactAssertJTest {

    @Test
    void bundle() {
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

:::warning

Chaining `hasSize()`, `contains()`, and `doesNotContain()` requires multiple assertions and still doesn't guarantee no unexpected elements exist.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ExactAssertJTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExactAssertJTest {

    @Test
    void bundle() {
        List<Book> books = new Bundle().getBooks();

        assertThat(books)
                .containsExactlyInAnyOrder(
                        new Book("Effective Java", "Joshua Bloch", 2001),
                        new Book("Java Concurrency in Practice", "Brian Goetz", 2006),
                        new Book("Clean Code", "Robert C. Martin", 2008));
    }
}
```

:::tip

Using `containsExactlyInAnyOrder()` replaces three separate checks (size, contains, and implicit no-extras) with a single precise assertion.

:::

</TabItem>
</Tabs>

## Choosing the right assertion

AssertJ provides several exact collection assertions, each with specific semantics:

- **`containsExactly(...)`** - Verifies the collection contains exactly the given elements **in that order**
- **`containsExactlyInAnyOrder(...)`** - Verifies the collection contains exactly the given elements **in any order**
- **`containsOnly(...)`** - Verifies the collection contains only the given elements, **allowing duplicates**

<Tabs>
<TabItem value="order" label="Order matters">

```java title="OrderedAssertions.java"
@Test
void ordered() {
    List<String> steps = getWorkflowSteps();

    // Verifies exact order
    assertThat(steps).containsExactly("Init", "Process", "Validate", "Complete");
}
```

</TabItem>
<TabItem value="anyorder" label="Order doesn't matter">

```java title="UnorderedAssertions.java"
@Test
void unordered() {
    Set<String> tags = getArticleTags();

    // Verifies exact content, any order
    assertThat(tags).containsExactlyInAnyOrder("java", "testing", "assertj");
}
```

</TabItem>
<TabItem value="only" label="Duplicates allowed">

```java title="DuplicatesAllowed.java"
@Test
void withDuplicates() {
    List<String> values = List.of("A", "B", "A", "C", "B");

    // Verifies only these elements exist, duplicates allowed
    assertThat(values).containsOnly("A", "B", "C");
}
```

</TabItem>
</Tabs>

:::tip

Choose the most precise assertion that matches your intent. This makes tests self-documenting and provides clearer failure messages when expectations aren't met.

:::
