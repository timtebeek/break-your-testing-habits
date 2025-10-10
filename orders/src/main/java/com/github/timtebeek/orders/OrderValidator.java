package com.github.timtebeek.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates orders to ensure they meet business rules before processing.
 */
public class OrderValidator {

    /**
     * Validates an order and returns a list of validation errors.
     * An empty list means the order is valid.
     */
    public List<String> validate(Order order) {
        List<String> errors = new ArrayList<>();

        if (order == null) {
            errors.add("Order cannot be null");
            return errors;
        }

        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            errors.add("Order ID is required");
        }

        if (order.getCustomerId() == null || order.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }

        if (order.getOrderDate() == null) {
            errors.add("Order date is required");
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            errors.add("Order must contain at least one item");
        }

        if (order.getTotal() == null) {
            errors.add("Order total is required");
        } else if (order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Order total must be greater than zero");
        }

        if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
            errors.add("Order status is required");
        } else if (!isValidStatus(order.getStatus())) {
            errors.add("Invalid order status: " + order.getStatus());
        }

        return errors;
    }

    /**
     * Checks if an order is valid (has no validation errors).
     */
    public boolean isValid(Order order) {
        return validate(order).isEmpty();
    }

    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || 
               status.equals("CONFIRMED") || 
               status.equals("SHIPPED") || 
               status.equals("DELIVERED") || 
               status.equals("CANCELLED");
    }
}
