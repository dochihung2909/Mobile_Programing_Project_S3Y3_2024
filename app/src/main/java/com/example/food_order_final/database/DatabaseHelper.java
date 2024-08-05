package com.example.food_order_final.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database setup
    public static final String DATABASE_NAME = "FoodOrderDB";
    public static final int DATABASE_VERSION = 1;

    // Table setup
    public static final String TABLE_ROLE_NAME = "Role";
    public static final String TABLE_USER_NAME = "User";
    public static final String TABLE_RESTAURANT_CATEGORY_NAME = "RestaurantCategory";
    public static final String TABLE_RESTAURANT_NAME = "Restaurant";
    public static final String TABLE_FOOD_CATEGORY_NAME = "FoodCategory";
    public static final String TABLE_FOOD_NAME = "Food";

    // Table Role columns
    public static final String ROLE_ID_FIELD = "id";
    public static final String ROLE_NAME_FIELD = "name";

    // Table User columns
    public static final String USER_ID_FIELD = "id";
    public static final String USER_FULL_NAME_FIELD = "full_name";
    public static final String USER_PHONE_NUMBER_FIELD = "phone_number";
    public static final String USER_EMAIL_FIELD = "email";
    public static final String USER_USERNAME_FIELD = "username";
    public static final String USER_PASSWORD_FIELD = "password";
    public static final String USER_ROLE_FIELD = "role_id";


    // Table Restaurant Category columns
    public static final String RESTAURANT_CATEGORY_ID_FIELD = "id";
    public static final String RESTAURANT_CATEGORY_NAME_FIELD = "name";

    // Table Restaurant columns
    public static final String RESTAURANT_ID_FIELD = "id";
    public static final String RESTAURANT_NAME_FIELD = "name";
    public static final String RESTAURANT_ADDRESS_FIELD = "address";
    public static final String RESTAURANT_PHONE_FIELD = "phone";
    public static final String RESTAURANT_CATEGORY_FIELD = "category";
    public static final String RESTAURANT_IS_PARTNER_FIELD = "is_partner";

    // Table Food Category columns
    public static final String FOOD_CATEGORY_ID_FIELD = "id";
    public static final String FOOD_CATEGORY_NAME_FIELD = "name";

    // Table Food columns
    public static final String FOOD_ID_FIELD = "id";
    public static final String FOOD_NAME_FIELD = "name";
    public static final String FOOD_PRICE_FIELD = "price";
    public static final String FOOD_CATEGORY_FIELD = "category";
    public static final String FOOD_RESTAURANT_FIELD = "restaurant";

    public RoleDao roleDao;
    public UserDao userDao;
    public RestaurantCategoryDao resCateDao;
    public RestaurantDao resDao;
    public FoodCategoryDao foodCateDao;
    public FoodDao foodDao;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.roleDao = new RoleDao(this);
        this.userDao = new UserDao(this, this.roleDao);
        this.resCateDao = new RestaurantCategoryDao(this);
        this.resDao = new RestaurantDao(this, this.resCateDao);
        this.foodCateDao = new FoodCategoryDao(this);
        this.foodDao = new FoodDao(this, this.foodCateDao, this.resDao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlRole = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT)",
                TABLE_ROLE_NAME,
                ROLE_ID_FIELD,
                ROLE_NAME_FIELD);
        db.execSQL(sqlRole);

        String sqlUser = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INT)",
                TABLE_USER_NAME, USER_ID_FIELD,
                USER_USERNAME_FIELD, USER_PHONE_NUMBER_FIELD, USER_EMAIL_FIELD,
                USER_FULL_NAME_FIELD, USER_PASSWORD_FIELD, USER_ROLE_FIELD);
        db.execSQL(sqlUser);

        String sqlRestaurantCate = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT)",
                TABLE_RESTAURANT_CATEGORY_NAME,
                RESTAURANT_CATEGORY_ID_FIELD,
                RESTAURANT_CATEGORY_NAME_FIELD);
        db.execSQL(sqlRestaurantCate);

        String sqlRestaurant = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s BOOLEAN)",
                TABLE_RESTAURANT_NAME,
                RESTAURANT_ID_FIELD,
                RESTAURANT_NAME_FIELD, RESTAURANT_ADDRESS_FIELD, RESTAURANT_PHONE_FIELD,
                RESTAURANT_CATEGORY_FIELD, RESTAURANT_IS_PARTNER_FIELD);
        db.execSQL(sqlRestaurant);

        String sqlFoodCate = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT)",
                TABLE_FOOD_CATEGORY_NAME,
                FOOD_CATEGORY_ID_FIELD,
                FOOD_CATEGORY_NAME_FIELD);
        db.execSQL(sqlFoodCate);

        String sqlFood = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_FOOD_NAME,
                FOOD_ID_FIELD,
                FOOD_NAME_FIELD, FOOD_PRICE_FIELD, FOOD_CATEGORY_FIELD, FOOD_RESTAURANT_FIELD);
        db.execSQL(sqlFood);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        onCreate(db);
    }

    public void addUserToDatabase(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(USER_FULL_NAME_FIELD, user.getFullName());
        contentValues.put(USER_PASSWORD_FIELD, user.getPassword());
        db.insert(TABLE_USER_NAME, null, contentValues);
        db.close();
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT * FROM "
                    + TABLE_USER_NAME
                    + " WHERE "
                    + USER_USERNAME_FIELD
                    + " = ?", new String[]{username});

            if (cursor.moveToFirst()) {
                exists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return exists;
    }

    public boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT * FROM "
                    + TABLE_USER_NAME
                    + " WHERE "
                    + USER_USERNAME_FIELD
                    + " = ?"
                    + " AND "
                    + USER_PASSWORD_FIELD
                    + " = ?", new String[]{username, password});

            if (cursor.moveToFirst()) {
                exists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return exists;
    }

    public void initializeDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Role data
        Role adminRole = new Role(0, "Admin");
        Role userRole = new Role(1, "User");
        roleDao.insertRole(adminRole);
        roleDao.insertRole(userRole);

        // User data
        User u1 = new User("halent", "0364452867", "hung@gmail.com",
                "Nguyễn Văn Hùng", "123", adminRole);
        User u2 = new User("john_doe", "0987654321", "john.doe@example.com",
                "John Doe", "123", adminRole);
        User u3 = new User("alice_smith", "0912345678", "alice.smith@example.com",
                "Alice Smith", "123", userRole);
        User u4 = new User("bob_jones", "0901234567", "bob.jones@example.com",
                "Bob Jones", "123", userRole);
        User u5 = new User("carol_brown", "0987123456", "carol.brown@example.com",
                "Carol Brown", "123", userRole);
        User u6 = new User("dave_wilson", "0898765432", "dave.wilson@example.com",
                "Dave Wilson", "123", userRole);
        User u7 = new User("eve_white", "0876543210", "eve.white@example.com",
                "Eve White", "123", userRole);
        User u8 = new User("frank_black", "0865432109", "frank.black@example.com",
                "Frank Black", "123", userRole);
        User u9 = new User("grace_green", "0854321098", "grace.green@example.com",
                "Grace Green", "123", userRole);
        User u10 = new User("henry_adams", "0843210987", "henry.adams@example.com",
                "Henry Adams", "123", userRole);
        userDao.insertUser(u1);
        userDao.insertUser(u2);
        userDao.insertUser(u3);
        userDao.insertUser(u4);
        userDao.insertUser(u5);
        userDao.insertUser(u6);
        userDao.insertUser(u7);
        userDao.insertUser(u8);
        userDao.insertUser(u9);
        userDao.insertUser(u10);

        //Restaurant Category data
        RestaurantCategory rc1 = new RestaurantCategory("Noodles");
        RestaurantCategory rc2 = new RestaurantCategory("Pizza");
        RestaurantCategory rc3 = new RestaurantCategory("Burgers");
        RestaurantCategory rc4 = new RestaurantCategory("Sushi");
        RestaurantCategory rc5 = new RestaurantCategory("Mexican");
        RestaurantCategory rc6 = new RestaurantCategory("Indian");
        RestaurantCategory rc7 = new RestaurantCategory("Chinese");
        RestaurantCategory rc8 = new RestaurantCategory("Italian");
        RestaurantCategory rc9 = new RestaurantCategory("Desserts");
        resCateDao.insertRestaurantCategory(rc1);
        resCateDao.insertRestaurantCategory(rc2);
        resCateDao.insertRestaurantCategory(rc3);
        resCateDao.insertRestaurantCategory(rc4);
        resCateDao.insertRestaurantCategory(rc5);
        resCateDao.insertRestaurantCategory(rc6);
        resCateDao.insertRestaurantCategory(rc7);
        resCateDao.insertRestaurantCategory(rc8);
        resCateDao.insertRestaurantCategory(rc9);

        // Restaurant data
        Restaurant res1 = new Restaurant("KFC", "123 Washington", "057265484", rc1);
        Restaurant res2 = new Restaurant("Domino's Pizza", "456 Elm Street", "0123456789", rc2);
        Restaurant res3 = new Restaurant("Burger King", "789 Oak Avenue", "0987654321", rc3);
        Restaurant res4 = new Restaurant("Sakura Sushi", "321 Pine Road", "0234567890", rc4);
        Restaurant res5 = new Restaurant("Taco Bell", "654 Maple Lane", "0345678901", rc5);
        Restaurant res6 = new Restaurant("Curry House", "987 Birch Boulevard", "0456789012", rc6);
        Restaurant res7 = new Restaurant("Great Wall Chinese", "135 Cedar Street", "0567890123", rc7);
        Restaurant res8 = new Restaurant("La Dolce Vita", "246 Fir Street", "0678901234", rc8);
        Restaurant res9 = new Restaurant("Sweet Tooth", "357 Spruce Road", "0789012345", rc9);
        Restaurant res10 = new Restaurant("Panda Express", "468 Willow Drive", "0890123456", rc7);
        resDao.insertRestaurant(res1);
        resDao.insertRestaurant(res2);
        resDao.insertRestaurant(res3);
        resDao.insertRestaurant(res4);
        resDao.insertRestaurant(res5);
        resDao.insertRestaurant(res6);
        resDao.insertRestaurant(res7);
        resDao.insertRestaurant(res8);
        resDao.insertRestaurant(res9);
        resDao.insertRestaurant(res10);

        //Food Category data
        FoodCategory fc1 = new FoodCategory("Spaghetti");
        FoodCategory fc2 = new FoodCategory("Pizza");
        FoodCategory fc3 = new FoodCategory("Sushi");
        FoodCategory fc4 = new FoodCategory("Burgers");
        FoodCategory fc5 = new FoodCategory("Tacos");
        FoodCategory fc6 = new FoodCategory("Salads");
        FoodCategory fc7 = new FoodCategory("Sandwiches");
        FoodCategory fc8 = new FoodCategory("Desserts");
        FoodCategory fc9 = new FoodCategory("Seafood");
        FoodCategory fc10 = new FoodCategory("Soups");
        foodCateDao.insertFoodCategory(fc1);
        foodCateDao.insertFoodCategory(fc2);
        foodCateDao.insertFoodCategory(fc3);
        foodCateDao.insertFoodCategory(fc4);
        foodCateDao.insertFoodCategory(fc5);
        foodCateDao.insertFoodCategory(fc6);
        foodCateDao.insertFoodCategory(fc7);
        foodCateDao.insertFoodCategory(fc8);
        foodCateDao.insertFoodCategory(fc9);
        foodCateDao.insertFoodCategory(fc10);

        // Food data
        Food f1 = new Food("Minced beef spaghetti", 45000, fc1, res1);
        Food f2 = new Food("Pepperoni Pizza", 55000, fc2, res2);
        Food f3 = new Food("California Roll", 60000, fc3, res4);
        Food f4 = new Food("Cheeseburger", 40000, fc4, res3);
        Food f5 = new Food("Beef Tacos", 35000, fc5, res5);
        Food f6 = new Food("Caesar Salad", 30000, fc6, res6);
        Food f7 = new Food("Club Sandwich", 32000, fc7, res7);
        Food f8 = new Food("Chocolate Cake", 28000, fc8, res8);
        Food f9 = new Food("Grilled Salmon", 70000, fc9, res9);
        Food f10 = new Food("Tom Yum Soup", 33000, fc9, res4);
        foodDao.insertFood(f1);
        foodDao.insertFood(f2);
        foodDao.insertFood(f3);
        foodDao.insertFood(f4);
        foodDao.insertFood(f5);
        foodDao.insertFood(f6);
        foodDao.insertFood(f7);
        foodDao.insertFood(f8);
        foodDao.insertFood(f9);
        foodDao.insertFood(f10);
    }


}
