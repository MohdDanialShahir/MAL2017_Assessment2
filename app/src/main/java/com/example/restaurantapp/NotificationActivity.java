package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView btnBack;
    DBHelper DB;
    ArrayList<Notification> notifList;
    NotificationAdapter adapter;
    String recipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recipient = getIntent().getStringExtra("RECIPIENT");
        
        recyclerView = findViewById(R.id.recyclerNotifications);
        btnBack = findViewById(R.id.btnBackNotif);
        DB = new DBHelper(this);
        notifList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Load notifications
        loadNotifications();

        adapter = new NotificationAdapter(this, notifList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Mark all as read when opening this screen
        DB.markNotificationsAsRead(recipient);
    }
    
    private void loadNotifications() {
        Cursor cursor = DB.getNotifications(recipient);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Notifications", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                // 0=id, 1=recipient, 2=message, 3=isRead
                int id = cursor.getInt(0);
                String msg = cursor.getString(2);
                int isRead = cursor.getInt(3);
                notifList.add(new Notification(id, msg, isRead));
            }
        }
    }
}
