package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class activity_edit_customer extends AppCompatActivity {

    TextInputEditText etName, etSurname, etPhone, etEmail;
    MaterialButton btnSave, btnBack;
    DatabaseHelper db;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_customer);

        db         = new DatabaseHelper(this);
        customerId = getIntent().getIntExtra("customerID", -1);

        etName    = findViewById(R.id.etCustName);
        etSurname = findViewById(R.id.etCustSurname);
        etPhone   = findViewById(R.id.etCustPhone);
        etEmail   = findViewById(R.id.etCustEmail);
        btnSave   = findViewById(R.id.btnSaveCustomer);
        btnBack   = findViewById(R.id.btnBack);

        // Pre-fill existing values
        etName.setText(getIntent().getStringExtra("name"));
        etSurname.setText(getIntent().getStringExtra("surname"));
        etPhone.setText(getIntent().getStringExtra("phone"));
        etEmail.setText(getIntent().getStringExtra("email"));

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name    = etName.getText().toString().trim();
            String surname = etSurname.getText().toString().trim();
            String phone   = etPhone.getText().toString().trim();
            String email   = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(name))    { etName.setError("Required"); etName.requestFocus(); return; }
            if (TextUtils.isEmpty(surname)) { etSurname.setError("Required"); etSurname.requestFocus(); return; }
            if (phone.length() < 10)        { etPhone.setError("Enter valid phone"); etPhone.requestFocus(); return; }
            if (TextUtils.isEmpty(email))   { etEmail.setError("Required"); etEmail.requestFocus(); return; }

            int result = db.updateCustomer(customerId, name, surname, phone, email);
            if (result > 0) {
                Toast.makeText(this, "Customer updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}