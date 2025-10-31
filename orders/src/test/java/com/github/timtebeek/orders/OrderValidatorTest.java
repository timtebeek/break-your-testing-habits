package com.github.timtebeek.orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class demonstrating JUnit 4 patterns that should be migrated to JUnit 5. This test uses:
 *
 * <ol>
 *   <li>JUnit 4 annotations (@Before, @Test).
 *   <li>Try-catch with fail() pattern for exception testing.
 *   <li>Old assertion style.
 * </ol>
 *
 * Migrate this to JUnit 5 and improve the test patterns.
 */
public class OrderValidatorTest {

  private OrderValidator validator;
  private Order validOrder;

  @Before
  public void setUp() {
    validator = new OrderValidator();

    OrderItem item =
        new OrderItem(
            "PROD-001",
            "Test Product",
            "Electronics",
            2,
            new BigDecimal("50.00"),
            new BigDecimal("100.00"));

    validOrder =
        new Order(
            "ORD-001",
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            List.of(item),
            new BigDecimal("100.00"),
            new BigDecimal("8.50"),
            new BigDecimal("9.99"),
            BigDecimal.ZERO,
            new BigDecimal("118.49"));
  }

  @Test
  public void testValidOrderHasNoErrors() {
    List<String> errors = validator.validate(validOrder);
    assertTrue(errors.isEmpty());
    assertTrue(validator.isValid(validOrder));
  }

  @Test
  public void testNullOrderReturnsError() {
    try {
      List<String> errors = validator.validate(null);
      assertFalse("Should have validation errors", errors.isEmpty());
      assertEquals("Should have exactly one error", 1, errors.size());
      assertTrue("Error should mention null", errors.get(0).contains("null"));
    } catch (Exception e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testOrderWithEmptyItemsIsInvalid() {
    Order emptyItemsOrder =
        new Order(
            "ORD-002",
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            Collections.emptyList(),
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO);

    List<String> errors = validator.validate(emptyItemsOrder);
    assertFalse(errors.isEmpty());

    boolean hasItemError = false;
    for (String error : errors) {
      if (error.toLowerCase().contains("item")) {
        hasItemError = true;
      }
    }
    assertTrue("Should have error about items", hasItemError);
  }
}
