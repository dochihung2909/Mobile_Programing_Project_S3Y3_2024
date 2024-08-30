package com.example.food_order_final.dao;

import static com.android.volley.VolleyLog.TAG;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDao extends BaseDao {

    private DatabaseHelper dbHelper;
    private UserDao userDao;

    private RestaurantDao restaurantDao;

    public CartDao(DatabaseHelper dbHelper, UserDao userDao, RestaurantDao restaurantDao) {
        super(dbHelper);
        this.dbHelper = dbHelper;
        this.restaurantDao = restaurantDao;
        this.userDao = userDao;
    }

    public CartDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    public long insertCart(Cart cart) {
        long result = -1;
        if (cart == null) {
            Log.d(TAG, "Cart can not be null!");
            // Other validates
        } else {
            Log.d(TAG, "Cart: " + cart);
            Log.d(TAG, "User: " + cart.getUser().getUsername());
            Log.d(TAG, "Restaurant: " + cart.getRestaurant().getName());
            Log.d(TAG, "Status: " + cart.getStatus());

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = dbHelper.getCartContentValues(cart);

            result = db.insert(DatabaseHelper.TABLE_CART_NAME, null, contentValues);
            db.close();
        }

        if (result == -1)
            Log.e(TAG, "Failed to insert cart into the database.");
        else
            Log.d(TAG, "Cart inserted successfully with ID: " + result);

        return result;
    }

    public void updateCartStatus(int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_STATUS, 1);
        db.update(DatabaseHelper.TABLE_CART_NAME, contentValues, DatabaseHelper.ID_FIELD+" = ?", new String[]{String.valueOf(cartId)});
        db.close();
    }


    public int updateCart(Cart cart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CART_USER_FIELD, cart.getUser().getId());
        values.put(DatabaseHelper.CART_RESTAURANT_FIELD, cart.getRestaurant().getId());
        values.put(DatabaseHelper.CART_STATUS, cart.getStatus());

        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(cart.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_CART_NAME, values, whereClause, whereArgs);
        db.close();

        return rowAffected;
    }

    public long countCart() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        long total = 0;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM " + DatabaseHelper.TABLE_CART_NAME, null);
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

    public double getTotalRevenue() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        double total = 0;
        try {
            cursor = db.rawQuery("SELECT *  FROM " + DatabaseHelper.TABLE_CART_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    int cartId = getInt(cursor, DatabaseHelper.ID_FIELD);
                    double amountACart = getTotalAmountByCartId(cartId);
                    total += amountACart;
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return total;
    }

    public Cart addToCard(User user, Restaurant restaurant, Food food, int quantity) {
        Cart cart = null;
        if (!isUserHasCart(user.getId(), restaurant.getId())) {
            cart = new Cart(user, restaurant, 0);
            this.insertCart(cart);
        }
        cart = this.getCartByUserId(user.getId(), restaurant.getId());

        CartDetail cartDetail = null;
        CartDetailDao cartDetailDao = new CartDetailDao(dbHelper);
        if (!cartDetailDao.isFoodInCart(cart.getId(), food.getId())) {
            cartDetail = new CartDetail(food, quantity, cart);
            cartDetailDao.insertCartDetail(cartDetail);
        } else {
            cartDetail = cartDetailDao.getCartDetailByCartIdAndFoodId(cart.getId(), food.getId());
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetailDao.updateFoodQuantity(cartDetail.getQuantity(), cartDetail.getId());
        }

        return cart;
    }

    public List<Cart> getAllCarts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Cart> carts = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    carts.add(getCartInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return carts;
    }

    public List<Cart> getCartsByUsername(String username) {
        Log.d("CartDAO", "Username: " + username);
        List<Cart> carts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME +
                    " INNER JOIN " + DatabaseHelper.TABLE_USER_NAME +
                    " ON " + DatabaseHelper.TABLE_CART_NAME + ".user_id = " + DatabaseHelper.TABLE_USER_NAME + ".id " +
                    " WHERE " + DatabaseHelper.TABLE_USER_NAME + ".username = ?",
                    new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cart cart = getCartInfo(cursor);
                    Log.d("CartDao", "Cart Info: " + cart.getId() + " - User: " + cart.getUser().getId());
                    carts.add(cart);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return carts;
    }


    public Cart getCartByUserId(int userId, int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        Cart cart = null;
        userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
        restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME +
                            " WHERE " + DatabaseHelper.CART_USER_FIELD + " = ?"
                    +" AND " + DatabaseHelper.CART_RESTAURANT_FIELD + " = ?"
                    +" AND " + DatabaseHelper.CART_STATUS + " = 0"
                    , new String[]{String.valueOf(userId), String.valueOf(restaurantId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                User user = userDao.getUserById(userId);
                int restaurant_id = getInt(cursor, DatabaseHelper.CART_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
                int status = getInt(cursor, DatabaseHelper.CART_STATUS);
                cart = new Cart(id, user, restaurant, status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return cart;
    }

    public Cart getCartById(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        Cart cart = null;
        userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
        restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME +
                            " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{String.valueOf(cartId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                int userId = getInt(cursor, DatabaseHelper.CART_USER_FIELD);
                User user = userDao.getUserById(userId);
                int restaurant_id = getInt(cursor, DatabaseHelper.CART_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);

                int status = getInt(cursor, DatabaseHelper.CART_STATUS);
                cart = new Cart(id, user, restaurant, status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return cart;
    }

    public boolean isUserHasCart(int userId, int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME
                            + " WHERE " + DatabaseHelper.CART_USER_FIELD + " = ?"
                            + " AND " + DatabaseHelper.CART_RESTAURANT_FIELD + " = ?"
                            + " AND " + DatabaseHelper.CART_STATUS + " = 0"
                    , new String[]{String.valueOf(userId), String.valueOf(restaurantId)});
            if(cursor.getCount() > 0){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return false;
    }

    public int getTotalDishes(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int totalDishes = 0;
        try {
            cursor = db.rawQuery("SELECT SUM("+ DatabaseHelper.CART_DETAIL_QUANTITY_FIELD +") FROM "
                    + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD
                    + " = ?", new String[]{String.valueOf(cartId)});
            if (cursor.moveToFirst() && cursor != null) {
                totalDishes = cursor.getInt(0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return totalDishes;
    }

    public double getTotalAmountByCartId(int cartId) {
        double totalAmount = 0;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT SUM(" + DatabaseHelper.CART_DETAIL_PRICE_FIELD + "*" + DatabaseHelper.CART_DETAIL_QUANTITY_FIELD
                    +" ) FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD + " = ?", new String[]{String.valueOf(cartId)});
            if (cursor.moveToFirst() && cursor != null) {
                totalAmount = cursor.getDouble(0);
            }
        } catch (Exception e)  {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return totalAmount;
    }

    public void deleteCart(int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART_NAME, DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(cartId)});
    }

    public boolean isCartEmpty(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD + " = ?", new String[]{String.valueOf(cartId)});
            if (cursor.getCount() <= 0) {
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

    private Cart getCartInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        int userId = getInt(cursor, DatabaseHelper.CART_USER_FIELD);
        User user = dbHelper.userDao.getUserById(userId);
        int restaurantId = getInt(cursor, DatabaseHelper.CART_RESTAURANT_FIELD);
        Restaurant restaurant = dbHelper.resDao.getRestaurantById(restaurantId);
        int status = getInt(cursor, DatabaseHelper.CART_STATUS);
        boolean actived = getBoolean(cursor, DatabaseHelper.ACTIVE_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new Cart(id, user, restaurant, status, actived, createdDate, updatedDate));
    }
}
