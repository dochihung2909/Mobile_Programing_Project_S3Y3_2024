package com.example.food_order_final.dao;

import static com.android.volley.VolleyLog.TAG;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.database.DbBitmapUtility;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDao extends BaseDao{
    private DatabaseHelper dbHelper;
    private RoleDao roleDao;

    public UserDao(DatabaseHelper dbHelper, RoleDao roleDao) {
        super(dbHelper);
        this.dbHelper = dbHelper;
        this.roleDao = roleDao;
    }
    public long insertUser(User user) {
        long result = -1;

        if (user == null ||
                dbHelper.userDao.checkUsername(user.getUsername()) ||
                !user.getUsername().matches("^[\\S]{3,}$") ||
                !user.getEmail().matches(String.valueOf(Patterns.EMAIL_ADDRESS)) ||
                !user.getPhoneNumber().matches("(84|0[3|5|7|8|9])+([0-9]{8})\\b") ||
                user.getFullName() == null || user.getFullName().trim().isEmpty() ||
                !user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {

            if (user == null) {
                Log.d(TAG, "User can not be null");
            }
            if (dbHelper.userDao.checkUsername(user.getUsername())) {
                Log.d(TAG, "Username " + user.getUsername() + " already exist!");
//            throw new IllegalArgumentException("Invalid username: Username already exists.");
            }
            if (!user.getUsername().matches("^[\\S]{3,}$")) {
                Log.d(TAG, "Username too short!");
//            throw new IllegalArgumentException("Invalid username: Must be at least 3 non-whitespace characters.");
            }
            if (!user.getEmail().matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                Log.d(TAG, "Email not valid!");
//            throw new IllegalArgumentException("Invalid email address.");
            }
            if (!user.getPhoneNumber().matches("(84|0[3|5|7|8|9])+([0-9]{8})\\b")) {
                Log.d(TAG, "Phone number not available!");
//            throw new IllegalArgumentException("Invalid phone number format.");
            }
            if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
                Log.d(TAG, "Full name can not be null or contain only spaces!");
//            throw new IllegalArgumentException("Full name can not be null or contain only spaces!");
            }
            if (!user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
                Log.d(TAG, "Invalid password: Must be at least 8 characters, including letters, numbers, and special characters.");
//            throw new IllegalArgumentException("Invalid password: Must be at least 8 characters, including letters, numbers, and special characters.");
            }
            result = -1;
        } else {
            Log.d(TAG, "User: " + user);
            Log.d(TAG, "Username: " + user.getUsername());
            Log.d(TAG, "Phone number: " + user.getPhoneNumber());
            Log.d(TAG, "Email: " + user.getEmail());
            Log.d(TAG, "Full name: " + user.getFullName());
            Log.d(TAG, "Password: " + user.getPassword());
            Log.d(TAG, "Is active: " + user.getActived());

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = dbHelper.getUserContentValues(user);
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            contentValues.put(DatabaseHelper.USER_PASSWORD_FIELD, hashedPassword);

            result = db.insert(DatabaseHelper.TABLE_USER_NAME, null, contentValues);
            db.close();
        }

        if (result == -1) {
            Log.e(TAG, "Failed to insert user into the database.");
//            throw new IllegalArgumentException("Failed to insert user into the database.");
        } else {
            Log.d(TAG, "User inserted successfully with ID: " + result);
        }

        return result;
    }

    public boolean updateUserRole(int userId, int roleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_ROLE_FIELD, roleId);
        long update = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, DatabaseHelper.ID_FIELD + " = ?", new String[]{String.valueOf(userId)});
        if (update == -1) {
            return false;
        }
        return true;
    }

    public int updateUserInfo(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(DatabaseHelper.USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(DatabaseHelper.USER_FULL_NAME_FIELD, user.getFullName());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));
        contentValues.put(DatabaseHelper.USER_AVATAR_FIELD, (user.getAvatar()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public int updateUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(DatabaseHelper.USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
        contentValues.put(DatabaseHelper.USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(DatabaseHelper.USER_FULL_NAME_FIELD, user.getFullName());
        contentValues.put(DatabaseHelper.USER_ROLE_FIELD, user.getRole().getId());
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));
        contentValues.put(DatabaseHelper.USER_AVATAR_FIELD, (user.getAvatar()));

        String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};

        int rowAffected = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, whereClause, whereArgs);

        db.close();
        return rowAffected;
    }

    public int authorizeEmployee(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Role employeeRole = dbHelper.roleDao.getRoleByName("Employee");
        ContentValues values = dbHelper.getUserContentValues(user);
        values.put(DatabaseHelper.USER_ROLE_FIELD, employeeRole.getId());
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};
        int result = db.update(DatabaseHelper.TABLE_USER_NAME, values, whereClause, whereArgs);
        db.close();

        if (result != -1)
            Log.d(TAG, "Authorize user " + user.getUsername() + " to employee successful");
        else
            Log.d(TAG, "Authorize user " + user.getUsername() + " to employee failed !");

        return result;
    }

    public int authorizeOwner(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Role ownerRole = dbHelper.roleDao.getRoleByName("Owner");
        ContentValues values = dbHelper.getUserContentValues(user);
        values.put(DatabaseHelper.USER_ROLE_FIELD, ownerRole.getId());
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};
        int result = db.update(DatabaseHelper.TABLE_USER_NAME, values, whereClause, whereArgs);
        db.close();

        if (result != -1)
            Log.d(TAG, "Authorize user " + user.getUsername() + " to 'Owner' successful");
        else
            Log.d(TAG, "Authorize user " + user.getUsername() + " to 'Owner' failed !");

        return result;
    }

    public int updateUserPassword(int userId, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put(DatabaseHelper.USER_PASSWORD_FIELD, hashedPassword);
        contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));

        int rowAffected = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues,
                DatabaseHelper.ID_FIELD+ " = ? ", new String[]{String.valueOf(userId)});

        return rowAffected;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        db.delete(DatabaseHelper.TABLE_USER_NAME, whereClause, whereArgs);
        db.close();
    }

    public boolean hasRestaurant(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean result = false;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                    + " WHERE " + DatabaseHelper.RESTAURANT_USER_FIELD + " = ?",
                    new String[]{String.valueOf(userId)});
            result = cursor.getCount() > 1;
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return result;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT 1 FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? ",
                    new String[]{username});
            if (cursor.moveToFirst()){
                exists = true;
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return exists;
    }

    public boolean isUserCredential(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean isValid = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? ",
                    new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                int passwordColumnIndex = cursor.getColumnIndex(DatabaseHelper.USER_PASSWORD_FIELD);
                if (passwordColumnIndex != -1) {
                    String hashedPassword = cursor.getString(passwordColumnIndex);
                    isValid = BCrypt.checkpw(password, hashedPassword);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return isValid;
    }

    public boolean isAdmin(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean result = false;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{username});
            if (cursor.moveToNext()) {

            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return result;
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    users.add(getUserInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

    public List<User> getUsersByUsername(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null; 
        List<User> users = new ArrayList<>();
 
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                    + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " LIKE ?",
                    new String[]{"%" + name + "%"});

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    users.add(getUserInfo(cursor));
                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

    public List<User> getUsersByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_FULL_NAME_FIELD + " LIKE ?",
                    new String[]{"%" + name + "%"});

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    users.add(getUserInfo(cursor));
                } while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return users;
    }

    public User getUserByUsername(String userUsername) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ?",
                    new String[]{userUsername});

            if (cursor != null && cursor.moveToFirst()) {
                user = getUserInfo(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?",
                    new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {

                user = getUserInfo(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return user;
    }

    public List<User> getUsersByRole(String roleName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        Role role = roleDao.getRoleByName(roleName);

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME +
                    " INNER JOIN " + DatabaseHelper.TABLE_ROLE_NAME +
                    " ON " + DatabaseHelper.TABLE_USER_NAME + ".role_id = " + DatabaseHelper.TABLE_ROLE_NAME + ".id" +
                    " WHERE " + DatabaseHelper.TABLE_ROLE_NAME + ".name = ?", new String[]{roleName});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    users.add(getUserInfo(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return users;
    }

    private User getUserInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.ID_FIELD);
        String username = getString(cursor, DatabaseHelper.USER_USERNAME_FIELD);
        String phone = getString(cursor, DatabaseHelper.USER_PHONE_NUMBER_FIELD);
        String email = getString(cursor, DatabaseHelper.USER_EMAIL_FIELD);
        String fullName = getString(cursor, DatabaseHelper.USER_FULL_NAME_FIELD);
        String password = getString(cursor, DatabaseHelper.USER_PASSWORD_FIELD);
        int role_id = getInt(cursor, DatabaseHelper.USER_ROLE_FIELD);
        Role role = roleDao.getRoleById(role_id);
        String avatar = getString(cursor, DatabaseHelper.USER_AVATAR_FIELD);
        boolean isActived = getBoolean(cursor, DatabaseHelper.ACTIVE_FIELD);
        String createdDateString = getString(cursor, DatabaseHelper.CREATED_DATE_FIELD);
        String updatedDateString = getString(cursor, DatabaseHelper.UPDATED_DATE_FIELD);
        Date createdDate = DateUtil.timestampToDate(createdDateString);
        Date updatedDate = DateUtil.timestampToDate(updatedDateString);

        return (new User(id, username, phone, email, fullName, password, role, avatar, isActived, createdDate, updatedDate));
    }

}