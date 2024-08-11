package com.example.food_order_final.models;

import java.util.Date;

public class Review extends Base{
    private String comment;
    private User user;
    private Restaurant restaurant;
    double rating;

    public Review(int id, String comment, double rating, User user, Restaurant restaurant, Date createdDate, Date updatedDate) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.restaurant = restaurant;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public Review(String comment, double rating, User user, Restaurant restaurant) {
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.restaurant = restaurant;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
