package com.github.timtebeek.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating opportunities for advanced AssertJ features.
 * This test uses basic assertions that could be improved with:
 *
 * <ol>
 *   <li>Extracting properties from collections
 *   <li>Filtering collections before assertions
 *   <li>Soft assertions for multiple checks
 *   <li>Flat extracting for nested collections
 * </ol>
 *
 * Refactor these tests to use advanced AssertJ features.
 */
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
        // Using streams to extract order IDs - could be simplified with extracting()
        List<String> orderIds = orders.stream()
          .map(Order::getOrderId)
          .toList();

        assertEquals(3, orderIds.size());
        assertEquals("ORD-001", orderIds.get(0));
        assertEquals("ORD-002", orderIds.get(1));
        assertEquals("ORD-003", orderIds.get(2));
    }

    @Test
    void filteringAndExtracting() {
        // Manual filtering with streams - could use filteredOn()
        List<String> customerOrders = orders.stream()
          .filter(order -> order.getCustomerId().equals("CUST-001"))
          .map(Order::getOrderId)
          .toList();

        assertEquals(2, customerOrders.size());
        assertTrue(customerOrders.contains("ORD-001"));
        assertTrue(customerOrders.contains("ORD-003"));
    }

    @Test
    void multipleAssertionsOnOrder() {
        // Multiple separate assertions - could benefit from soft assertions
        Order order = orders.getFirst();

        assertEquals("ORD-001", order.getOrderId());
        assertEquals("CUST-001", order.getCustomerId());
        assertEquals("PENDING", order.getStatus());
        assertEquals(0, order.getSubtotal().compareTo(new BigDecimal("1000.00")));
        assertEquals(0, order.getTotal().compareTo(new BigDecimal("1035.00")));
        assertEquals(1, order.getItems().size());
    }

    @Test
    void flatExtractingItems() {
        // Nested stream operations - could use flatExtracting()
        List<String> productNames = orders.stream()
          .flatMap(order -> order.getItems().stream())
          .map(OrderItem::getProductName)
          .toList();

        assertTrue(productNames.contains("Laptop"));
        assertTrue(productNames.contains("Mouse"));
        assertTrue(productNames.contains("Keyboard"));
    }
}
