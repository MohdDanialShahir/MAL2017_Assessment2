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

public class StaffDashboardActivity extends AppCompatActivity {

    Button btnMenu, btnReservations, btnSettings;
    ImageView imgBell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // Request Notification Permission explicitly on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btnMenu = findViewById(R.id.btnManageMenu);
        btnReservations = findViewById(R.id.btnManageReservations);
        btnSettings = findViewById(R.id.btnSettings);
        imgBell = findViewById(R.id.imgNotificationBell);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageMenuActivity.class);
                startActivity(intent);
            }
        });

        btnReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageReservationsActivity.class);
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
        
        // Bell Icon Logic - Show Unread Notifications
        imgBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(StaffDashboardActivity.this, NotificationActivity.class);
                 intent.putExtra("RECIPIENT", "STAFF");
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
        // Check for unread notifications for 'STAFF' or 'ALL'
        Cursor cursor = DB.getUnreadNotifications("STAFF");
        if (cursor.getCount() > 0) {
            // Trigger the "Pop up" notification
            if(cursor.moveToFirst()) {
                String message = cursor.getString(2); // Index 2 is message
                NotificationHelper.sendNotification(this, "New Update", message);
            }
        }
    }
}
