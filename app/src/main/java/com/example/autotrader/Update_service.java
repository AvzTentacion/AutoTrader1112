package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class Update_service extends AppCompatActivity {
    TextInputEditText etBookingID;
    Spinner spinnerStatus;
    Button btnUpdate, btnBack;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_service);

        db            = new DatabaseHelper(this);
        etBookingID   = findViewById(R.id.etBookingID);
        spinnerStatus = findViewById(R.id.spinnerServiceType);
        btnUpdate     = findViewById(R.id.UpdateBtn);
        btnBack       = findViewById(R.id.backBtn);

        String[] statuses = {"Pending", "In Progress", "Completed"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses));

        btnUpdate.setOnClickListener(v -> {
            String idStr  = etBookingID.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString();

            if (TextUtils.isEmpty(idStr)) { etBookingID.setError("Required"); etBookingID.requestFocus(); return; }

            int id = Integer.parseInt(idStr);
            int result = db.updateBookingStatus(id, status);
            if (result > 0) {
                Toast.makeText(this, "Status updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Booking ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
