package com.example.food_order_final.models;

import java.util.Date;
import java.util.List;

public class Cart extends Base{
    int id;
    private User user;
    private Restaurant restaurant;


    public Cart(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }
    public Cart(int id, User user, Restaurant restaurant) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
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
}