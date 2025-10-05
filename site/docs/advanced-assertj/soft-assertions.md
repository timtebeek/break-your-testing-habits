import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Soft assertions

By default, assertions fail fast—the first failed assertion stops test execution and reports the failure.
Soft assertions allow you to collect multiple assertion failures and report them all at once, making it easier to fix multiple issues in a single test run.

## Fail fast vs fail at end

Standard assertions stop at the first failure, hiding subsequent problems.

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

        assertThat(book.getTitle()).contains("Ineffective");    // Fails here
        assertThat(book.getAuthor()).contains("Blog");          // Never executed
        assertThat(book.getYear()).isEven();                    // Never executed
    }
}
```

:::warning

With standard assertions, the test stops at the first failure.
You only see that the title assertion failed, hiding the other two issues.
This requires multiple test runs to discover and fix all problems.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(book.getTitle()).as("title").contains("Ineffective");
            softly.assertThat(book.getAuthor()).as("author").contains("Blog");
            softly.assertThat(book.getYear()).as("year").isEven();
        });
    }
}
```

:::tip

Soft assertions collect all failures and report them together at the end of the lambda.
You'll see all three failures in a single test run, making it faster to identify and fix all issues.

:::

</TabItem>
</Tabs>

## Custom soft assertions

For domain objects that are tested frequently, you can create custom soft assertion classes to make tests more readable and reusable.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(book.getTitle()).as("title").contains("Effective");
            softly.assertThat(book.getAuthor()).as("author").contains("Bloch");
            softly.assertThat(book.getYear()).as("year").isLessThan(2003);
        });
    }
}
```

:::info

While this works, repeatedly asserting on the same object properties across multiple tests can become verbose.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="SoftBookAssertions.java"
import com.github.timtebeek.books.Book;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.StringAssert;

public class SoftBookAssertions extends AutoCloseableSoftAssertions {

    private final Book book;

    public SoftBookAssertions(Book book) {
        this.book = book;
    }

    public StringAssert title() {
        return assertThat(book.getTitle()).describedAs("title");
    }

    public StringAssert author() {
        return assertThat(book.getAuthor()).describedAs("author");
    }

    public IntegerAssert year() {
        return assertThat(book.getYear()).describedAs("year");
    }
}
```

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void verifyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        try (SoftBookAssertions softly = new SoftBookAssertions(book)) {
            softly.title().contains("Effective");
            softly.author().contains("Bloch");
            softly.year().isLessThan(2003);
        }
    }
}
```

:::tip

Custom soft assertion classes provide:
- Named property accessors for better readability
- Automatic descriptions for each property
- Reusability across multiple tests
- Try-with-resources for automatic assertion verification

The `AutoCloseableSoftAssertions` base class automatically verifies all assertions when the try block exits.

:::

</TabItem>
</Tabs>

## Multiple failures reported

When soft assertions fail, all failures are collected and reported together.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void faultyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        assertThat(book.getTitle()).contains("Ineffective");    // Stops here
        assertThat(book.getAuthor()).contains("Blog");          // Never runs
        assertThat(book.getYear()).isEven();                    // Never runs
    }
}
```

:::danger

Output shows only the first failure:
```
Expected: "Effective Java" to contain "Ineffective"
```

You need to run the test three times to see all failures.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BookTest.java"
import com.github.timtebeek.books.Book;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void faultyBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 2001);

        try (SoftBookAssertions softly = new SoftBookAssertions(book)) {
            softly.title().contains("Ineffective");
            softly.author().contains("Blog");
            softly.year().isEven();
        }
    }
}
```

:::tip

Output shows all three failures together:
```
Multiple Failures (3 failures)
-- failure 1 --
[title]
Expecting actual:
  "Effective Java"
to contain:
  "Ineffective"
-- failure 2 --
[author]
Expecting actual:
  "Joshua Bloch"
to contain:
  "Blog"
-- failure 3 --
[year]
Expecting actual:
  2001
to be even
```

All failures are visible in a single test run, saving time and effort.

:::

</TabItem>
</Tabs>
