import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# AssertThrows verify


<Tabs>
<TabItem value="before" label="Before">

```java title="AssertThrowsCatchTest.java"
import org.junit.jupiter.api.Test;

import static com.github.timtebeek.junit5.Mock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertThrowsCatchTest {

    Mock mock = new Mock();

    // TODO Can you spot the issue? It's subtle, but broken.
    @Test
    void assertThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            boom();
            verify(mock).method();
        });
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }

}

```

:::warning

Using `assertThrows` with multiple statements can lead to misleading test results, as only the first statement that throws an exception is evaluated.
Subsequent statements, such as verifications, may not be executed, potentially causing tests to pass when they should fail.
It's better to separate the exception assertion from other verifications to ensure all intended checks are performed.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="AssertThrowsCatchTest.java"
import org.junit.jupiter.api.Test;

import static com.github.timtebeek.junit5.Mock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class AssertThrowsCatchTest {

    Mock mock = new Mock();

    @Test
    void assertThrowsTest() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> boom());
        verify(mock).method();
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }

}
```

:::tip

By moving the `verify(mock).method();` call outside the exception assertion, we ensure that it is always executed.

:::

</TabItem>
</Tabs>
