package com.github.timtebeek.orders;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentInfo {
    private final String method;
    private final String lastFourDigits;
    private final String transactionId;
    private final LocalDateTime timestamp;

    public PaymentInfo(String method, String lastFourDigits, String transactionId, LocalDateTime timestamp) {
        this.method = method;
        this.lastFourDigits = lastFourDigits;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentInfo that = (PaymentInfo) o;
        return Objects.equals(method, that.method) &&
                Objects.equals(lastFourDigits, that.lastFourDigits) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, lastFourDigits, transactionId, timestamp);
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "method='" + method + '\'' +
                ", lastFourDigits='" + lastFourDigits + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
