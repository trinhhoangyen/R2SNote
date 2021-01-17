package com.example.r2snote.DTO;

public class User {
    private String username, password, id;
    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}
