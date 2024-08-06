package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.google.common.primitives.Booleans;

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDao {
    private DatabaseHelper dbHelper;
    RestaurantCategoryDao resCateDao;

    public RestaurantDao(DatabaseHelper dbHelper, RestaurantCategoryDao resCateDao) {
        this.dbHelper = dbHelper;
        this.resCateDao = resCateDao;
    }

    public void insertRestaurant(Restaurant restaurant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RESTAURANT_NAME_FIELD, restaurant.getName());
        contentValues.put(DatabaseHelper.RESTAURANT_ADDRESS_FIELD, restaurant.getAddress());
        contentValues.put(DatabaseHelper.RESTAURANT_PHONE_FIELD, restaurant.getPhoneNumber());
        contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_FIELD, restaurant.getCategory().getId());
        contentValues.put(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD, restaurant.isPartner());

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

        String whereClause = DatabaseHelper.RESTAURANT_ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(restaurant.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_RESTAURANT_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteRestaurant(int restaurantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.RESTAURANT_ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(restaurantId)};

        db.delete(DatabaseHelper.TABLE_USER_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<Restaurant> getAllRestaurant() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Restaurant> restaurants = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_ID_FIELD);
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_NAME_FIELD);
                    int addressIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_ADDRESS_FIELD);
                    int phoneIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_PHONE_FIELD);
                    int cateIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_CATEGORY_FIELD);
                    int isPartnerIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD);
                    if (idIndex != -1 && nameIndex != -1 && addressIndex != -1 && phoneIndex != -1 &&
                            cateIndex != -1 && isPartnerIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String name = cursor.getString(nameIndex);
                        String address = cursor.getString(addressIndex);
                        String phone = cursor.getString(phoneIndex);
                        int cateId = cursor.getInt(cateIndex);
                        boolean isPartner = cursor.getInt(isPartnerIndex) > 0;
                        RestaurantCategory resCate = resCateDao.getRestaurantCategoryById(cateId);
                        Restaurant restaurant = new Restaurant(id, name, address, phone, resCate, isPartner);
                        restaurants.add(restaurant);
                    } else {
                        throw new RuntimeException("Column index not found !");
                    }
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                            + " WHERE " + DatabaseHelper.RESTAURANT_ID_FIELD + " = ?",
                    new String[]{String.valueOf(resId)});
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_ID_FIELD);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_NAME_FIELD);
                int addressIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_ADDRESS_FIELD);
                int phoneIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_PHONE_FIELD);
                int cateIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_CATEGORY_FIELD);
                int isPartnerIndex = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD);
                if (idIndex != -1 && nameIndex != -1 && addressIndex != -1 && phoneIndex != -1 &&
                        cateIndex != -1 && isPartnerIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String address = cursor.getString(nameIndex);
                    String phone = cursor.getString(nameIndex);
                    int cateId = cursor.getInt(nameIndex);
                    boolean isPartner = cursor.getInt(nameIndex) > 0;
                    RestaurantCategory resCate = resCateDao.getRestaurantCategoryById(cateId);
                    restaurant = new Restaurant(id, name, address, phone, resCate, isPartner);
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
        return restaurant;
    }

    public List<Restaurant> findRestaurantByName(String resName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.RESTAURANT_NAME_FIELD + " = ? ",
                    new String[]{resName});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIdx = cursor.getColumnIndex(DatabaseHelper.USER_ID_FIELD);
                    int nameIdx = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_NAME_FIELD);
                    int addressIdx = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_ADDRESS_FIELD);
                    int phoneIdx = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_PHONE_FIELD);
                    int cateIdx = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_CATEGORY_FIELD);
                    int isPartnerIdx = cursor.getColumnIndex(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD);

                    if (idIdx != -1 && nameIdx != -1 && addressIdx != -1 &&
                            phoneIdx != -1 && cateIdx != -1 && isPartnerIdx != -1) {

                        int id = cursor.getInt(idIdx);
                        String nameValue = cursor.getString(nameIdx);
                        String addressValue = cursor.getString(addressIdx);
                        String phoneValue = cursor.getString(phoneIdx);
                        int resCateId = cursor.getInt(cateIdx);
                        boolean isPartnerValue = cursor.getInt(isPartnerIdx) > 0;

                        RestaurantCategory resCate = resCateDao.getRestaurantCategoryById(resCateId);
                        Restaurant restaurant = new Restaurant(id, nameValue, addressValue, phoneValue, resCate, isPartnerValue);
                        restaurants.add(restaurant);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return restaurants;
    }

}
