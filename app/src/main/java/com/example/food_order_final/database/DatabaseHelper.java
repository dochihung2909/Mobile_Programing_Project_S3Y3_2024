package com.example.food_order_final.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import java.util.Date;

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
    public static final String TABLE_CART_NAME = "Cart";

    public static final String TABLE_CART_DETAIL_NAME = "CartDetail";

    // Commons columns
    public static final String CREATED_DATE_FIELD = "created_date";
    public static final String UPDATED_DATE_FIELD = "updated_date";
    public static final String ID_FIELD = "id";

    // Table Cart Detail columns
    public static final String CART_DETAIL_FOOD_FIELD = "food_id";
    public static final String CART_DETAIL_CART_FIELD = "cart_id";
    public static final String CART_DETAIL_QUANTITY_FIELD = "quantity";
    public static final String CART_DETAIL_PRICE_FIELD = "price";

    // Table Cart columns
    public static final String CART_USER_FIELD = "user_id";
    public static final String CART_ORDER_DATE = "order_date";
    public static final String CART_RESTAURANT_FIELD = "restaurant_id";


    // Table Role columns
    public static final String ROLE_NAME_FIELD = "name";

    // Table User columns
    public static final String USER_FULL_NAME_FIELD = "full_name";
    public static final String USER_PHONE_NUMBER_FIELD = "phone_number";
    public static final String USER_EMAIL_FIELD = "email";
    public static final String USER_AVATAR_FIELD = "avatar";
    public static final String USER_USERNAME_FIELD = "username";
    public static final String USER_PASSWORD_FIELD = "password";
    public static final String USER_ROLE_FIELD = "role_id";


    // Table Restaurant Category columns
    public static final String RESTAURANT_CATEGORY_NAME_FIELD = "name";

    // Table Restaurant columns
    public static final String RESTAURANT_NAME_FIELD = "name";
    public static final String RESTAURANT_ADDRESS_FIELD = "address";
    public static final String RESTAURANT_PHONE_FIELD = "phone";
    public static final String RESTAURANT_CATEGORY_FIELD = "category";
    public static final String RESTAURANT_AVATAR_FIELD = "avatar";
    public static final String RESTAURANT_IS_PARTNER_FIELD = "is_partner";
    public static final String RESTAURANT_RATING_FIELD = "rating";
    public static final String RESTAURANT_USER_FIELD = "user_id";

    // Table Food Category columns
    public static final String FOOD_CATEGORY_NAME_FIELD = "name";

    // Table Food columns
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
                        "%s TEXT, " +
                        "%s TIMESTAMP, %s TIMESTAMP)",
                TABLE_ROLE_NAME,
                ID_FIELD,
                ROLE_NAME_FIELD,
                CREATED_DATE_FIELD, UPDATED_DATE_FIELD);
        db.execSQL(sqlRole);

        String sqlUser = "CREATE TABLE " + TABLE_USER_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_USERNAME_FIELD + " TEXT, " +
                USER_PHONE_NUMBER_FIELD + " TEXT, " +
                USER_EMAIL_FIELD + " TEXT, " +
                USER_FULL_NAME_FIELD + " TEXT, " +
                USER_PASSWORD_FIELD + " TEXT, " +
                USER_ROLE_FIELD + " INTEGER, " +
                USER_AVATAR_FIELD + " VARCHAR, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP," +
                "FOREIGN KEY (" + USER_ROLE_FIELD + ") REFERENCES " + TABLE_ROLE_NAME + " (" + ID_FIELD + ")) ";
        db.execSQL(sqlUser);

        String sqlRestaurantCate = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TIMESTAMP, %s TIMESTAMP)",
                TABLE_RESTAURANT_CATEGORY_NAME,
                ID_FIELD,
                RESTAURANT_CATEGORY_NAME_FIELD,
                CREATED_DATE_FIELD, UPDATED_DATE_FIELD);
        db.execSQL(sqlRestaurantCate);

        String sqlRestaurant = "CREATE TABLE " + TABLE_RESTAURANT_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RESTAURANT_NAME_FIELD + " TEXT, " +
                RESTAURANT_ADDRESS_FIELD + " TEXT, " +
                RESTAURANT_PHONE_FIELD + " TEXT, " +
                RESTAURANT_CATEGORY_FIELD + " INTEGER, " +
                RESTAURANT_IS_PARTNER_FIELD + " BOOLEAN, " +
                RESTAURANT_RATING_FIELD + " FLOAT, " +
                RESTAURANT_AVATAR_FIELD + " VARCHAR, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                RESTAURANT_USER_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + RESTAURANT_USER_FIELD + ") REFERENCES " + TABLE_USER_NAME + " (" + ID_FIELD + "), " +
                "FOREIGN KEY (" + RESTAURANT_CATEGORY_FIELD + ") REFERENCES " + TABLE_RESTAURANT_CATEGORY_NAME + " (" + ID_FIELD + "))";

        db.execSQL(sqlRestaurant);

        String sqlFoodCate = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TIMESTAMP, %s TIMESTAMP)",
                TABLE_FOOD_CATEGORY_NAME,
                ID_FIELD,
                FOOD_CATEGORY_NAME_FIELD,
                CREATED_DATE_FIELD, UPDATED_DATE_FIELD);
        db.execSQL(sqlFoodCate);

        String sqlFood = "CREATE TABLE " + TABLE_FOOD_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FOOD_NAME_FIELD + " TEXT, " +
                FOOD_PRICE_FIELD + " FLOAT, " +
                FOOD_CATEGORY_FIELD + " INTEGER, " +
                FOOD_RESTAURANT_FIELD + " INTEGER, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + FOOD_CATEGORY_FIELD + ") REFERENCES " + TABLE_FOOD_CATEGORY_NAME + " (" + ID_FIELD + "), " +
                "FOREIGN KEY (" + FOOD_RESTAURANT_FIELD + ") REFERENCES " + TABLE_RESTAURANT_NAME + " (" + ID_FIELD + "))";
        db.execSQL(sqlFood);

        String sqlCart = "CREATE TABLE " + TABLE_CART_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CART_ORDER_DATE + " TIMESTAMP, " +
                CART_USER_FIELD + " INTEGER, " +
                CART_RESTAURANT_FIELD + " INTEGER, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + CART_USER_FIELD + ") REFERENCES " + TABLE_USER_NAME + "(" + ID_FIELD + "), " +
                "FOREIGN KEY (" + CART_RESTAURANT_FIELD + ") REFERENCES " + TABLE_RESTAURANT_NAME + "(" + ID_FIELD + "))";
        db.execSQL(sqlCart);

        String sqlCartDetail = "CREATE TABLE " + TABLE_CART_DETAIL_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CART_DETAIL_CART_FIELD + " INTEGER, " +
                CART_DETAIL_FOOD_FIELD + " INTEGER, " +
                CART_DETAIL_QUANTITY_FIELD + " INTEGER, " +
                CART_DETAIL_PRICE_FIELD + " FLOAT, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + CART_DETAIL_CART_FIELD + ") REFERENCES " + TABLE_CART_NAME + "(" + ID_FIELD + "), " +
                "FOREIGN KEY (" + CART_DETAIL_FOOD_FIELD + ") REFERENCES " + TABLE_FOOD_NAME + "(" + ID_FIELD + "))";
        db.execSQL(sqlCartDetail);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        onCreate(db);
    }

    public ContentValues getRoleContentValues(Role role) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put(ROLE_ID_FIELD, role.getId());
        contentValues.put(ROLE_NAME_FIELD, role.getName());
