---
sidebar_position: 6
---
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Redundant assertions

Sometimes overlapping assertions are used in sequence, where one would imply the other.
For example, checking that a list is not null before checking its size or contents is redundant,
as a null list would cause the subsequent assertions to fail anyway.

<Tabs>
<TabItem value="before" label="Before">

```java title="RedundantListTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedundantListTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListSize() {
        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains("b"));
    }
}
```

:::warning

The `assertNotNull(list)` is redundant because if `list` were null,
the subsequent assertions would throw a `NullPointerException` anyway.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="RedundantListTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RedundantListTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListSize() {
        assertThat(list)
                .hasSize(3)
                .contains("b");
    }
}
```

:::tip

The AssertJ version is not only more concise, but also more expressive,
as it clearly states the intent of the test without unnecessary boilerplate.
We've also dropped the redundant null check, as AssertJ's assertions will handle that appropriately.

:::

</TabItem>
</Tabs>
