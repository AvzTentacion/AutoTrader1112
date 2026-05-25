package com.example.autotrader;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class activity_edit_booking extends AppCompatActivity {

    TextInputEditText etServiceType;
    Spinner spinnerStatus, spinnerMechanic;
    MaterialButton btnSave, btnBack, btnPickDate;
    TextView tvSelectedDate;
    DatabaseHelper db;
    int bookingId;
    String selectedDate = "";
    List<String> mechanicNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_booking);

        db        = new DatabaseHelper(this);
        bookingId = getIntent().getIntExtra("bookingID", -1);

        etServiceType   = findViewById(R.id.etServiceType);
        spinnerStatus   = findViewById(R.id.spinnerStatus);
        spinnerMechanic = findViewById(R.id.spinnerMechanic);
        btnSave         = findViewById(R.id.btnSaveBooking);
        btnBack         = findViewById(R.id.btnBack);
        btnPickDate     = findViewById(R.id.btnPickDate);
        tvSelectedDate  = findViewById(R.id.tvSelectedDate);

        // Set up status spinner
        String[] statuses = {"Pending", "In Progress", "Completed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerStatus.setAdapter(statusAdapter);

        // Load mechanics into spinner
        loadMechanicsIntoSpinner();

        // Pre-fill values from intent
        etServiceType.setText(getIntent().getStringExtra("serviceType"));

        // Pre-fill date
        String existingDate = getIntent().getStringExtra("date");
        if (existingDate != null && !existingDate.isEmpty()) {
            selectedDate = existingDate;
            tvSelectedDate.setText("Selected: " + selectedDate);
            btnPickDate.setText(selectedDate);
        }

        // Pre-select status
        String currentStatus = getIntent().getStringExtra("status");
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(currentStatus)) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        // Pre-select mechanic
        String currentMechanic = getIntent().getStringExtra("mechanic");
        for (int i = 0; i < mechanicNames.size(); i++) {
            if (mechanicNames.get(i).contains(currentMechanic)) {
                spinnerMechanic.setSelection(i);
                break;
            }
        }

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year  = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day   = cal.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDate = y + "-" + String.format("%02d", m + 1) + "-" + String.format("%02d", d);
                tvSelectedDate.setText("Selected: " + selectedDate);
                btnPickDate.setText(selectedDate);
            }, year, month, day).show();
        });

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String type     = etServiceType.getText().toString().trim();
            String status   = spinnerStatus.getSelectedItem().toString();
            String mechanic = spinnerMechanic.getSelectedItem().toString();

            if (TextUtils.isEmpty(type)) {
                etServiceType.setError("Required");
                etServiceType.requestFocus();
                return;
            }
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            int result = db.updateBooking(bookingId, type, selectedDate, status, mechanic, "");
            if (result > 0) {
                Toast.makeText(this, "Booking updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMechanicsIntoSpinner() {
        mechanicNames.clear();

        Cursor cursor = db.getAllMechanics();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_NAME));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
                mechanicNames.add(name + " - " + spec);
            } while (cursor.moveToNext());
            cursor.close();
        }

        spinnerMechanic.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, mechanicNames));
    }
}