package com.example.food_order_final.models;

import java.util.Date;
import java.util.List;

public class Cart extends Base{
    int id;
    private User user;
    private Restaurant restaurant;
    private int status;

    public Cart(int id, User user, Restaurant restaurant, int status, boolean actived, Date createdDate, Date updatedDate) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }
    public Cart(User user, Restaurant restaurant,int status) {
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }
    public Cart(int id, User user, Restaurant restaurant, int status) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
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