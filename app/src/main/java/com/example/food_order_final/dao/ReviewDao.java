package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.Review;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDao extends BaseDao{
    private UserDao userDao;
    private RestaurantDao restaurantDao;

    public ReviewDao(DatabaseHelper dbHelper, UserDao userDao, RestaurantDao restaurantDao) {
        super(dbHelper);
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
    }

    public long insertReview(Review review) {
        if (review.getRating() < 0 || review.getRating() > 5)
            throw new IllegalArgumentException("Rating must be between 0 and 5.");

        long result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, review.getComment());
        contentValues.put(DatabaseHelper.REVIEW_USER_FIELD, review.getUser().getId());
        contentValues.put(DatabaseHelper.REVIEW_RESTAURANT_FIELD, review.getRestaurant().getId());
        contentValues.put(DatabaseHelper.CREATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        result = db.insert(DatabaseHelper.TABLE_REVIEW_NAME, null, contentValues);

        if (result == -1)
            throw new IllegalArgumentException("Failed to insert review into the database.");

        return result;
    }

    public int updateReview(Review review){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, review.getComment());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(review.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_REVIEW_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(reviewId)};

        db.delete(DatabaseHelper.TABLE_REVIEW_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<Review> getAllReviews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Review> reviews = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_NAME, null);
            do {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                int user_id = getInt(cursor, DatabaseHelper.REVIEW_USER_FIELD);
                String comment = getString(cursor, DatabaseHelper.REVIEW_COMMENT_FIELD);
                double rating = getDouble(cursor, DatabaseHelper.REVIEW_RATING_FIELD);
                User user = userDao.getUserById(user_id);
                int restaurant_id = getInt(cursor, DatabaseHelper.REVIEW_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                reviews.add(new Review(id, comment, rating, user, restaurant, createdDate, updatedDate));

            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviews;
    }

    public List<Review> getReviewsByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Review> reviews = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_USER_FIELD + " = ?",
                    new String[]{String.valueOf(userId)});
            do {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                int user_id = getInt(cursor, DatabaseHelper.REVIEW_USER_FIELD);
                String comment = getString(cursor, DatabaseHelper.REVIEW_COMMENT_FIELD);
                double rating = getDouble(cursor, DatabaseHelper.REVIEW_RATING_FIELD);
                User user = userDao.getUserById(user_id);
                int restaurant_id = getInt(cursor, DatabaseHelper.REVIEW_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                reviews.add(new Review(id, comment, rating, user, restaurant, createdDate, updatedDate));

            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviews;
    }

    public List<Review> getReviewsByRestaurantId(int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Review> reviews = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(restaurantId)});
            do {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                int user_id = getInt(cursor, DatabaseHelper.REVIEW_USER_FIELD);
                String comment = getString(cursor, DatabaseHelper.REVIEW_COMMENT_FIELD);
                double rating = getDouble(cursor, DatabaseHelper.REVIEW_RATING_FIELD);
                User user = userDao.getUserById(user_id);
                int restaurant_id = getInt(cursor, DatabaseHelper.REVIEW_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
                String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
                Date createdDate = DateUtil.timestampToDate(createdDateString);
                String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
                Date updatedDate = DateUtil.timestampToDate(updatedDateString);

                reviews.add(new Review(id, comment, rating, user, restaurant, createdDate, updatedDate));

            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviews;
    }
}
