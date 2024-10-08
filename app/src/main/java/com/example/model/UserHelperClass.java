package com.example.model;

public class UserHelperClass {
    private String name,email,phoneNo,address,username;

    public UserHelperClass(String name, String email, String phoneNo, String address, String username) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.username = username;
    }

    public UserHelperClass(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
