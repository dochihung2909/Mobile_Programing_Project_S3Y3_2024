package com.example.food_order_final.models;

import java.util.Date;

public class Payment extends Base {
    private int id;

    private PaymentStatus status;

    private PaymentMethod method;

    public Payment(int id, PaymentStatus status, PaymentMethod method, boolean actived, Date createdDate, Date updatedDate) {
        this.id = id;
        this.status = status;
        this.method = method;
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public Payment(int id, PaymentStatus status, PaymentMethod method) {
        this.id = id;
        this.status = status;
        this.method = method;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
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
