package com.example.food_order_final.models;

public class Payment extends Base {
    private int id;

    private PaymentStatus status;

    private PaymentMethod method;

    public Payment(int id, PaymentStatus status, PaymentMethod method) {
        this.id = id;
        this.status = status;
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public PaymentMethod getPaymentMethod() {
        return method;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.method = paymentMethod;
    }
}
