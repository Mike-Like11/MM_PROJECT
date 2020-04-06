package com.example.myapplication.Models;

public class User {
    private  String name,phone,pass,email;

    public User() {
    }

    public User(String name, String phone, String pass, String nunber) {
        this.name = name;
        this.phone= phone;
        this.pass = pass;
        this.email = nunber;
    }

    public String getDisplayName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
