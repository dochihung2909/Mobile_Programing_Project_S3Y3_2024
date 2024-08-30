package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewFoodDao extends BaseDao{
    private UserDao userDao;
    private FoodDao foodDao;

    public ReviewFoodDao(DatabaseHelper dbHelper, UserDao userDao, FoodDao foodDao) {
        super(dbHelper);
        this.userDao = userDao;
        this.foodDao = foodDao;
    }

    public long insertReview(ReviewFood reviewFood) {
        if (reviewFood.getRating() < 0 || reviewFood.getRating() > 5)
            throw new IllegalArgumentException("Rating must be between 0 and 5.");

        long result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getReviewFoodContentValues(reviewFood);

        try {
            result = db.insert(DatabaseHelper.TABLE_REVIEW_FOOD_NAME, null, contentValues);

            if (result == -1)
                throw new IllegalArgumentException("Failed to insert review into the database.");

            int foodId = reviewFood.getFood().getId();
            dbHelper.foodDao.updateFoodRatingByFoodId(foodId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result;
    }

    public int updateReview(ReviewFood reviewFood){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, reviewFood.getComment());
        contentValues.put(DatabaseHelper.RATING_FIELD, reviewFood.getRating());
        contentValues.put(DatabaseHelper.REVIEW_IMAGE_FIELD, reviewFood.getImage());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(reviewFood.getId())};
        int rowAffected = db.update(DatabaseHelper.TABLE_REVIEW_FOOD_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public void deleteReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(reviewId)};

        db.delete(DatabaseHelper.TABLE_REVIEW_FOOD_NAME, whereClause, whereArgs);
        db.close();
    }

    public List<ReviewFood> getAllReviews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewFood> reviewFoods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_FOOD_NAME + " WHERE " + DatabaseHelper.ACTIVE_FIELD + " = 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    reviewFoods.add(getReviewFoodInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return reviewFoods;
    }

    public ReviewFood getReviewById(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        ReviewFood reviewFood = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_FOOD_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(reviewId)});

            if (cursor != null && cursor.moveToFirst()) {
                reviewFood = getReviewFoodInfo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return reviewFood;
    }

    public List<ReviewFood> getReviewsByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewFood> reviewFoods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_FOOD_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_USER_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(userId)});
            do {
                ReviewFood reviewFood = getReviewFoodInfo(cursor);
                reviewFoods.add(reviewFood);
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviewFoods;
    }

    public List<ReviewFood> getReviewsByFoodId(int foodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<ReviewFood> reviewFoods = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEW_FOOD_NAME
                            + " WHERE " + DatabaseHelper.REVIEW_FOOD_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(foodId)});
            if (cursor.moveToFirst()) {
                do {
                    reviewFoods.add(getReviewFoodInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return reviewFoods;
    }

    public ReviewFood getReviewFoodInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        int user_id = getInt(cursor, DatabaseHelper.REVIEW_USER_FIELD);
        String comment = getString(cursor, DatabaseHelper.REVIEW_COMMENT_FIELD);
        double rating = getDouble(cursor, DatabaseHelper.RATING_FIELD);
        String image = getString(cursor, DatabaseHelper.REVIEW_IMAGE_FIELD);
        User user = userDao.getUserById(user_id);
        int food_id = getInt(cursor, DatabaseHelper.REVIEW_FOOD_FIELD);
        Food food = foodDao.getFoodById(food_id);
        boolean isActived = getBoolean(cursor, DatabaseHelper.ACTIVE_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new ReviewFood(id, comment, rating, image, user, food, isActived, createdDate, updatedDate));
    }
}
