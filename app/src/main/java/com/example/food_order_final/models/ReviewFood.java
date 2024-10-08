package com.example.food_order_final.models;

import java.util.Date;

public class ReviewFood extends Base{
    private String comment;
    private int id;
    private User user;
    private Food food;
    private double rating;
    private String image;

    public ReviewFood(int id, String comment, double rating, String image, User user, Food food, boolean actived, Date createdDate, Date updatedDate) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.image = image;
        this.user = user;
        this.food = food;
        this.setActived(actived);
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }

    public ReviewFood(String comment, double rating, String image, User user, Food food) {
        this.comment = comment;
        this.rating = rating;
        this.image = image;
        this.user = user;
        this.food = food;
        this.setActived(true);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public ReviewFood(String comment, double rating, User user, Food food) {
        this.comment = comment;
        this.rating = rating;
        this.image = null;
        this.user = user;
        this.food = food;
        this.setActived(true);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
