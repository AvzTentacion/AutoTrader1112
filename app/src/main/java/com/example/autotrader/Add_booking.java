package com.example.autotrader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class Add_booking extends AppCompatActivity {
    TextInputEditText etServiceType, etBookingDate, etMechanic, etVehicle;
    Spinner spinnerStatus;
    Button btnAdd, btnBack;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_booking);

        db             = new DatabaseHelper(this);
        etServiceType  = findViewById(R.id.etServiceType);
        etBookingDate  = findViewById(R.id.etBookingDate);
        etMechanic     = findViewById(R.id.etMechanic);
        etVehicle      = findViewById(R.id.etVehicle);
        spinnerStatus  = findViewById(R.id.spinnerStatus);
        btnAdd         = findViewById(R.id.AddBtn);
        btnBack        = findViewById(R.id.backBtn);

        String[] statuses = {"Pending", "In Progress", "Completed"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses));

        btnAdd.setOnClickListener(v -> {
            String type    = etServiceType.getText().toString().trim();
            String date    = etBookingDate.getText().toString().trim();
            String mech    = etMechanic.getText().toString().trim();
            String vehicle = etVehicle.getText().toString().trim();
            String status  = spinnerStatus.getSelectedItem().toString();

            if (TextUtils.isEmpty(type))    { etServiceType.setError("Required"); etServiceType.requestFocus(); return; }
            if (TextUtils.isEmpty(date))    { etBookingDate.setError("Required"); etBookingDate.requestFocus(); return; }
            if (TextUtils.isEmpty(mech))    { etMechanic.setError("Required"); etMechanic.requestFocus(); return; }
            if (TextUtils.isEmpty(vehicle)) { etVehicle.setError("Required"); etVehicle.requestFocus(); return; }

            long result = db.addBooking(type, date, status, mech, vehicle);
            if (result != -1) {
                Toast.makeText(this, "Booking added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add booking", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
