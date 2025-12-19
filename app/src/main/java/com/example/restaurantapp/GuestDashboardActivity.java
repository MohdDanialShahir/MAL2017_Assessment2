package com.example.restaurantapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GuestDashboardActivity extends AppCompatActivity {

    Button btnMenu, btnReservations, btnSettings;
    ImageView imgBell;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        // Request Notification Permission explicitly on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        currentUsername = getIntent().getStringExtra("USERNAME");

        btnMenu = findViewById(R.id.btnGuestMenu);
        btnReservations = findViewById(R.id.btnReservations);
        btnSettings = findViewById(R.id.btnGuestSettings);
        imgBell = findViewById(R.id.imgNotificationBell);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GuestMenuActivity.class);
                startActivity(intent);
            }
        });

        btnReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyReservationsActivity.class);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        
        // Bell Logic
        imgBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(GuestDashboardActivity.this, NotificationActivity.class);
                 intent.putExtra("RECIPIENT", currentUsername);
                 startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifications();
    }
    
    private void checkNotifications() {
        DBHelper DB = new DBHelper(this);
        // Check for unread notifications for this User
        Cursor cursor = DB.getUnreadNotifications(currentUsername);
        if (cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                String message = cursor.getString(2);
                NotificationHelper.sendNotification(this, "Reservation Update", message);
            }
        }
    }
}
