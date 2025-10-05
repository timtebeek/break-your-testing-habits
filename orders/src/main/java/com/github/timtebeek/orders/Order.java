package com.github.timtebeek.orders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Order {
    private final String orderId;
    private final String customerId;
    private final LocalDate orderDate;
    private final String status;
    private final List<OrderItem> items;
    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal shippingCost;
    private final BigDecimal discount;
    private final BigDecimal total;

    public Order(String orderId, String customerId, LocalDate orderDate, String status, List<OrderItem> items,
                 BigDecimal subtotal, BigDecimal tax, BigDecimal shippingCost, BigDecimal discount, BigDecimal total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.shippingCost = shippingCost;
        this.discount = discount;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) &&
                Objects.equals(customerId, order.customerId) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(status, order.status) &&
                Objects.equals(items, order.items) &&
                Objects.equals(subtotal, order.subtotal) &&
                Objects.equals(tax, order.tax) &&
                Objects.equals(shippingCost, order.shippingCost) &&
                Objects.equals(discount, order.discount) &&
                Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId, orderDate, status, items, subtotal, tax, shippingCost, discount, total);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", items=" + items +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", shippingCost=" + shippingCost +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }
}
