package com.example.restaurantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Restaurant.db";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_MENU = "menu";
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String TABLE_NOTIFICATIONS = "notifications"; // New Table

    public DBHelper(Context context) {
        super(context, DBNAME, null, 2); // Incremented Version for Upgrade
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // 1. Create Users Table (Staff and Guest)
        MyDB.execSQL("create Table " + TABLE_USERS + "(username TEXT primary key, password TEXT, role INTEGER, phone TEXT, email TEXT)");

        // 2. Create Menu Table
        MyDB.execSQL("create Table " + TABLE_MENU + "(id INTEGER primary key autoincrement, name TEXT, price REAL, imageUri TEXT)");

        // 3. Create Reservations Table
        MyDB.execSQL("create Table " + TABLE_RESERVATIONS + "(id INTEGER primary key autoincrement, customerName TEXT, date TEXT, time TEXT, partySize INTEGER, status TEXT)");

        // 4. Create Notifications Table (New)
        // recipient: 'STAFF' for all staff, or specific username for guests
        MyDB.execSQL("create Table " + TABLE_NOTIFICATIONS + "(id INTEGER primary key autoincrement, recipient TEXT, message TEXT, isRead INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists " + TABLE_USERS);
        MyDB.execSQL("drop Table if exists " + TABLE_MENU);
        MyDB.execSQL("drop Table if exists " + TABLE_RESERVATIONS);
        MyDB.execSQL("drop Table if exists " + TABLE_NOTIFICATIONS);
        onCreate(MyDB);
    }

    // --- NOTIFICATION METHODS ---

    public void insertNotification(String recipient, String message) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recipient", recipient);
        contentValues.put("message", message);
        contentValues.put("isRead", 0); // 0 = Unread
        MyDB.insert(TABLE_NOTIFICATIONS, null, contentValues);
    }

    public Cursor getNotifications(String recipient) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        // Get unread messages first, or just all messages
        return MyDB.rawQuery("Select * from " + TABLE_NOTIFICATIONS + " where recipient = ? OR recipient = 'ALL' ORDER BY id DESC", new String[]{recipient});
    }

    public Cursor getUnreadNotifications(String recipient) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from " + TABLE_NOTIFICATIONS + " where (recipient = ? OR recipient = 'ALL') AND isRead = 0", new String[]{recipient});
    }

    public void markNotificationsAsRead(String recipient) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isRead", 1);
        MyDB.update(TABLE_NOTIFICATIONS, contentValues, "recipient = ? OR recipient = 'ALL'", new String[]{recipient});
    }

    // --- USER METHODS ---

    public Boolean insertUser(String username, String password, int role, String phone, String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("role", role); 
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        long result = MyDB.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_USERS + " where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_USERS + " where username = ? and password = ?", new String[]{username,password});
        return cursor.getCount() > 0;
    }

    public int getUserRole(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select role from " + TABLE_USERS + " where username = ?", new String[]{username});
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1; // Error
    }

    // --- MENU METHODS ---

    public Boolean insertMenuItem(String name, double price, String imageUri) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("imageUri", imageUri);
        long result = MyDB.insert(TABLE_MENU, null, contentValues);
        return result != -1;
    }

    public Cursor getMenu() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from " + TABLE_MENU, null);
    }

    public void deleteMenuItem(String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete(TABLE_MENU, "name=?", new String[]{name});
    }

    // --- RESERVATION METHODS ---

    public Boolean insertReservation(String customerName, String date, String time, int partySize) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customerName", customerName);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("partySize", partySize);
        contentValues.put("status", "Confirmed");
        long result = MyDB.insert(TABLE_RESERVATIONS, null, contentValues);
        return result != -1;
    }

    public Cursor getReservations() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from " + TABLE_RESERVATIONS, null);
    }

    public Cursor getReservationsForUser(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from " + TABLE_RESERVATIONS + " where customerName = ?", new String[]{username});
    }

    public boolean cancelReservation(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        long result = MyDB.delete(TABLE_RESERVATIONS, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    
    public boolean updateReservationStatus(int id, String newStatus) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", newStatus);
        long result = MyDB.update(TABLE_RESERVATIONS, contentValues, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
