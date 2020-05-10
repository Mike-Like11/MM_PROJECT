package com.example.myapplication.Models;

import java.util.Date;

public class Message {
    public String name,text;
   private long time;

    public String getName() {
        return name;
    }

    public Message() {
    }
    public Message(String name,String text){
        this.name=name;
        this.text=text;
        time= new Date().getTime();
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
