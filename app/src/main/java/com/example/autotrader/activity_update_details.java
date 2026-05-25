package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class activity_update_details extends AppCompatActivity {

    TextInputEditText etPhone, etEmail;
    MaterialButton btnUpdate, btnBack;
    DatabaseHelper db;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_details);

        db = new DatabaseHelper(this);

        etPhone   = findViewById(R.id.etNewPhone);
        etEmail   = findViewById(R.id.etNewEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack   = findViewById(R.id.btnBack);

        customerId = getIntent().getIntExtra("customerID", -1);

        btnBack.setOnClickListener(v -> finish());

        btnUpdate.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (phone.length() < 10) {
                etPhone.setError("Enter valid phone number");
                etPhone.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            if (customerId == -1) {
                Toast.makeText(this, "Customer not found", Toast.LENGTH_SHORT).show();
                return;
            }

            int result = db.updateCustomerContact(customerId, phone, email);
            if (result > 0) {
                Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}