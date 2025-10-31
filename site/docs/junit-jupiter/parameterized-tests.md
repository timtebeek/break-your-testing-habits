---
sidebar_position: 4
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Parameterized tests

Instead of writing multiple similar test methods that differ only in input values and expected results, use `@ParameterizedTest` to run the same test logic with different parameters. This reduces code duplication and makes it easier to add more test cases.

## Avoid duplicate test methods

When testing the same logic with different inputs, writing separate test methods creates unnecessary duplication and makes maintenance harder.

<Tabs>
<TabItem value="before" label="Before">

```java title="StringUtilsTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StringUtilsTest {

    @Test
    void emptyStringIsBlank() {
        assertTrue(StringUtils.isBlank(""));
    }

    @Test
    void whitespaceStringIsBlank() {
        assertTrue(StringUtils.isBlank("  "));
    }

    @Test
    void tabStringIsBlank() {
        assertTrue(StringUtils.isBlank("\t"));
    }

    @Test
    void nullStringIsBlank() {
        assertTrue(StringUtils.isBlank(null));
    }

    @Test
    void nonBlankStringIsNotBlank() {
        assertFalse(StringUtils.isBlank("hello"));
    }

    @Test
    void stringWithTextIsNotBlank() {
        assertFalse(StringUtils.isBlank("  hello  "));
    }
}
```

:::warning

Multiple test methods with similar logic create unnecessary duplication and make maintenance harder. Adding a new test case requires copying an entire method.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="StringUtilsTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource // Adds `null` and `""`.
    @ValueSource(strings = {"  ", "\t", "\n"})
    void blankStrings(String input) {
        assertThat(StringUtils.isBlank(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "  hello  ", "test"})
    void nonBlankStrings(String input) {
        assertThat(StringUtils.isBlank(input)).isFalse();
    }
}
```

:::tip

Parameterized tests offer several benefits:
- **Less duplication**: Test logic is written once and reused with different inputs.
- **Easier maintenance**: Changes to test logic only need to be made in one place.
- **Better readability**: All test cases for similar scenarios are grouped together.
- **Simple to extend**: Adding new test cases is as easy as adding a value to the source.

:::

</TabItem>
</Tabs>

## Multiple parameter sources

JUnit 5 and 6 provide various parameter sources for different use cases.

<Tabs>
<TabItem value="valueSource" label="@ValueSource">

```java title="MathUtilsTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MathUtilsTest {

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10})
    void evenNumbers(int number) {
        assertThat(number % 2).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"radar", "level", "noon"})
    void palindromes(String word) {
        assertThat(isPalindrome(word)).isTrue();
    }
}
```

:::info

`@ValueSource` is the simplest parameter source, supporting primitive types and strings.

:::

</TabItem>
<TabItem value="csvSource" label="@CsvSource">

```java title="CalculatorTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorTest {

    @ParameterizedTest
    @CsvSource({
        "2, 3, 5",
        "5, 7, 12",
        "10, 20, 30",
        "100, 200, 300"
    })
    void addition(int a, int b, int expected) {
        assertThat(a + b).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "apple, APPLE",
        "hello, HELLO",
        "test, TEST"
    })
    void uppercase(String input, String expected) {
        assertThat(input.toUpperCase()).isEqualTo(expected);
    }
}
```

:::info

`@CsvSource` allows you to provide multiple parameters per test case using CSV format.

:::

</TabItem>
<TabItem value="methodSource" label="@MethodSource">

```java title="UserValidatorTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UserValidatorTest {

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void rejectsInvalidEmails(String email) {
        assertThat(UserValidator.isValidEmail(email)).isFalse();
    }

    static Stream<String> invalidEmails() {
        return Stream.of(
            "not-an-email",
            "@example.com",
            "user@",
            "user name@example.com"
        );
    }

    @ParameterizedTest
    @MethodSource("userTestCases")
    void validatesUsers(String name, int age, boolean valid) {
        User user = new User(name, age);
        assertThat(UserValidator.isValid(user)).isEqualTo(valid);
    }

    static Stream<Arguments> userTestCases() {
        return Stream.of(
            arguments("John", 25, true),
            arguments("", 25, false),
            arguments("Jane", -1, false),
            arguments("Bob", 150, false)
        );
    }
}
```

:::info

`@MethodSource` provides the most flexibility, allowing complex objects and reusable parameter sets.

:::

</TabItem>
<TabItem value="enumSource" label="@EnumSource">

```java title="DayTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

class DayTest {

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void allDaysHaveNames(DayOfWeek day) {
        assertThat(day.name()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"})
    void weekendDays(DayOfWeek day) {
        assertThat(isWeekend(day)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, mode = EnumSource.Mode.EXCLUDE,
                names = {"SATURDAY", "SUNDAY"})
    void weekDays(DayOfWeek day) {
        assertThat(isWeekend(day)).isFalse();
    }
}
```

:::info

`@EnumSource` is perfect for testing all values of an enum or a filtered subset.

:::

</TabItem>
</Tabs>

## Custom display names

You can customize how each parameterized test is displayed in test reports.

<Tabs>
<TabItem value="default" label="Default">

```java title="StringTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\t"})
    void blankStrings(String input) {
        assertThat(input.isBlank()).isTrue();
    }
}
```

:::info

By default, test names include the parameter index: `blankStrings(String) [1]`, `blankStrings(String) [2]`, etc.

:::

</TabItem>
<TabItem value="custom" label="Custom Names">

```java title="StringTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringTest {

    @ParameterizedTest(name = "input=''{0}'' should be blank")
    @ValueSource(strings = {"", "  ", "\t"})
    void blankStrings(String input) {
        assertThat(input.isBlank()).isTrue();
    }

    @ParameterizedTest(name = "[{index}] {0} + {1} = {2}")
    @CsvSource({"1, 2, 3", "5, 10, 15", "100, 200, 300"})
    void addition(int a, int b, int expected) {
        assertThat(a + b).isEqualTo(expected);
    }
}
```

:::tip

Custom display names make test reports more readable:
- `{0}`, `{1}`, etc. refer to parameter values.
- `{index}` refers to the invocation index.
- Use `''` to escape single quotes in the display name.

:::

</TabItem>
<TabItem value="argumentsSource" label="Named ArgumentSets">

```java title="CalculatorTest.java"
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

class CalculatorTest {

    @ParameterizedTest
    @MethodSource("additionTestCases")
    void addition(int a, int b, int expected) {
        assertThat(a + b).isEqualTo(expected);
    }

    static Stream<Arguments> additionTestCases() {
        return Stream.of(
            argumentSet("small numbers", 2, 3, 5),
            argumentSet("medium numbers", 10, 20, 30),
            argumentSet("large numbers", 100, 200, 300),
            argumentSet("negative numbers", -5, -3, -8)
        );
    }
}
```

:::tip

Using `argumentSet(name, ...)` instead of `arguments(...)` provides named test cases:
- Each test displays with its descriptive name: `addition(int, int, int) [small numbers]`.
- Makes it immediately clear which scenario failed without looking at parameter values.
- Especially useful for complex test scenarios with multiple parameters.
- Available in JUnit Jupiter 5.8+.

:::

</TabItem>
</Tabs>
