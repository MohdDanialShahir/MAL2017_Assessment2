package com.example.restaurantapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MakeReservationActivity extends AppCompatActivity {

    EditText dateInput, timeInput, partyInput;
    Button btnConfirm;
    ImageView btnBack;
    DBHelper DB;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        // Get the username passed from the Dashboard
        currentUsername = getIntent().getStringExtra("USERNAME");

        // Initialize Views
        dateInput = findViewById(R.id.etResDate);
        timeInput = findViewById(R.id.etResTime);
        partyInput = findViewById(R.id.etPartySize);
        btnConfirm = findViewById(R.id.btnSubmitRes);
        btnBack = findViewById(R.id.btnBackRes);

        DB = new DBHelper(this);

        // --- BACK BUTTON LOGIC ---
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Goes back to Guest Dashboard
            }
        });

        // 1. Date Picker Logic
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MakeReservationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                dateInput.setText(d + "/" + (m + 1) + "/" + y);
                            }
                        }, year, month, day);
                dialog.show();
            }
        });

        // 2. Time Picker Logic
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(MakeReservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int m) {
                                timeInput.setText(String.format("%02d:%02d", h, m));
                            }
                        }, hour, minute, true);
                dialog.show();
            }
        });

        // 3. Submit Button Logic
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d = dateInput.getText().toString();
                String t = timeInput.getText().toString();
                String p = partyInput.getText().toString();

                if(d.equals("") || t.equals("") || p.equals("")) {
                    Toast.makeText(MakeReservationActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                    // Show confirmation popup as per request
                    showConfirmationDialog(d, t, p);
                }
            }
        });
    }

    private void showConfirmationDialog(String date, String time, String partySize) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm your reservation?")
                .setMessage("Date: " + date + "\nTime: " + time + "\nParty Size: " + partySize)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveReservation(date, time, partySize);
                    }
                })
                .setNegativeButton("Edit", null) // Closes dialog, stays on screen to edit
                .show();
    }

    private void saveReservation(String date, String time, String partySizeStr) {
        int partySize = Integer.parseInt(partySizeStr);
        Boolean checkInsert = DB.insertReservation(currentUsername, date, time, partySize);

        if(checkInsert) {
            
            // --- NOTIFICATION LOGIC ---
            // Notify STAFF that a new reservation is made
            DB.insertNotification("STAFF", "New Reservation by " + currentUsername + " on " + date + " at " + time);

            // Show success popup
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Your reservation has been submitted")
                    .setPositiveButton("Go to Reservation page", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); // Close this activity
                        }
                    })
                    .show();
        } else {
            Toast.makeText(MakeReservationActivity.this, "Error booking table", Toast.LENGTH_SHORT).show();
        }
    }
}
