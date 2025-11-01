---
sidebar_position: 6
---

# Practice: Advanced AssertJ

Now it's your turn! Apply the advanced AssertJ techniques you've learned to improve the test code in the `orders` package.

## Exercise: Order Collection Tests

Navigate to `orders/src/test/java/com/github/timtebeek/orders/OrderServiceTest.java`.

This test class works with a collection of orders and demonstrates several opportunities to use advanced AssertJ features:

1. **Extracting properties** - Instead of manually extracting properties with streams or loops, use `extracting()`
2. **Filtering collections** - Use `filteredOn()` to filter collections before asserting
3. **Soft assertions** - Group multiple assertions that should all be checked
4. **Flat extracting** - Use `flatExtracting()` for nested collections

### Your tasks

1. Refactor the tests to use `extracting()` where appropriate
2. Use `filteredOn()` to filter collections in a fluent way
3. Replace multiple separate assertions with soft assertions
4. Use `flatExtracting()` for nested collection operations

## Tips

- Use `extracting()` with method references for single properties
- Use `filteredOn()` to filter before asserting
- Use `SoftAssertions` when you want to check multiple conditions
- Use `flatExtracting()` to work with nested collections

## Solution

<details>
<summary>Click to see the refactored tests</summary>

```java title="OrderServiceTest.java"
package com.github.timtebeek.orders;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        OrderItem item1 = new OrderItem("P1", "Laptop", "Electronics", 1,
          new BigDecimal("1000.00"), new BigDecimal("1000.00"));
        OrderItem item2 = new OrderItem("P2", "Mouse", "Electronics", 2,
          new BigDecimal("25.00"), new BigDecimal("50.00"));
        OrderItem item3 = new OrderItem("P3", "Keyboard", "Electronics", 1,
          new BigDecimal("75.00"), new BigDecimal("75.00"));

        orders = List.of(
          new Order("ORD-001", "CUST-001", LocalDate.of(2024, 1, 15), "PENDING",
            List.of(item1), new BigDecimal("1000.00"), new BigDecimal("85.00"),
            new BigDecimal("0.00"), new BigDecimal("50.00"), new BigDecimal("1035.00")),
          new Order("ORD-002", "CUST-002", LocalDate.of(2024, 1, 16), "CONFIRMED",
            List.of(item2, item3), new BigDecimal("125.00"), new BigDecimal("10.63"),
            new BigDecimal("9.99"), new BigDecimal("12.50"), new BigDecimal("133.12")),
          new Order("ORD-003", "CUST-001", LocalDate.of(2024, 1, 17), "SHIPPED",
            List.of(item1, item2), new BigDecimal("1050.00"), new BigDecimal("89.25"),
            new BigDecimal("0.00"), new BigDecimal("52.50"), new BigDecimal("1086.75"))
        );
    }

    @Test
    void extractingOrderIds() {
        // Refactored: Using extracting() instead of manual stream mapping
        assertThat(orders)
          .extracting(Order::getOrderId)
          .containsExactly("ORD-001", "ORD-002", "ORD-003");
    }

    @Test
    void filteringAndExtracting() {
        // Refactored: Using filteredOn() instead of manual stream filtering
        assertThat(orders)
          .filteredOn(order -> order.getCustomerId().equals("CUST-001"))
          .extracting(Order::getOrderId)
          .containsExactly("ORD-001", "ORD-003");
    }

    @Test
    void multipleAssertionsOnOrder() {
        // Refactored: Using soft assertions to group multiple checks
        Order order = orders.getFirst();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(order.getOrderId()).isEqualTo("ORD-001");
            softly.assertThat(order.getCustomerId()).isEqualTo("CUST-001");
            softly.assertThat(order.getStatus()).isEqualTo("PENDING");
            softly.assertThat(order.getSubtotal()).isEqualByComparingTo(new BigDecimal("1000.00"));
            softly.assertThat(order.getTotal()).isEqualByComparingTo(new BigDecimal("1035.00"));
            softly.assertThat(order.getItems()).hasSize(1);
        });
    }

    @Test
    void flatExtractingItems() {
        // Refactored: Using flatExtracting() instead of manual flatMap operations
        assertThat(orders)
          .flatExtracting(Order::getItems)
          .extracting(OrderItem::getProductName)
          .contains("Laptop", "Mouse", "Keyboard");
    }
}
```

</details>

## Run the tests

```bash
cd orders
mvn test -Dtest=OrderServiceTest
```

## Next Steps

Once you're comfortable with these advanced AssertJ features, check out the [Practice: Custom Assertions](./practice-custom-assertions.md) to learn how to create domain-specific assertion classes for even more readable tests!

