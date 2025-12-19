package com.example.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageView btnBack;
    Switch switchNotif;
    Button btnLogout;

    // We use SharedPreferences to save the toggle state permanently
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_NOTIF = "NotificationsEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.btnBackSettings);
        switchNotif = findViewById(R.id.switchNotifications);
        btnLogout = findViewById(R.id.btnLogout);

        // Load saved state
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isEnabled = sharedPreferences.getBoolean(KEY_NOTIF, true); // Default is TRUE (On)
        switchNotif.setChecked(isEnabled);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Save state when toggled
        switchNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_NOTIF, isChecked);
                editor.apply();

                String message = isChecked ? "Notifications Enabled" : "Notifications Disabled";
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Logout Logic
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear stack and go to Login Activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // These flags clear the history so they can't press "Back" to return here
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(SettingsActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}