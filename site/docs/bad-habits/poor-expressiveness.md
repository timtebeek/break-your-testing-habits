import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Poor expressiveness

Tests should clearly convey their intent.
The limited expressiveness of JUnit's assertions often leads to verbose and less readable tests.
Using a more expressive assertion library like AssertJ can significantly improve the clarity of tests.
This not only makes the tests easier to understand but also helps in maintaining them over time.
For example, consider a test that checks if a list contains a specific element.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="PoorExpressivenessTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoorExpressivenessTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListContains() {
        assertTrue(list.contains("f"));
    }
}
```

:::warning

The `assertTrue(list.contains("f"))` is less expressive and can be harder to read, especially when negations are used.
When the test fails, the failure message will not clearly indicate what was expected versus what was actual.
This can complicate troubleshooting, especially when your tests only fail in certain environments, or after a long time.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="PoorExpressivenessTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PoorExpressivenessTest {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListContains() {
        assertThat(list).contains("f");
    }
}
```

:::tip

The AssertJ version is more expressive and provides clearer failure messages, making it easier to understand the intent of the test and diagnose issues when they arise.

:::

</TabItem>
</Tabs>
