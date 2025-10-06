package com.github.timtebeek.orders;

import java.time.LocalDateTime;

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
}
