package com.example.myapplication.Models;

import java.util.ArrayList;

public class Article {
    private String name,description,name_narrator;
    private int like=0;
    private boolean like_or_not=false;

    public Article() {
    }

    public Article(String name,String description,String email_narrator) {
        this.name = name;
        this.description=description;
        this.name_narrator=email_narrator;

    }

    public boolean isLike_or_not() {
        return like_or_not;
    }

    public void setLike_or_not(boolean like_or_not) {
        this.like_or_not = like_or_not;
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
    public boolean user_s(String name_narrator,String user){
        if(name_narrator.equals(user)){
            return true;
        }
        else{
            return false;
        }
    }



    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
