package com.example.food_order_final.models;

import com.example.food_order_final.dao.BaseDao;
import com.example.food_order_final.database.DatabaseHelper;

import java.util.Date;

public class Employee extends User {
    private Restaurant restaurant;

    public Employee(User user, Restaurant restaurant) {
        super(user);
        this.restaurant = restaurant;
    }

    public Employee(String username, String phoneNumber, String email, String fullName, String password, Role role, String avatar, Restaurant restaurant) {
        super(username, phoneNumber, email, fullName, password, role, avatar);
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
