package com.example.restaurantapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenuItemActivity extends AppCompatActivity {

    EditText etName, etPrice;
    Button btnSave, btnSelect;
    ImageView imgPreview;
    DBHelper DB;
    Uri selectedImageUri = null;
    private static final int PICK_IMAGE_REQUEST = 100;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        etName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etItemPrice);
        btnSave = findViewById(R.id.btnSaveItem);
        btnSelect = findViewById(R.id.btnSelectImage);
        imgPreview = findViewById(R.id.imgPreview);
        DB = new DBHelper(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String priceStr = etPrice.getText().toString();

                if(name.equals("") || priceStr.equals("")) {
                    Toast.makeText(AddMenuItemActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    double price = Double.parseDouble(priceStr);
                    String imagePath = (selectedImageUri != null) ? selectedImageUri.toString() : "";

                    // Create MenuItem object
                    MenuItem newItem = new MenuItem(name, price, imagePath);

                    // 1. Try to save to API first
                    Call<MenuItem> call = apiService.addMenuItem(newItem);
                    call.enqueue(new Callback<MenuItem>() {
                        @Override
                        public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                            if (response.isSuccessful()) {
                                // API Success -> Also save to Local DB for offline sync
                                DB.insertMenuItem(name, price, imagePath);
                                Toast.makeText(AddMenuItemActivity.this, "Item Added (Synced to API)", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // API Failed -> Save Locally Only
                                DB.insertMenuItem(name, price, imagePath);
                                Toast.makeText(AddMenuItemActivity.this, "API Failed (" + response.code() + "), Saved Locally", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<MenuItem> call, Throwable t) {
                            // Network Error -> Save Locally Only
                            DB.insertMenuItem(name, price, imagePath);
                            Toast.makeText(AddMenuItemActivity.this, "Network Error, Saved Locally", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgPreview.setImageURI(selectedImageUri);
        }
    }
}
