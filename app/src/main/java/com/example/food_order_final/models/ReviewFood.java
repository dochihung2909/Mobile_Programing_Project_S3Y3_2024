package com.example.food_order_final.models;

import java.util.Date;

public class ReviewFood extends Base{
    private String comment;
    private int id;
    private User user;
    private Food food;
    double rating;

    public ReviewFood(int id, String comment, double rating, User user, Food food, Date createdDate, Date updatedDate) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.food = food;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public ReviewFood(String comment, double rating, User user, Food food) {
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.food = food;
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

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
