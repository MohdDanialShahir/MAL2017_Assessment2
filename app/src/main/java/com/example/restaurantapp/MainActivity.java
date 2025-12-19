package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;
    TextView tvRegister;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Setup Notification Channel on App Start
        NotificationHelper.createNotificationChannel(this);

        username = findViewById(R.id.etLoginUsername);
        password = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvGoToRegister);
        DB = new DBHelper(this);

        // Login Logic
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                    if(checkuserpass) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Check Role to redirect correctly
                        int role = DB.getUserRole(user);
                        if(role == 1) {
                            // Go to Staff Dashboard
                            Intent intent = new Intent(getApplicationContext(), StaffDashboardActivity.class);
                            // Pass username so we know who is logged in
                            intent.putExtra("USERNAME", user);
                            startActivity(intent);
                        } else {
                            // Go to Guest Dashboard
                            Intent intent = new Intent(getApplicationContext(), GuestDashboardActivity.class);
                            intent.putExtra("USERNAME", user);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Go to Register Page
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
