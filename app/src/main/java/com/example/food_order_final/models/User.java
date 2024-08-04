package com.example.food_order_final.models;

public class User {
    private int id;
    private String username;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String password;
    private Role role;

    public User(int id, String username, String phoneNumber, String email, String fullName, String password, Role role) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }
    public User(String username, String phoneNumber, String email, String fullName, String password, Role role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }
//    public User(String username, String phoneNumber, String email, String fullName, String password, Role role) {
//        this.username = username;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.fullName = fullName;
//        this.password = password;
//        this.role = role;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
