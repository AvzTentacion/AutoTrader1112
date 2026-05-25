package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class activity_edit_mechanic extends AppCompatActivity {

    TextInputEditText etName, etSpec, etExp;
    MaterialButton btnSave, btnBack;
    DatabaseHelper db;
    int mechanicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_mechanic);

        db         = new DatabaseHelper(this);
        mechanicId = getIntent().getIntExtra("mechanicID", -1);

        etName  = findViewById(R.id.etMechName);
        etSpec  = findViewById(R.id.etSpec);
        etExp   = findViewById(R.id.etExp);
        btnSave = findViewById(R.id.btnSaveMechanic);
        btnBack = findViewById(R.id.btnBack);

        // Pre-fill existing values
        etName.setText(getIntent().getStringExtra("name"));
        etSpec.setText(getIntent().getStringExtra("spec"));
        etExp.setText(String.valueOf(getIntent().getIntExtra("exp", 0)));

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name   = etName.getText().toString().trim();
            String spec   = etSpec.getText().toString().trim();
            String expStr = etExp.getText().toString().trim();

            if (TextUtils.isEmpty(name))   { etName.setError("Required"); etName.requestFocus(); return; }
            if (TextUtils.isEmpty(spec))   { etSpec.setError("Required"); etSpec.requestFocus(); return; }
            if (TextUtils.isEmpty(expStr)) { etExp.setError("Required"); etExp.requestFocus(); return; }

            int exp    = Integer.parseInt(expStr);
            int result = db.updateMechanic(mechanicId, name, spec, exp);
            if (result > 0) {
                Toast.makeText(this, "Mechanic updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}