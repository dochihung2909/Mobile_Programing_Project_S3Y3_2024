package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;
import com.google.common.primitives.Booleans;

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantDao extends BaseDao{
    private DatabaseHelper dbHelper;
    RestaurantCategoryDao resCateDao;

    public RestaurantDao(DatabaseHelper dbHelper, RestaurantCategoryDao resCateDao) {
        super(dbHelper);
        this.dbHelper = dbHelper;
        this.resCateDao = resCateDao;
    }

    public void insertRestaurant(Restaurant restaurant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getRestaurantContentValues(restaurant);
        db.insert(DatabaseHelper.TABLE_RESTAURANT_NAME, null, contentValues);
        db.close();
    }

    public int updateRestaurant(Restaurant restaurant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RESTAURANT_NAME_FIELD, restaurant.getName());
        contentValues.put(DatabaseHelper.RESTAURANT_ADDRESS_FIELD, restaurant.getAddress());
        contentValues.put(DatabaseHelper.RESTAURANT_PHONE_FIELD, restaurant.getPhoneNumber());
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_FIELD, restaurant.getCategory().getId());
        contentValues.put(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD, restaurant.isPartner());
        contentValues.put(DatabaseHelper.RESTAURANT_AVATAR_FIELD, restaurant.getAvatar());

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(restaurant.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_RESTAURANT_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRestaurant(int restaurantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(restaurantId)};

        db.delete(DatabaseHelper.TABLE_RESTAURANT_NAME, whereClause, whereArgs);
        db.close();
    }

    public int updateRestaurantRating(int restaurantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        int result = -1;

        try {
            cursor = db.rawQuery("SELECT AVG(" + DatabaseHelper.REVIEW_RATING_FIELD
                            + ") AS averageRating FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(restaurantId)});
            if (cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                double averageRating = getDouble(cursor, DatabaseHelper.RESTAURANT_RATING_FIELD);
                contentValues.put(DatabaseHelper.RESTAURANT_RATING_FIELD, averageRating);
                String whereClause = DatabaseHelper.ID_FIELD + " = ?";
                String[] whereArgs = new String[]{String.valueOf(restaurantId)};
                result = db.update(DatabaseHelper.TABLE_RESTAURANT_NAME, contentValues, whereClause, whereArgs);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        if (result == -1)
            throw new IllegalArgumentException("Failed to update rating.");

        return result;
    }
 
    public List<Restaurant> getAllRestaurants() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Restaurant> restaurants = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    Restaurant restaurant = getRestaurantInfo((cursor));
                    restaurants.add(restaurant);

                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        } 
        return restaurants;
    }

    public Restaurant getRestaurantById(int resId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Restaurant restaurant = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{String.valueOf(resId)});
            if (cursor != null && cursor.moveToFirst()) {

                restaurant = getRestaurantInfo(cursor);
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return restaurant;
    }

    public List<Restaurant> getRestaurantsByName(String resName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " LIKE ?",
                    new String[]{"%" + resName + "%"});
            if (cursor != null && cursor.moveToFirst()) {

                restaurants.add(getRestaurantInfo(cursor));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return restaurants;
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        Restaurant restaurant = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.RESTAURANT_NAME_FIELD + " = ?",
                    new String[]{restaurantName});

            if (cursor != null && cursor.moveToFirst()) {

                restaurant = getRestaurantInfo(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return restaurant;
    }

    public Restaurant getRestaurantByFoodId(int foodId) {
        Restaurant restaurant = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            cursor = db.rawQuery("SELECT r.* FROM " + DatabaseHelper.TABLE_FOOD_NAME + " f " +
                    " INNER JOIN " + DatabaseHelper.TABLE_RESTAURANT_NAME + " r on r." + DatabaseHelper.ID_FIELD + " = f." + DatabaseHelper.FOOD_RESTAURANT_FIELD +
                    "  WHERE f." + DatabaseHelper.FOOD_RESTAURANT_FIELD + " = ?", new String[]{String.valueOf(foodId)});

            if (cursor != null && cursor.moveToFirst()) {
                restaurant = getRestaurantInfo(cursor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return restaurant;
    }

    private Restaurant getRestaurantInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        String name = getString(cursor, DatabaseHelper.RESTAURANT_NAME_FIELD);
        String address = getString(cursor, DatabaseHelper.RESTAURANT_ADDRESS_FIELD);
        String phone = getString(cursor, DatabaseHelper.RESTAURANT_PHONE_FIELD);
        int cateId = getInt(cursor, DatabaseHelper.RESTAURANT_CATEGORY_FIELD);
        RestaurantCategory resCate = resCateDao.getRestaurantCategoryById(cateId);
        String avatar = getString(cursor, DatabaseHelper.RESTAURANT_AVATAR_FIELD);
        boolean isPartner = getBoolean(cursor, DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD);
        double rating = getDouble(cursor, DatabaseHelper.RESTAURANT_RATING_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new Restaurant(id, name, address, phone, resCate, avatar, isPartner, rating, createdDate, updatedDate));
    }

}
