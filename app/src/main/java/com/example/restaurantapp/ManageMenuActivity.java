package com.example.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;
    ImageView btnBack;
    DBHelper DB;
    ArrayList<MenuItem> menuList;
    MenuAdapter adapter;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        recyclerView = findViewById(R.id.recyclerViewMenu);
        btnAdd = findViewById(R.id.btnGoToAddItem);
        btnBack = findViewById(R.id.btnBack);

        DB = new DBHelper(this);
        menuList = new ArrayList<>();
        apiService = ApiClient.getClient().create(ApiService.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMenuItemActivity.class);
                startActivity(intent);
            }
        });

        adapter = new MenuAdapter(this, menuList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenuData();
    }

    private void loadMenuData() {
        // Attempt to fetch from API in a background thread (Retrofit does this async)
        Call<List<MenuItem>> call = apiService.getMenu();
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuList.clear();
                    menuList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    
                    // Sync with local DB (optional, but good for offline)
                    // syncToLocalDB(response.body());
                } else {
                    Toast.makeText(ManageMenuActivity.this, "Failed to load menu from API", Toast.LENGTH_SHORT).show();
                    loadFromLocalDB();
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Toast.makeText(ManageMenuActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Fallback to Local DB
                loadFromLocalDB();
            }
        });
    }

    private void loadFromLocalDB() {
        menuList.clear();
        Cursor cursor = DB.getMenu();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Menu Items Found (Local)", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String image = cursor.getString(3);
                menuList.add(new MenuItem(name, price, image));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
