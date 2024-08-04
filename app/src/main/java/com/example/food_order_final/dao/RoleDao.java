package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import org.mindrot.jbcrypt.BCrypt;

public class RoleDao {
    private DatabaseHelper dbHelper;
    public RoleDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insertRole(Role role){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ROLE_ID_FIELD, role.getId());
        contentValues.put(DatabaseHelper.ROLE_NAME_FIELD, role.getName());
        db.insert(DatabaseHelper.TABLE_ROLE_NAME, null, contentValues);
        db.close();
    }

    public int updateRole(Role role){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ROLE_ID_FIELD, role.getId());
        contentValues.put(DatabaseHelper.ROLE_NAME_FIELD, role.getName());

        String whereClause = DatabaseHelper.ROLE_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(role.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_ROLE_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRole(int roleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ROLE_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(roleId)};

        db.delete(DatabaseHelper.TABLE_ROLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public Role getRoleById(int roleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Role role = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROLE_NAME
                    + " WHERE " + DatabaseHelper.ROLE_ID_FIELD + " = ?",
                    new String[]{String.valueOf(roleId)});
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.ROLE_ID_FIELD);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.ROLE_NAME_FIELD);
                if (idIndex != -1 && nameIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    role = new Role(id, name);
                } else {
                    throw new RuntimeException("Column index not found !");
                }
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return role;
    }

}
