package com.example.autotrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView signupText;
    EditText txtAdminName, txtAdminPass;
    Button signinBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        signupText   = findViewById(R.id.txtSignup);
        signinBtn    = findViewById(R.id.SigninBtn);
        txtAdminName = findViewById(R.id.IDtxtAdmin);
        txtAdminPass = findViewById(R.id.passtxtAdmin);

        signinBtn.setOnClickListener(v -> {
            String username = txtAdminName.getText().toString().trim();
            String password = txtAdminPass.getText().toString().trim();

            if (username.isEmpty()) { txtAdminName.setError("Required"); txtAdminName.requestFocus(); return; }
            if (password.isEmpty()) { txtAdminPass.setError("Required"); txtAdminPass.requestFocus(); return; }

            String role = db.loginUser(username, password);
            if (role == null) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent;
            switch (role) {
                case "admin":    intent = new Intent(this, MenuActivity.class); break;
                case "mechanic": intent = new Intent(this, MechanicDashboardActivity.class); break;
                case "customer": intent = new Intent(this, CustomerDashboardActivity.class); break;
                default: Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show(); return;
            }
            intent.putExtra("role", role);
            intent.putExtra("username", username);
            Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });

        signupText.setOnClickListener(v -> startActivity(new Intent(this, Signup.class)));
    }
}
