package com.example.food_order_final.models;

import java.util.Date;

public class Role extends Base{
    private int id;
    private String name;

    public Role(int id, String name, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
    }
    public Role(String name) {
        this.name = name;
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
