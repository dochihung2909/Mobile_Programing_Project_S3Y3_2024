package com.example.food_order_final.models;

public class UserAddress extends Base{

    private int id;
    private User user;
    private String address;
    private String addressType;

    public UserAddress(int id, User user, String address, String addressType) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.addressType = addressType;
    }

    public UserAddress(User user, String address, String addressType) {
        this.user = user;
        this.address = address;
        this.addressType = addressType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
}
