package com.example.myapplication.Models;

public class Rating {
    private String name;
    private Integer rating;

    public Rating() {
    }

    public Rating(String name, Integer rating) {
        this.name=name;
        this.rating=rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating=rating;
    }
}
