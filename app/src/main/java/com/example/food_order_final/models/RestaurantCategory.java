package com.example.food_order_final.models;

public class RestaurantCategory {
    private int id;
    private String name;

    public RestaurantCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RestaurantCategory(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}