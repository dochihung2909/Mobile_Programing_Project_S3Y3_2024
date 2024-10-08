package com.example.food_order_final.models;

import java.util.Date;

public class CartDetail extends Base{
    private int id;
    private Food food;
    private int quantity;
    private double price;
    private Cart cart;

    public CartDetail(int id, Food food, int quantity, boolean actived, Date createdDate, Date updatedDate, Cart cart) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.price = food.getPrice() - food.getDiscount();
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
        this.cart = cart;
    }

    public CartDetail(Food food, int quantity, Cart cart) {
        this.food = food;
        this.quantity = quantity;
        this.price = food.getPrice() - food.getDiscount();
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
        this.cart = cart;
    }

    public int getId() {
        return id;
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}