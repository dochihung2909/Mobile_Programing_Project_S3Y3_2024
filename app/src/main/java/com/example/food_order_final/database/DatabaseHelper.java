package com.example.food_order_final.database;

import static com.android.volley.VolleyLog.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.PaymentPendingDao;
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
    public static final String TABLE_REVIEW_RESTAURANT_NAME = "ReviewRestaurant";
    public static final String TABLE_REVIEW_FOOD_NAME = "ReviewFood";

    public static final String TABLE_CART_DETAIL_NAME = "CartDetail";
    public static final String TABLE_PAYMENT_PENDING_NAME = "PaymentPending";

    // Commons columns
    public static final String CREATED_DATE_FIELD = "created_date";
    public static final String UPDATED_DATE_FIELD = "updated_date";
    public static final String ID_FIELD = "id";
    public static final String ACTIVE_FIELD = "is_actived";
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
    public PaymentPendingDao paymentPendingDao;
    public CartDetailDao cartDetailDao;
    public CartDao cartDao;

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
        this.paymentPendingDao = new PaymentPendingDao(this);
        this.cartDetailDao = new CartDetailDao(this);
        this.cartDao = new CartDao(this);
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                RESTAURANT_USER_FIELD + " INTEGER, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                FOOD_AVATAR_FIELD + " TEXT, " +
                FOOD_DESCRIPTION_FIELD + " TEXT, " +
                ACTIVE_FIELD + " BOOL, " +
                CREATED_DATE_FIELD + " TIMESTAMP, " +
                UPDATED_DATE_FIELD + " TIMESTAMP, " +
                "FOREIGN KEY (" + FOOD_CATEGORY_FIELD + ") REFERENCES " + TABLE_FOOD_CATEGORY_NAME + " (" + ID_FIELD + "), " +
                "FOREIGN KEY (" + FOOD_RESTAURANT_FIELD + ") REFERENCES " + TABLE_RESTAURANT_NAME + " (" + ID_FIELD + "))";
        db.execSQL(sqlFood);

        String sqlCart = "CREATE TABLE " + TABLE_CART_NAME + " (" +
                ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                CART_ORDER_DATE + " TIMESTAMP, " +
                CART_USER_FIELD + " INTEGER, " +
                CART_RESTAURANT_FIELD + " INTEGER, " +
                CART_STATUS + " INTEGER, " +
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
                ACTIVE_FIELD + " BOOL, " +
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
        contentValues.put(ACTIVE_FIELD, role.isActived());
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
        contentValues.put(ACTIVE_FIELD, user.isActived());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(user.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(user.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getResCateContentValues(RestaurantCategory resCate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESTAURANT_CATEGORY_NAME_FIELD, resCate.getName());
        contentValues.put(ACTIVE_FIELD, resCate.isActived());
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

        contentValues.put(RESTAURANT_USER_FIELD, restaurant.getOwner().getId());
//        User owner = restaurant.getOwner();
//        if (owner != null) {
//            contentValues.put(RESTAURANT_USER_FIELD, owner.getId());
//        }
        contentValues.put(ACTIVE_FIELD, restaurant.isActived());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(restaurant.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(restaurant.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getFoodCateContentValues(FoodCategory foodCate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FOOD_CATEGORY_NAME_FIELD, foodCate.getName());
        contentValues.put(ACTIVE_FIELD, foodCate.isActived());
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
        contentValues.put(ACTIVE_FIELD, food.isActived());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(food.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(food.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getCartContentValues(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_USER_FIELD, cart.getUser().getId());
        contentValues.put(DatabaseHelper.CART_RESTAURANT_FIELD, cart.getRestaurant().getId());
        contentValues.put(DatabaseHelper.CART_STATUS, cart.getStatus());
        contentValues.put(ACTIVE_FIELD, cart.isActived());
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
        contentValues.put(ACTIVE_FIELD, cartDetail.isActived());
        contentValues.put(CREATED_DATE_FIELD, DateUtil.dateToTimestamp(cartDetail.getCreatedDate()));
        contentValues.put(UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(cartDetail.getUpdatedDate()));

        return contentValues;
    }

    public ContentValues getReviewFoodContentValues(ReviewFood reviewFood) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, reviewFood.getComment());
        contentValues.put(DatabaseHelper.REVIEW_USER_FIELD, reviewFood.getUser().getId());
        contentValues.put(DatabaseHelper.REVIEW_FOOD_FIELD, reviewFood.getFood().getId());
        contentValues.put(DatabaseHelper.RATING_FIELD, reviewFood.getRating());
        contentValues.put(DatabaseHelper.REVIEW_IMAGE_FIELD, reviewFood.getImage());
        contentValues.put(DatabaseHelper.ACTIVE_FIELD, reviewFood.getActived());
        contentValues.put(DatabaseHelper.CREATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        return contentValues;
    }

    public ContentValues getReviewResContentValues(ReviewRestaurant reviewRestaurant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REVIEW_COMMENT_FIELD, reviewRestaurant.getComment());
        contentValues.put(DatabaseHelper.REVIEW_USER_FIELD, reviewRestaurant.getUser().getId());
        contentValues.put(DatabaseHelper.REVIEW_RESTAURANT_FIELD, reviewRestaurant.getRestaurant().getId());
        contentValues.put(DatabaseHelper.RATING_FIELD, reviewRestaurant.getRating());
        contentValues.put(DatabaseHelper.REVIEW_IMAGE_FIELD, reviewRestaurant.getImage());
        contentValues.put(DatabaseHelper.ACTIVE_FIELD, reviewRestaurant.getActived());
        contentValues.put(DatabaseHelper.CREATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        return contentValues;
    }

    public void initializeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Role data
        roleDao.insertRole(new Role("Admin"));
        roleDao.insertRole(new Role("User"));
        roleDao.insertRole(new Role("Owner"));
        roleDao.insertRole(new Role("Employee"));
        Role adminRole = roleDao.getRoleByName("Admin");
        Role userRole = roleDao.getRoleByName("User");
        Role ownerRole = roleDao.getRoleByName("Owner");
        Role employeeRole = roleDao.getRoleByName("Employee");

        // User data
        userDao.insertUser(new User("ngynhg", "0364452867", "hung@gmail.com",
                "Nguyễn Văn Hùng", "admin@123", adminRole,
                "https://static.vecteezy.com/system/resources/previews/004/819/327/original/male-avatar-profile-icon-of-smiling-caucasian-man-vector.jpg"));
        userDao.insertUser(new User("tranquangh", "0976543210", "tranquangh@gmail.com",
                "Trần Quang Hải", "admin@12345", adminRole,
                "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"));
        userDao.insertUser(new User("lethanhh", "0912345679", "lethanhh@gmail.com",
                "Lê Thanh Hà", "admin@12345", ownerRole,
                "https://static.vecteezy.com/system/resources/previews/014/212/681/original/female-user-profile-avatar-is-a-woman-a-character-for-a-screen-saver-with-emotions-for-website-and-mobile-app-design-illustration-on-a-white-isolated-background-vector.jpg"));
        userDao.insertUser(new User("nguyenducm", "0901234568", "nguyenducm@gmail.com",
                "Nguyễn Đức Minh", "admin@12345", ownerRole,
                "https://img.freepik.com/premium-vector/female-user-profile-avatar-is-woman-character-screen-saver-with-emotions_505620-617.jpg"));
        userDao.insertUser(new User("hoangthi", "0987123457", "hoangthi@gmail.com",
                "Hoàng Thị Lan", "admin@12345", ownerRole,
                "https://img.freepik.com/premium-vector/avatar-icon002_750950-52.jpg"));
        userDao.insertUser(new User("phanvanq", "0898765433", "phanvanq@gmail.com",
                "Phan Văn Quân", "admin@12345", ownerRole,
                "https://img.freepik.com/free-photo/androgynous-avatar-non-binary-queer-person_23-2151100270.jpg?size=338&ext=jpg&ga=GA1.1.2008272138.1724630400&semt=ais_hybrid"));
        userDao.insertUser(new User("vuongthanh", "0364623646", "vuongthanh@gmail.com",
                "Vương Thành", "admin@12345", ownerRole,
                "https://cdn1.iconfinder.com/data/icons/user-pictures/101/malecostume-512.png"));
        userDao.insertUser(new User("daohoang", "0364623642", "daohoang@gmail.com",
                "Đào Hoàng Nam", "admin@12345", employeeRole,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIJxOLfDct_vEPdS-6OsRnS-kDl_HCv5nI2A&s"));
        userDao.insertUser(new User("ngoanguyen", "0854321099", "ngoanguyen@gmail.com",
                "Ngô Anh Nguyễn", "admin@12345", employeeRole,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnTPndLSFHM9SS8t1wJYeBjfurdYsg8MVYsg&s"));
        userDao.insertUser(new User("phamquang", "0843210988", "phamquang@gmail.com",
                "Phạm Quang Hưng", "admin@12345", userRole,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0BtGB_9GUsrrRqX2Wo4sgzHfh9LNmm4gkkQ&s"));

        User user10 = userDao.getUserByUsername("phamquang");
        User user9 = userDao.getUserByUsername("ngoanguyen");
        User user3 = userDao.getUserByUsername("lethanhh");
        User user4 = userDao.getUserByUsername("nguyenducm");
        User user5 = userDao.getUserByUsername("hoangthi");
        User user6 = userDao.getUserByUsername("phanvanq");
        User user7 = userDao.getUserByUsername("vuongthanh");
        User user8 = userDao.getUserByUsername("daohoang");
        User user2 = userDao.getUserByUsername("tranquangh");
        User user1 = userDao.getUserByUsername("ngynhg");

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
        resDao.insertRestaurant(new Restaurant("Gà Rán KFC",
                "123 Nguyễn Thị Minh Khai, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh",
                "057265484", rc1,
                "https://down-bs-vn.img.susercontent.com/vn-11134513-7r98o-lstz97xambb85d@resize_ss280x175!@crop_w280_h175_cT",
                user3));
        resDao.insertRestaurant(new Restaurant("Pizza Domino's",
                "456 Lê Lai, Phường Bến Thành, Quận 1, Thành phố Hồ Chí Minh",
                "0123456789", rc2,
                "https://food-cms.grab.com/compressed_webp/merchants/5-C6TFVTBYPEBVAN/hero/c28c456ed51847aea61c10191cdaafad_1720594176567847800.webp",
                user4));
        resDao.insertRestaurant(new Restaurant("Burgers King",
                "789 Phạm Văn Đồng, Phường 11, Quận Bình Thạnh, Thành phố Hồ Chí Minh",
                "0987654321", rc3,
                "https://food-cms.grab.com/compressed_webp/merchants/5-C6TFVTBYPEBVAN/hero/c28c456ed51847aea61c10191cdaafad_1720594176567847800.webp",
                user5));
        resDao.insertRestaurant(new Restaurant("Sakura Sushi",
                "321 Đinh Tiên Hoàng, Phường 1, Quận 1, Thành phố Hồ Chí Minh",
                "0234567890", rc4,
                "https://food-cms.grab.com/compressed_webp/merchants/5-C6TFVTBYPEBVAN/hero/c28c456ed51847aea61c10191cdaafad_1720594176567847800.webp",
                user6));
        resDao.insertRestaurant(new Restaurant("Taco Bell",
                "654 Nguyễn Huệ, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh",
                "0345678901", rc5,
                "https://food-cms.grab.com/compressed_webp/merchants/5-C6TFVTBYPEBVAN/hero/c28c456ed51847aea61c10191cdaafad_1720594176567847800.webp",
                user7));

        Restaurant res1 = resDao.getRestaurantByName("Gà Rán KFC");
        Restaurant res2 = resDao.getRestaurantByName("Pizza Domino's");
        Restaurant res3 = resDao.getRestaurantByName("Burgers King");
        Restaurant res4 = resDao.getRestaurantByName("Sakura Sushi");
        Restaurant res5 = resDao.getRestaurantByName("Taco Bell");

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
        foodDao.insertFood(new Food("Mì Ý Bò Băm",
                45000, 0,
                "https://hidafoods.vn/wp-content/uploads/2024/06/cach-lam-mi-y-sot-thit-bo-bam-ngon-chuan-vi-Y-1.jpg",
                "Mì bò 2 trứng",
                fc1, res1));
        foodDao.insertFood(new Food("Pizza Pepperoni",
                50000, 0,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Eq_it-na_pizza-margherita_sep2005_sml.jpg/640px-Eq_it-na_pizza-margherita_sep2005_sml.jpg",
                "Pizza Pepperoni với phô mai và ớt",
                fc2, res2));
        foodDao.insertFood(new Food("Sushi California",
                55000, 0,
                "https://www.vinmec.com/static/uploads/20210317_143609_055773_sushi_max_1800x1800_jpg_3690d18076.jpg",
                "Sushi California với cá hồi",
                fc3, res3));
        foodDao.insertFood(new Food("Bánh Hamburger Phô Mai",
                40000, 0,
                "https://www.google.com/search?sca_esv=0d4a60feda95d33d&q=sushi&udm=2&fbs=AEQNm0D7NTKsOqMPi-yhU7bWDsijXeHIssQxQHiKhz3Orm0Szk2q6O3Esev6DIwpyqAb2BgF85BRuPPo79PGgFxkr43-tgC09mLCjuCWnGc7KSn2Tfk8NzZmTtYZjrvI6GV5ySofPj92LXd54Sp_06vdKT2PuwAv57L1mtkNcwdHezXL3Ok_k4jNKW5hL3_JXQeK63u95elp-ny7uQrE57cLx3PQ897PGeofiD-NpMob5Qsj__OMly0&sa=X&ved=2ahUKEwiO65DilpCIAxV67jgGHYdgEAEQtKgLegQIERAB&biw=1912&bih=964&dpr=1#vhid=RPdruozn2Wt7wM&vssid=mosaic",
                "Bánh hamburger với phô mai và thịt bò",
                fc4, res4));
        foodDao.insertFood(new Food("Tacos Bò",
                35000, 0, "https://bizweb.dktcdn.net/100/004/714/files/banh-taco-mexico.jpg?v=1662973104483",
                "Tacos bò với rau và sốt",
                fc5, res5));
        foodDao.insertFood(new Food("Bánh Chocolate",
                27000, 0,
                "https://cdn.tgdd.vn/Files/2020/05/05/1253712/cach-lam-banh-fudge-socola-tan-chay-mem-min-thom-n-6-760x367.jpg",
                "Bánh chocolate mềm với lớp kem",
                fc8, res1));
        foodDao.insertFood(new Food("Pizza Margherita",
                55000, 0,
                "https://au.ooni.com/cdn/shop/articles/20220211142645-margherita-9920.jpg?crop=center&height=800&v=1662539926&width=800",
                "Pizza Margherita với cà chua và phô mai",
                fc2, res1));
        foodDao.insertFood(new Food("Pizza BBQ Chicken",
                60000, 0,
                "https://www.allrecipes.com/thmb/qZ7LKGV1_RYDCgYGSgfMn40nmks=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/AR-24878-bbq-chicken-pizza-beauty-4x3-39cd80585ad04941914dca4bd82eae3d.jpg",
                "Pizza BBQ Chicken với gà và sốt BBQ",
                fc2, res1));
        foodDao.insertFood(new Food("Pizza Veggie",
                57000, 0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOkN2Vc0CMbhBQbEpTcaZiaA2q_022efEmjg&s",
                "Pizza Veggie với rau củ",
                fc2, res1));
        foodDao.insertFood(new Food("Bánh Burger Gà",
                42000, 0,
                "https://cdn.tgdd.vn/Files/2022/01/06/1409368/3-cach-lam-banh-hamburger-ga-ngon-nhu-ngoai-tiem-202201070934570531.jpg",
                "Bánh Burger với thịt gà",
                fc4, res2));
        foodDao.insertFood(new Food("Bánh Burger Veggie",
                40000, 0,
                "https://cdn-images.vtv.vn/zoom/640_400/2019/10/10/0018livekindly-hungry-jacks-1-1570683910192838263212.jpg",
                "Bánh Burger với rau củ",
                fc4, res2));
        foodDao.insertFood(new Food("Khoai Tây Chiên",
                25000, 0,
                "https://cdn.tgdd.vn/2021/07/CookProduct/khoai-tay-chien-bao-nhieu-calo-an-khoai-lang-hay-khoai-tay-chien-tot-hon-00-1200x676.jpg",
                "Khoai tây chiên giòn",
                fc4, res3));
        foodDao.insertFood(new Food("Sushi Ngũ Cốc",
                60000, 0,
                "https://afamilycdn.com/150157425591193600/2021/4/1/photo-1617291824523-16172918247441452577508.jpg",
                "Sushi với ngũ cốc và rau củ",
                fc3, res4));
        foodDao.insertFood(new Food("Sushi Salmon",
                65000, 0,
                "https://recipes.net/wp-content/uploads/2023/07/easy-salmon-sushi_e1b62dc704501a791c2bd8a92e60dd54.jpeg",
                "Sushi với cá hồi tươi",
                fc3, res4));
        foodDao.insertFood(new Food("Sushi Tuna",
                62000, 0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbfV7iV6CZBb5VR_92P5pFHdcbtsNv8ABAKg&s",
                "Sushi với cá ngừ tươi",
                fc3, res4));
        foodDao.insertFood(new Food("Sashimi",
                70000, 0,
                "https://cdn.tgdd.vn/2021/10/CookRecipe/CookTipsNote/mon-sashimi-la-gi-cach-an-sashimi-nhat-ban-phan-biet-sushi-va-tipsnote-800x533.jpg",
                "Sashimi với các loại cá tươi",
                fc3, res4));
        foodDao.insertFood(new Food("Burrito",
                38000, 0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHoo43FA37wwpActbqR1Bjvm_s5gaq_Ik3Xw&s",
                "Burrito với thịt bò và rau",
                fc5, res5));
        foodDao.insertFood(new Food("Quesadilla",
                35000, 0,
                "https://cdn.loveandlemons.com/wp-content/uploads/2024/01/quesadilla.jpg",
                "Quesadilla với phô mai và gà",
                fc5, res5));
        foodDao.insertFood(new Food("Nachos",
                33000, 0,
                "https://www.simplyrecipes.com/thmb/xTCx1mKCjjPYgGasys_JGafuem0=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2019__04__Nachos-LEAD-3-e6dd6cbb61474c9889e1524b3796601e.jpg",
                "Nachos với sốt phô mai và thịt",
                fc5, res5));

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

        // Cart data
        cartDao.insertCart(new Cart(user1, res1, 0));
        cartDao.insertCart(new Cart(user2, res1, 0));
        cartDao.insertCart(new Cart(user3, res2, 0));
        cartDao.insertCart(new Cart(user4, res3, 0));
        cartDao.insertCart(new Cart(user5, res4, 1));
        cartDao.insertCart(new Cart(user6, res5, 1));
        cartDao.insertCart(new Cart(user7, res1, 1));
        cartDao.insertCart(new Cart(user8, res2, 1));
        cartDao.insertCart(new Cart(user9, res3, 1));
        cartDao.insertCart(new Cart(user10, res4, 1));
        cartDao.insertCart(new Cart(user2, res5, 1));

        Cart cart1 = cartDao.getCartById(1);
        Cart cart2 = cartDao.getCartById(2);
        Cart cart3 = cartDao.getCartById(3);
        Cart cart4 = cartDao.getCartById(4);
        Cart cart5 = cartDao.getCartById(5);
        Cart cart6 = cartDao.getCartById(6);
        Cart cart7 = cartDao.getCartById(7);
        Cart cart8 = cartDao.getCartById(8);
        Cart cart9 = cartDao.getCartById(9);
        Cart cart10 = cartDao.getCartById(10);

        // Cart detail data
        cartDetailDao.insertCartDetail(new CartDetail(f1, 2, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 3, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 4, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 5, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f1, 1, cart1));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 2, cart1));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 3, cart1));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 4, cart1));
        cartDetailDao.insertCartDetail(new CartDetail(f5, 5, cart1));
        cartDetailDao.insertCartDetail(new CartDetail(f6, 1, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f7, 2, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f8, 3, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f9, 4, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f10, 5, cart2));
        cartDetailDao.insertCartDetail(new CartDetail(f1, 2, cart3));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 3, cart3));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 4, cart3));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 5, cart3));
        cartDetailDao.insertCartDetail(new CartDetail(f5, 1, cart3));
        cartDetailDao.insertCartDetail(new CartDetail(f6, 2, cart4));
        cartDetailDao.insertCartDetail(new CartDetail(f7, 3, cart4));
        cartDetailDao.insertCartDetail(new CartDetail(f8, 4, cart4));
        cartDetailDao.insertCartDetail(new CartDetail(f9, 5, cart4));
        cartDetailDao.insertCartDetail(new CartDetail(f10, 1, cart4));
        cartDetailDao.insertCartDetail(new CartDetail(f1, 3, cart5));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 4, cart5));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 5, cart5));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 1, cart5));
        cartDetailDao.insertCartDetail(new CartDetail(f5, 2, cart5));
        cartDetailDao.insertCartDetail(new CartDetail(f6, 3, cart6));
        cartDetailDao.insertCartDetail(new CartDetail(f7, 4, cart6));
        cartDetailDao.insertCartDetail(new CartDetail(f8, 5, cart6));
        cartDetailDao.insertCartDetail(new CartDetail(f9, 1, cart6));
        cartDetailDao.insertCartDetail(new CartDetail(f10, 2, cart6));
        cartDetailDao.insertCartDetail(new CartDetail(f1, 4, cart7));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 5, cart7));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 1, cart7));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 2, cart7));
        cartDetailDao.insertCartDetail(new CartDetail(f5, 3, cart7));
        cartDetailDao.insertCartDetail(new CartDetail(f6, 4, cart8));
        cartDetailDao.insertCartDetail(new CartDetail(f7, 5, cart8));
        cartDetailDao.insertCartDetail(new CartDetail(f8, 1, cart8));
        cartDetailDao.insertCartDetail(new CartDetail(f9, 2, cart8));
        cartDetailDao.insertCartDetail(new CartDetail(f10, 3, cart8));
        cartDetailDao.insertCartDetail(new CartDetail(f1, 5, cart9));
        cartDetailDao.insertCartDetail(new CartDetail(f2, 1, cart9));
        cartDetailDao.insertCartDetail(new CartDetail(f3, 2, cart9));
        cartDetailDao.insertCartDetail(new CartDetail(f4, 3, cart9));
        cartDetailDao.insertCartDetail(new CartDetail(f5, 4, cart9));
        cartDetailDao.insertCartDetail(new CartDetail(f6, 5, cart10));
        cartDetailDao.insertCartDetail(new CartDetail(f7, 1, cart10));
        cartDetailDao.insertCartDetail(new CartDetail(f8, 2, cart10));
        cartDetailDao.insertCartDetail(new CartDetail(f9, 3, cart10));
        cartDetailDao.insertCartDetail(new CartDetail(f10, 4, cart10));

        // Review data
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tại đây rất chuyên nghiệp và không gian rất ấm cúng và thoải mái.", 5.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian quán đẹp mắt nhưng giá cả hơi cao so với chất lượng dịch vụ.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian sang trọng và dịch vụ tận tình, tạo cảm giác dễ chịu.", 4.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Mặc dù không gian và trang trí khá đẹp, nhưng dịch vụ cần được cải thiện thêm.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Tôi thích không gian và trang trí của quán, nhưng có vẻ hơi ồn ào và cần thêm sự riêng tư.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ rất nhanh chóng và không gian thật sự dễ chịu.", 4.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian sang trọng, nhưng giá cả hơi cao.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian đẹp, món ăn ngon, nhưng dịch vụ hơi chậm.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Món ăn tuyệt vời, nhưng không gian quán khá ồn ào.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Quán có dịch vụ rất tốt và không gian rất ấm cúng.", 5.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng sạch sẽ nhưng trang trí có vẻ hơi đơn giản.", 3.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tốt nhưng không gian hơi nhỏ và không thoải mái lắm.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Tôi rất thích món ăn, tuy nhiên không gian có phần ồn ào.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian đẹp và phục vụ tận tình.", 4.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Món ăn rất ngon nhưng giá cả hơi cao so với chất lượng.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ và không gian đều rất tốt, mình sẽ quay lại.", 5.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian đẹp, nhưng món ăn và dịch vụ cần cải thiện.", 3.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Quán có không gian thoải mái nhưng món ăn hơi nhạt.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ rất chuyên nghiệp nhưng không gian hơi nhỏ.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Tôi rất thích không gian quán, mặc dù món ăn còn cần cải thiện.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user5, res5));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có không gian ấm cúng và dịch vụ nhanh chóng.", 4.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user1, res1));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tốt nhưng không gian hơi chật chội.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user2, res2));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Không gian rất đẹp và món ăn rất ngon.", 4.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user3, res3));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Nhà hàng có phong cách trang trí đẹp nhưng món ăn hơi đắt.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user4, res4));
        reviewRestaurantDao.insertReview(new ReviewRestaurant("Dịch vụ tuyệt vời và không gian thoải mái, nhưng món ăn chưa đặc sắc.", 3.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSJ_5X-qEcnCYAJ_TrGfICezQSIBOTjcpNsA&s",
                user5, res5));

        reviewFoodDao.insertReview(new ReviewFood("Rất ngon, dịch vụ tuyệt vời!", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Rất ngon, dịch vụ tuyệt vời!", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Pizza rất thơm, nhưng giá hơi cao.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Sushi rất tươi ngon, không gian đẹp.", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Hamburger hơi bị nhạt, cần thêm gia vị.", 3.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Tacos rất tuyệt, nhưng món tráng miệng không ngon lắm.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Salad rất tươi, dịch vụ rất nhanh.", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Bánh mì rất ngon, giá cả hợp lý.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Bánh chocolate không được như kỳ vọng.", 2.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Cá hồi nướng rất ngon, không gian thoải mái.", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Súp Tom Yum có vị hơi chua, cần điều chỉnh.", 3.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user10, f10));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn tuyệt vời, sẽ quay lại lần nữa!", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ tốt, món ăn hơi nhạt.", 3.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Thực phẩm tươi ngon, nhưng thời gian chờ hơi lâu.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Không gian sạch sẽ, món ăn không như kỳ vọng.", 2.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn rất tốt, giá hơi cao.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ không đạt yêu cầu, món ăn cũng bình thường.", 2.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon và phục vụ nhanh chóng.", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Nhà hàng có không gian đẹp, nhưng món ăn chỉ ở mức trung bình.", 3.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ xuất sắc và món ăn rất ngon.", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn không đạt yêu cầu, nhưng không gian khá dễ chịu.", 2.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user10, f10));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn ngon tuyệt vời, dịch vụ rất thân thiện.", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user1, f1));
        reviewFoodDao.insertReview(new ReviewFood("Thực đơn đa dạng, nhưng món ăn hơi ngọt.", 3.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user2, f2));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ hơi chậm, món ăn không đồng đều.", 2.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user3, f3));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn rất tốt, không gian hơi ồn ào.", 4.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user4, f4));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon nhưng giá cả hơi cao.", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user5, f5));
        reviewFoodDao.insertReview(new ReviewFood("Nhà hàng sạch sẽ nhưng món ăn khá đơn giản.", 3.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user6, f6));
        reviewFoodDao.insertReview(new ReviewFood("Dịch vụ tận tâm và món ăn rất sáng tạo.", 5.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user7, f7));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn được trình bày đẹp mắt, nhưng hương vị chưa thuyết phục.", 3.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user8, f8));
        reviewFoodDao.insertReview(new ReviewFood("Món ăn rất ngon và phong phú, phục vụ chuyên nghiệp.", 4.5,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user9, f9));
        reviewFoodDao.insertReview(new ReviewFood("Chất lượng món ăn và dịch vụ không như mong đợi.", 2.0,
                "https://spicyfoodstudio.com/wp-content/uploads/2023/05/anh-do-an-vat-tu-chup-09.jpg",
                user10, f10));
    }


}
