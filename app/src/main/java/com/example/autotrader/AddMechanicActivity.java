package com.example.autotrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddMechanicActivity extends AppCompatActivity {
    TextInputEditText etMechName, etSpec, etExp;
    MaterialButton btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_mechanic);

        db          = new DatabaseHelper(this);
        etMechName  = findViewById(R.id.etMechName);
        etSpec      = findViewById(R.id.etSpec);
        etExp       = findViewById(R.id.etExp);
        btnSave     = findViewById(R.id.btnSaveMechanic);

        btnSave.setOnClickListener(v -> {
            String name = etMechName.getText().toString().trim();
            String spec = etSpec.getText().toString().trim();
            String expStr = etExp.getText().toString().trim();

            if (TextUtils.isEmpty(name)) { etMechName.setError("Required"); etMechName.requestFocus(); return; }
            if (TextUtils.isEmpty(spec)) { etSpec.setError("Required"); etSpec.requestFocus(); return; }
            if (TextUtils.isEmpty(expStr)) { etExp.setError("Required"); etExp.requestFocus(); return; }

            int exp = Integer.parseInt(expStr);
            long result = db.addMechanic(name, spec, exp);
            if (result != -1) {
                Toast.makeText(this, "Mechanic added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add mechanic", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
