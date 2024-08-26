package com.example.food_order_final.models;

import java.util.Date;

public class ReviewRestaurant extends Base{
    private String comment;

    private int id;
    private User user;
    private Restaurant restaurant;
    private double rating;
    private String image;

    public ReviewRestaurant(int id, String comment, double rating, String image, User user, Restaurant restaurant, boolean actived, Date createdDate, Date updatedDate) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.image = image;
        this.user = user;
        this.restaurant = restaurant;
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public ReviewRestaurant(String comment, double rating, String image, User user, Restaurant restaurant) {
        this.comment = comment;
        this.rating = rating;
        this.image = image;
        this.user = user;
        this.restaurant = restaurant;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }


    public ReviewRestaurant(String comment, double rating, User user, Restaurant restaurant) {
        this.comment = comment;
        this.rating = rating;
        this.image = null;
        this.user = user;
        this.restaurant = restaurant;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public int getId() {
        return id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
