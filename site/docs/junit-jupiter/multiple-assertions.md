import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Multiple assertions

When checking for multiple elements, chaining assertions is cleaner than multiple separate `assertTrue()` calls.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="AssertListContains.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertListContains {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertMultipleElements() {
        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
        assertTrue(list.contains("c"));
    }
}
```

:::warning

Multiple `assertTrue()` calls are verbose and stop at the first failure, hiding subsequent issues.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="AssertListContains.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertListContains {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertMultipleElements() {
        assertThat(list).contains("a", "b", "c");
    }
}
```

:::tip

AssertJ allows checking for multiple elements in a single assertion, making the test more concise and showing all missing elements in case of failure.

:::

</TabItem>
</Tabs>
