package com.example.food_order_final.models;

import java.util.Date;

public abstract class Base {
    private Date createdDate;
    private Date updatedDate;
    private boolean isActived;

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }

    public Date update(){
        return this.updatedDate = new Date();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}

