package com.example.autotrader;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class activity_manage_vehicles extends AppCompatActivity {

    LinearLayout vehiclesContainer;
    DatabaseHelper db;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_vehicles);

        db         = new DatabaseHelper(this);
        customerId = getIntent().getIntExtra("customerID", -1);

        vehiclesContainer = findViewById(R.id.vehiclesContainer);

        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadVehicles();
    }

    private void loadVehicles() {
        vehiclesContainer.removeAllViews();
        Cursor cursor = db.getVehiclesByCustomer(customerId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODEL));
                String reg   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REG));
                String year  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_YEAR));

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(16, 16, 16, 16);
                row.setBackgroundColor(0xFFFFFFFF);
                row.setElevation(4f);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                rowParams.setMargins(0, 0, 0, 16);
                row.setLayoutParams(rowParams);

                LinearLayout iconBox = new LinearLayout(this);
                iconBox.setBackgroundColor(0xFF2E7D32);
                iconBox.setGravity(Gravity.CENTER);
                iconBox.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
                TextView iconText = new TextView(this);
                iconText.setText(String.valueOf(brand.charAt(0)).toUpperCase());
                iconText.setTextColor(0xFFFFFFFF);
                iconText.setTextSize(22);
                iconText.setGravity(Gravity.CENTER);
                iconBox.addView(iconText);

                LinearLayout textBox = new LinearLayout(this);
                textBox.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                textParams.setMarginStart(16);
                textBox.setLayoutParams(textParams);

                TextView tvBrand = new TextView(this);
                tvBrand.setText(brand + " " + model);
                tvBrand.setTextColor(0xFF222222);
                tvBrand.setTextSize(16);
                tvBrand.setTypeface(null, android.graphics.Typeface.BOLD);
                tvBrand.setPadding(0, 0, 0, 4);

                TextView tvReg = new TextView(this);
                tvReg.setText("Reg: " + reg);
                tvReg.setTextColor(0xFF888888);
                tvReg.setTextSize(13);
                tvReg.setPadding(0, 0, 0, 4);

                TextView tvYear = new TextView(this);
                tvYear.setText("Year: " + year);
                tvYear.setTextColor(0xFF888888);
                tvYear.setTextSize(13);

                textBox.addView(tvBrand);
                textBox.addView(tvReg);
                textBox.addView(tvYear);

                row.addView(iconBox);
                row.addView(textBox);
                vehiclesContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();

        } else {
            TextView empty = new TextView(this);
            empty.setText("No vehicles found for your account.");
            empty.setTextSize(15);
            empty.setTextColor(0xFFBDBDBD);
            empty.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 80, 0, 0);
            empty.setLayoutParams(p);
            vehiclesContainer.addView(empty);
        }
    }
}