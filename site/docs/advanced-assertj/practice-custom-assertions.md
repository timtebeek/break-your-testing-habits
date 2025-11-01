---
sidebar_position: 7
---

# Practice: Custom Assertions

Now it's your turn to create custom assertions for the `Order` domain objects!

## Why Custom Assertions?

While the advanced AssertJ features we've learned (extracting, filtering, soft assertions) are powerful, you might notice that we're still writing repetitive assertion code when dealing with domain objects.

Custom assertions solve this by:
- **Reducing duplication** - Write the assertion logic once, reuse everywhere
- **Improving readability** - Domain-specific fluent API that reads like natural language
- **Making tests more maintainable** - Changes to assertions are centralized
- **Providing better error messages** - Tailored failure messages for your domain

## Exercise: Create OrderAssert

Looking at the `OrderServiceTest.java`, notice how we repeatedly check the same properties on `Order` objects. Let's create a custom assertion class to make these tests more expressive!

### Step 1: Create the OrderAssert class

Create a new file `OrderAssert.java` in the `orders/src/test/java/com/github/timtebeek/orders/` directory.

Your custom assertion should:
1. Extend `AbstractObjectAssert<OrderAssert, Order>`
2. Provide a static `assertThat(Order)` method as the entry point
3. Include methods for common assertions on Order properties

### Step 2: Implement assertion methods

Think about what assertions you frequently make on `Order` objects:
- Checking specific order IDs
- Checking customer IDs
- Verifying order status (and convenience methods like `isPending()`, `isConfirmed()`, `isShipped()`)
- Comparing totals and subtotals
- Checking the number of items
- Verifying order dates

### Step 3: Refactor a test to use your custom assertion

Once you've created `OrderAssert`, try using it in a test. For example:

```java
@Test
void testWithCustomAssertion() {
    Order order = orders.getFirst();

    // Instead of multiple separate assertions...
    // Try using your custom OrderAssert here!
}
```

## Tips

- Extend `AbstractObjectAssert<YourAssert, YourDomain>`
- Always call `isNotNull()` at the start of each assertion method
- Return `this` from each method to enable fluent chaining
- Use `failWithMessage()` to provide clear failure messages
- Create convenience methods for common checks (like `isPending()`)

## Solution

<details>
<summary>Click to see the complete OrderAssert implementation</summary>

```java title="OrderAssert.java"
package com.github.timtebeek.orders;

import org.assertj.core.api.AbstractObjectAssert;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderAssert extends AbstractObjectAssert<OrderAssert, Order> {

    private OrderAssert(Order order) {
        super(order, OrderAssert.class);
    }

    public static OrderAssert assertThat(Order actual) {
        return new OrderAssert(actual);
    }

    public OrderAssert hasOrderId(String orderId) {
        isNotNull();
        if (!actual.getOrderId().equals(orderId)) {
            failWithMessage("Expected order ID to be <%s> but was <%s>",
                orderId, actual.getOrderId());
        }
        return this;
    }

    public OrderAssert hasCustomerId(String customerId) {
        isNotNull();
        if (!actual.getCustomerId().equals(customerId)) {
            failWithMessage("Expected customer ID to be <%s> but was <%s>",
                customerId, actual.getCustomerId());
        }
        return this;
    }

    public OrderAssert hasStatus(String status) {
        isNotNull();
        if (!actual.getStatus().equals(status)) {
            failWithMessage("Expected status to be <%s> but was <%s>",
                status, actual.getStatus());
        }
        return this;
    }

    public OrderAssert isPending() {
        return hasStatus("PENDING");
    }

    public OrderAssert isConfirmed() {
        return hasStatus("CONFIRMED");
    }

    public OrderAssert isShipped() {
        return hasStatus("SHIPPED");
    }

    public OrderAssert hasTotalGreaterThan(BigDecimal amount) {
        isNotNull();
        if (actual.getTotal().compareTo(amount) <= 0) {
            failWithMessage("Expected total to be greater than <%s> but was <%s>",
                amount, actual.getTotal());
        }
        return this;
    }

    public OrderAssert hasTotal(BigDecimal total) {
        isNotNull();
        if (actual.getTotal().compareTo(total) != 0) {
            failWithMessage("Expected total to be <%s> but was <%s>",
                total, actual.getTotal());
        }
        return this;
    }

    public OrderAssert hasSubtotal(BigDecimal subtotal) {
        isNotNull();
        if (actual.getSubtotal().compareTo(subtotal) != 0) {
            failWithMessage("Expected subtotal to be <%s> but was <%s>",
                subtotal, actual.getSubtotal());
        }
        return this;
    }

    public OrderAssert hasItemCount(int count) {
        isNotNull();
        int actualCount = actual.getItems().size();
        if (actualCount != count) {
            failWithMessage("Expected <%s> items but found <%s>", count, actualCount);
        }
        return this;
    }

    public OrderAssert hasOrderDate(LocalDate date) {
        isNotNull();
        if (!actual.getOrderDate().equals(date)) {
            failWithMessage("Expected order date to be <%s> but was <%s>",
                date, actual.getOrderDate());
        }
        return this;
    }

    public OrderAssert hasDiscount(BigDecimal discount) {
        isNotNull();
        if (actual.getDiscount().compareTo(discount) != 0) {
            failWithMessage("Expected discount to be <%s> but was <%s>",
                discount, actual.getDiscount());
        }
        return this;
    }
}
```

