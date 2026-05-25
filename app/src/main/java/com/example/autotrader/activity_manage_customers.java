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

public class activity_manage_customers extends AppCompatActivity {

    LinearLayout customersContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_customers);

        db = new DatabaseHelper(this);
        customersContainer = findViewById(R.id.customersContainer);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddNew).setOnClickListener(v ->
                startActivity(new Intent(this, AddCustomerActivity.class)));

        loadCustomers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }

    private void loadCustomers() {
        customersContainer.removeAllViews();
        Cursor cursor = db.getAllCustomers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id       = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ID));
                String name  = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME));
                String sname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_SNAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));

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
                iconBox.setBackgroundColor(0xFF6C63FF);
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
                tvName.setText(name + " " + sname);
                tvName.setTextColor(0xFF222222);
                tvName.setTextSize(16);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                tvName.setPadding(0, 0, 0, 4);

                TextView tvPhone = new TextView(this);
                tvPhone.setText(phone);
                tvPhone.setTextColor(0xFF888888);
                tvPhone.setTextSize(13);
                tvPhone.setPadding(0, 0, 0, 4);

                TextView tvEmail = new TextView(this);
                tvEmail.setText(email);
                tvEmail.setTextColor(0xFF888888);
                tvEmail.setTextSize(13);

                textBox.addView(tvName);
                textBox.addView(tvPhone);
                textBox.addView(tvEmail);

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
                    Intent i = new Intent(this, activity_edit_customer.class);
                    i.putExtra("customerID", id);
                    i.putExtra("name", name);
                    i.putExtra("surname", sname);
                    i.putExtra("phone", phone);
                    i.putExtra("email", email);
                    startActivity(i);
                });

                MaterialButton btnDelete = new MaterialButton(this);
                btnDelete.setText("Delete");
                btnDelete.setTextSize(11);
                btnDelete.setBackgroundColor(0xFFB71C1C);
                btnDelete.setOnClickListener(v -> confirmDelete(id));

                btnCol.addView(btnEdit);
                btnCol.addView(btnDelete);

                row.addView(iconBox);
                row.addView(textBox);
                row.addView(btnCol);
                customersContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView empty = new TextView(this);
            empty.setText("No customers found. Add one above.");
            empty.setTextSize(15);
            empty.setTextColor(0xFFBDBDBD);
            empty.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 80, 0, 0);
            empty.setLayoutParams(p);
            customersContainer.addView(empty);
        }
    }

    private void confirmDelete(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Customer")
                .setMessage("Are you sure you want to delete this customer?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteCustomer(id);
                    loadCustomers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}