//        contentValues.put(ROLE_CREATED_DATE_FIELD, role.getCreatedDate().toString());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(role.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(role.getUpdatedDate()));

        return contentValues;
    }
    public ContentValues getUserContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(USER_FULL_NAME_FIELD, user.getFullName());
        contentValues.put(USER_ROLE_FIELD, user.getRole().getId());
        contentValues.put(USER_AVATAR_FIELD, user.getAvatar());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(user.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(user.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getResCateContentValues(RestaurantCategory resCate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESTAURANT_CATEGORY_NAME_FIELD, resCate.getName());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(resCate.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(resCate.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getRestaurantContentValues(Restaurant restaurant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESTAURANT_NAME_FIELD, restaurant.getName());
        contentValues.put(RESTAURANT_ADDRESS_FIELD, restaurant.getAddress());
        contentValues.put(RESTAURANT_PHONE_FIELD, restaurant.getPhoneNumber());
        contentValues.put(RESTAURANT_AVATAR_FIELD, restaurant.getAvatar());
        contentValues.put(RESTAURANT_CATEGORY_FIELD, restaurant.getCategory().getId());
        contentValues.put(RESTAURANT_IS_PARTNER_FIELD, restaurant.isPartner());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(restaurant.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(restaurant.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getFoodCateContentValues(FoodCategory foodCate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_NAME_FIELD, foodCate.getName());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(foodCate.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(foodCate.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getFoodContentValues(Food food) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_NAME_FIELD, food.getName());
        contentValues.put(DatabaseHelper.FOOD_PRICE_FIELD, food.getPrice());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_FIELD, food.getCategory().getId());
        contentValues.put(DatabaseHelper.FOOD_RESTAURANT_FIELD, food.getRestaurant().getId());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(food.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(food.getUpdatedDate()));

        return contentValues;
    }



    public ContentValues getCartContentValues(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_USER_FIELD, cart.getUser().getId());
        contentValues.put(DatabaseHelper.CART_RESTAURANT_FIELD, cart.getRestaurant().getId());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(cart.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(cart.getUpdatedDate()));

        return contentValues;
    }



    public ContentValues getCartDetailContentValues(CartDetail cartDetail) {
        ContentValues contentValues = new ContentValues();
        Log.d("CartDetail", String.valueOf(cartDetail.getCart().getId()));
        contentValues.put(DatabaseHelper.CART_DETAIL_FOOD_FIELD, cartDetail.getFood().getId());
        contentValues.put(DatabaseHelper.CART_DETAIL_QUANTITY_FIELD, cartDetail.getQuantity());
        contentValues.put(DatabaseHelper.CART_DETAIL_CART_FIELD, cartDetail.getCart().getId());
        contentValues.put(DatabaseHelper.CART_DETAIL_PRICE_FIELD, cartDetail.getPrice());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(cartDetail.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(cartDetail.getUpdatedDate()));

        return contentValues;
    }

    public void initializeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Role data
        roleDao.insertRole(new Role("Admin"));
        roleDao.insertRole(new Role("User"));
        Role adminRole = roleDao.getRoleByName("Admin");
        Role userRole = roleDao.getRoleByName("User");

        // User data
        userDao.insertUser(new User("ngynhg", "0364452867", "hung@gmail.com",
                "Nguyễn Văn Hùng", "123", adminRole));
        userDao.insertUser(new User("tranquangh", "0976543210", "tranquangh@gmail.com",
                "Trần Quang Hải", "123", adminRole));
        userDao.insertUser(new User("lethanhh", "0912345679", "lethanhh@gmail.com",
                "Lê Thanh Hà", "123", userRole));
        userDao.insertUser(new User("nguyenducm", "0901234568", "nguyenducm@gmail.com",
                "Nguyễn Đức Minh", "123", userRole));
        userDao.insertUser(new User("hoangthi", "0987123457", "hoangthi@gmail.com",
                "Hoàng Thị Lan", "123", userRole));
        userDao.insertUser(new User("phanvanq", "0898765433", "phanvanq@gmail.com",
                "Phan Văn Quân", "123", userRole));
        userDao.insertUser(new User("vuongthanh", "0876543211", "vuongthanh@gmail.com",
                "Vương Thành", "123", userRole));
        userDao.insertUser(new User("daohoang", "0865432110", "daohoang@gmail.com",
                "Đào Hoàng Nam", "123", userRole));
        userDao.insertUser(new User("ngoanguyen", "0854321099", "ngoanguyen@gmail.com",
                "Ngô Anh Nguyễn", "123", userRole));
        userDao.insertUser(new User("phamquang", "0843210988", "phamquang@gmail.com",
                "Phạm Quang Hưng", "123", userRole));

        User user1 = userDao.getUserByUsername("ngynhg");
        User user2 = userDao.getUserByUsername("tranquangh");
        User user3 = userDao.getUserByUsername("lethanhh");
        User user4 = userDao.getUserByUsername("nguyenducm");
        User user5 = userDao.getUserByUsername("hoangthi");
        User user6 = userDao.getUserByUsername("phanvanq");
        User user7 = userDao.getUserByUsername("vuongthanh");
        User user8 = userDao.getUserByUsername("daohoang");
        User user9 = userDao.getUserByUsername("ngoanguyen");
        User user10 = userDao.getUserByUsername("phamquang");
        Log.d("User1", user1.getFullName());


        //Restaurant Category data
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Mỳ"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Pizza"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Burgers"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Sushi"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Món Mexico"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Món Ấn Độ"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Món Trung Quốc"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Món Ý"));
        resCateDao.insertRestaurantCategory(new RestaurantCategory("Tráng miệng"));

        RestaurantCategory rc1 = resCateDao.getRestaurantCategoryByName("Mỳ");
        RestaurantCategory rc2 = resCateDao.getRestaurantCategoryByName("Pizza");
        RestaurantCategory rc3 = resCateDao.getRestaurantCategoryByName("Burgers");
        RestaurantCategory rc4 = resCateDao.getRestaurantCategoryByName("Sushi");
        RestaurantCategory rc5 = resCateDao.getRestaurantCategoryByName("Món Mexico");
        RestaurantCategory rc6 = resCateDao.getRestaurantCategoryByName("Món Ấn Độ");
        RestaurantCategory rc7 = resCateDao.getRestaurantCategoryByName("Món Trung Quốc");
        RestaurantCategory rc8 = resCateDao.getRestaurantCategoryByName("Món Ý");
        RestaurantCategory rc9 = resCateDao.getRestaurantCategoryByName("Tráng miệng");


        // Restaurant data
        resDao.insertRestaurant(new Restaurant("Gà Rán KFC", "123 Nguyễn Thị Minh Khai, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh", "057265484", rc1));
        resDao.insertRestaurant(new Restaurant("Pizza Domino's", "456 Lê Lai, Phường Bến Thành, Quận 1, Thành phố Hồ Chí Minh", "0123456789", rc2));
        resDao.insertRestaurant(new Restaurant("Burgers King", "789 Phạm Văn Đồng, Phường 11, Quận Bình Thạnh, Thành phố Hồ Chí Minh", "0987654321", rc3));
        resDao.insertRestaurant(new Restaurant("Sakura Sushi", "321 Đinh Tiên Hoàng, Phường 1, Quận 1, Thành phố Hồ Chí Minh", "0234567890", rc4));
        resDao.insertRestaurant(new Restaurant("Taco Bell", "654 Nguyễn Huệ, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh", "0345678901", rc5));
        resDao.insertRestaurant(new Restaurant("Nhà Hàng Cà Ri", "987 Bùi Viện, Phường Phạm Ngũ Lão, Quận 1, Thành phố Hồ Chí Minh", "0456789012", rc6));
        resDao.insertRestaurant(new Restaurant("Nhà Hàng Đại Trường Thành", "135 Nguyễn Thị Thập, Phường Tân Phú, Quận 7, Thành phố Hồ Chí Minh", "0567890123", rc7));
        resDao.insertRestaurant(new Restaurant("La Dolce Vita", "246 Trường Sơn, Phường 15, Quận Tân Bình, Thành phố Hồ Chí Minh", "0678901234", rc8));
        resDao.insertRestaurant(new Restaurant("Quán Ngọt Ngào", "357 Nguyễn Đình Chiểu, Phường 5, Quận 3, Thành phố Hồ Chí Minh", "0789012345", rc9));
        resDao.insertRestaurant(new Restaurant("Nhà Hàng Panda", "468 Cách Mạng Tháng Tám, Phường 11, Quận 3, Thành phố Hồ Chí Minh", "0890123456", rc7));

        Restaurant res1 = resDao.getRestaurantByName("Gà Rán KFC");
        Restaurant res2 = resDao.getRestaurantByName("Pizza Domino's");
        Restaurant res3 = resDao.getRestaurantByName("Burgers King");
        Restaurant res4 = resDao.getRestaurantByName("Sakura Sushi");
        Restaurant res5 = resDao.getRestaurantByName("Taco Bell");
        Restaurant res6 = resDao.getRestaurantByName("Nhà Hàng Cà Ri");
        Restaurant res7 = resDao.getRestaurantByName("Nhà Hàng Đại Trường Thành");
        Restaurant res8 = resDao.getRestaurantByName("La Dolce Vita");
        Restaurant res9 = resDao.getRestaurantByName("Quán Ngọt Ngào");
        Restaurant res10 = resDao.getRestaurantByName("Nhà Hàng Panda");

        //Food Category data
        foodCateDao.insertFoodCategory(new FoodCategory("Mì Ý"));
        foodCateDao.insertFoodCategory(new FoodCategory("Pizza"));
        foodCateDao.insertFoodCategory(new FoodCategory("Sushi"));
        foodCateDao.insertFoodCategory(new FoodCategory("Burgers"));
        foodCateDao.insertFoodCategory(new FoodCategory("Tacos"));
        foodCateDao.insertFoodCategory(new FoodCategory("Salad"));
        foodCateDao.insertFoodCategory(new FoodCategory("Bánh Mì"));
        foodCateDao.insertFoodCategory(new FoodCategory("Tráng Miệng"));
        foodCateDao.insertFoodCategory(new FoodCategory("Hải Sản"));
        foodCateDao.insertFoodCategory(new FoodCategory("Súp"));

        FoodCategory fc1 = foodCateDao.getFoodCategoryByName("Mì Ý");
        FoodCategory fc2 = foodCateDao.getFoodCategoryByName("Pizza");
        FoodCategory fc3 = foodCateDao.getFoodCategoryByName("Sushi");
        FoodCategory fc4 = foodCateDao.getFoodCategoryByName("Burgers");
        FoodCategory fc5 = foodCateDao.getFoodCategoryByName("Tacos");
        FoodCategory fc6 = foodCateDao.getFoodCategoryByName("Salad");
        FoodCategory fc7 = foodCateDao.getFoodCategoryByName("Bánh Mì");
        FoodCategory fc8 = foodCateDao.getFoodCategoryByName("Tráng Miệng");
        FoodCategory fc9 = foodCateDao.getFoodCategoryByName("Hải Sản");
        FoodCategory fc10 = foodCateDao.getFoodCategoryByName("Súp");

        // Food data
        foodDao.insertFood(new Food("Mì Ý Bò Băm", 45000, fc1, res1));
        foodDao.insertFood(new Food("Pizza Pepperoni", 55000, fc2, res2));
        foodDao.insertFood(new Food("Sushi California", 60000, fc3, res4));
        foodDao.insertFood(new Food("Bánh Hamburger Phô Mai", 40000, fc4, res3));
        foodDao.insertFood(new Food("Tacos Bò", 35000, fc5, res5));
        foodDao.insertFood(new Food("Salad Caesar", 30000, fc6, res6));
        foodDao.insertFood(new Food("Bánh Mì Club", 32000, fc7, res7));
        foodDao.insertFood(new Food("Bánh Chocolate", 28000, fc8, res8));
        foodDao.insertFood(new Food("Cá Hồi Nướng", 70000, fc9, res9));
        foodDao.insertFood(new Food("Súp Tom Yum", 33000, fc10, res10));

        Food f1 = new Food("Mì Ý Bò Băm", 45000, fc1, res1);
        Food f2 = new Food("Pizza Pepperoni", 55000, fc2, res2);
        Food f3 = new Food("Sushi California", 60000, fc3, res4);
        Food f4 = new Food("Bánh Hamburger Phô Mai", 40000, fc4, res3);
        Food f5 = new Food("Tacos Bò", 35000, fc5, res5);
        Food f6 = new Food("Salad Caesar", 30000, fc6, res6);
        Food f7 = new Food("Bánh Mì Club", 32000, fc7, res7);
        Food f8 = new Food("Bánh Chocolate", 28000, fc8, res8);
        Food f9 = new Food("Cá Hồi Nướng", 70000, fc9, res9);
        Food f10 = new Food("Súp Tom Yum", 33000, fc10, res10);
    }


}
