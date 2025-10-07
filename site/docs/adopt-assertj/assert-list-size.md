---
sidebar_position: 1
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Assert list size

When testing collection sizes with JUnit Jupiter, it's easy to make mistakes with argument order or write verbose assertions.
AssertJ provides more expressive and less error-prone alternatives.

## Redundant null checks

Checking for null before checking collection size is redundant, as a null collection would fail the size check anyway.

<Tabs>
<TabItem value="before" label="Before">

```java title="AssertListSize.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AssertListSize {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListSize() {
        assertNotNull(list);
        assertEquals(3, list.size());
    }
}
```

:::warning

The `assertNotNull(list)` check is redundant. If the list were null, `assertEquals(3, list.size())` would throw a `NullPointerException` anyway.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="AssertListSize.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertListSize {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListSize() {
        assertThat(list).hasSize(3);
    }
}
```

:::tip

AssertJ's `hasSize()` is concise, expressive, and will appropriately handle null collections with a clear error message.

:::

</TabItem>
</Tabs>

## Confusing failure messages

Using JUnit's `assertEquals()` with the actual and expected values swapped leads to confusing error messages.

<Tabs>
<TabItem value="before" label="Before">

```java title="AssertListSize.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssertListSize {

    List<String> list = List.of("a", "b", "c");

    @Test
    void incorrectArgumentOrder() {
        assertEquals(list.size(), 4);
    }
}
```

:::danger

The arguments are backwards. JUnit 5's `assertEquals()` expects the expected value first, then the actual value.
This produces a confusing error: `expected: <3> but was: <4>` when it should be `expected: <4> but was: <3>`.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="AssertListSize.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertListSize {

    List<String> list = List.of("a", "b", "c");

    @Test
    void incorrectArgumentOrder() {
        assertThat(list).hasSize(4);
    }
}
```

:::tip

AssertJ eliminates argument order confusion. The actual value is always the subject, and the assertion is always chained after it.

:::

</TabItem>
</Tabs>
