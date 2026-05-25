package com.example.autotrader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class activity_manage_mechanics extends AppCompatActivity {

    LinearLayout mechanicsContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_mechanics);

        db = new DatabaseHelper(this);
        mechanicsContainer = findViewById(R.id.mechanicsContainer);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddNew).setOnClickListener(v ->
                startActivity(new Intent(this, AddMechanicActivity.class)));

        loadMechanics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMechanics();
    }

    private void loadMechanics() {
        mechanicsContainer.removeAllViews();
        Cursor cursor = db.getAllMechanics();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id       = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_ID));
                String name  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MECHANIC_NAME));
                String spec  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
                int exp      = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXPERIENCE));

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
                iconBox.setBackgroundColor(0xFF4CAF50);
                iconBox.setGravity(Gravity.CENTER);
                iconBox.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
                TextView iconText = new TextView(this);
                iconText.setText(String.valueOf(name.charAt(0)).toUpperCase());
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

                TextView tvName = new TextView(this);
                tvName.setText(name);
                tvName.setTextColor(0xFF222222);
                tvName.setTextSize(16);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                tvName.setPadding(0, 0, 0, 4);

                TextView tvSpec = new TextView(this);
                tvSpec.setText("Specialization: " + spec);
                tvSpec.setTextColor(0xFF888888);
                tvSpec.setTextSize(13);
                tvSpec.setPadding(0, 0, 0, 4);

                TextView tvExp = new TextView(this);
                tvExp.setText("Experience: " + exp + " years");
                tvExp.setTextColor(0xFF888888);
                tvExp.setTextSize(13);

                textBox.addView(tvName);
                textBox.addView(tvSpec);
                textBox.addView(tvExp);

                MaterialButton btnDelete = new MaterialButton(this);
                btnDelete.setText("Delete");
                btnDelete.setTextSize(12);
                btnDelete.setBackgroundColor(0xFFB71C1C);
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                btnParams.setMarginStart(8);
                btnDelete.setLayoutParams(btnParams);
                btnDelete.setOnClickListener(v -> confirmDelete(id));

                row.addView(iconBox);
                row.addView(textBox);
                row.addView(btnDelete);
                mechanicsContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView empty = new TextView(this);
            empty.setText("No mechanics found. Add one above.");
            empty.setTextSize(15);
            empty.setTextColor(0xFFBDBDBD);
            empty.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 80, 0, 0);
            empty.setLayoutParams(p);
            mechanicsContainer.addView(empty);
        }
    }

    private void confirmDelete(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Mechanic")
                .setMessage("Are you sure you want to delete this mechanic?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteMechanic(id);
                    loadMechanics();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}