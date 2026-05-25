package com.example.autotrader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "AutoTraderDB";
    private static final int    DATABASE_VERSION = 1;

    public static final String TABLE_USERS    = "users";
    public static final String COL_USER_ID    = "userID";
    public static final String COL_USERNAME   = "username";
    public static final String COL_PASSWORD   = "password";
    public static final String COL_ROLE       = "role";
    public static final String COL_LINKED_ID  = "linkedID";

    public static final String TABLE_CUSTOMERS    = "customers";
    public static final String COL_CUSTOMER_ID    = "customerID";
    public static final String COL_CUSTOMER_NAME  = "customerName";
    public static final String COL_CUSTOMER_SNAME = "customerSurname";
    public static final String COL_PHONE          = "phoneNumber";
    public static final String COL_EMAIL          = "emailAddress";

    public static final String TABLE_VEHICLES  = "vehicles";
    public static final String COL_VEHICLE_ID  = "vehicleID";
    public static final String COL_BRAND       = "vehicleBrand";
    public static final String COL_MODEL       = "vehicleModel";
    public static final String COL_REG         = "registrationNumber";
    public static final String COL_YEAR        = "vehicleYear";
    public static final String COL_OWNER_ID    = "ownerCustomerID";

    public static final String TABLE_MECHANICS    = "mechanics";
    public static final String COL_MECHANIC_ID    = "mechanicID";
    public static final String COL_MECHANIC_NAME  = "mechanicName";
    public static final String COL_SPECIALIZATION = "specialization";
    public static final String COL_EXPERIENCE     = "yearsOfExperience";

    public static final String TABLE_BOOKINGS      = "bookings";
    public static final String COL_BOOKING_ID      = "bookingID";
    public static final String COL_SERVICE_TYPE    = "serviceType";
    public static final String COL_BOOKING_DATE    = "bookingDate";
    public static final String COL_STATUS          = "serviceStatus";
    public static final String COL_ASSIGNED_MECH   = "assignedMechanic";
    public static final String COL_BOOKED_VEHICLE  = "vehicleID_fk";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_ROLE + " TEXT NOT NULL, " +
                COL_LINKED_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                COL_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUSTOMER_NAME + " TEXT NOT NULL, " +
                COL_CUSTOMER_SNAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_VEHICLES + " (" +
                COL_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BRAND + " TEXT NOT NULL, " +
                COL_MODEL + " TEXT NOT NULL, " +
                COL_REG + " TEXT NOT NULL, " +
                COL_YEAR + " TEXT NOT NULL, " +
                COL_OWNER_ID + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_MECHANICS + " (" +
                COL_MECHANIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MECHANIC_NAME + " TEXT NOT NULL, " +
                COL_SPECIALIZATION + " TEXT NOT NULL, " +
                COL_EXPERIENCE + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SERVICE_TYPE + " TEXT NOT NULL, " +
                COL_BOOKING_DATE + " TEXT NOT NULL, " +
                COL_STATUS + " TEXT NOT NULL DEFAULT 'Pending', " +
                COL_ASSIGNED_MECH + " TEXT NOT NULL, " +
                COL_BOOKED_VEHICLE + " TEXT NOT NULL)");

        ContentValues admin = new ContentValues();
        admin.put(COL_USERNAME, "admin");
        admin.put(COL_PASSWORD, "admin123");
        admin.put(COL_ROLE, "admin");
        db.insert(TABLE_USERS, null, admin);

        ContentValues mechanic = new ContentValues();
        mechanic.put(COL_USERNAME, "mechanic");
        mechanic.put(COL_PASSWORD, "mechanic123");
        mechanic.put(COL_ROLE, "mechanic");
        db.insert(TABLE_USERS, null, mechanic);

        ContentValues customer = new ContentValues();
        customer.put(COL_USERNAME, "customer");
        customer.put(COL_PASSWORD, "customer123");
        customer.put(COL_ROLE, "customer");
        db.insert(TABLE_USERS, null, customer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MECHANICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public String loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ROLE},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        return null;
    }

    public int getLinkedId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_LINKED_ID},
                COL_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public boolean registerCustomer(String name, String surname, String phone, String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COL_CUSTOMER_NAME, name);
            cv.put(COL_CUSTOMER_SNAME, surname);
            cv.put(COL_PHONE, phone);
            cv.put(COL_EMAIL, email);
            long customerId = db.insert(TABLE_CUSTOMERS, null, cv);
            if (customerId == -1) return false;

            ContentValues ucv = new ContentValues();
            ucv.put(COL_USERNAME, username);
            ucv.put(COL_PASSWORD, password);
            ucv.put(COL_ROLE, "customer");
            ucv.put(COL_LINKED_ID, customerId);
            long userId = db.insert(TABLE_USERS, null, ucv);
            if (userId == -1) return false;

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public long addCustomer(String name, String surname, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CUSTOMER_NAME, name);
        cv.put(COL_CUSTOMER_SNAME, surname);
        cv.put(COL_PHONE, phone);
        cv.put(COL_EMAIL, email);
        return db.insert(TABLE_CUSTOMERS, null, cv);
    }

    public Cursor getAllCustomers() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_CUSTOMERS, null);
    }

    public int updateCustomer(int id, String name, String surname, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CUSTOMER_NAME, name);
        cv.put(COL_CUSTOMER_SNAME, surname);
        cv.put(COL_PHONE, phone);
        cv.put(COL_EMAIL, email);
        return db.update(TABLE_CUSTOMERS, cv, COL_CUSTOMER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateCustomerContact(int id, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PHONE, phone);
        cv.put(COL_EMAIL, email);
        return db.update(TABLE_CUSTOMERS, cv, COL_CUSTOMER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteCustomer(int id) {
        this.getWritableDatabase().delete(TABLE_CUSTOMERS, COL_CUSTOMER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long addVehicle(String brand, String model, String reg, String year, int ownerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BRAND, brand);
        cv.put(COL_MODEL, model);
        cv.put(COL_REG, reg);
        cv.put(COL_YEAR, year);
        cv.put(COL_OWNER_ID, ownerID);
        return db.insert(TABLE_VEHICLES, null, cv);
    }

    public Cursor getAllVehicles() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_VEHICLES, null);
    }

    public void deleteVehicle(int id) {
        this.getWritableDatabase().delete(TABLE_VEHICLES, COL_VEHICLE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long addMechanic(String name, String spec, int experience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MECHANIC_NAME, name);
        cv.put(COL_SPECIALIZATION, spec);
        cv.put(COL_EXPERIENCE, experience);
        return db.insert(TABLE_MECHANICS, null, cv);
    }

    public Cursor getAllMechanics() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_MECHANICS, null);
    }

    public void deleteMechanic(int id) {
        this.getWritableDatabase().delete(TABLE_MECHANICS, COL_MECHANIC_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long addBooking(String serviceType, String date, String status, String mechanic, String vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SERVICE_TYPE, serviceType);
        cv.put(COL_BOOKING_DATE, date);
        cv.put(COL_STATUS, status);
        cv.put(COL_ASSIGNED_MECH, mechanic);
        cv.put(COL_BOOKED_VEHICLE, vehicle);
        return db.insert(TABLE_BOOKINGS, null, cv);
    }

    public Cursor getAllBookings() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_BOOKINGS, null);
    }

    public int updateBookingStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        return db.update(TABLE_BOOKINGS, cv, COL_BOOKING_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteBooking(int id) {
        this.getWritableDatabase().delete(TABLE_BOOKINGS, COL_BOOKING_ID + "=?", new String[]{String.valueOf(id)});
    }
}