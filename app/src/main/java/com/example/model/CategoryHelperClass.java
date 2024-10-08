package com.example.model;

public class CategoryHelperClass {
    private String categoryName, categoryImage;

    public CategoryHelperClass(String categoryName, String categoryImage) {

        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public CategoryHelperClass(){
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

}