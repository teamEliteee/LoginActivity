package com.example.simpleloginactivity;

public class User {
    public String email,fullname,age;

    public User(){

    }
    public User(String fullname,String age,String email){
        this.fullname = fullname;
        this.age = age;
        this.email = email;
    }
}
