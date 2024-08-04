package com.example.food_order_final.models;

public class OrderDetail {
    private int id;
    private Food food;
    private int quantity;
    private double price;

    public OrderDetail(int id, Food food, int quantity, double price) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.price = price;
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