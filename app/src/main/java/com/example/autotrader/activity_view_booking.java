package com.example.autotrader;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class activity_view_booking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_booking);

        MaterialButton btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(v -> finish());
    }
}