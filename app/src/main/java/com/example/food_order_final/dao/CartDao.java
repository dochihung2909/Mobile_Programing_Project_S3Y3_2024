package com.example.food_order_final.dao;

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

    public void insertCart(Cart cart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = dbHelper.getCartContentValues(cart);
        db.insert(DatabaseHelper.TABLE_CART_NAME, null, contentValues);
        db.close();
    }

    public Cart addToCard(User user, Restaurant restaurant, Food food, int quantity) {
        Cart cart = null;
        if (!isUserHasCart(user.getId(), restaurant.getId())) {
            cart = new Cart(user, restaurant);
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


    public Cart getCartByUserId(int userId, int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        Cart cart = null;
        userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
        restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CART_NAME +
                            " WHERE " + DatabaseHelper.CART_USER_FIELD + " = ?"
                    +" AND " + DatabaseHelper.CART_RESTAURANT_FIELD + " = ?",
                    new String[]{String.valueOf(userId), String.valueOf(restaurantId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = getInt(cursor, DatabaseHelper.ID_FIELD);
                User user = userDao.getUserById(userId);
                int restaurant_id = getInt(cursor, DatabaseHelper.CART_RESTAURANT_FIELD);
                Restaurant restaurant = restaurantDao.getRestaurantById(restaurant_id);
                cart = new Cart(id, user, restaurant);
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
                cart = new Cart(id, user, restaurant);
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
            cursor = db.rawQuery("SELECT * FROM "
                    + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD
                    + " = ?", new String[]{String.valueOf(cartId)});
            totalDishes = cursor.getCount();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return totalDishes;
    }

    public double getTotalAmount(int cartId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        double totalAmount = 0;
        try {
            cursor = db.rawQuery("SELECT SUM("+ DatabaseHelper.CART_DETAIL_PRICE_FIELD +") FROM "
                    + DatabaseHelper.TABLE_CART_DETAIL_NAME
                    + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD
                    + " = ?", new String[]{String.valueOf(cartId)});
            totalAmount = cursor.getDouble(0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return totalAmount;
    }

    public double getTotalAmountByCartId(int cartId) {
        double totalAmount = 0;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT SUM(" + DatabaseHelper.CART_DETAIL_PRICE_FIELD + "*" + DatabaseHelper.CART_DETAIL_QUANTITY_FIELD +" ) FROM " + DatabaseHelper.TABLE_CART_DETAIL_NAME
            + " WHERE " + DatabaseHelper.CART_DETAIL_CART_FIELD + " = ?", new String[]{String.valueOf(cartId)});
            if (cursor.moveToFirst() && cursor != null) {
                totalAmount = cursor.getInt(0);
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
}
