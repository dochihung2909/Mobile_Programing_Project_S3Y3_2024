package com.example.food_order_final.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private DatabaseHelper dbHelper;
    private RoleDao roleDao;

    public UserDao(DatabaseHelper dbHelper, RoleDao roleDao) {
        this.dbHelper = dbHelper;
        this.roleDao = roleDao;
    }

    public void insertUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(DatabaseHelper.USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(DatabaseHelper.USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(DatabaseHelper.USER_FULL_NAME_FIELD, user.getFullName());

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        contentValues.put(DatabaseHelper.USER_PASSWORD_FIELD, hashedPassword);

        db.insert(DatabaseHelper.TABLE_USER_NAME, null, contentValues);
        db.close();
    }

    public int updateUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(DatabaseHelper.USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(DatabaseHelper.USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(DatabaseHelper.USER_FULL_NAME_FIELD, user.getFullName());

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        contentValues.put(DatabaseHelper.USER_PASSWORD_FIELD, hashedPassword);

        String whereClause = DatabaseHelper.USER_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.USER_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        db.delete(DatabaseHelper.TABLE_USER_NAME, whereClause, whereArgs);
        db.close();
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT 1 FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? ",
                    new String[]{username});
            if (cursor.moveToFirst()){
                exists = true;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return exists;
    }

    public boolean isUserCredential(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean isValid = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? "
                            + " AND " + DatabaseHelper.USER_PASSWORD_FIELD + " = ?",
                    new String[]{username, password});

            if (cursor != null && cursor.moveToFirst()) {
                int passwordColumnIndex = cursor.getColumnIndex(DatabaseHelper.USER_PASSWORD_FIELD);
                if (passwordColumnIndex != -1) {
                    String hashedPassword = cursor.getString(passwordColumnIndex);
                    isValid = BCrypt.checkpw(password, hashedPassword);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return isValid;
    }

    public List<User> findUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                    + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? ",
                    new String[]{username});

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    int idIdx = cursor.getColumnIndex(DatabaseHelper.USER_ID_FIELD);
                    int usernameIdx = cursor.getColumnIndex(DatabaseHelper.USER_USERNAME_FIELD);
                    int phoneIdx = cursor.getColumnIndex(DatabaseHelper.USER_PHONE_NUMBER_FIELD);
                    int emailIdx = cursor.getColumnIndex(DatabaseHelper.USER_EMAIL_FIELD);
                    int fullNameIdx = cursor.getColumnIndex(DatabaseHelper.USER_FULL_NAME_FIELD);
                    int passwordIdx = cursor.getColumnIndex(DatabaseHelper.USER_PASSWORD_FIELD);
                    int roleIdx = cursor.getColumnIndex(DatabaseHelper.USER_ROLE_FIELD);

                    if (idIdx != -1 && usernameIdx != -1 && phoneIdx != -1 &&
                            emailIdx != -1 && fullNameIdx != -1 && passwordIdx != -1 &&
                            roleIdx != -1) {
                        int id = cursor.getInt(idIdx);
                        String usernameValue = cursor.getString(usernameIdx);
                        String phoneValue = cursor.getString(phoneIdx);
                        String emailValue = cursor.getString(emailIdx);
                        String fullNameValue = cursor.getString(fullNameIdx);
                        String passwordValue = cursor.getString(passwordIdx);
                        int roleId = cursor.getInt(roleIdx);

                        Role role = roleDao.getRoleById(roleId);

                        User user = new User(id, usernameValue, phoneValue, emailValue, fullNameValue, passwordValue, role);
                        users.add(user);
                    }
                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return users;
    }

}