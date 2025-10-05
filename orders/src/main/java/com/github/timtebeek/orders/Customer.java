package com.github.timtebeek.orders;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(shippingAddress, customer.shippingAddress) &&
                Objects.equals(loyaltyTier, customer.loyaltyTier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, email, name, shippingAddress, loyaltyTier);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", shippingAddress=" + shippingAddress +
                ", loyaltyTier='" + loyaltyTier + '\'' +
                '}';
    }
}
