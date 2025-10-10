package com.github.timtebeek.orders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for creating and processing orders.
 */
public class OrderService {

    private final OrderValidator validator;
    private final DiscountCalculator discountCalculator;

    public OrderService() {
        this.validator = new OrderValidator();
        this.discountCalculator = new DiscountCalculator();
    }

    public OrderService(OrderValidator validator, DiscountCalculator discountCalculator) {
        this.validator = validator;
        this.discountCalculator = discountCalculator;
    }

    /**
     * Creates an order with calculated totals and discounts.
     */
    public Order createOrder(String orderId, Customer customer, List<OrderItem> items) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal discount = discountCalculator.calculateTotalDiscount(customer, subtotal);
        BigDecimal tax = calculateTax(subtotal.subtract(discount));
        BigDecimal shippingCost = calculateShippingCost(customer, subtotal);
        BigDecimal total = subtotal.subtract(discount).add(tax).add(shippingCost);

        Order order = new Order(
            orderId,
            customer.getCustomerId(),
            LocalDate.now(),
            "PENDING",
            items,
            subtotal,
            tax,
            shippingCost,
            discount,
            total
        );

        return order;
    }

    /**
     * Validates and processes an order.
     */
    public void processOrder(Order order) {
        List<String> errors = validator.validate(order);
        if (!errors.isEmpty()) {
            throw new OrderValidationException("Order validation failed: " + String.join(", ", errors));
        }

        // In a real system, this would save to database, send notifications, etc.
        System.out.println("Processing order: " + order.getOrderId());
    }

    /**
     * Calculates the subtotal by summing all line totals.
     */
    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
            .map(OrderItem::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates tax at 8.5% rate.
     */
    private BigDecimal calculateTax(BigDecimal taxableAmount) {
        BigDecimal taxRate = new BigDecimal("0.085");
        return taxableAmount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates shipping cost based on customer location and order value.
     * Free shipping for orders over $100 or for GOLD/PLATINUM customers.
     */
    private BigDecimal calculateShippingCost(Customer customer, BigDecimal subtotal) {
        String tier = customer.getLoyaltyTier();
        if (tier != null && (tier.equals("GOLD") || tier.equals("PLATINUM"))) {
            return BigDecimal.ZERO;
        }

        if (subtotal.compareTo(new BigDecimal("100.00")) >= 0) {
            return BigDecimal.ZERO;
        }

        // Base shipping cost
        return new BigDecimal("9.99");
    }

    public static class OrderValidationException extends RuntimeException {
        public OrderValidationException(String message) {
            super(message);
        }
    }
}
