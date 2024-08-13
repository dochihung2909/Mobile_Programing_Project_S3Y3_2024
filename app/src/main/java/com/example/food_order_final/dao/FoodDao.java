package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodDao extends BaseDao{
    private DatabaseHelper dbHelper;
    private FoodCategoryDao foodCategoryDao;
    private RestaurantDao restaurantDao;

    public FoodDao(DatabaseHelper dbHelper, FoodCategoryDao foodCategoryDao, RestaurantDao restaurantDao) {
        super(dbHelper);
        this.dbHelper = dbHelper;
        this.foodCategoryDao = foodCategoryDao;
        this.restaurantDao = restaurantDao;
    }

    public void insertFood(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getFoodContentValues(food);
        db.insert(DatabaseHelper.TABLE_FOOD_NAME, null, contentValues);
        db.close();
    }

    public int updateFood(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME_FIELD, food.getName());
        contentValues.put(DatabaseHelper.FOOD_PRICE_FIELD, food.getPrice());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_FIELD, food.getCategory().getId());
        contentValues.put(DatabaseHelper.FOOD_RESTAURANT_FIELD, food.getRestaurant().getId());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(food.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_FOOD_NAME, contentValues, whereClause, whereArgs);
        db.close();
        return rowAffected;
    }

    public void deleteFood(int foodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(foodId)};

        db.delete(DatabaseHelper.TABLE_FOOD_NAME, whereClause, whereArgs);
        db.close();
    }

    public Food getFoodById(int foodId) {
        Food food = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_NAME
                    + " WHERE " + DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(foodId)});

            if (cursor != null && cursor.moveToFirst()) {
                food = getFoodInfo(cursor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return food;
    }

    public List<Food> getFoodsByName(String foodName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Food> foods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_NAME
                            + " WHERE " + DatabaseHelper.FOOD_NAME_FIELD + " LIKE ?",
                    new String[]{"%" + foodName + "%"});

            if (cursor != null && cursor.moveToFirst()) {
                do {

                    foods.add(getFoodInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return foods;
    }

    public List<Food> getFoodsByRestaurantId(int resId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Food> foods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_NAME +
                            " WHERE " + DatabaseHelper.FOOD_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(resId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {

                    foods.add(getFoodInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return foods;
    }

    public List<Food> getAllFoodsInCart(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Food> foods = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT f.* FROM " + DatabaseHelper.TABLE_CART_NAME + " c " +
                    "INNER JOIN " + DatabaseHelper.TABLE_CART_DETAIL_NAME + " cd ON c." + DatabaseHelper.ID_FIELD + " = cd." + DatabaseHelper.CART_DETAIL_CART_FIELD + " " +
                    "INNER JOIN " + DatabaseHelper.TABLE_FOOD_NAME + " f ON cd." + DatabaseHelper.CART_DETAIL_FOOD_FIELD + " = f." + DatabaseHelper.ID_FIELD + " " +
                    "WHERE c." + DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(cartId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    foods.add(getFoodInfo(cursor));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return foods;
    }

    public Food getFoodInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        String name = getString(cursor, DatabaseHelper.FOOD_NAME_FIELD);
        double price  = getDouble(cursor, DatabaseHelper.FOOD_PRICE_FIELD);
        double discount  = getDouble(cursor, DatabaseHelper.FOOD_DISCOUNT_FIELD);
        int categoryId = getInt(cursor, DatabaseHelper.FOOD_CATEGORY_FIELD);
        FoodCategory category = foodCategoryDao.getFoodCategoryById(categoryId);
        int restaurantId = getInt(cursor, DatabaseHelper.FOOD_RESTAURANT_FIELD);
        Restaurant restaurant = restaurantDao.getRestaurantById(restaurantId);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new Food(id, name, price, discount, category, restaurant, createdDate, updatedDate));
    }
}
