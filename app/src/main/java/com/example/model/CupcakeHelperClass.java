package com.example.model;

public class CupcakeHelperClass {
    private String cupcakeImage, cupcakeName, cupcakeCategory, cupcakePrice, cupcakeIngredients;

    public CupcakeHelperClass(String cupcakeImage, String cupcakeName, String cupcakeCategory, String cupcakePrice, String cupcakeIngredients) {
        this.cupcakeImage = cupcakeImage;
        this.cupcakeName = cupcakeName;
        this.cupcakeCategory = cupcakeCategory;
        this.cupcakePrice = cupcakePrice;
        this.cupcakeIngredients = cupcakeIngredients;
    }

    public CupcakeHelperClass(){
    }

    public String getCupcakeImage() {
        return cupcakeImage;
    }

    public void setCupcakeImage(String cupcakeImage) {
        this.cupcakeImage = cupcakeImage;
    }

    public String getCupcakeName() {
        return cupcakeName;
    }

    public void setCupcakeName(String cupcakeName) {
        this.cupcakeName = cupcakeName;
    }

    public String getCupcakeCategory() {
        return cupcakeCategory;
    }

    public void setCupcakeCategory(String cupcakeCategory) {
        this.cupcakeCategory = cupcakeCategory;
    }

    public String getCupcakePrice() {
        return cupcakePrice;
    }

    public void setCupcakePrice(String cupcakePrice) {
        this.cupcakePrice = cupcakePrice;
    }

    public String getCupcakeIngredients() {
        return cupcakeIngredients;
    }

    public void setCupcakeIngredients(String cupcakeIngredients) {
        this.cupcakeIngredients = cupcakeIngredients;
    }
}