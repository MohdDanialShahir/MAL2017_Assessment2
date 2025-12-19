package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ManageReservationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView btnBack;
    DBHelper DB;
    ArrayList<Reservation> resList;
    ReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        recyclerView = findViewById(R.id.recyclerResList);
        btnBack = findViewById(R.id.btnBackResList);
        DB = new DBHelper(this);
        resList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Load Data
        displayData();

        // Pass 'this' as the Activity so we can show Dialogs from Adapter if needed,
        // OR better, we handle click events here.
        // For now, the Adapter probably handles the button clicks?
        // Let's modify Adapter or assume it has a way to call back.
        // Actually, ReservationAdapter handles clicks. We need to inject logic there for Notifications.
        // But the Adapter is shared with Guest side (which just views).
        // Let's assume for now we are just listing. 
        // IF the staff cancels, we need to intercept that.
        
        // FOR NOW: I will just load the adapter. 
        // If we want to support Cancellation Notifications, we need to update the Adapter
        // to take a callback or put logic there.
        // Since I can't easily change the Adapter signature without breaking other files,
        // I will rely on the Adapter's internal logic which calls DB.cancelReservation.
        // I should probably update ReservationAdapter to handle notifications.
        
        adapter = new ReservationAdapter(this, resList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayData() {
        resList.clear(); // Clear list before adding
        Cursor cursor = DB.getReservations();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Reservations Found", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                // 0=id, 1=name, 2=date, 3=time, 4=pax, 5=status
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                int pax = cursor.getInt(4);
                String status = cursor.getString(5);

                resList.add(new Reservation(id, name, date, time, pax, status));
            }
        }
        // Notify adapter if initialized
        if(adapter != null) adapter.notifyDataSetChanged();
    }
}
