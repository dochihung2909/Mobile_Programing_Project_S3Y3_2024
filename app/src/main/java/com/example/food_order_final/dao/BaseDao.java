package com.example.food_order_final.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.example.food_order_final.database.DatabaseHelper;

public abstract class BaseDao {
    protected DatabaseHelper dbHelper;

    public BaseDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected String getString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1)
            return cursor.getString(index);
        throw new SQLException("Column " + columnName + " not found !");
    }

    protected byte[] getBlob(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1)
            return cursor.getBlob(index);
        throw new SQLException("Column " + columnName + " not found !");
    }

    protected int getInt(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1)
            return cursor.getInt(index);
        throw new SQLException("Column " + columnName + " not found !");
    }

    protected double getDouble(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1)
            return cursor.getDouble(index);
        throw new SQLException("Column " + columnName + " not found !");
    }

    protected boolean getBoolean(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1)
            return cursor.getInt(index) > 0;
        throw new SQLException("Column " + columnName + " not found !");
    }

}