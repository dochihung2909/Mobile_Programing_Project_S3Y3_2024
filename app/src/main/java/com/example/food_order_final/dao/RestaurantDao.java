package com.example.food_order_final.dao;

import static com.android.volley.VolleyLog.TAG;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

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

    public long insertRestaurant(Restaurant restaurant){
        long result = -1;
        User owner = restaurant.getOwner();
        boolean hasRestaurant = dbHelper.userDao.hasRestaurant(owner.getId());
        boolean isRestaurantExit = dbHelper.resDao.isRestaurantExists(restaurant);
        if (restaurant == null || hasRestaurant || isRestaurantExit) {
            if (restaurant == null) {
                Log.d(TAG, "Restaurant can not be null");
            } 
            if (hasRestaurant) {
                Log.d(TAG, "User " + owner.getUsername() + " already has restaurant!");
//            throw new IllegalArgumentException("User " + owner.getUsername() + " already has restaurant!");
            }
            if (isRestaurantExit) {
                Log.d(TAG, "Restaurant has name " + restaurant.getName() + " already exists at " + restaurant.getAddress());
//                throw new IllegalArgumentException("Restaurant has name " + restaurant.getName() + " already exists at " + restaurant.getAddress());
            }
        } else {
            Log.d(TAG, "Restaurant: " + restaurant);
            Log.d(TAG, "Name: " + restaurant.getName());
            Log.d(TAG, "Address: " + restaurant.getAddress());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues restaurantValues = dbHelper.getRestaurantContentValues(restaurant);
            result = db.insert(DatabaseHelper.TABLE_RESTAURANT_NAME, null, restaurantValues);

            db.close();
        }

        if (result == -1) {
            Log.e(TAG, "Failed to insert restaurant into the database.");
//            throw new IllegalArgumentException("Failed to insert restaurant into the database.");
        } else {
            Log.d(TAG, "Restaurant inserted successfully with ID: " + result);
        }

        return result;
    }

    public int updateRestaurant(Restaurant restaurant){
        Log.d("RestaurantDAO", "Restaurannt Name: " + restaurant.getName());
        Log.d("RestaurantDAO", "Restaurannt Address: " + restaurant.getAddress());
        Log.d("RestaurantDAO", "Restaurannt Phone: " + restaurant.getPhoneNumber());
        Log.d("RestaurantDAO", "Restaurannt Category: " + restaurant.getCategory().getName());
        Log.d("RestaurantDAO", "Restaurannt Owner: " + restaurant.getOwner().getUsername());
        Log.d("RestaurantDAO", "Restaurannt Avatar: " + restaurant.getAvatar());
        boolean isResExists = dbHelper.resDao.isRestaurantExists(restaurant);
        if (!isResExists) {
            Log.d("RestaurantDAO", "Not exists restaurant before");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.RESTAURANT_NAME_FIELD, restaurant.getName());
            contentValues.put(DatabaseHelper.RESTAURANT_ADDRESS_FIELD, restaurant.getAddress());
            contentValues.put(DatabaseHelper.RESTAURANT_PHONE_FIELD, restaurant.getPhoneNumber());
            contentValues.put(DatabaseHelper.RESTAURANT_CATEGORY_FIELD, restaurant.getCategory().getId());
            contentValues.put(DatabaseHelper.RESTAURANT_USER_FIELD, restaurant.getOwner().getId());
            contentValues.put(DatabaseHelper.RESTAURANT_IS_PARTNER_FIELD, restaurant.isPartner());
            contentValues.put(DatabaseHelper.RESTAURANT_AVATAR_FIELD, restaurant.getAvatar());
            contentValues.put(DatabaseHelper.RESTAURANT_USER_FIELD, restaurant.getOwner().getId());

            String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
            String[] whereArgs = new String[]{String.valueOf(restaurant.getId())};

            int rowAffected = db.update(DatabaseHelper.TABLE_RESTAURANT_NAME, contentValues, whereClause, whereArgs);

            db.close();
            return rowAffected;
        }
        return -1;
    }

    public void deleteRestaurant(int restaurantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(restaurantId)};

        db.delete(DatabaseHelper.TABLE_RESTAURANT_NAME, whereClause, whereArgs);
        db.close();
    }

    public boolean updateAllResRatings() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        boolean result = false;
        Cursor restaurantCursor = db.rawQuery("SELECT " + DatabaseHelper.ID_FIELD + " FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME, null);

        if (restaurantCursor.moveToFirst()) {
            do {
                int restaurantId = getInt(restaurantCursor, DatabaseHelper.ID_FIELD);

                Cursor ratingCursor = db.rawQuery("SELECT AVG(" + DatabaseHelper.RATING_FIELD
                                + ") AS averageRating FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                                + " WHERE " + DatabaseHelper.REVIEW_RESTAURANT_FIELD + " = ?",
                        new String[]{String.valueOf(restaurantId)});

                if (ratingCursor.moveToFirst()) {
                    double averageRating = getDouble(ratingCursor, "averageRating");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.RATING_FIELD, averageRating);
                    String whereClause = DatabaseHelper.ID_FIELD + " = ?";
                    String[] whereArgs = new String[]{String.valueOf(restaurantId)};
                    db.update(DatabaseHelper.TABLE_RESTAURANT_NAME, contentValues, whereClause, whereArgs);
                }
                ratingCursor.close();
            } while (restaurantCursor.moveToNext());
            result = true;
        }
        restaurantCursor.close();

        if (result == false)
            throw new IllegalArgumentException("Update all rating restaurant failed.");

        return result;
    }

    public int updateResRatingById(int restaurantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        int result = -1;

        try {
            cursor = db.rawQuery("SELECT AVG(" + DatabaseHelper.RATING_FIELD
                            + ") AS averageRating FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(restaurantId)});
            if (cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                double averageRating = getDouble(cursor, "averageRating");
                contentValues.put(DatabaseHelper.RATING_FIELD, averageRating);
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

    public boolean isRestaurantExists(Restaurant restaurant) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean result = false;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                    + " WHERE " + DatabaseHelper.RESTAURANT_NAME_FIELD + " = ? "
                    + " AND " + DatabaseHelper.RESTAURANT_ADDRESS_FIELD + " = ?",
                    new String[]{restaurant.getName(), restaurant.getAddress()});
            if (cursor.moveToFirst())
                result = true;
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return result;
    }

    public long countRestaurant() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        long total = 0;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME, null);
            if (cursor.moveToFirst()) {
                total = getLong(cursor, "total");
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return total;
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
                    restaurants.add(getRestaurantInfo(cursor));
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

    public Restaurant getRestaurantByUserId(int ownerId) {
        Restaurant restaurant = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                    + " WHERE " + DatabaseHelper.RESTAURANT_USER_FIELD + " = ? ", new String[]{String.valueOf(ownerId)});

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

    public List<Restaurant> getRestaurantsByName(String resName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " LIKE ?",
                    new String[]{"%" + resName + "%"});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    restaurants.add(getRestaurantInfo(cursor));
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

    public List<Food> getAllFoodsInRestaurant(int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Food> foods = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FOOD_NAME
                            + " WHERE " + DatabaseHelper.FOOD_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(restaurantId)});
            if (cursor.moveToFirst()) {
                do {
                    Food food = dbHelper.foodDao.getFoodInfo(cursor);
                    foods.add(food);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }

        return foods;
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
        double rating = getDouble(cursor, DatabaseHelper.RATING_FIELD);
        boolean isActived = getBoolean(cursor, DatabaseHelper.ACTIVE_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        int ownerId = getInt(cursor, DatabaseHelper.RESTAURANT_USER_FIELD);
        User owner = dbHelper.userDao.getUserById(ownerId);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new Restaurant(id, name, address, phone, resCate, avatar, owner, isPartner, rating, isActived, createdDate, updatedDate));
    }
}
