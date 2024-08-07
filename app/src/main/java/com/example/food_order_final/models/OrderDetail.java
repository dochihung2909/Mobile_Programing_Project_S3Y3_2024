package com.example.food_order_final.models;

import java.util.Date;

public class OrderDetail extends Base{
    private int id;
    private Food food;
    private int quantity;
    private double price;

    public OrderDetail(int id, Food food, int quantity, double price, Date createdDate, Date updatedDate) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.price = price;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public OrderDetail(Food food, int quantity, double price) {
        this.food = food;
        this.quantity = quantity;
        this.price = price;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}