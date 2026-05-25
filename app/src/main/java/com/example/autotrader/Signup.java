package com.example.autotrader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class Signup extends AppCompatActivity {

    private TextInputEditText etName, etSurname, etPhone, etEmail, etUsername, etPassword, etConfirmPass;
    private Button btnSignup;
    private TextView txtSignin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        db            = new DatabaseHelper(this);
        etName        = findViewById(R.id.etName);
        etSurname     = findViewById(R.id.etSurname);
        etPhone       = findViewById(R.id.etPhone);
        etEmail       = findViewById(R.id.etEmail);
        etUsername    = findViewById(R.id.etUsername);
        etPassword    = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btnSignup     = findViewById(R.id.SignupBtn);
        txtSignin     = findViewById(R.id.txtSignin);

        btnSignup.setOnClickListener(v -> attemptRegister());
        txtSignin.setOnClickListener(v -> { startActivity(new Intent(this, MainActivity.class)); finish(); });
    }

    private void attemptRegister() {
        String name        = etName.getText().toString().trim();
        String surname     = etSurname.getText().toString().trim();
        String phone       = etPhone.getText().toString().trim();
        String email       = etEmail.getText().toString().trim();
        String username    = etUsername.getText().toString().trim();
        String password    = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(name))    { etName.setError("Required"); etName.requestFocus(); return; }
        if (TextUtils.isEmpty(surname)) { etSurname.setError("Required"); etSurname.requestFocus(); return; }
        if (phone.length() < 10)        { etPhone.setError("Enter valid 10-digit number"); etPhone.requestFocus(); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Invalid email"); etEmail.requestFocus(); return; }
        if (username.length() < 4)      { etUsername.setError("Min 4 characters"); etUsername.requestFocus(); return; }
        if (password.length() < 6)      { etPassword.setError("Min 6 characters"); etPassword.requestFocus(); return; }
        if (!password.equals(confirmPass)) { etConfirmPass.setError("Passwords don't match"); etConfirmPass.requestFocus(); return; }
        if (db.usernameExists(username)) { etUsername.setError("Username taken"); etUsername.requestFocus(); return; }

        boolean success = db.registerCustomer(name, surname, phone, email, username, password);
        if (success) {
            Toast.makeText(this, "Account created! Please sign in.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
