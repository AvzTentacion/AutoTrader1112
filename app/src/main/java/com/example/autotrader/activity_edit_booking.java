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
    TextView tvSelectedDate;
    Spinner spinnerStatus, spinnerVehicle, spinnerMechanic;
    MaterialButton btnSave, btnBack, btnPickDate;
    DatabaseHelper db;
    int bookingId;
    List<String> vehicleList = new ArrayList<>();
    List<String> mechanicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_booking);

        db        = new DatabaseHelper(this);
        bookingId = getIntent().getIntExtra("bookingID", -1);

        etServiceType  = findViewById(R.id.etServiceType);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        spinnerStatus  = findViewById(R.id.spinnerStatus);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        spinnerMechanic= findViewById(R.id.spinnerMechanic);
        btnSave        = findViewById(R.id.btnSaveBooking);
        btnBack        = findViewById(R.id.btnBack);
        btnPickDate    = findViewById(R.id.btnPickDate);

        // Load statuses
        String[] statuses = {"Pending", "In Progress", "Completed"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses));

        // Load vehicles and mechanics
        loadVehicles();
        loadMechanics();

        // Pre-fill values
        etServiceType.setText(getIntent().getStringExtra("serviceType"));
        tvSelectedDate.setText(getIntent().getStringExtra("date"));

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
        for (int i = 0; i < mechanicList.size(); i++) {
            if (mechanicList.get(i).contains(currentMechanic)) {
                spinnerMechanic.setSelection(i);
                break;
            }
        }

        // Pre-select vehicle
        String currentVehicle = getIntent().getStringExtra("vehicle");
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).contains(currentVehicle)) {
                spinnerVehicle.setSelection(i);
                break;
            }
        }

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                String date = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
                tvSelectedDate.setText(date);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String type    = etServiceType.getText().toString().trim();
            String date    = tvSelectedDate.getText().toString().trim();
            String vehicle = spinnerVehicle.getSelectedItem() != null
                    ? spinnerVehicle.getSelectedItem().toString() : "";
            String mech    = spinnerMechanic.getSelectedItem() != null
                    ? spinnerMechanic.getSelectedItem().toString() : "";
            String status  = spinnerStatus.getSelectedItem().toString();

            if (TextUtils.isEmpty(type))                 { Toast.makeText(this, "Enter service type", Toast.LENGTH_SHORT).show(); return; }
            if (date.equals("No date selected"))         { Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show(); return; }
            if (vehicle.equals("No vehicles available")) { Toast.makeText(this, "No vehicles available", Toast.LENGTH_SHORT).show(); return; }
            if (mech.equals("No mechanics available"))   { Toast.makeText(this, "No mechanics available", Toast.LENGTH_SHORT).show(); return; }

            int result = db.updateBooking(bookingId, type, date, status, mech, vehicle);
            if (result > 0) {
                Toast.makeText(this, "Booking updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadVehicles() {
        vehicleList.clear();
        Cursor cursor = db.getAllVehicles();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODEL));
                String reg   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REG));
                vehicleList.add(brand + " " + model + " (" + reg + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (vehicleList.isEmpty()) vehicleList.add("No vehicles available");
        spinnerVehicle.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, vehicleList));
    }

    private void loadMechanics() {
        mechanicList.clear();
        Cursor cursor = db.getAllMechanics();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_NAME));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
                mechanicList.add(name + " (" + spec + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (mechanicList.isEmpty()) mechanicList.add("No mechanics available");
        spinnerMechanic.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, mechanicList));
    }
}