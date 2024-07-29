package com.example.food_order_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.food_order_final.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database setup
    public static final String DATABASE_NAME = "FoodOrderDB";
    public static final int DATABASE_VERSION = 1;

    // Table setup
    public static final String TABLE_USER_NAME = "User";

    // Table User columns
    public static final String USER_ID_FIELD = "id";
    public static final String USER_FULL_NAME_FIELD = "full_name";
    public static final String USER_PHONE_NUMBER_FIELD = "phone_number";
    public static final String USER_EMAIL_FIELD = "email";
    public static final String USER_USERNAME_FIELD = "username";
    public static final String USER_PASSWORD_FIELD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_USER_NAME, USER_ID_FIELD, USER_USERNAME_FIELD, USER_PHONE_NUMBER_FIELD, USER_EMAIL_FIELD, USER_FULL_NAME_FIELD, USER_PASSWORD_FIELD);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        onCreate(db);
    }

    public void addUserToDatabase(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(USER_FULL_NAME_FIELD, user.getFullName());
        contentValues.put(USER_PASSWORD_FIELD, user.getPassword());
        db.insert(TABLE_USER_NAME, null, contentValues);
        db.close();
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT * FROM "
                    + TABLE_USER_NAME
                    + " WHERE "
                    + USER_USERNAME_FIELD
                    + " = ?", new String[]{username});

            if (cursor.moveToFirst()) {
                exists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return exists;
    }

    public boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT * FROM "
                    + TABLE_USER_NAME
                    + " WHERE "
                    + USER_USERNAME_FIELD
                    + " = ?"
                    + " AND "
                    + USER_PASSWORD_FIELD
                    + " = ?", new String[]{username, password});

            if (cursor.moveToFirst()) {
                exists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return exists;
    }
}
