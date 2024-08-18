package com.example.food_order_final.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.ReviewFoodDao;
import com.example.food_order_final.dao.ReviewRestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.ReviewRestaurant;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

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
    public static final String TABLE_REVIEW_RESTAURANT_NAME = "ReviewRestaurant";
    public static final String TABLE_REVIEW_FOOD_NAME = "ReviewFood";

    public static final String TABLE_CART_DETAIL_NAME = "CartDetail";
    public static final String TABLE_PAYMENT_PENDING_NAME = "PaymentPending";

    // Commons columns
    public static final String CREATED_DATE_FIELD = "created_date";
    public static final String UPDATED_DATE_FIELD = "updated_date";
    public static final String ID_FIELD = "id";
    public static final String RATING_FIELD = "rating";

    // Table Cart Detail columns
    public static final String CART_DETAIL_FOOD_FIELD = "food_id";
    public static final String CART_DETAIL_CART_FIELD = "cart_id";
    public static final String CART_DETAIL_QUANTITY_FIELD = "quantity";
    public static final String CART_DETAIL_PRICE_FIELD = "price";

    // Table PaymentPending Columns
    public static final String PAYMENT_PENDING_STATUS = "status";
    public static final String PAYMENT_PENDING_CART = "cart_id";
    public static final String PAYMENT_PENDING_TOTAL = "total";
    public static final String PAYMENT_PENDING_METHOD = "method";
    public static final String PAYMENT_PENDING_NOTE = "note";

    // Table Cart columns
    public static final String CART_USER_FIELD = "user_id";
    public static final String CART_ORDER_DATE = "order_date";
    public static final String CART_RESTAURANT_FIELD = "restaurant_id";
    public static final String CART_STATUS = "status";


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
//    public static final String RATING_FIELD = "rating";
    public static final String RESTAURANT_USER_FIELD = "user_id";

    // Table Food Category columns
    public static final String FOOD_CATEGORY_NAME_FIELD = "name";

    // Table Food columns
    public static final String FOOD_NAME_FIELD = "name";
    public static final String FOOD_PRICE_FIELD = "price";
    public static final String FOOD_DISCOUNT_FIELD = "discount";
    public static final String FOOD_AVATAR_FIELD = "avatar";
    public static final String FOOD_CATEGORY_FIELD = "category";
    public static final String FOOD_RESTAURANT_FIELD = "restaurant";
    public static final String FOOD_DESCRIPTION_FIELD = "description";

    // Table Review columns
    public static final String REVIEW_USER_FIELD = "user_id";
    public static final String REVIEW_RESTAURANT_FIELD = "restaurant_id";
    public static final String REVIEW_COMMENT_FIELD = "comment";
    public static final String REVIEW_IMAGE_FIELD = "image";
