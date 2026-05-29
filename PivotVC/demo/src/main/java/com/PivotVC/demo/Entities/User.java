package com.PivotVC.demo.Entities;

public class User {
    private String name;
    private String emailId;
    public User(String name,String emailId){
        this.name=name;
        this.emailId=emailId;
    }
    public String getName() {
        return name;
    }
    public String getEmailId(){return emailId;}
    public void setEmailId(String emailId){
        this.emailId=emailId;
    }
    public void setName(String name) {
        this.name = name;
    }
}
