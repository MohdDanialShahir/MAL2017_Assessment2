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

public class GuestMenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView btnBack;
    DBHelper DB;
    ArrayList<MenuItem> menuList;
    GuestMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        recyclerView = findViewById(R.id.recyclerGuestMenu);
        btnBack = findViewById(R.id.btnBackGuest);
        DB = new DBHelper(this);
        menuList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Load Data
        Cursor cursor = DB.getMenu();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Menu is empty", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String image = cursor.getString(3);
                menuList.add(new MenuItem(name, price, image));
            }
        }

        adapter = new GuestMenuAdapter(this, menuList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}