//    public static final String RATING_FIELD = "rating";
    public static final String REVIEW_FOOD_FIELD = "food_id";

    public RoleDao roleDao;
    public UserDao userDao;
    public RestaurantCategoryDao resCateDao;
    public RestaurantDao resDao;
    public FoodCategoryDao foodCateDao;
    public FoodDao foodDao;
    public ReviewRestaurantDao reviewRestaurantDao;
    public ReviewFoodDao reviewFoodDao;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.roleDao = new RoleDao(this);
        this.userDao = new UserDao(this, this.roleDao);
        this.resCateDao = new RestaurantCategoryDao(this);
        this.resDao = new RestaurantDao(this, this.resCateDao);
        this.foodCateDao = new FoodCategoryDao(this);
        this.foodDao = new FoodDao(this, this.foodCateDao, this.resDao);
        this.reviewRestaurantDao = new ReviewRestaurantDao(this, this.userDao, this.resDao);
        this.reviewFoodDao = new ReviewFoodDao(this, this.userDao, this.foodDao);
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
                USER_AVATAR_FIELD + " TEXT, " +
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
                RATING_FIELD + " FLOAT, " +
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
                FOOD_DISCOUNT_FIELD + " FLOAT, " +
                RATING_FIELD + " FLOAT, " +
                FOOD_CATEGORY_FIELD + " INTEGER, " +
                FOOD_RESTAURANT_FIELD + " INTEGER, " +
                FOOD_AVATAR_FIELD + " BLOB, " +
                FOOD_DESCRIPTION_FIELD + " TEXT, " +
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
                CART_STATUS + " INTEGER, " +
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

        String sqlReviewRes = "CREATE TABLE " + TABLE_REVIEW_RESTAURANT_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REVIEW_USER_FIELD + " INTEGER, " +
                REVIEW_RESTAURANT_FIELD + " INTEGER, " +
                REVIEW_COMMENT_FIELD + " TEXT, " +
                RATING_FIELD + " FLOAT, " +
                REVIEW_IMAGE_FIELD + " TEXT, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + REVIEW_USER_FIELD + ") REFERENCES " + TABLE_USER_NAME + "(" + ID_FIELD + "), "+
                "FOREIGN KEY (" + REVIEW_RESTAURANT_FIELD + ") REFERENCES " + TABLE_RESTAURANT_NAME + "(" + ID_FIELD + "))";
        db.execSQL(sqlReviewRes);

        String sqlReviewFood = "CREATE TABLE " + TABLE_REVIEW_FOOD_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REVIEW_USER_FIELD + " INTEGER, " +
                REVIEW_FOOD_FIELD + " INTEGER, " +
                REVIEW_COMMENT_FIELD + " TEXT, " +
                RATING_FIELD + " FLOAT, " +
                REVIEW_IMAGE_FIELD + " TEXT, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + REVIEW_USER_FIELD + ") REFERENCES " + TABLE_USER_NAME + "(" + ID_FIELD + "), "+
                "FOREIGN KEY (" + REVIEW_FOOD_FIELD + ") REFERENCES " + TABLE_FOOD_NAME + "(" + ID_FIELD + "))";
        db.execSQL(sqlReviewFood);

        String sqlPaymentPending = "CREATE TABLE " + TABLE_PAYMENT_PENDING_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PAYMENT_PENDING_METHOD + " INTEGER, " +
                PAYMENT_PENDING_CART + " INTEGER, " +
                PAYMENT_PENDING_STATUS + " INTEGER, " +
                PAYMENT_PENDING_TOTAL + " FLOAT, " +
                PAYMENT_PENDING_NOTE + " TEXT, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + PAYMENT_PENDING_CART + ") REFERENCES " + TABLE_CART_NAME + "(" + ID_FIELD + "))";
        db.execSQL(sqlPaymentPending);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        onCreate(db);
    }

    public ContentValues getRoleContentValues(Role role) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROLE_NAME_FIELD, role.getName());
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
        contentValues.put(RATING_FIELD, restaurant.getRating());
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
        contentValues.put(DatabaseHelper.FOOD_DISCOUNT_FIELD, food.getDiscount());
        contentValues.put(DatabaseHelper.FOOD_AVATAR_FIELD, food.getAvatar());
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_FIELD, food.getCategory().getId());
        contentValues.put(DatabaseHelper.RATING_FIELD, food.getRating());
        contentValues.put(DatabaseHelper.FOOD_DESCRIPTION_FIELD, food.getDescription());
        contentValues.put(DatabaseHelper.FOOD_RESTAURANT_FIELD, food.getRestaurant().getId());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(food.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(food.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getCartContentValues(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_USER_FIELD, cart.getUser().getId());
        contentValues.put(DatabaseHelper.CART_RESTAURANT_FIELD, cart.getRestaurant().getId());
        contentValues.put(DatabaseHelper.CART_STATUS, cart.getStatus());
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
        foodDao.insertFood(new Food("Mì Ý Bò Băm", 45000, "Mì bò 2 trứng", fc1, res1));
        foodDao.insertFood(new Food("Pizza Pepperoni", 55000, "Mì bò 2 trứng", fc2, res2));
        foodDao.insertFood(new Food("Sushi California", 60000, "Mì bò 2 trứng", fc3, res4));
        foodDao.insertFood(new Food("Bánh Hamburger Phô Mai", 40000, "Mì bò 2 trứng", fc4, res3));
        foodDao.insertFood(new Food("Tacos Bò", 35000, "Mì bò 2 trứng", fc5, res5));
        foodDao.insertFood(new Food("Salad Caesar", 30000, "Mì bò 2 trứng", fc6, res6));
        foodDao.insertFood(new Food("Bánh Mì Club", 32000, "Mì bò 2 trứng", fc7, res7));
        foodDao.insertFood(new Food("Bánh Chocolate", 28000, "Mì bò 2 trứng", fc8, res8));
        foodDao.insertFood(new Food("Cá Hồi Nướng", 70000, "Mì bò 2 trứng", fc9, res9));
        foodDao.insertFood(new Food("Súp Tom Yum", 33000, "Mì bò 2 trứng", fc10, res10));

        Food f1 = foodDao.getFoodById(1);
        Food f2 = foodDao.getFoodById(2);
        Food f3 = foodDao.getFoodById(3);
        Food f4 = foodDao.getFoodById(4);
        Food f5 = foodDao.getFoodById(5);
        Food f6 = foodDao.getFoodById(6);
        Food f7 = foodDao.getFoodById(7);
        Food f8 = foodDao.getFoodById(8);
        Food f9 = foodDao.getFoodById(9);
        Food f10 = foodDao.getFoodById(10);

        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tại đây rất chuyên nghiệp và không gian rất ấm cúng và thoải mái.", 5.0, user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian quán đẹp mắt nhưng giá cả hơi cao so với chất lượng dịch vụ.", 4.0, user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian sang trọng và dịch vụ tận tình, tạo cảm giác dễ chịu.", 4.5, user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Mặc dù không gian và trang trí khá đẹp, nhưng dịch vụ cần được cải thiện thêm.", 3.5, user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Tôi thích không gian và trang trí của quán, nhưng có vẻ hơi ồn ào và cần thêm sự riêng tư.", 4.0, user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Quán có dịch vụ nhanh chóng và không khí dễ chịu, trang trí phù hợp với chủ đề của quán.", 4.5, user6, res6));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian quán rộng rãi và thoải mái, tuy nhiên, cách trang trí có thể cần thêm sự tinh tế.", 4.0, user7, res7));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tại đây không tốt lắm và không gian cũng khá đơn điệu.", 2.5, user8, res8));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian thoải mái và ấm cúng, nhưng dịch vụ hơi chậm và cần cải thiện.", 5.0, user9, res9));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Quán có không gian dễ chịu và dịch vụ ổn, tuy nhiên trang trí cần phải được nâng cấp.", 3.5, user10, res10));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Thực đơn đa dạng và món ăn rất ngon, phục vụ nhiệt tình.", 4.5, user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian đẹp, nhưng món ăn hơi đắt so với chất lượng.", 3.5, user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ không được như mong đợi, nhân viên chưa thân thiện.", 2.0, user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng sạch sẽ và đồ ăn rất tươi ngon. Giá cả hợp lý.", 4.0, user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian lãng mạn, rất phù hợp cho các cặp đôi.", 5.0, user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Món ăn ngon nhưng phục vụ hơi chậm. Hy vọng sẽ cải thiện.", 3.0, user6, res6));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Chất lượng món ăn rất tốt, nhưng không khí có phần ồn ào.", 4.0, user7, res7));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng rất đẹp, tuy nhiên giá cả có phần cao hơn so với mặt bằng chung.", 3.5, user8, res8));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Thực sự không hài lòng với chất lượng món ăn và thái độ phục vụ.", 1.5, user9, res9));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tận tâm và món ăn rất đặc sắc. Mình sẽ quay lại.", 4.5, user10, res10));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian thoải mái và thực đơn phong phú. Món ăn hơi ngọt.", 4.0, user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có vẻ hơi cũ và cần nâng cấp. Đồ ăn cũng bình thường.", 2.5, user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Chất lượng phục vụ rất tốt, nhưng món ăn chưa được đặc biệt lắm.", 3.5, user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Món ăn ngon, nhưng không gian quá chật hẹp và không thoải mái.", 3.0, user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Mình rất ấn tượng với sự sáng tạo trong món ăn và cách phục vụ chuyên nghiệp.", 5.0, user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có phong cách trang trí đẹp mắt nhưng món ăn chưa đạt yêu cầu.", 3.0, user6, res6));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ rất nhanh chóng và đồ ăn luôn tươi mới. Giá cả hợp lý.", 4.5, user7, res7));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian nhà hàng khá đơn giản, nhưng món ăn thì tuyệt vời.", 4.0, user8, res8));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng không đáp ứng được mong đợi về chất lượng phục vụ và món ăn.", 2.0, user9, res9));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Một trải nghiệm ẩm thực đáng nhớ với món ăn đa dạng và dịch vụ chu đáo.", 5.0, user10, res10));

        reviewFoodDao.insertReview(new ReviewFood("Rất ngon, dịch vụ tuyệt vời!", 5.0, user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Pizza rất thơm, nhưng giá hơi cao.", 4.0, user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Sushi rất tươi ngon, không gian đẹp.", 4.5, user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Hamburger hơi bị nhạt, cần thêm gia vị.", 3.5, user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Tacos rất tuyệt, nhưng món tráng miệng không ngon lắm.", 4.0, user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Salad rất tươi, dịch vụ rất nhanh.", 4.5, user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Bánh mì rất ngon, giá cả hợp lý.", 4.0, user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Bánh chocolate không được như kỳ vọng.", 2.5, user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Cá hồi nướng rất ngon, không gian thoải mái.", 5.0, user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Súp Tom Yum có vị hơi chua, cần điều chỉnh.", 3.5, user10, f10));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn tuyệt vời, sẽ quay lại lần nữa!", 4.5, user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ tốt, món ăn hơi nhạt.", 3.5, user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Thực phẩm tươi ngon, nhưng thời gian chờ hơi lâu.", 4.0, user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Không gian sạch sẽ, món ăn không như kỳ vọng.", 2.5, user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn rất tốt, giá hơi cao.", 4.0, user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ không đạt yêu cầu, món ăn cũng bình thường.", 2.0, user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon và phục vụ nhanh chóng.", 5.0, user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Nhà hàng có không gian đẹp, nhưng món ăn chỉ ở mức trung bình.", 3.0, user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ xuất sắc và món ăn rất ngon.", 4.5, user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn không đạt yêu cầu, nhưng không gian khá dễ chịu.", 2.5, user10, f10));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn ngon tuyệt vời, dịch vụ rất thân thiện.", 5.0, user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Thực đơn đa dạng, nhưng món ăn hơi ngọt.", 3.5, user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ hơi chậm, món ăn không đồng đều.", 2.5, user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn rất tốt, không gian hơi ồn ào.", 4.0, user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon nhưng giá cả hơi cao.", 4.5, user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Nhà hàng sạch sẽ nhưng món ăn khá đơn giản.", 3.0, user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ tận tâm và món ăn rất sáng tạo.", 5.0, user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn được trình bày đẹp mắt, nhưng hương vị chưa thuyết phục.", 3.5, user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon và phong phú, phục vụ chuyên nghiệp.", 4.5, user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn và dịch vụ không như mong đợi.", 2.0, user10, f10));

    }


}
