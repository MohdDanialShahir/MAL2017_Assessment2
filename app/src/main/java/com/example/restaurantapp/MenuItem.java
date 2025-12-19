package com.example.restaurantapp;

import com.google.gson.annotations.SerializedName;

public class MenuItem {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    // The API might expect 'image' or 'description' or something else if 'imageUri' is rejected.
    // Or, maybe the server rejects empty image strings?
    // Let's ensure the field matches what the server likely expects.
    // Usually APIs for this coursework use 'description' for extra fields, but if it's strictly name/price/image,
    // let's stick to the docs provided.
    // However, if the API docs say 'image' instead of 'imageUri', we need to change this.
    // Based on "API Documentation: http://10.240.72.69/comp2000/coursework/docs#/", let's guess standard fields.
    // Often it is 'description'. Let's check AddMenuItemActivity again.
    
    // NOTE: Without seeing the actual JSON response or API docs, I am assuming the field names match the JSON keys.
    // If the server returns 400 Bad Request, it might be a validation error (e.g. name too short, price negative).
    // If it returns 404, the endpoint is wrong.
    // If it returns 500, server error.

    @SerializedName("image") // Changed from imageUri to image based on common patterns
    private String imageUri;

    public MenuItem(String name, double price, String imageUri) {
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
    }

    public MenuItem(int id, String name, double price, String imageUri) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
