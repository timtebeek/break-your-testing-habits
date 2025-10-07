---
sidebar_position: 5
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Backwards

Between JUnit 4 and JUnit 5 the order of arguments in assertions was swapped to improve readability.
In JUnit 4 the expected value came second, while in JUnit 5 it comes first.
This change was made to align with the natural reading order of "actual value should be expected value".
There is a danger though for folks who upgraded without changing their existing tests, that the arguments are now in the wrong order.

## Confusing failure messages
This can lead to confusing error messages when tests fail, as the expected and actual values will be reported incorrectly.

<Tabs>
<TabItem value="before" label="Before">

```java title="ArgumentOrderTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentOrderTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void confusingFailureMessage() {
        assertEquals(list.size(), 4);
    }
}
```

:::warning

Warning: The arguments to `assertEquals` are in the wrong order. The actual value should be the first argument, and the expected value should be the second argument.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ArgumentOrderTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentOrderTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void confusingFailureMessage() {
        assertThat(list).hasSize(4);
    }
}
```

:::tip

Using AssertJ's fluent assertions can help avoid issues with argument order, as the actual value is always the subject of the assertion.

:::

</TabItem>
</Tabs>


## Incorrectly passing tests
Worse still, if the values are of the same type, tests may pass when they should fail, or vice versa.

<Tabs>
<TabItem value="before" label="Before">

```java title="ArgumentOrderTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArgumentOrderTest {
    @Test
    void incorrectlyPassing() {
        String actual = null;
        assertNotNull("message", actual);
    }
}
```

:::danger

The arguments to `assertNotNull` are in the wrong order. This test will incorrectly pass because the string "message" is not null.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="ArgumentOrderTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentOrderTest {
    @Test
    void incorrectlyPassing() {
        String actual = null;
        assertThat(actual).as("message").isNotNull();
    }
}
```

:::tip

The message is now correctly associated with the assertion, and the test will fail as expected.

:::

</TabItem>
</Tabs>
