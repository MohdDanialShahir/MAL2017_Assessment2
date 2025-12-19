package com.example.restaurantapp;

public class Notification {
    private int id;
    private String message;
    private int isRead;

    public Notification(int id, String message, int isRead) {
        this.id = id;
        this.message = message;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public String getMessage() { return message; }
    public int getIsRead() { return isRead; }
}
