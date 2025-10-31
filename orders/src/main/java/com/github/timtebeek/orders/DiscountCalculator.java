package com.github.timtebeek.orders;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates discounts based on customer loyalty tier and order value.
 */
public class DiscountCalculator {

    private static final BigDecimal BRONZE_DISCOUNT = new BigDecimal("0.05"); // 5%
    private static final BigDecimal SILVER_DISCOUNT = new BigDecimal("0.10"); // 10%
    private static final BigDecimal GOLD_DISCOUNT = new BigDecimal("0.15");   // 15%
    private static final BigDecimal PLATINUM_DISCOUNT = new BigDecimal("0.20"); // 20%
    
    private static final BigDecimal BULK_ORDER_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal BULK_ORDER_DISCOUNT = new BigDecimal("0.05"); // Additional 5%

    /**
     * Calculates the discount amount for a customer based on their loyalty tier.
     */
    public BigDecimal calculateLoyaltyDiscount(Customer customer, BigDecimal subtotal) {
        if (customer == null || subtotal == null) {
            throw new IllegalArgumentException("Customer and subtotal cannot be null");
        }

        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }

        String tier = customer.getLoyaltyTier();
        if (tier == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountRate = switch (tier.toUpperCase()) {
            case "BRONZE" -> BRONZE_DISCOUNT;
            case "SILVER" -> SILVER_DISCOUNT;
            case "GOLD" -> GOLD_DISCOUNT;
            case "PLATINUM" -> PLATINUM_DISCOUNT;
            default -> BigDecimal.ZERO;
        };

        return subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates an additional bulk order discount if the subtotal exceeds the threshold.
     */
    public BigDecimal calculateBulkDiscount(BigDecimal subtotal) {
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal cannot be null");
        }

        if (subtotal.compareTo(BULK_ORDER_THRESHOLD) >= 0) {
            return subtotal.multiply(BULK_ORDER_DISCOUNT).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calculates the total discount for an order.
     */
    public BigDecimal calculateTotalDiscount(Customer customer, BigDecimal subtotal) {
        BigDecimal loyaltyDiscount = calculateLoyaltyDiscount(customer, subtotal);
        BigDecimal bulkDiscount = calculateBulkDiscount(subtotal);
        return loyaltyDiscount.add(bulkDiscount);
    }
}
