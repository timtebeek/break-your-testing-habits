import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Nested tests

JUnit Jupiter supports nested test classes to better organize related tests and share setup code.
However, nested classes without the `@Nested` annotation are silently ignored, which can lead to tests not running.

## Missing @Nested annotation

Inner classes in JUnit Jupiter test classes are not automatically recognized as test classes.
They must be annotated with `@Nested` or the tests inside them will be silently skipped.

<Tabs>
<TabItem value="before" label="Before">

```java title="JUnitJupiterTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JUnitJupiterTest {

    @Test
    void outerTest() {
        assertTrue(true);
    }

    // Missing @Nested annotation
    class InnerClass {
        @Test
        void innerTest() {
            assertTrue(true);
        }
    }
}
```

:::danger

The `innerTest()` method will not run because the `InnerClass` is missing the `@Nested` annotation.
JUnit silently ignores the inner class, which can give false confidence that tests are passing.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="JUnitJupiterTest.java"
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JUnitJupiterTest {

    @Test
    void outerTest() {
        assertTrue(true);
    }

    @Nested
    class InnerClass {
        @Test
        void innerTest() {
            assertTrue(true);
        }
    }
}
```

:::tip

Adding the `@Nested` annotation ensures the inner test class is recognized and executed.
Nested classes are useful for grouping related tests and sharing setup code via `@BeforeEach` methods.

:::

</TabItem>
</Tabs>

## Organizing related tests

Nested test classes help organize related test scenarios and provide better test report structure.

<Tabs>
<TabItem value="before" label="Before">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void addPositiveNumbers() {
        assertEquals(5, new Calculator().add(2, 3));
    }

    @Test
    void addNegativeNumbers() {
        assertEquals(-5, new Calculator().add(-2, -3));
    }

    @Test
    void subtractPositiveNumbers() {
        assertEquals(1, new Calculator().subtract(3, 2));
    }

    @Test
    void subtractNegativeNumbers() {
        assertEquals(-1, new Calculator().subtract(-3, -2));
    }
}
```

:::info

Flat test structure can be hard to navigate as the number of tests grows.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="CalculatorTest.java"
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Nested
    class Addition {
        @Test
        void positiveNumbers() {
            assertEquals(5, new Calculator().add(2, 3));
        }

        @Test
        void negativeNumbers() {
            assertEquals(-5, new Calculator().add(-2, -3));
        }
    }

    @Nested
    class Subtraction {
        @Test
        void positiveNumbers() {
            assertEquals(1, new Calculator().subtract(3, 2));
        }

        @Test
        void negativeNumbers() {
            assertEquals(-1, new Calculator().subtract(-3, -2));
        }
    }
}
```

:::tip

Nested classes group related tests together, making the test structure clearer and test reports more organized.
You can also share setup code and context between tests in the same nested class.

:::

</TabItem>
</Tabs>
