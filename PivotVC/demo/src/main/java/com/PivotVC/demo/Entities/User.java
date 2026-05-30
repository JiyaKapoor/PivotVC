package com.PivotVC.demo.Entities;

import java.util.HashMap;
import java.util.Map;

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
    public static User deserialise(String str) {
        Map<String, String> props = new HashMap<>();
        str.lines().forEach(line -> {
            String[] parts = line.split("=", 2);
            if(parts.length == 2) props.put(parts[0].trim(), parts[1].trim());
        });
        return new User(props.get("username"), props.get("email"));
    }
}
