package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantCategoryDao extends BaseDao{
    private DatabaseHelper dbHelper;

    public RestaurantCategoryDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    public void insertRestaurantCategory(RestaurantCategory resCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getResCateContentValues(resCate);
        db.insert(DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME, null, contentValues);
        db.close();
    }

    public int updateRestaurantCategory(RestaurantCategory resCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD, resCate.getName());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(resCate.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRestaurantCategory(int resCateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(resCateId)};

        db.delete(DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<RestaurantCategory> getAllRestaurantCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<RestaurantCategory> resCategories = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                resCategories.add(new RestaurantCategory(id, name, createdDate, updatedDate));
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return resCategories;
    }

    public RestaurantCategory getRestaurantCategoryById(int resCateId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        RestaurantCategory resCate = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{String.valueOf(resCateId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                resCate = new RestaurantCategory(id, name, createdDate, updatedDate);
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return resCate;
    }

    public RestaurantCategory getRestaurantCategoryByName(String resCateName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        RestaurantCategory restaurantCategory = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_CATEGORY_NAME
                            + " WHERE " + DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD + " = ?",
                    new String[]{resCateName});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                String name = getString(cursor, DatabaseHelper.RESTAURANT_CATEGORY_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                restaurantCategory = new RestaurantCategory(id, name, createdDate, updatedDate);
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return restaurantCategory;
    }

}
