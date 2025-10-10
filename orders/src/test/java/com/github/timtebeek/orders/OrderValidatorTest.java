package com.github.timtebeek.orders;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class demonstrating JUnit 4 patterns that should be migrated to JUnit 5.
 * This test uses:
 * - JUnit 4 annotations (@Before, @Test)
 * - Try-catch with fail() pattern for exception testing
 * - Old assertion style
 * 
 * Workshop participants should migrate this to JUnit 5 and improve the test patterns.
 */
public class OrderValidatorTest {

    private OrderValidator validator;
    private Order validOrder;

    @Before
    public void setUp() {
        validator = new OrderValidator();
        
        OrderItem item = new OrderItem(
            "PROD-001",
            "Test Product",
            "Electronics",
            2,
            new BigDecimal("50.00"),
            new BigDecimal("100.00")
        );
        
        validOrder = new Order(
            "ORD-001",
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            List.of(item),
            new BigDecimal("100.00"),
            new BigDecimal("8.50"),
            new BigDecimal("9.99"),
            BigDecimal.ZERO,
            new BigDecimal("118.49")
        );
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
    public void testOrderWithoutIdIsInvalid() {
        Order invalidOrder = new Order(
            null,
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            List.of(new OrderItem("P1", "Product", "Cat", 1, BigDecimal.TEN, BigDecimal.TEN)),
            BigDecimal.TEN,
            BigDecimal.ONE,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            new BigDecimal("11.00")
        );
        
        try {
            List<String> errors = validator.validate(invalidOrder);
            assertFalse("Order should be invalid", errors.isEmpty());
            boolean foundOrderIdError = false;
            for (String error : errors) {
                if (error.contains("Order ID")) {
                    foundOrderIdError = true;
                    break;
                }
            }
            assertTrue("Should have Order ID error", foundOrderIdError);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOrderWithEmptyItemsIsInvalid() {
        Order emptyItemsOrder = new Order(
            "ORD-002",
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            Collections.emptyList(),
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );
        
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

    @Test
    public void testOrderWithInvalidStatusIsInvalid() {
        Order invalidStatusOrder = new Order(
            "ORD-003",
            "CUST-001",
            LocalDate.now(),
            "INVALID_STATUS",
            List.of(new OrderItem("P1", "Product", "Cat", 1, BigDecimal.TEN, BigDecimal.TEN)),
            BigDecimal.TEN,
            BigDecimal.ONE,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            new BigDecimal("11.00")
        );
        
        try {
            assertFalse("Order should be invalid", validator.isValid(invalidStatusOrder));
            List<String> errors = validator.validate(invalidStatusOrder);
            assertTrue("Should have at least one error", errors.size() > 0);
            
            String errorMessage = String.join(" ", errors);
            assertTrue("Error should mention status", errorMessage.contains("status"));
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOrderWithNegativeTotalIsInvalid() {
        Order negativeTotalOrder = new Order(
            "ORD-004",
            "CUST-001",
            LocalDate.now(),
            "PENDING",
            List.of(new OrderItem("P1", "Product", "Cat", 1, BigDecimal.TEN, BigDecimal.TEN)),
            BigDecimal.TEN,
            BigDecimal.ONE,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            new BigDecimal("-5.00")
        );
        
        List<String> errors = validator.validate(negativeTotalOrder);
        assertTrue("Should have errors", errors.size() > 0);
        assertFalse("Should not be valid", validator.isValid(negativeTotalOrder));
    }
}