</details>

<details>
<summary>Click to see example usage in tests</summary>

```java title="OrderServiceTest.java"
package com.github.timtebeek.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Import your custom assertion
import static com.github.timtebeek.orders.OrderAssert.assertThat;

class OrderServiceTest {

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        // ...existing setup...
    }

    @Test
    void verifyOrderWithCustomAssertion() {
        Order order = orders.getFirst();

        // Look how readable and fluent this is!
        assertThat(order)
            .hasOrderId("ORD-001")
            .hasCustomerId("CUST-001")
            .isPending()
            .hasSubtotal(new BigDecimal("1000.00"))
            .hasTotal(new BigDecimal("1035.00"))
            .hasItemCount(1)
            .hasOrderDate(LocalDate.of(2024, 1, 15));
    }

    @Test
    void verifyShippedOrder() {
        Order order = orders.get(2);

        assertThat(order)
            .hasOrderId("ORD-003")
            .isShipped()
            .hasTotalGreaterThan(new BigDecimal("1000.00"))
            .hasItemCount(2);
    }

    @Test
    void verifyMultipleOrders() {
        assertThat(orders.get(0)).isPending();
        assertThat(orders.get(1)).isConfirmed();
        assertThat(orders.get(2)).isShipped();
    }
}
```

**Notice how much more readable the tests become!** The custom assertion provides:
- A fluent, chainable API
- Clear, domain-specific method names
- Better failure messages
- Less code duplication

</details>

## Key Concepts

### Extending AbstractObjectAssert

```java
public class OrderAssert extends AbstractObjectAssert<OrderAssert, Order>
```

The two generic parameters are:
1. **SELF** (`OrderAssert`) - Your assertion class (enables method chaining)
2. **ACTUAL** (`Order`) - The type you're asserting on

### Static Factory Method

```java
public static OrderAssert assertThat(Order actual) {
    return new OrderAssert(actual);
}
```

This provides the entry point for your fluent API. Import it statically to use it like: `assertThat(order).isPending()`

### Null Safety

Always call `isNotNull()` at the start of each assertion method to prevent NullPointerExceptions:

```java
public OrderAssert hasOrderId(String orderId) {
    isNotNull();  // Always check this first!
    // ...rest of assertion
}
```

### Fluent Chaining

Return `this` from each method to enable chaining:

```java
public OrderAssert hasOrderId(String orderId) {
    // ...assertion logic...
    return this;  // Enables chaining
}
```

### Custom Error Messages

Use `failWithMessage()` to provide clear, helpful error messages:

```java
failWithMessage("Expected order ID to be <%s> but was <%s>",
    expected, actual.getOrderId());
```

## Try it yourself!

1. Create the `OrderAssert` class
2. Implement at least 5 assertion methods
3. Refactor one of the existing tests to use your custom assertion
4. Run the tests to see your custom assertions in action!

```bash
cd orders
mvn test -Dtest=OrderServiceTest
```

## Bonus Challenge

Can you also create a custom assertion for `OrderItem`? Think about what properties and conditions you might want to assert on order items!

