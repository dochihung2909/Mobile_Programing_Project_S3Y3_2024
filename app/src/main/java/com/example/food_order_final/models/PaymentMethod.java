package com.example.food_order_final.models;

public enum PaymentMethod {
    BANK(2),
    E_WALLET(1),
    CASH(0);

    private final int method;

    PaymentMethod(int method) {
        this.method = method;
    }

    public int getMethod() {
        return method;
    }

    public static PaymentMethod fromMethod(int method) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getMethod() == method) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("No enum constant with method " + method);
    }
}
