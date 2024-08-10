package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Base;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoleDao extends BaseDao {
    private final DatabaseHelper dbHelper;

    public RoleDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    public void insertRole(Role role){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getRoleContentValues(role);
        db.insert(DatabaseHelper.TABLE_ROLE_NAME, null, contentValues);
        db.close();
    }

    public int updateRole(Role role){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ROLE_NAME_FIELD, role.getName());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(role.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_ROLE_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRole(int roleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(roleId)};

        db.delete(DatabaseHelper.TABLE_ROLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<Role> getAllRoles() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Role> roles = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROLE_NAME, null);
            do {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.ROLE_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                roles.add(new Role(id, name, createdDate, updatedDate));
            }while(cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return roles;
    }

    public Role getRoleById(int roleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Role role = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROLE_NAME
                    + " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{String.valueOf(roleId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.ROLE_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                String updatedDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                role = new Role(id, name, createdDate, updatedDate);
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return role;
    }

    public Role getRoleByName(String roleName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Role role = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROLE_NAME
                            + " WHERE " + DatabaseHelper.ROLE_NAME_FIELD + " = ?",
                    new String[]{roleName});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.ROLE_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                String updatedDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                role = new Role(id, name, createdDate, updatedDate);
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return role;
    }

    public List<Role> getRolesByName(String roleName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Role> roles = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROLE_NAME +
                    " WHERE " + DatabaseHelper.ROLE_NAME_FIELD + " LIKE ?",
                    new String[]{"%" + roleName + "%"});
            do {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.ROLE_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                roles.add(new Role(id, name, createdDate, updatedDate));
            }while(cursor.moveToNext());
        }finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return roles;
    }

}
