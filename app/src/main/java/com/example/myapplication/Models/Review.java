package com.example.myapplication.Models;

public class Review {
    private String name,description;
    private int rating;

    public Review() {
    }

    public Review(String name, String description, int rating) {
        this.rating = rating;
        this.name=name;
        this.description=description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
