package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDetailDao extends BaseDao {
    DatabaseHelper dbHelper;
    CartDao cartDao;
    public CartDetailDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }


    public void insertCartDetail(CartDetail cartDetail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getCartDetailContentValues(cartDetail);
        db.insert(DatabaseHelper.TABLE_CART_DETAIL_NAME, null, contentValues);
        db.close();
    }

    public List<CartDetail> getAllCartDetailInCart(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<CartDetail> cartDetails = new ArrayList<>();
        try {

            cursor = db.rawQuery("SELECT cd.* FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME + " cd "
                    + "INNER JOIN " + DatabaseHelper.TABLE_CART_NAME + " c ON c." + DatabaseHelper.ID_FIELD + " = cd." + DatabaseHelper.CART_DETAIL_CART_FIELD
                    + " WHERE c." + DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(cartId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {

                    cartDetails.add(getCartDetailInfo(cursor));

                } while(cursor.moveToNext());
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return cartDetails;
    }

    public void updateFoodQuantity(int quantity, int cartDetailId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_DETAIL_QUANTITY_FIELD, quantity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(DatabaseHelper.TABLE_CART_DETAIL_NAME, contentValues, DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(cartDetailId)});
    }

    public void deleteCartDetail(int cartDetailId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART_DETAIL_NAME, DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(cartDetailId)});
    }

    public boolean isFoodInCart(int cartId, int foodId) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD + " = ?"
                    + " AND " + DatabaseHelper.CART_DETAIL_FOOD_FIELD + " = ?", new String[]{String.valueOf(cartId), String.valueOf(foodId)});

            if (cursor.getCount() > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return false;
    }

    public CartDetail getCartDetailByCartIdAndFoodId(int cartId, int foodId) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CartDetail cartDetail = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD + " = ?"
                    + " AND " + DatabaseHelper.CART_DETAIL_FOOD_FIELD + " = ?", new String[]{String.valueOf(cartId), String.valueOf(foodId)});

            if (cursor != null && cursor.moveToFirst()) {
                cartDetail = getCartDetailInfo(cursor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return cartDetail;
    }

    public CartDetail getCartDetailInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        CartDao cartDao = new CartDao(dbHelper);
        int cartId = getInt(cursor, DatabaseHelper.CART_DETAIL_CART_FIELD);
        Cart cart = cartDao.getCartById(cartId);
        int foodId = getInt(cursor, DatabaseHelper.CART_DETAIL_FOOD_FIELD);
        FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
        Food food = foodDao.getFoodById(foodId);
        int quantity = getInt(cursor, DatabaseHelper.CART_DETAIL_QUANTITY_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new CartDetail(id ,food, quantity, createdDate, updatedDate, cart));
    }

}
