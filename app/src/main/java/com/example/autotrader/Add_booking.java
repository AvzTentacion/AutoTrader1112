package com.example.autotrader;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Add_booking extends AppCompatActivity {

    TextInputEditText etServiceType;
    Spinner spinnerStatus, spinnerMechanic;
    Button btnAdd, btnBack;
    MaterialButton btnPickDate;
    TextView tvSelectedDate;
    DatabaseHelper db;
    List<Integer> mechanicIds = new ArrayList<>();
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_booking);

        db              = new DatabaseHelper(this);
        etServiceType   = findViewById(R.id.etServiceType);
        spinnerStatus   = findViewById(R.id.spinnerStatus);
        spinnerMechanic = findViewById(R.id.spinnerMechanic);
        btnAdd          = findViewById(R.id.AddBtn);
        btnBack         = findViewById(R.id.backBtn);
        btnPickDate     = findViewById(R.id.btnPickDate);
        tvSelectedDate  = findViewById(R.id.tvSelectedDate);

        String[] statuses = {"Pending", "In Progress", "Completed"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses));

        loadMechanicsIntoSpinner();

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

        btnAdd.setOnClickListener(v -> {
            String type   = etServiceType.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString();

            if (TextUtils.isEmpty(type)) {
                etServiceType.setError("Required");
                etServiceType.requestFocus();
                return;
            }
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mechanicIds.isEmpty()) {
                Toast.makeText(this, "No mechanics found. Add a mechanic first.", Toast.LENGTH_SHORT).show();
                return;
            }

            String mechanic = spinnerMechanic.getSelectedItem().toString();

            long result = db.addBooking(type, selectedDate, status, mechanic, "");
            if (result != -1) {
                Toast.makeText(this, "Booking added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add booking", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadMechanicsIntoSpinner() {
        List<String> mechanicNames = new ArrayList<>();
        mechanicIds.clear();

        Cursor cursor = db.getAllMechanics();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id      = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_NAME));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
                mechanicIds.add(id);
                mechanicNames.add(name + " - " + spec);
            } while (cursor.moveToNext());
            cursor.close();
        }

        spinnerMechanic.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, mechanicNames));
    }
}