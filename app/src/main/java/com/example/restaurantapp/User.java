package com.example.restaurantapp;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private int role; // 0 = Guest, 1 = Staff

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    public User(String username, String password, int role, String phone, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }
    
    // Login constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getRole() { return role; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
