package com.example.restaurantapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- MENU ENDPOINTS ---
    // Changed 'menu' to 'menus' based on 404 error
    @GET("menus")
    Call<List<MenuItem>> getMenu();

    @POST("menus")
    Call<MenuItem> addMenuItem(@Body MenuItem menuItem);

    @DELETE("menus/{id}")
    Call<Void> deleteMenuItem(@Path("id") int id);

    // --- RESERVATION ENDPOINTS ---
    @GET("reservations")
    Call<List<Reservation>> getReservations();

    @GET("reservations")
    Call<List<Reservation>> getUserReservations(@Query("customerName") String customerName);

    @POST("reservations")
    Call<Reservation> createReservation(@Body Reservation reservation);

    @DELETE("reservations/{id}")
    Call<Void> cancelReservation(@Path("id") int id);

    // --- USER/AUTH ENDPOINTS ---
    @POST("users/register")
    Call<Void> registerUser(@Body User user);

    @POST("users/login")
    Call<User> loginUser(@Body User user);
}
