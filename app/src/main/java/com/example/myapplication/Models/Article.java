package com.example.myapplication.Models;

public class Article {
    private String name,description,name_narrator;

    public Article() {
    }

    public Article(String name,String description,String email_narrator) {
        this.name = name;
        this.description=description;
        this.name_narrator=email_narrator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_narrator() {
        return name_narrator;
    }

    public void setName_narrator(String email_narrator) {
        this.name_narrator = email_narrator;
    }
}
