package com.example.food_order_final.models;

import java.util.Date;

public class User extends Base{
    private int id;
    private String username;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String password;
    private Role role;
    private String avatar;

    public User(int id, String username, String phoneNumber, String email, String fullName, String password, Role role, String avatar, Date createdDate, Date updatedDate) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }
    public User(String username, String phoneNumber, String email, String fullName, String password, Role role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.avatar = null;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }

    public User(int id, String username, String phoneNumber, String email, String fullName, Role role, String avatar) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
