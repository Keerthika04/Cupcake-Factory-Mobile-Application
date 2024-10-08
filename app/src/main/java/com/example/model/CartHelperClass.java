package com.example.model;

public class CartHelperClass {
    String img,name,price, quantity;

    public CartHelperClass() {
    }

    public CartHelperClass(String img, String name, String price, String quantity) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}
