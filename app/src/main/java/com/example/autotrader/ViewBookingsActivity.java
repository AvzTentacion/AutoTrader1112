package com.example.autotrader;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ViewBookingsActivity extends AppCompatActivity {

    LinearLayout bookingsContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_bookings);

        db = new DatabaseHelper(this);
        bookingsContainer = findViewById(R.id.bookingsContainer);

        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadBookings();
    }

    private void loadBookings() {
        bookingsContainer.removeAllViews();
        Cursor cursor = db.getAllBookings();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id     = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_ID));
                String type   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SERVICE_TYPE));
                String date   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS));
                String mech   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNED_MECH));

                // Outer card row
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

                // Colour icon box
                LinearLayout iconBox = new LinearLayout(this);
                iconBox.setBackgroundColor(0xFF6C63FF);
                iconBox.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120);
                iconBox.setLayoutParams(iconParams);

                TextView iconText = new TextView(this);
                iconText.setText("#" + id);
                iconText.setTextColor(0xFFFFFFFF);
                iconText.setTextSize(14);
                iconText.setGravity(Gravity.CENTER);
                iconBox.addView(iconText);

                // Text section
                LinearLayout textBox = new LinearLayout(this);
                textBox.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                textParams.setMarginStart(24);
                textBox.setLayoutParams(textParams);

                TextView tvType = new TextView(this);
                tvType.setText(type);
                tvType.setTextColor(0xFF222222);
                tvType.setTextSize(16);
                tvType.setPadding(0, 0, 0, 4);
                tvType.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView tvDate = new TextView(this);
                tvDate.setText("Date: " + date);
                tvDate.setTextColor(0xFF888888);
                tvDate.setTextSize(13);
                tvDate.setPadding(0, 0, 0, 4);

                TextView tvStatus = new TextView(this);
                tvStatus.setText("Status: " + status);
                tvStatus.setTextSize(13);
                tvStatus.setTypeface(null, android.graphics.Typeface.BOLD);
                tvStatus.setPadding(0, 0, 0, 4);
                // Colour status text
                switch (status) {
                    case "Completed":
                        tvStatus.setTextColor(0xFF4CAF50); break;
                    case "In Progress":
                        tvStatus.setTextColor(0xFF2196F3); break;
                    default:
                        tvStatus.setTextColor(0xFFFF9800); break;
                }

                TextView tvMech = new TextView(this);
                tvMech.setText("Mechanic: " + mech);
                tvMech.setTextColor(0xFF888888);
                tvMech.setTextSize(13);

                textBox.addView(tvType);
                textBox.addView(tvDate);
                textBox.addView(tvStatus);
                textBox.addView(tvMech);

                row.addView(iconBox);
                row.addView(textBox);
                bookingsContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();

        } else {
            // Empty state
            TextView empty = new TextView(this);
            empty.setText("No bookings found");
            empty.setTextSize(15);
            empty.setTextColor(0xFFBDBDBD);
            empty.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 80, 0, 0);
            empty.setLayoutParams(p);
            bookingsContainer.addView(empty);
        }
    }
}