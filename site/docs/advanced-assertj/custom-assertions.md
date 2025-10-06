import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Custom assertions

AssertJ allows you to create custom assertion classes for your domain objects, making tests more readable and expressive.
Custom assertions provide fluent APIs tailored to your domain, reducing boilerplate and improving maintainability.

## Domain-specific assertions

Instead of repeatedly extracting properties and asserting on them, create custom assertion classes.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book.getTitle()).contains("Effective");
        assertThat(book.getAuthor()).contains("Bloch");
        assertThat(book.getYear()).isLessThan(2003);
    }

    @Test
    void verifyAnotherBook() {
        Book book = new Book("Clean Code", "Robert C. Martin", 2008);

        assertThat(book.getTitle()).contains("Clean");
        assertThat(book.getAuthor()).contains("Martin");
        assertThat(book.getYear()).isLessThan(2010);
    }
}
```

:::warning

Repeatedly extracting and asserting on the same properties across multiple tests becomes verbose and repetitive.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTester.java"
import com.github.timtebeek.books.Book;
import org.assertj.core.api.*;

public class BookTester extends AbstractObjectAssert<BookTester, Book>
        implements AssertProvider<BookTester> {

    private final Book book;

    private BookTester(Book book) {
        super(book, BookTester.class);
        this.book = book;
    }

    public static BookTester of(Book book) {
        return new BookTester(book);
    }

    @Override
    public BookTester assertThat() {
        return new BookTester(book);
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
}
```

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyBook() {
        BookTester bookTester = BookTester.of(new Book("Effective Java", "Joshua Bloch", 2001));

        assertThat(bookTester).title().contains("Effective");
        assertThat(bookTester).author().contains("Bloch");
        assertThat(bookTester).year().isLessThan(2003);
    }

    @Test
    void verifyAnotherBook() {
        BookTester bookTester = BookTester.of(new Book("Clean Code", "Robert C. Martin", 2008));

        assertThat(bookTester).title().contains("Clean");
        assertThat(bookTester).author().contains("Martin");
        assertThat(bookTester).year().isLessThan(2010);
    }
}
```

:::tip

Custom assertions provide:
- Named property accessors for better readability
- Automatic property descriptions in failure messages
- Fluent API that works with `assertThat()`
- Reusability across all tests

:::

</TabItem>
</Tabs>

## Exact match assertions

Custom assertions can provide convenience methods for common assertion patterns.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyExactMatch() {
        BookTester bookTester = BookTester.of(new Book("Effective Java", "Joshua Bloch", 2001));

        assertThat(bookTester).title().isEqualTo("Effective Java");
        assertThat(bookTester).author().isEqualTo("Joshua Bloch");
        assertThat(bookTester).year().isEqualTo(2001);
    }
}
```

:::info

While the property accessors work well, exact match assertions can be even more concise.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTester.java"
public class BookTester extends AbstractObjectAssert<BookTester, Book>
        implements AssertProvider<BookTester> {

    // ... previous code ...

    public BookTester hasTitle(String title) {
        assertThat()
                .isNotNull()
                .title()
                .describedAs("title")
                .isEqualTo(title);
        return myself;
    }

    public BookTester hasAuthor(String author) {
        assertThat()
                .isNotNull()
                .author()
                .describedAs("author")
                .isEqualTo(author);
        return myself;
    }

    public BookTester hasYear(int year) {
        assertThat()
                .isNotNull()
                .year()
                .describedAs("year")
                .isEqualTo(year);
        return myself;
    }
}
```

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyExactMatch() {
        BookTester bookTester = BookTester.of(new Book("Effective Java", "Joshua Bloch", 2001));

        assertThat(bookTester)
                .hasTitle("Effective Java")
                .hasAuthor("Joshua Bloch")
                .hasYear(2001);
    }
}
```

:::tip

Custom `has*()` methods enable fluent chaining for exact matches, making tests more concise and readable.
The methods return `myself` (the assertion instance) to allow method chaining.

:::

</TabItem>
</Tabs>

## Collection assertions

Custom assertions work seamlessly with collection assertions using `map()` and `allSatisfy()`.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BundleTest.java"
import com.github.timtebeek.books.Book;
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void allBooksFromEarlierDecade() {
        assertThat(new Bundle().getBooks())
                .allSatisfy(book -> {
                    assertThat(book.getYear()).isBetween(1999, 2010);
                });
    }
}
```

:::info

While this works, it doesn't leverage the custom assertion we created.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BundleTest {

    @Test
    void allBooksFromEarlierDecade() {
        assertThat(new Bundle().getBooks())
                .map(BookTester::of)
                .allSatisfy(book -> assertThat(book).year().isBetween(1999, 2010));
    }
}
```

:::tip

Use `map(BookTester::of)` to convert collection elements to your custom assertion type.
This allows you to use the same fluent API for individual objects and collections.

:::

</TabItem>
</Tabs>

## Satisfies with custom assertions

Custom assertions can be used with `satisfies()` to verify multiple conditions on an object.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyMultipleConditions() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book).satisfies(
                b -> assertThat(b.getTitle()).contains("Effective"),
                b -> assertThat(b.getAuthor()).contains("Bloch"),
                b -> assertThat(b.getYear()).isLessThan(2003)
        );
    }
}
```

:::info

Using `satisfies()` with multiple lambdas works but requires manual property extraction.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void verifyMultipleConditions() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book).satisfies(
                b -> BookTester.of(book).title().contains("Effective"),
                b -> BookTester.of(book).author().contains("Bloch"),
                b -> BookTester.of(book).year().isLessThan(2003)
        );
    }
}
```

:::tip

Custom assertions with `satisfies()` provide named property accessors and automatic descriptions,
making the assertions more readable and failure messages more helpful.

:::

</TabItem>
</Tabs>
