package com.example.r2snote.DTO;

public class User {
    private String username;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public User() {

    }

    public User(String id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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
