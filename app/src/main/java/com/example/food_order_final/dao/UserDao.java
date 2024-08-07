package com.example.food_order_final.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDao extends BaseDao{
    private DatabaseHelper dbHelper;
    private RoleDao roleDao;

    public UserDao(DatabaseHelper dbHelper, RoleDao roleDao) {
        super(dbHelper);
        this.dbHelper = dbHelper;
        this.roleDao = roleDao;
    }

    public void insertUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getUserContentValues(user);

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
        contentValues.put(DatabaseHelper.USER_UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

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
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return exists;
    }

    public boolean isUserCredential(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean isValid = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? ",
                    new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                int passwordColumnIndex = cursor.getColumnIndex(DatabaseHelper.USER_PASSWORD_FIELD);
                if (passwordColumnIndex != -1) {
                    String hashedPassword = cursor.getString(passwordColumnIndex);
                    isValid = BCrypt.checkpw(password, hashedPassword);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return isValid;
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = getInt(cursor, DatabaseHelper.USER_ID_FIELD);
                    String username = getString(cursor, DatabaseHelper.USER_USERNAME_FIELD);
                    String phone = getString(cursor, DatabaseHelper.USER_PHONE_NUMBER_FIELD);
                    String email = getString(cursor, DatabaseHelper.USER_EMAIL_FIELD);
                    String fullName = getString(cursor, DatabaseHelper.USER_FULL_NAME_FIELD);
                    String password = getString(cursor, DatabaseHelper.USER_PASSWORD_FIELD);
                    int role_id = getInt(cursor, DatabaseHelper.USER_ROLE_FIELD);
                    Role role = roleDao.getRoleById(role_id);
                    String avatar = getString(cursor, DatabaseHelper.USER_AVATAR_FIELD);
                    String createdDateString = getString(cursor, DatabaseHelper.USER_CREATED_DATE_FIELD);
                    String updatedDateString = getString(cursor, DatabaseHelper.USER_UPDATED_DATE_FIELD);
                    Date createdDate = DateUtil.timestampToDate(createdDateString);
                    Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                    users.add(new User(id, username, email, phone, fullName, password, role, avatar, createdDate, updatedDate));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

    public List<User> findUserByUsername(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                    + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " LIKE ?",
                    new String[]{"%" + name + "%"});

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    int id = getInt(cursor, DatabaseHelper.USER_ID_FIELD);
                    String username = getString(cursor, DatabaseHelper.USER_USERNAME_FIELD);
                    String phone = getString(cursor, DatabaseHelper.USER_PHONE_NUMBER_FIELD);
                    String email = getString(cursor, DatabaseHelper.USER_EMAIL_FIELD);
                    String fullName = getString(cursor, DatabaseHelper.USER_FULL_NAME_FIELD);
                    String password = getString(cursor, DatabaseHelper.USER_PASSWORD_FIELD);
                    int role_id = getInt(cursor, DatabaseHelper.USER_ROLE_FIELD);
                    Role role = roleDao.getRoleById(role_id);
                    String avatar = getString(cursor, DatabaseHelper.USER_AVATAR_FIELD);
                    String createdDateString = getString(cursor, DatabaseHelper.USER_CREATED_DATE_FIELD);
                    String updatedDateString = getString(cursor, DatabaseHelper.USER_UPDATED_DATE_FIELD);
                    Date createdDate = DateUtil.timestampToDate(createdDateString);
                    Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                    users.add(new User(id, username, email, phone, fullName, password, role, avatar, createdDate, updatedDate));
                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

    public List<User> findUsersByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_FULL_NAME_FIELD + " LIKE ?",
                    new String[]{"%" + name + "%"});

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    int id = getInt(cursor, DatabaseHelper.USER_ID_FIELD);
                    String username = getString(cursor, DatabaseHelper.USER_USERNAME_FIELD);
                    String phone = getString(cursor, DatabaseHelper.USER_PHONE_NUMBER_FIELD);
                    String email = getString(cursor, DatabaseHelper.USER_EMAIL_FIELD);
                    String fullName = getString(cursor, DatabaseHelper.USER_FULL_NAME_FIELD);
                    String password = getString(cursor, DatabaseHelper.USER_PASSWORD_FIELD);
                    int role_id = getInt(cursor, DatabaseHelper.USER_ROLE_FIELD);
                    Role role = roleDao.getRoleById(role_id);
                    String avatar = getString(cursor, DatabaseHelper.USER_AVATAR_FIELD);
                    String createdDateString = getString(cursor, DatabaseHelper.USER_CREATED_DATE_FIELD);
                    String updatedDateString = getString(cursor, DatabaseHelper.USER_UPDATED_DATE_FIELD);
                    Date createdDate = DateUtil.timestampToDate(createdDateString);
                    Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                    users.add(new User(id, username, email, phone, fullName, password, role, avatar, createdDate, updatedDate));
                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

}