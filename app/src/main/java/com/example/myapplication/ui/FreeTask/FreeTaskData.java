package com.example.myapplication.ui.FreeTask;

public class FreeTaskData {
    private String name,req, name_narrator, adress;

    public FreeTaskData(String name,String req,String email_narrator, String adress){
        this.name=name;
        this.req=req;
        this.name_narrator=email_narrator;
        this.adress=adress;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getReq() {
        return this.req;
    }

    public void setReq(String req) {
        this.req = req;
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
}

