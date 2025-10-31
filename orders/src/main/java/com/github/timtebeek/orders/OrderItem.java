package com.github.timtebeek.orders;

import java.math.BigDecimal;

public class OrderItem {
    private final String productId;
    private final String productName;
    private final String category;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal lineTotal;

    public OrderItem(String productId, String productName, String category, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
