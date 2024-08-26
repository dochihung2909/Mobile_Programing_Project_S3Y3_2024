package com.example.food_order_final.models;

import java.util.Date;

public class PaymentPending extends Base {

    private int id;
    private PaymentStatus paymentStatus;
    private Cart cart;
    private double total;
    private PaymentMethod paymentMethod;
    private String note;

    public PaymentPending(int id, PaymentStatus paymentStatus, Cart cart, double total, PaymentMethod paymentMethod, String note, boolean actived, Date createdDate, Date updatedDate) {
        this.id = id;
        this.paymentStatus = paymentStatus;
        this.cart = cart;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public PaymentPending(PaymentStatus paymentStatus, Cart cart, double total, PaymentMethod paymentMethod, String note) {
        this.paymentStatus = paymentStatus;
        this.cart = cart;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public int getId() {
        return id;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
