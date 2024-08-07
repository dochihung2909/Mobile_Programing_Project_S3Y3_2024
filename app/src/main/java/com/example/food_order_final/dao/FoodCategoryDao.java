package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodCategoryDao extends BaseDao{
    private DatabaseHelper dbHelper;

    public FoodCategoryDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    public void insertFoodCategory(FoodCategory foodCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getFoodCateContentValues(foodCate);
        db.insert(DatabaseHelper.TABLE_FOOD_CATEGORY_NAME, null, contentValues);
        db.close();
    }

    public int updateFoodCategory(FoodCategory foodCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_NAME_FIELD, foodCate.getName());

        String whereClause = DatabaseHelper.FOOD_CATEGORY_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(foodCate.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_FOOD_CATEGORY_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteFoodCategory(int foodCateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.FOOD_CATEGORY_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(foodCateId)};

        db.delete(DatabaseHelper.TABLE_FOOD_CATEGORY_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<FoodCategory> getAllFoodCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<FoodCategory> foodCategories = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_CATEGORY_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.FOOD_CATEGORY_ID_FIELD);
                String name = getString(cursor, DatabaseHelper.FOOD_CATEGORY_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.FOOD_CATEGORY_CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.FOOD_CATEGORY_UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                foodCategories.add(new FoodCategory(id, name, createdDate, updatedDate));
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return foodCategories;
    }

    public FoodCategory getFoodCategoryById(int resCateId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        FoodCategory foodCate = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_CATEGORY_NAME
                            + " WHERE " + DatabaseHelper.FOOD_CATEGORY_ID_FIELD + " = ?",
                    new String[]{String.valueOf(resCateId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.FOOD_CATEGORY_ID_FIELD);
                String name = getString(cursor, DatabaseHelper.FOOD_CATEGORY_NAME_FIELD);
                String createdDateString = getString(cursor, DatabaseHelper.FOOD_CATEGORY_CREATED_DATE_FIELD);
                String updatedDateString = getString(cursor, DatabaseHelper.FOOD_CATEGORY_UPDATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                foodCate = new FoodCategory(id, name, createdDate, updatedDate);
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return foodCate;
    }

}
