package com.example.android.venessavisintry_1202150247_modul66.model;


public class User {
    String userID;
    String username;
    String email;

    public User(){}

    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
