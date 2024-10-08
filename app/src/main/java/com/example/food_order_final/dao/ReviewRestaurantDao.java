package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.ReviewRestaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewRestaurantDao extends BaseDao{
    private UserDao userDao;
    private RestaurantDao restaurantDao;

    public ReviewRestaurantDao(DatabaseHelper dbHelper, UserDao userDao, RestaurantDao restaurantDao) {
        super(dbHelper);
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
    }

    public long insertReview(ReviewRestaurant reviewRestaurant) {
        if (reviewRestaurant.getRating() < 0 || reviewRestaurant.getRating() > 5)
            throw new IllegalArgumentException("Rating must be between 0 and 5.");

        long result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getReviewResContentValues(reviewRestaurant);

        try {
            result = db.insert(DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME, null, contentValues);

            if (result == -1)
                throw new IllegalArgumentException("Failed to insert review into the database.");

            int restaurantId = reviewRestaurant.getRestaurant().getId();
            dbHelper.resDao.updateResRatingById(restaurantId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result;
    }

    public int updateReview(ReviewRestaurant reviewRestaurant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, reviewRestaurant.getComment());
        contentValues.put(DatabaseHelper.RATING_FIELD, reviewRestaurant.getRating());
        contentValues.put(DatabaseHelper.REVIEW_IMAGE_FIELD, reviewRestaurant.getImage());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(reviewRestaurant.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(reviewId)};

        db.delete(DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<ReviewRestaurant> getAllReviews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewRestaurant> reviewRestaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME + " WHERE " + DatabaseHelper.ACTIVE_FIELD + " = 1", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ReviewRestaurant reviewRestaurant = getReviewResInfo(cursor);
                    reviewRestaurants.add(reviewRestaurant);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return reviewRestaurants;
    }

    public ReviewRestaurant getReviewById(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        ReviewRestaurant reviewRestaurant = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(reviewId)});

            if (cursor != null && cursor.moveToFirst()) {
                reviewRestaurant = getReviewResInfo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return reviewRestaurant;
    }

    public List<ReviewRestaurant> getReviewsByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewRestaurant> reviewRestaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_USER_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(userId)});
            do {
                ReviewRestaurant reviewRestaurant = getReviewResInfo(cursor);
                reviewRestaurants.add(reviewRestaurant);
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviewRestaurants;
    }

    public List<ReviewRestaurant> getReviewsByRestaurantId(int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewRestaurant> reviewRestaurants = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_RESTAURANT_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_RESTAURANT_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(restaurantId)});
            do {
                ReviewRestaurant reviewRestaurant = getReviewResInfo(cursor);
                reviewRestaurants.add(reviewRestaurant);
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviewRestaurants;
    }

    public ReviewRestaurant getReviewResInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        int user_id = getInt(cursor, DatabaseHelper.REVIEW_USER_FIELD);
        String comment = getString(cursor, DatabaseHelper.REVIEW_COMMENT_FIELD);
        double rating = getDouble(cursor, DatabaseHelper.RATING_FIELD);
        String image = getString(cursor, DatabaseHelper.REVIEW_IMAGE_FIELD);
        User user = userDao.getUserById(user_id);
        int restaurant_id = getInt(cursor, DatabaseHelper.REVIEW_RESTAURANT_FIELD);
        Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
        boolean isActived = getBoolean(cursor, DatabaseHelper.ACTIVE_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new ReviewRestaurant(id, comment, rating, image, user, restaurant, isActived, createdDate, updatedDate));
    }
}
