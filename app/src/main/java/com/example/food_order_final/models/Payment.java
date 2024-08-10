package com.example.food_order_final.models;

public class Payment extends Base {

    private int id;

    private PaymentPending paymentPending;

    private String paymentMethod;

    public Payment(int id, PaymentPending paymentPending, String paymentMethod) {
        this.id = id;
        this.paymentPending = paymentPending;
        this.paymentMethod = paymentMethod;
    }

    public PaymentPending getPaymentPending() {
        return paymentPending;
    }

    public void setPaymentPending(PaymentPending paymentPending) {
        this.paymentPending = paymentPending;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
