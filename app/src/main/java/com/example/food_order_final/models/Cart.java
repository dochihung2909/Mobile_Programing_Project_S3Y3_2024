package com.example.food_order_final.models;

import java.util.Date;
import java.util.List;

public class Cart extends Base{
    int id;
    private User user;
    private Restaurant restaurant;

    private int status;


    public Cart(User user, Restaurant restaurant,int status) {
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }
    public Cart(int id, User user, Restaurant restaurant, int status) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}