---
sidebar_position: 2
---

# Recursive comparison

The recursive comparison feature of AssertJ allows you to compare complex objects by recursively comparing their fields.
This is particularly useful when you want to verify that two objects are equivalent in terms of their state,
without having to write custom `equals` methods or manually compare each field.

```java title="RecursiveComparisonTest.java"
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveComparisonTest {

    @Test
    void compareFieldsIgnoringYear() {
        Book hardcover = new Book("Effective Java", "Joshua Bloch", 2001);
        Book paperback = new Book("Effective Java", "Joshua Bloch", 2018);

        // Compare all fields except 'year'
        assertThat(hardcover)
                .usingRecursiveComparison()
                .ignoringFields("year")
                .isEqualTo(paperback);
    }
}

```

:::tip

Note how `ignoringFields("year")` is used to exclude the `year` field from the recursive comparison.
This is especially useful when certain fields are expected to differ, such as timestamps or IDs.

:::
