package com.example.myapplication.Models;

public class User {
    private  String name,phone,pass,email;
    int task_done,task_not_done,rating;

    public User() {
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User(String name, String phone, String pass, String nunber) {
        this.name = name;
        this.phone= phone;
        this.pass = pass;
        this.email = nunber;
        this.task_done=0;
        this.task_not_done=0;
        this.rating=50;
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

    public int getTask_not_done() {
        return task_not_done;
    }

    public void setTask_not_done(int task_not_done) {
        this.task_not_done = task_not_done;
    }

    public int getTask_done() {
        return task_done;
    }

    public void setTask_done(int task_done) {
        this.task_done = task_done;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
