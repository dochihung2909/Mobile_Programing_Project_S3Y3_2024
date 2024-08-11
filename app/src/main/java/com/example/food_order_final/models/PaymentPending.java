package com.example.food_order_final.models;

public class PaymentPending extends Base {

    private int id;
    private boolean status;
    private User user;
    private Cart cart;
    private double total;

    public PaymentPending(int id, boolean status, User user, Cart cart, double total) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.cart = cart;
        this.total = total;
    }


    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
