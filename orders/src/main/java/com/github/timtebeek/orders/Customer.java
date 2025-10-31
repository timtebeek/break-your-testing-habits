package com.github.timtebeek.orders;

public class Customer {
    private final String customerId;
    private final String email;
    private final String name;
    private final Address shippingAddress;
    private final String loyaltyTier;

    public Customer(String customerId, String email, String name, Address shippingAddress, String loyaltyTier) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
        this.shippingAddress = shippingAddress;
        this.loyaltyTier = loyaltyTier;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }
}
