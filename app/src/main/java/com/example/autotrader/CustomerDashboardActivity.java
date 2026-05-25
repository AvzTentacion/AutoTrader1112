package com.example.autotrader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CustomerDashboardActivity extends AppCompatActivity {

    DatabaseHelper db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_dashboard);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        TextView tvWelcome = findViewById(R.id.tvWelcomeCustomer);
        if (tvWelcome != null && username != null) tvWelcome.setText("Welcome, " + username);

        LinearLayout btnViewVehicles  = findViewById(R.id.btnViewVehicles);
        LinearLayout btnViewBookings  = findViewById(R.id.btnViewBookings);
        LinearLayout btnUpdateDetails = findViewById(R.id.btnUpdateDetails);
        MaterialButton btnLogout      = findViewById(R.id.btnLogout);

        btnViewVehicles.setOnClickListener(v ->
                startActivity(new Intent(this, ViewBookingsActivity.class)));

        btnViewBookings.setOnClickListener(v ->
                startActivity(new Intent(this, ViewBookingsActivity.class)));

        btnUpdateDetails.setOnClickListener(v -> {
            int linkedId = db.getLinkedId(username);
            Intent i = new Intent(this, activity_update_details.class);
            i.putExtra("customerID", linkedId);
            startActivity(i);
        });

        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (d, w) -> {
                    Intent i = new Intent(this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmLogout();
    }
}