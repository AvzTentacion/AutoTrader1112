package com.example.autotrader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MechanicDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanic_dashboard);

        String username = getIntent().getStringExtra("username");
        TextView tvWelcome = findViewById(R.id.tvWelcomeMechanic);
        if (tvWelcome != null && username != null) tvWelcome.setText("Welcome, " + username);

        LinearLayout btnCreateBooking = findViewById(R.id.btnCreateBooking);
        LinearLayout btnUpdateStatus  = findViewById(R.id.btnUpdateStatus);
        LinearLayout btnViewAssigned  = findViewById(R.id.btnViewAssigned);
        MaterialButton btnLogout      = findViewById(R.id.btnLogout);

        btnCreateBooking.setOnClickListener(v -> startActivity(new Intent(this, Add_booking.class)));
        btnUpdateStatus.setOnClickListener(v -> startActivity(new Intent(this, Update_service.class)));
        btnViewAssigned.setOnClickListener(v -> startActivity(new Intent(this, View_Booking.class)));
        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this).setTitle("Logout").setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (d, w) -> {
                    Intent i = new Intent(this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i); finish();
                }).setNegativeButton("Cancel", null).show();
    }

    @Override public void onBackPressed() { confirmLogout(); }
}
