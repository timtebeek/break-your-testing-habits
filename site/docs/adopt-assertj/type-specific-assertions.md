---
sidebar_position: 1
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Type-specific assertions

AssertJ provides rich, expressive assertions tailored to specific types. These specialized assertions make your tests more readable and provide better failure messages compared to generic boolean checks.

## Collection assertions

AssertJ offers numerous assertions specifically designed for collections, going far beyond simple size checks.

<Tabs>
<TabItem value="before" label="Before">

```java title="CollectionAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionAssertions {

    List<String> languages = List.of("Java", "Python", "JavaScript", "Kotlin");

    @Test
    void testCollection() {
        assertFalse(languages.isEmpty());
        assertEquals(4, languages.size());
        assertTrue(languages.contains("Java"));
        assertTrue(languages.containsAll(List.of("Java", "Kotlin")));
        assertFalse(languages.contains("Ruby"));
    }
}
```

:::warning

Multiple separate assertions are verbose and don't provide context about the collection being tested.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="CollectionAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionAssertions {

    List<String> languages = List.of("Java", "Python", "JavaScript", "Kotlin");

    @Test
    void testCollection() {
        assertThat(languages)
                .isNotEmpty()
                .hasSize(4)
                .contains("Java", "Kotlin")
                .doesNotContain("Ruby")
                .startsWith("Java")
                .endsWith("Kotlin");
    }
}
```

:::tip

AssertJ's collection assertions are chainable, expressive, and provide detailed failure messages showing the actual collection contents.

:::

</TabItem>
</Tabs>

### Advanced collection assertions

<Tabs>
<TabItem value="before" label="Before">

```java title="AdvancedCollectionAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedCollectionAssertions {

    List<Integer> numbers = List.of(2, 4, 6, 8, 10);

    @Test
    void testAllMatch() {
        boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);
        assertTrue(allEven);

        boolean anyGreaterThan5 = numbers.stream().anyMatch(n -> n > 5);
        assertTrue(anyGreaterThan5);

        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        assertTrue(noneNegative);
    }
}
```

</TabItem>
<TabItem value="after" label="After">

```java title="AdvancedCollectionAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdvancedCollectionAssertions {

    List<Integer> numbers = List.of(2, 4, 6, 8, 10);

    @Test
    void testAllMatch() {
        assertThat(numbers)
                .allMatch(n -> n % 2 == 0, "all numbers are even")
                .anyMatch(n -> n > 5)
                .noneMatch(n -> n < 0);
    }
}
```

:::tip

AssertJ eliminates the need for manual stream operations and provides clear descriptions in the assertion chain.

:::

</TabItem>
</Tabs>

## Date and time assertions

Testing temporal values requires careful handling of precision and timezones. AssertJ provides specialized assertions that make date comparisons intuitive.

<Tabs>
<TabItem value="before" label="Before">

```java title="DateAssertions.java"
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateAssertions {

    LocalDateTime now = LocalDateTime.now();
    LocalDate today = LocalDate.now();

    @Test
    void testDates() {
        assertTrue(now.isAfter(LocalDateTime.now().minusDays(1)));
        assertTrue(now.isBefore(LocalDateTime.now().plusDays(1)));
        assertEquals(2025, today.getYear());
        assertTrue(today.getMonthValue() >= 1 && today.getMonthValue() <= 12);
    }
}
```

:::warning

Manual date comparisons are verbose and error-prone, especially when checking ranges or specific components.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="DateAssertions.java"
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateAssertions {

    LocalDateTime now = LocalDateTime.now();
    LocalDate today = LocalDate.now();

    @Test
    void testDates() {
        assertThat(now)
                .isAfter(LocalDateTime.now().minusDays(1))
                .isBefore(LocalDateTime.now().plusDays(1))
                .hasYear(today.getYear());

        assertThat(today)
                .hasYear(2025)
                .isInTheFuture()  // relative to comparison date
                .isBetween(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
    }
}
```

:::tip

AssertJ's temporal assertions handle comparisons naturally and provide methods for checking specific date components.

:::

</TabItem>
</Tabs>

### Date tolerance assertions

<Tabs>
<TabItem value="before" label="Before">

```java title="DateToleranceAssertions.java"
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateToleranceAssertions {

    @Test
    void testTimestampWithinTolerance() {
        LocalDateTime expected = LocalDateTime.of(2025, 10, 7, 12, 0, 0);
        LocalDateTime actual = LocalDateTime.of(2025, 10, 7, 12, 0, 3);

        Duration diff = Duration.between(expected, actual);
        assertTrue(Math.abs(diff.getSeconds()) <= 5);
    }
}
```

</TabItem>
<TabItem value="after" label="After">

```java title="DateToleranceAssertions.java"
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class DateToleranceAssertions {

    @Test
    void testTimestampWithinTolerance() {
        LocalDateTime expected = LocalDateTime.of(2025, 10, 7, 12, 0, 0);
        LocalDateTime actual = LocalDateTime.of(2025, 10, 7, 12, 0, 3);

        assertThat(actual).isCloseTo(expected, within(5, ChronoUnit.SECONDS));
    }
}
```

