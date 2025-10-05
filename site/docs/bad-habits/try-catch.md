import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Try/Catch fail

Using try/catch blocks with `fail()` calls to test for exceptions is an awkward outdated pattern.
JUnit4 introduced the `@Test(expected=...)` annotation parameter to simplify this pattern, but it had its own limitations.
An alternative `@Rule ExpectedException` approach was also available, but it was more verbose and more disconnected.

<Tabs groupId="state">
<TabItem value="before" label="Before">

```java title="TryCatchFailTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TryCatchFailTest {

    @Test
    void expectException() {
        try {
            boom();
            fail("Exception uncaught");
        } catch (IllegalStateException e) {
            // Catch before fail is called
        }
    }

    private void boom() {
        throw new IllegalStateException("boom!");
    }

    @Test
    void expectNoException() {
        try {
            noBoom();
            assertTrue(true, "No Exception");
        } catch (IllegalStateException e) {
            fail("Exception caught");
        }
    }

    private void noBoom() {
    }
}
```

:::warning

Using try/catch blocks with fail() calls is an outdated and less readable way to test for exceptions in JUnit 5.
Instead, we should use the `assertThatThrownBy()` and `assertDoesNotThrow` methods, which are more concise and expressive.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="TryCatchFailTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TryCatchFailTest {

    @Test
    void expectException() {
        assertThatThrownBy(() -> boom()).isInstanceOf(IllegalStateException.class);
    }

    private void boom() {
        throw new IllegalStateException("boom!");
    }

    @Test
    void expectNoException() {
        assertDoesNotThrow(() -> noBoom(), "Exception caught");
    }

    private void noBoom() {
    }
}

```

:::tip

Both JUnit 5+ and AssertJ provide built-in methods to handle exception assertions, making tests cleaner and more maintainable.

:::

</TabItem>
</Tabs>
