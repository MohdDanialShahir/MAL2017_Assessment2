package com.example.restaurantapp;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    @SerializedName("id")
    private int id;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("partySize")
    private int partySize;

    @SerializedName("status")
    private String status;

    public Reservation(int id, String customerName, String date, String time, int partySize, String status) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.status = status;
    }

    public Reservation(String customerName, String date, String time, int partySize, String status) {
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getPartySize() { return partySize; }
    public void setPartySize(int partySize) { this.partySize = partySize; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
