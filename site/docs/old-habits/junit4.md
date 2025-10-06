---
sidebar_position: 2
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# JUnit 4

JUnit 4 introduced annotations and removed the need to extend `TestCase`, which was a major improvement over JUnit 3.
However, it still has limitations compared to modern frameworks like JUnit 5 and AssertJ.
Tests and classes must still be public, the assertion library is basic, and exception testing is awkward.

## Public visibility required

JUnit 4 requires both test classes and test methods to be public, which adds unnecessary boilerplate.
This restriction was removed in JUnit 5, where package-private visibility is sufficient.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitFourTest.java"
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JUnitFourTest {

    @Test
    public void bundle() {
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

JUnit 4 requires test classes and methods to be public, adding unnecessary boilerplate.
The basic assertion library also makes tests verbose and less expressive.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitFourTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitFourTest {

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

        assertThat(books.get(0).getTitle())
                .as("Title should match")
                .isEqualTo("Effective Java");
    }
}
```

:::tip

JUnit 5 allows package-private test classes and methods, reducing boilerplate.
Combined with AssertJ, tests become more concise and expressive.

:::

</TabItem>
</Tabs>

## Backwards argument order

JUnit 4 improved on JUnit 3 by standardizing argument order, but it's still backwards compared to JUnit 5.
In JUnit 4, the expected value comes first, while in JUnit 5 the actual value comes first to match natural reading order.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitFourTest.java"
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JUnitFourTest {

    @Test
    public void incorrectArgumentOrder() {
        String actualTitle = new Bundle().getBooks().get(0).getTitle();
        assertNotNull(actualTitle, "Title not null");
        assertEquals("Title should match", actualTitle, "Effective Java");
    }
}
```

:::danger

In JUnit 4, `assertNotNull` takes the value first and message second, while `assertEquals` takes message first, then expected, then actual.
This inconsistency leads to errors. The test above will incorrectly pass because it's comparing the actual title to the message string.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitFourTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitFourTest {

    @Test
    void incorrectArgumentOrder() {
        String actualTitle = new Bundle().getBooks().get(0).getTitle();
        assertThat(actualTitle)
                .as("Title should match")
                .isNotNull()
                .isEqualTo("Effective Java");
    }
}
```

:::tip

AssertJ eliminates argument order confusion with its fluent API that always starts with the actual value.

:::

</TabItem>
</Tabs>

## ExpectedException rule

JUnit 4 introduced the `@Rule ExpectedException` to test for exceptions, which was an improvement over try/catch blocks.
However, it's disconnected from the code that throws the exception and can lead to confusing test logic.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitFourTest.java"
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JUnitFourTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void expectException() throws IllegalArgumentException {
        int i = 1 + 2;
        assertEquals(i, 3);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("boom!");
        boom();
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }
}
```

:::warning

The `ExpectedException` rule is declared at the top but configured in the middle of the test, making it hard to follow.
Any code after the `thrown.expect()` call must throw the exception, or the test will fail in a confusing way.
Any code before the `thrown.expect()` call could throw the expected exception and cause the test to incorrectly pass.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitFourTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JUnitFourTest {

    @Test
    void expectException() {
        int i = 1 + 2;
        assertThat(i).isEqualTo(3);

        assertThatThrownBy(() -> boom())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boom!");
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }
}
```

:::tip

AssertJ's `assertThatThrownBy` makes it clear exactly which code is expected to throw an exception, making tests more readable and less error-prone.

:::

</TabItem>
</Tabs>
