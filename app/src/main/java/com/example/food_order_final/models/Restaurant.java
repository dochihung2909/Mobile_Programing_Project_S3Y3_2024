package com.example.food_order_final.models;

import java.util.Date;

public class Restaurant extends Base{
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private RestaurantCategory category;
    private boolean isPartner;
    private double rating;
    private String avatar;
    private User owner;

    public Restaurant(int id, String name, String address, String phoneNumber, RestaurantCategory category, String avatar, boolean isPartner, double rating, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.avatar = avatar;
        this.isPartner = isPartner;
        this.rating = rating;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public Restaurant(String name, String address, String phoneNumber, RestaurantCategory category) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.isPartner = false;
        this.rating = 0.0;
        this.avatar = null;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public int getId() {
        return id;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}