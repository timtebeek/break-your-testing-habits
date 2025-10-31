package com.github.timtebeek.orders;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class demonstrating poor assertion practices that should be improved with AssertJ. This test
 * uses:
 *
 * <ol>
 *   <li>Multiple JUnit assertions that could be more expressive.
 *   <li>Manual comparison of BigDecimal values.
 *   <li>Verbose null checks and comparisons.
 * </ol>
 *
 * Refactor these to use AssertJ for better expressiveness.
 */
class DiscountCalculatorTest {

  private DiscountCalculator calculator;
  private Customer silverCustomer;
  private Customer goldCustomer;

  @BeforeEach
  void setUp() {
    calculator = new DiscountCalculator();

    Address address = new Address("123 Main St", "Springfield", "IL", "62701", "USA");

    silverCustomer = new Customer("C002", "silver@example.com", "Silver User", address, "SILVER");
    goldCustomer = new Customer("C003", "gold@example.com", "Gold User", address, "GOLD");
  }

  @Test
  void testGoldCustomerDiscount() {
    BigDecimal subtotal = new BigDecimal("300.00");
    BigDecimal discount = calculator.calculateLoyaltyDiscount(goldCustomer, subtotal);

    assertNotNull(discount);
    assertEquals(0, discount.compareTo(new BigDecimal("45.00")));
    assertTrue(discount.compareTo(new BigDecimal("20.00")) > 0);
    assertTrue(discount.compareTo(new BigDecimal("50.00")) < 0);
  }

  @Test
  void testTotalDiscountCombinesLoyaltyAndBulk() {
    BigDecimal subtotal = new BigDecimal("600.00");
    BigDecimal totalDiscount = calculator.calculateTotalDiscount(goldCustomer, subtotal);

    assertNotNull(totalDiscount);

    // Gold customer gets 15% = 90.00
    // Bulk discount gets 5% = 30.00
    // Total should be 120.00
    BigDecimal expected = new BigDecimal("120.00");
    assertEquals(0, totalDiscount.compareTo(expected));
    assertTrue(totalDiscount.compareTo(new BigDecimal("100.00")) > 0);
    assertTrue(totalDiscount.compareTo(new BigDecimal("150.00")) < 0);
  }

  @Test
  void testDiscountScaleIsCorrect() {
    BigDecimal subtotal = new BigDecimal("123.45");
    BigDecimal discount = calculator.calculateLoyaltyDiscount(silverCustomer, subtotal);

    assertNotNull(discount);
    assertEquals(2, discount.scale());
    assertTrue(discount.toString().matches("\\d+\\.\\d{2}"));
  }
}
