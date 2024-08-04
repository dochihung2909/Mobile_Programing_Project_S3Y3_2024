package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;

public class RestaurantCategoryDao {

    private DatabaseHelper dbHelper;
    public RestaurantCategoryDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insertRestaurantCategory(RestaurantCategory resCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD, resCate.getId());
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD, resCate.getName());
        db.insert(DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME, null, contentValues);
        db.close();
    }

    public int updateRestaurantCategory(RestaurantCategory resCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD, resCate.getId());
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD, resCate.getName());

        String whereClause = DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(resCate.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_ROLE_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRestaurantCategory(int resCateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(resCateId)};

        db.delete(DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME, whereClause, whereArgs);
        db.close();
    }

    public RestaurantCategory getRestaurantCategoryById(int resCateId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        RestaurantCategory resCate = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME
                            + " WHERE " + DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD + " = ?",
                    new String[]{String.valueOf(resCateId)});
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_CATEGORY_ID_FIELD);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD);
                if (idIndex != -1 && nameIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    resCate = new RestaurantCategory(id, name);
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
        return resCate;
    }

}
