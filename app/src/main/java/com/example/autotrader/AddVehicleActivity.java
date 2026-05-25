package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddVehicleActivity extends AppCompatActivity {
    TextInputEditText etBrand, etModel, etReg, etYear, etOwnerID;
    MaterialButton btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_vehicle);

        db        = new DatabaseHelper(this);
        etBrand   = findViewById(R.id.etBrand);
        etModel   = findViewById(R.id.etModel);
        etReg     = findViewById(R.id.etReg);
        etYear    = findViewById(R.id.etYear);
        etOwnerID = findViewById(R.id.etOwnerID);
        btnSave   = findViewById(R.id.btnSaveVehicle);

        btnSave.setOnClickListener(v -> {
            String brand   = etBrand.getText().toString().trim();
            String model   = etModel.getText().toString().trim();
            String reg     = etReg.getText().toString().trim();
            String year    = etYear.getText().toString().trim();
            String ownerStr= etOwnerID.getText().toString().trim();

            if (TextUtils.isEmpty(brand))   { etBrand.setError("Required"); etBrand.requestFocus(); return; }
            if (TextUtils.isEmpty(model))   { etModel.setError("Required"); etModel.requestFocus(); return; }
            if (TextUtils.isEmpty(reg))     { etReg.setError("Required"); etReg.requestFocus(); return; }
            if (TextUtils.isEmpty(year))    { etYear.setError("Required"); etYear.requestFocus(); return; }
            if (TextUtils.isEmpty(ownerStr)){ etOwnerID.setError("Required"); etOwnerID.requestFocus(); return; }

            int ownerID = Integer.parseInt(ownerStr);
            long result = db.addVehicle(brand, model, reg, year, ownerID);
            if (result != -1) {
                Toast.makeText(this, "Vehicle added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add vehicle", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
