package com.example.model;

public class OrderHelperClass {
    String img,user,name,price, quantity, status;
    boolean cancelled;

    public OrderHelperClass() {
    }

    public OrderHelperClass(String img, String user, String name, String price, String quantity, String status, boolean cancelled) {
        this.img = img;
        this.user = user;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.cancelled = cancelled;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