:::tip

AssertJ's `isCloseTo()` with temporal units provides clear, readable tolerance-based date comparisons.

:::

</TabItem>
</Tabs>

## File and path assertions

Testing file system operations benefits greatly from AssertJ's specialized assertions for files and paths.

<Tabs>
<TabItem value="before" label="Before">

```java title="FileAssertions.java"
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileAssertions {

    Path configFile = Path.of("config.properties");

    @Test
    void testFile() throws IOException {
        assertTrue(Files.exists(configFile));
        assertTrue(Files.isRegularFile(configFile));
        assertFalse(Files.isDirectory(configFile));
        assertTrue(Files.isReadable(configFile));
        assertFalse(Files.size(configFile) == 0);
        assertEquals(".properties", getExtension(configFile.toString()));
    }

    String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }
}
```

:::warning

File checks require verbose `Files` utility calls and manual extension parsing.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="FileAssertions.java"
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileAssertions {

    Path configFile = Path.of("config.properties");

    @Test
    void testFile() {
        assertThat(configFile)
                .exists()
                .isRegularFile()
                .isNotEmptyFile()
                .isReadable()
                .hasExtension("properties")
                .hasParent(Path.of("."));
    }
}
```

:::tip

AssertJ's file assertions provide chainable, expressive checks that handle common file system operations elegantly.

:::

</TabItem>
</Tabs>

### File content assertions

<Tabs>
<TabItem value="before" label="Before">

```java title="FileContentAssertions.java"
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileContentAssertions {

    Path logFile = Path.of("app.log");

    @Test
    void testFileContent() throws IOException {
        String content = Files.readString(logFile);
        assertTrue(content.contains("ERROR"));

        List<String> lines = Files.readAllLines(logFile);
        assertTrue(lines.stream().anyMatch(line -> line.contains("ERROR")));
    }
}
```

</TabItem>
<TabItem value="after" label="After">

```java title="FileContentAssertions.java"
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileContentAssertions {

    Path logFile = Path.of("app.log");

    @Test
    void testFileContent() {
        assertThat(logFile)
                .content()
                .contains("ERROR");

        assertThat(logFile)
                .usingCharset("UTF-8")
                .hasContent("expected content");  // exact match;
    }
}
```

:::tip

AssertJ can directly assert on file contents without manual reading, and chains with string assertions.

:::

</TabItem>
</Tabs>

## Map assertions

Maps have their own set of specialized assertions for keys, values, and entries.

<Tabs>
<TabItem value="before" label="Before">

```java title="MapAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapAssertions {

    Map<String, Integer> scores = Map.of(
            "Alice", 95,
            "Bob", 87,
            "Charlie", 92
    );

    @Test
    void testMap() {
        assertFalse(scores.isEmpty());
        assertEquals(3, scores.size());
        assertTrue(scores.containsKey("Alice"));
        assertEquals(95, scores.get("Alice"));
        assertTrue(scores.containsValue(87));
    }
}
```

</TabItem>
<TabItem value="after" label="After">

```java title="MapAssertions.java"
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class MapAssertions {

    Map<String, Integer> scores = Map.of(
            "Alice", 95,
            "Bob", 87,
            "Charlie", 92
    );

    @Test
    void testMap() {
        assertThat(scores)
                .isNotEmpty()
                .hasSize(3)
                .containsKey("Alice")
                .containsValue(87)
                .containsEntry("Alice", 95)
                .contains(entry("Bob", 87), entry("Charlie", 92))
                .doesNotContainKey("David");
    }
}
```

:::tip

AssertJ's map assertions allow testing keys, values, and entries in a fluent, chainable style with clear failure messages.

:::

</TabItem>
</Tabs>

## String assertions

String assertions go beyond simple equality checks to offer pattern matching, case handling, and content validation.

<Tabs>
<TabItem value="before" label="Before">

```java title="StringAssertions.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringAssertions {

    String email = "user@example.com";

    @Test
    void testString() {
        assertFalse(email.isEmpty());
        assertTrue(email.contains("@"));
        assertTrue(email.startsWith("user"));
        assertTrue(email.endsWith(".com"));
        assertTrue(email.matches(".*@.*\\..*"));
    }
}
```

</TabItem>
<TabItem value="after" label="After">

```java title="StringAssertions.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringAssertions {

    String email = "user@example.com";

    @Test
    void testString() {
        assertThat(email)
                .isNotEmpty()
                .contains("@")
                .startsWith("user")
                .endsWith(".com")
                .matches(".*@.*\\..*")
                .containsOnlyOnce("@")
                .doesNotContain(" ");
    }
}
```

:::tip

AssertJ's string assertions provide intuitive methods for common string validations without manual boolean checks.

:::

</TabItem>
</Tabs>
