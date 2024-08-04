package com.example.food_order_final.models;

import java.util.Date;
import java.util.List;

public class Cart extends Base{
    private int id;
    private Date orderDate;
    private double totalAmount;
    private List<OrderDetail> orderDetails;
    private int user_id;
    private int restaurant_id;

    public Cart(int id, Date orderDate, double totalAmount, List<OrderDetail> orderDetails, int user_id, int restaurant_id) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.setCreatedDate(new Date());
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}