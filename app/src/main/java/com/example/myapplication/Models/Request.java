package com.example.myapplication.Models;

public class Request {
    private String name_1, name_2,task , description,address,data;
    public Request(){

    }
    public Request(String name_1, String name_2, String task, String description,String address, String data) {
        this.name_1 = name_1;
        this.name_2 = name_2;
        this.task = task;
        this.description = description;
        this.address = address;
        this.data = data;
    }
    public String getName_1() {
        return name_1;
    }

    public void setName_1(String name_1) {
        this.name_1 = name_1;
    }

    public String getName_2() {
        return name_2;
    }

    public void setName_2(String name_2) {
        this.name_2 = name_2;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
