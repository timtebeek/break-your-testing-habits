package com.github.timtebeek.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating poor assertion practices that should be improved with AssertJ.
 * This test uses:
 * - Multiple JUnit assertions that could be more expressive
 * - Manual comparison of BigDecimal values
 * - Verbose null checks and comparisons
 * 
 * Workshop participants should refactor these to use AssertJ for better expressiveness.
 */
class DiscountCalculatorTest {

    private DiscountCalculator calculator;
    private Customer bronzeCustomer;
    private Customer silverCustomer;
    private Customer goldCustomer;
    private Customer platinumCustomer;

    @BeforeEach
    void setUp() {
        calculator = new DiscountCalculator();
        
        Address address = new Address("123 Main St", "Springfield", "IL", "62701", "USA");
        
        bronzeCustomer = new Customer("C001", "bronze@example.com", "Bronze User", address, "BRONZE");
        silverCustomer = new Customer("C002", "silver@example.com", "Silver User", address, "SILVER");
        goldCustomer = new Customer("C003", "gold@example.com", "Gold User", address, "GOLD");
        platinumCustomer = new Customer("C004", "platinum@example.com", "Platinum User", address, "PLATINUM");
    }

    @Test
    void testBronzeCustomerDiscount() {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal discount = calculator.calculateLoyaltyDiscount(bronzeCustomer, subtotal);
        
        assertNotNull(discount);
        assertEquals(0, discount.compareTo(new BigDecimal("5.00")));
        assertTrue(discount.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(discount.compareTo(subtotal) < 0);
    }

    @Test
    void testSilverCustomerDiscount() {
        BigDecimal subtotal = new BigDecimal("200.00");
        BigDecimal discount = calculator.calculateLoyaltyDiscount(silverCustomer, subtotal);
        
        assertNotNull(discount);
        assertEquals(0, discount.compareTo(new BigDecimal("20.00")));
        assertTrue(discount.compareTo(BigDecimal.ZERO) > 0);
        assertEquals(2, discount.scale());
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
    void testPlatinumCustomerDiscount() {
        BigDecimal subtotal = new BigDecimal("500.00");
        BigDecimal discount = calculator.calculateLoyaltyDiscount(platinumCustomer, subtotal);
        
        assertNotNull(discount);
        assertEquals(0, discount.compareTo(new BigDecimal("100.00")));
        assertTrue(discount.compareTo(BigDecimal.ZERO) > 0);
        assertEquals("100.00", discount.toString());
    }

    @Test
    void testBulkDiscountApplied() {
        BigDecimal subtotal = new BigDecimal("600.00");
        BigDecimal bulkDiscount = calculator.calculateBulkDiscount(subtotal);
        
        assertNotNull(bulkDiscount);
        assertEquals(0, bulkDiscount.compareTo(new BigDecimal("30.00")));
        assertTrue(bulkDiscount.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testBulkDiscountNotAppliedForSmallOrders() {
        BigDecimal subtotal = new BigDecimal("400.00");
        BigDecimal bulkDiscount = calculator.calculateBulkDiscount(subtotal);
        
        assertNotNull(bulkDiscount);
        assertEquals(0, bulkDiscount.compareTo(BigDecimal.ZERO));
        assertTrue(bulkDiscount.compareTo(BigDecimal.ZERO) == 0);
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

    @Test
    void testZeroSubtotalGivesZeroDiscount() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discount = calculator.calculateLoyaltyDiscount(goldCustomer, subtotal);
        
        assertNotNull(discount);
        assertEquals(0, discount.compareTo(BigDecimal.ZERO));
        assertTrue(discount.compareTo(BigDecimal.ZERO) >= 0);
    }
}
