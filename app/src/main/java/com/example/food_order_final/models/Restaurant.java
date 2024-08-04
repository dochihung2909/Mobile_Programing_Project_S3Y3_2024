package com.example.food_order_final.models;

import java.util.ArrayList;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private RestaurantCategory category;
    private boolean isPartner;

    public Restaurant(int id, String name, String address, String phoneNumber, RestaurantCategory category, boolean isPartner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.isPartner = isPartner;
    }

    public Restaurant(String name, String address, String phoneNumber, RestaurantCategory category, boolean isPartner) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.isPartner = isPartner;
    }

    public Restaurant(String name, String address, String phoneNumber, RestaurantCategory category) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.isPartner = false;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RestaurantCategory getCategory() {
        return category;
    }

    public void setCategory(RestaurantCategory category) {
        this.category = category;
    }

    public boolean isPartner() {
        return isPartner;
    }

    public void setPartner(boolean partner) {
        isPartner = partner;
    }
}