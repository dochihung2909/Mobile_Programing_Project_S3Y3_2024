package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.CartDetail;

public class CartDetailDao extends BaseDao {
    DatabaseHelper dbHelper;
    CartDao cartDao;
    public CartDetailDao(DatabaseHelper dbHelper) {
        super(dbHelper);
    }


    public void insertCartDetail(CartDetail cartDetail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getCartDetailContentValues(cartDetail);
        db.insert(DatabaseHelper.TABLE_CART_NAME, null, contentValues);
        db.close();
    }

}
