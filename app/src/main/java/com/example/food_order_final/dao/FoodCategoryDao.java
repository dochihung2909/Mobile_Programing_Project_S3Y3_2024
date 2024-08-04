package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.RestaurantCategory;

public class FoodCategoryDao {
    private DatabaseHelper dbHelper;
    public FoodCategoryDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insertFoodCategory(FoodCategory foodCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_ID_FIELD, foodCate.getId());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_NAME_FIELD, foodCate.getName());
        db.insert(DatabaseHelper.TABLE_FOOD_CATEGORY_NAME, null, contentValues);
        db.close();
    }

    public int updateFoodCategory(FoodCategory foodCate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_ID_FIELD, foodCate.getId());
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

    public FoodCategory getFoodCategoryById(int resCateId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        FoodCategory foodCate = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_CATEGORY_NAME
                            + " WHERE " + DatabaseHelper.FOOD_CATEGORY_ID_FIELD + " = ?",
                    new String[]{String.valueOf(resCateId)});
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.FOOD_CATEGORY_ID_FIELD);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.FOOD_CATEGORY_NAME_FIELD);
                if (idIndex != -1 && nameIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    foodCate = new FoodCategory(id, name);
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
        return foodCate;
    }

}
