package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class activity_edit_booking extends AppCompatActivity {

    TextInputEditText etServiceType, etDate, etMechanic, etVehicle;
    Spinner spinnerStatus;
    MaterialButton btnSave, btnBack;
    DatabaseHelper db;
    int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_booking);

        db        = new DatabaseHelper(this);
        bookingId = getIntent().getIntExtra("bookingID", -1);

        etServiceType = findViewById(R.id.etServiceType);
        etDate        = findViewById(R.id.etBookingDate);
        etMechanic    = findViewById(R.id.etMechanic);
        etVehicle     = findViewById(R.id.etVehicle);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave       = findViewById(R.id.btnSaveBooking);
        btnBack       = findViewById(R.id.btnBack);

        String[] statuses = {"Pending", "In Progress", "Completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerStatus.setAdapter(adapter);

        // Pre-fill values
        etServiceType.setText(getIntent().getStringExtra("serviceType"));
        etDate.setText(getIntent().getStringExtra("date"));
        etMechanic.setText(getIntent().getStringExtra("mechanic"));
        etVehicle.setText(getIntent().getStringExtra("vehicle"));

        String currentStatus = getIntent().getStringExtra("status");
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(currentStatus)) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String type    = etServiceType.getText().toString().trim();
            String date    = etDate.getText().toString().trim();
            String mech    = etMechanic.getText().toString().trim();
            String vehicle = etVehicle.getText().toString().trim();
            String status  = spinnerStatus.getSelectedItem().toString();

            if (TextUtils.isEmpty(type))    { etServiceType.setError("Required"); etServiceType.requestFocus(); return; }
            if (TextUtils.isEmpty(date))    { etDate.setError("Required"); etDate.requestFocus(); return; }
            if (TextUtils.isEmpty(mech))    { etMechanic.setError("Required"); etMechanic.requestFocus(); return; }
            if (TextUtils.isEmpty(vehicle)) { etVehicle.setError("Required"); etVehicle.requestFocus(); return; }

            int result = db.updateBooking(bookingId, type, date, status, mech, vehicle);
            if (result > 0) {
                Toast.makeText(this, "Booking updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}