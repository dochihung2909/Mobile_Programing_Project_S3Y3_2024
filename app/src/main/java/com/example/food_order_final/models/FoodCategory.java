package com.example.food_order_final.models;

public class FoodCategory {
    private int id;
    private String name;

    public FoodCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public FoodCategory(String name) {
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