package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FoodDao {
    private DatabaseHelper dbHelper;
    private FoodCategoryDao foodCategoryDao;
    private RestaurantDao restaurantDao;

    public FoodDao(DatabaseHelper dbHelper, FoodCategoryDao foodCategoryDao, RestaurantDao restaurantDao) {
        this.dbHelper = dbHelper;
        this.foodCategoryDao = foodCategoryDao;
        this.restaurantDao = restaurantDao;
    }

    // Insert a new food item into the database
    public void insertFood(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME_FIELD, food.getName());
        contentValues.put(DatabaseHelper.FOOD_PRICE_FIELD, food.getPrice());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_FIELD, food.getCategory().getId());
        contentValues.put(DatabaseHelper.FOOD_RESTAURANT_FIELD, food.getRestaurant().getId());

        db.insert(DatabaseHelper.TABLE_FOOD_NAME, null, contentValues);
        db.close();
    }

    // Update an existing food item in the database
    public int updateFood(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME_FIELD, food.getName());
        contentValues.put(DatabaseHelper.FOOD_PRICE_FIELD, food.getPrice());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_FIELD, food.getCategory().getId());
        contentValues.put(DatabaseHelper.FOOD_RESTAURANT_FIELD, food.getRestaurant().getId());

        String whereClause = DatabaseHelper.FOOD_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(food.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_FOOD_NAME, contentValues, whereClause, whereArgs);
        db.close();
        return rowAffected;
    }

    // Delete a food item from the database
    public void deleteFood(int foodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.FOOD_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(foodId)};

        db.delete(DatabaseHelper.TABLE_FOOD_NAME, whereClause, whereArgs);
        db.close();
    }

    // Find food items by name
    public List<Food> findFoodByName(String foodName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Food> foods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_NAME
                            + " WHERE " + DatabaseHelper.FOOD_NAME_FIELD + " = ?",
                    new String[]{foodName});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIdx = cursor.getColumnIndex(DatabaseHelper.FOOD_ID_FIELD);
                    int nameIdx = cursor.getColumnIndex(DatabaseHelper.FOOD_NAME_FIELD);
                    int priceIdx = cursor.getColumnIndex(DatabaseHelper.FOOD_PRICE_FIELD);
                    int categoryIdx = cursor.getColumnIndex(DatabaseHelper.FOOD_CATEGORY_FIELD);
                    int restaurantIdx = cursor.getColumnIndex(DatabaseHelper.FOOD_RESTAURANT_FIELD);

                    if (idIdx != -1 && nameIdx != -1 && priceIdx != -1 &&
                            categoryIdx != -1 && restaurantIdx != -1) {

                        int id = cursor.getInt(idIdx);
                        String nameValue = cursor.getString(nameIdx);
                        double priceValue = cursor.getDouble(priceIdx);
                        int categoryId = cursor.getInt(categoryIdx);
                        int restaurantId = cursor.getInt(restaurantIdx);

                        FoodCategory foodCategory = foodCategoryDao.getFoodCategoryById(categoryId);
                        Restaurant restaurant = restaurantDao.getRestaurantById(restaurantId);
                        Food food = new Food(id, nameValue, priceValue, foodCategory, restaurant);
                        foods.add(food);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return foods;
    }
}
