package com.example.food_order_final.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.Payment;
import com.example.food_order_final.models.PaymentMethod;
import com.example.food_order_final.models.PaymentPending;
import com.example.food_order_final.models.PaymentStatus;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentPendingDao extends BaseDao{

    private  DatabaseHelper dbHelper;
    public PaymentPendingDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    public void insertPaymentPending(PaymentPending paymentPending) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        contentValues.put(DatabaseHelper.PAYMENT_PENDING_STATUS, paymentPending.getPaymentStatus().getStatus());
        contentValues.put(DatabaseHelper.PAYMENT_PENDING_CART, paymentPending.getCart().getId());
        contentValues.put(DatabaseHelper.PAYMENT_PENDING_TOTAL, paymentPending.getTotal());
        contentValues.put(DatabaseHelper.PAYMENT_PENDING_METHOD, paymentPending.getPaymentMethod().getMethod());
        contentValues.put(DatabaseHelper.PAYMENT_PENDING_NOTE, paymentPending.getNote());
        contentValues.put(DatabaseHelper.CREATED_DATE_FIELD, DateUtil.dateToTimestamp(paymentPending.getCreatedDate()));
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(paymentPending.getUpdatedDate()));
        db.insert(DatabaseHelper.TABLE_PAYMENT_PENDING_NAME, null, contentValues);
        db.close();
    }

    public List<PaymentPending> getPaymentHistoryByUserId(int userId) {

        List<PaymentPending> paymentPendings = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT p.* FROM " + DatabaseHelper.TABLE_PAYMENT_PENDING_NAME +
                    " p INNER JOIN " + DatabaseHelper.TABLE_CART_NAME + " c on c." + DatabaseHelper.ID_FIELD + " = p." + DatabaseHelper.PAYMENT_PENDING_CART +
                    " WHERE " + DatabaseHelper.CART_USER_FIELD + " = ?", new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    paymentPendings.add(getPaymentPendingInfo(cursor));
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

        return paymentPendings;
    }

    public ArrayList<PaymentPending> getAllPaymentByRestaurantId(int restaurantId) {
        Cursor cursor = null;
        ArrayList<PaymentPending> paymentPendings = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            cursor = db.rawQuery("SELECT p.* from " + DatabaseHelper.TABLE_PAYMENT_PENDING_NAME + " p " +
                    "INNER JOIN " + DatabaseHelper.TABLE_CART_NAME + " c ON c." + DatabaseHelper.ID_FIELD + " = p." + DatabaseHelper.PAYMENT_PENDING_CART +
                    " WHERE " + DatabaseHelper.CART_RESTAURANT_FIELD + " = ?", new String[]{String.valueOf(restaurantId)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    paymentPendings.add(getPaymentPendingInfo(cursor));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentPendings;

    }

    public PaymentPending getPaymentPendingInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        int methodId = getInt(cursor, DatabaseHelper.PAYMENT_PENDING_METHOD);
        PaymentMethod paymentMethod = PaymentMethod.fromMethod(methodId);
        int statusId = getInt(cursor, DatabaseHelper.PAYMENT_PENDING_STATUS);
        PaymentStatus paymentStatus = PaymentStatus.fromStatus(statusId);
        int cartId = getInt(cursor, DatabaseHelper.PAYMENT_PENDING_CART);
        CartDao cartDao = new CartDao(dbHelper);
        double total = getDouble(cursor, DatabaseHelper.PAYMENT_PENDING_TOTAL);
        Cart cart = cartDao.getCartById(cartId);
        String note = getString(cursor, DatabaseHelper.PAYMENT_PENDING_NOTE);

        return (new PaymentPending(id, paymentStatus, cart, total, paymentMethod, note));
    }

}
