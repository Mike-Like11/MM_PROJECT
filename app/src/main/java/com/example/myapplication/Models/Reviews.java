package com.example.myapplication.Models;

public class Reviews {
    private String name,description;

    public Reviews() {
    }

    public Reviews(String name, String description) {
        this.name=name;
        this.description=description;
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