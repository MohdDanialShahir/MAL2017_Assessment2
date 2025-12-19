package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationDetailsActivity extends AppCompatActivity {

    TextView tvName, tvDate, tvTime, tvPax;
    Button btnCancel;
    ImageView btnBack;
    DBHelper DB;
    int reservationId;
    String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        tvName = findViewById(R.id.tvDetailName);
        tvDate = findViewById(R.id.tvDetailDate);
        tvTime = findViewById(R.id.tvDetailTime);
        tvPax = findViewById(R.id.tvDetailPax);
        btnCancel = findViewById(R.id.btnCancelReservation);
        btnBack = findViewById(R.id.btnBackDetails);
        DB = new DBHelper(this);

        // Receive data from the list
        reservationId = getIntent().getIntExtra("ID", -1);
        customerName = getIntent().getStringExtra("NAME");
        tvName.setText(customerName);
        tvDate.setText(getIntent().getStringExtra("DATE"));
        tvTime.setText(getIntent().getStringExtra("TIME"));
        tvPax.setText(String.valueOf(getIntent().getIntExtra("PAX", 0)));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Cancellation");
        builder.setMessage("Are you sure you want to cancel this reservation?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = DB.cancelReservation(reservationId);
                if (result) {
                    // --- NOTIFICATION LOGIC ---
                    // Notify the Guest that their reservation was cancelled
                    String message = "Your reservation on " + tvDate.getText().toString() + " has been cancelled by staff.";
                    DB.insertNotification(customerName, message);

                    Toast.makeText(ReservationDetailsActivity.this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                    finish(); // Close this screen and return to list
                } else {
                    Toast.makeText(ReservationDetailsActivity.this, "Failed to Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
