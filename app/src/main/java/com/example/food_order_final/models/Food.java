package com.example.food_order_final.models;

import java.util.Date;

public class Food extends Base{
    private int id;
    private String name;
    private double price;
    private FoodCategory category;
    private Restaurant restaurant;
    private double discount;

    public Food(int id, String name, double price,  double discount, FoodCategory category, Restaurant restaurant, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.category = category;
        this.restaurant = restaurant;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public Food(String name, double price, FoodCategory category, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.discount = 0.0;
        this.restaurant = restaurant;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter và Setter cho category
    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    // Getter và Setter cho restaurant
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
