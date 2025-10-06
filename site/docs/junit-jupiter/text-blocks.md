import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Text blocks

Java 13 introduced text blocks (multi-line strings) as a preview feature, finalized in Java 15.
They make working with multi-line strings much more readable, but traditional string concatenation is still commonly found in older tests.

## String concatenation

Traditional string concatenation with `+` operators for multi-line strings is hard to read and maintain.
Each line needs explicit `\n` newline characters and quote escaping.

<Tabs>
<TabItem value="before" label="Before">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("Books:\n" +
                        "Effective Java by Joshua Bloch (2001)\n" +
                        "Java Concurrency in Practice by Brian Goetz (2006)\n" +
                        "Clean Code by Robert C. Martin (2008)\n" +
                        "Authors:\n" +
                        "Joshua Bloch\n" +
                        "Brian Goetz\n" +
                        "Robert C. Martin\n" +
                        "Total books: 3\n" +
                        "Total authors: 3\n",
                summary);
    }
}
```

:::warning

String concatenation with `+` is verbose and error-prone. Missing newlines, extra spaces, and quote escaping make it hard to see the actual content.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """,
                summary);
    }
}
```

:::tip

Text blocks make multi-line strings much more readable. The content is exactly as it appears, without escape sequences or concatenation operators.

:::

</TabItem>
</Tabs>

## Even better with AssertJ

While text blocks improve readability, AssertJ can make multi-line string comparisons even clearer with better diff output.

<Tabs>
<TabItem value="before" label="Before">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertEquals("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """,
                summary);
    }
}
```

:::info

JUnit's `assertEquals()` works with text blocks, but the diff output for multi-line strings can be hard to read when tests fail.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="TextBlockTest.java"
import com.github.timtebeek.books.Bundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextBlockTest {
    @Test
    void summary() {
        String summary = new Bundle().summary();
        assertThat(summary).isEqualToIgnoringWhitespace("""
                Books:
                Effective Java by Joshua Bloch (2001)
                Java Concurrency in Practice by Brian Goetz (2006)
                Clean Code by Robert C. Martin (2008)
                Authors:
                Joshua Bloch
                Brian Goetz
                Robert C. Martin
                Total books: 3
                Total authors: 3
                """);
    }
}
```

:::tip

AssertJ provides better diff output for multi-line strings and offers flexible comparison methods like `isEqualToIgnoringWhitespace()` for when exact whitespace doesn't matter.

:::

</TabItem>
</Tabs>
