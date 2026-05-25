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

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        bookingsContainer.removeAllViews();
        Cursor cursor = db.getAllBookings();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id         = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_ID));
                String type    = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SERVICE_TYPE));
                String date    = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_DATE));
                String status  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS));
                String mech    = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNED_MECH));
                String vehicle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKED_VEHICLE));

                // Row
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

                // Icon box
                LinearLayout iconBox = new LinearLayout(this);
                iconBox.setBackgroundColor(0xFF6C63FF);
                iconBox.setGravity(Gravity.CENTER);
                iconBox.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
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
                textParams.setMarginStart(16);
                textBox.setLayoutParams(textParams);

                TextView tvType = new TextView(this);
                tvType.setText(type);
                tvType.setTextColor(0xFF222222);
                tvType.setTextSize(16);
                tvType.setTypeface(null, android.graphics.Typeface.BOLD);
                tvType.setPadding(0, 0, 0, 4);

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
                switch (status) {
                    case "Completed":   tvStatus.setTextColor(0xFF4CAF50); break;
                    case "In Progress": tvStatus.setTextColor(0xFF2196F3); break;
                    default:            tvStatus.setTextColor(0xFFFF9800); break;
                }

                TextView tvMech = new TextView(this);
                tvMech.setText("Mechanic: " + mech);
                tvMech.setTextColor(0xFF888888);
                tvMech.setTextSize(13);
                tvMech.setPadding(0, 0, 0, 4);

                TextView tvVehicle = new TextView(this);
                tvVehicle.setText("Vehicle: " + vehicle);
                tvVehicle.setTextColor(0xFF888888);
                tvVehicle.setTextSize(13);

                textBox.addView(tvType);
                textBox.addView(tvDate);
                textBox.addView(tvStatus);
                textBox.addView(tvMech);
                textBox.addView(tvVehicle);

                // Button column
                LinearLayout btnCol = new LinearLayout(this);
                btnCol.setOrientation(LinearLayout.VERTICAL);
                btnCol.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams btnColParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                btnColParams.setMarginStart(8);
                btnCol.setLayoutParams(btnColParams);

                MaterialButton btnEdit = new MaterialButton(this);
                btnEdit.setText("Edit");
                btnEdit.setTextSize(11);
                LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                editParams.setMargins(0, 0, 0, 6);
                btnEdit.setLayoutParams(editParams);
                btnEdit.setOnClickListener(v -> {
                    Intent i = new Intent(this, activity_edit_booking.class);
                    i.putExtra("bookingID", id);
                    i.putExtra("serviceType", type);
                    i.putExtra("date", date);
                    i.putExtra("status", status);
                    i.putExtra("mechanic", mech);
                    i.putExtra("vehicle", vehicle);
                    startActivity(i);
                });

                MaterialButton btnDelete = new MaterialButton(this);
                btnDelete.setText("Delete");
                btnDelete.setTextSize(11);
                btnDelete.setBackgroundColor(0xFFB71C1C);
                LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                btnDelete.setLayoutParams(deleteParams);
                btnDelete.setOnClickListener(v -> confirmDelete(id));

                btnCol.addView(btnEdit);
                btnCol.addView(btnDelete);

                row.addView(iconBox);
                row.addView(textBox);
                row.addView(btnCol);
                bookingsContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();

        } else {
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

    private void confirmDelete(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteBooking(id);
                    loadBookings();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}