package com.example.autotrader;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddVehicleActivity extends AppCompatActivity {

    TextInputEditText etBrand, etModel, etReg, etYear;
    Spinner spinnerCustomer;
    MaterialButton btnSave, btnBack;
    DatabaseHelper db;
    List<Integer> customerIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_vehicle);

        db            = new DatabaseHelper(this);
        etBrand       = findViewById(R.id.etBrand);
        etModel       = findViewById(R.id.etModel);
        etReg         = findViewById(R.id.etReg);
        etYear        = findViewById(R.id.etYear);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        btnSave       = findViewById(R.id.btnSaveVehicle);
        btnBack       = findViewById(R.id.btnBack);

        loadCustomersIntoSpinner();

        btnSave.setOnClickListener(v -> {
            String brand = etBrand.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String reg   = etReg.getText().toString().trim();
            String year  = etYear.getText().toString().trim();

            if (TextUtils.isEmpty(brand)) { etBrand.setError("Required"); etBrand.requestFocus(); return; }
            if (TextUtils.isEmpty(model)) { etModel.setError("Required"); etModel.requestFocus(); return; }
            if (TextUtils.isEmpty(reg))   { etReg.setError("Required"); etReg.requestFocus(); return; }
            if (TextUtils.isEmpty(year))  { etYear.setError("Required"); etYear.requestFocus(); return; }
            if (customerIds.isEmpty())    { Toast.makeText(this, "No customers found. Add a customer first.", Toast.LENGTH_SHORT).show(); return; }

            int ownerID = customerIds.get(spinnerCustomer.getSelectedItemPosition());
            long result = db.addVehicle(brand, model, reg, year, ownerID);
            if (result != -1) {
                Toast.makeText(this, "Vehicle added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add vehicle", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCustomersIntoSpinner() {
        List<String> customerNames = new ArrayList<>();
        customerIds.clear();

        Cursor cursor = db.getAllCustomers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id       = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ID));
                String name  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME));
                String sname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_SNAME));
                customerIds.add(id);
                customerNames.add(name + " " + sname + " (ID: " + id + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, customerNames);
        spinnerCustomer.setAdapter(adapter);
    }
}