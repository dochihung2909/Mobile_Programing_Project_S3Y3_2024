package com.example.food_order_final.models;

public enum PaymentStatus {
    PENDING(0),
    CONFIRM(1),
    DELIVERING(2),
    COMPLETED(3),
    FAILED(4),
    REFUNDED(5);

    private final int status;


    PaymentStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static PaymentStatus fromStatus(int status) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getStatus() == status) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant with status " + status);
    }

    public static String getNameFromStatus(int status) {
        PaymentStatus paymentStatus = fromStatus(status);
        return paymentStatus.name();
    }
}
