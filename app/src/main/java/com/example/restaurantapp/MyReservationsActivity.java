package com.example.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyReservationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout layoutNoReservation;
    Button btnMakeResFromEmpty;
    ImageView btnBack;
    DBHelper DB;
    ArrayList<Reservation> resList;
    ReservationAdapter adapter;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        // Get the logged-in username so we only show THEIR bookings
        currentUsername = getIntent().getStringExtra("USERNAME");

        recyclerView = findViewById(R.id.recyclerMyRes);
        layoutNoReservation = findViewById(R.id.layoutNoReservation);
        btnMakeResFromEmpty = findViewById(R.id.btnMakeResFromEmpty);
        btnBack = findViewById(R.id.btnBackMyRes);
        DB = new DBHelper(this);
        resList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // "Make a Reservation" button from the empty state
        btnMakeResFromEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyReservationsActivity.this, MakeReservationActivity.class);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
            }
        });

        // We can reuse the same Adapter we built for the Staff!
        adapter = new ReservationAdapter(this, resList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayData();
    }

    private void displayData() {
        resList.clear();
        // CALL THE SPECIFIC METHOD FOR USER RESERVATIONS
        Cursor cursor = DB.getReservationsForUser(currentUsername);

        if(cursor.getCount() == 0) {
            // Show "No Reservation" layout, hide RecyclerView
            recyclerView.setVisibility(View.GONE);
            layoutNoReservation.setVisibility(View.VISIBLE);
        } else {
            // Show RecyclerView, hide "No Reservation" layout
            recyclerView.setVisibility(View.VISIBLE);
            layoutNoReservation.setVisibility(View.GONE);

            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                int pax = cursor.getInt(4);
                String status = cursor.getString(5);

                resList.add(new Reservation(id, name, date, time, pax, status));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
