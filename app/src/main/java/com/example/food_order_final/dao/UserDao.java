package com.example.food_order_final.dao;

import static com.android.volley.VolleyLog.TAG;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

import com.example.food_order_final.database.DatabaseHelper;
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

    public boolean validateUser(User user) {
        if (user == null) {
            Log.d(TAG, "User can not be null");
            return false;
        }
        boolean isUsername = user.getUsername().matches("^[\\S]{3,}$");
        boolean isEmail = user.getEmail().matches(String.valueOf(Patterns.EMAIL_ADDRESS));
        boolean isPhoneNumber = user.getPhoneNumber().matches("^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|8|9]|9[0-4|6-9])[0-9]{7}$");
        boolean isFullname = user.getFullName() != null || !user.getFullName().trim().isEmpty();
        boolean isPassword = user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        boolean isUserExist = dbHelper.userDao.checkUsername(user.getUsername());

        if (!isUsername || !isEmail || !isPhoneNumber || !isFullname || !isPassword) {
            if (isUserExist) {
                Log.d(TAG, "Username " + user.getUsername() + " already exist!");
            }
            else if (!isUsername) {
                Log.d(TAG, "Username too short!");
            }
            else if (!isEmail) {
                Log.d(TAG, "Email not valid!");
            }
            else if (!isPhoneNumber) {
                Log.d(TAG, "Phone number not available!");
            }
            else if (!isFullname) {
                Log.d(TAG, "Full name can not be null or contain only spaces!");
            }
            else {
                Log.d(TAG, "Invalid password: Must be at least 8 characters, including letters, numbers, and special characters.");
            }

            return false;
        }

        return true;
    }

    public boolean validateUserNoPass(User user) {
        if (user == null) {
            Log.d(TAG, "User can not be null");
            return false;
        }
        boolean isUsername = user.getUsername().matches("^[\\S]{3,}$");
        boolean isEmail = user.getEmail().matches(String.valueOf(Patterns.EMAIL_ADDRESS));
        boolean isPhoneNumber = user.getPhoneNumber().matches("^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|8|9]|9[0-4|6-9])[0-9]{7}$");
        boolean isFullname = user.getFullName() != null || !user.getFullName().trim().isEmpty();
        boolean isUserExist = dbHelper.userDao.checkUsername(user.getUsername());

        if (!isUsername || !isEmail || !isPhoneNumber || !isFullname) {
            if (isUserExist) {
                Log.d(TAG, "Username " + user.getUsername() + " already exist!");
            }
            else if (!isUsername) {
                Log.d(TAG, "Username too short!");
            }
            else if (!isEmail) {
                Log.d(TAG, "Email not valid!");
            }
            else if (!isPhoneNumber) {
                Log.d(TAG, "Phone number not available!");
            }
            else if (!isFullname) {
                Log.d(TAG, "Full name can not be null or contain only spaces!");
            }

            return false;
        }

        return true;
    }

    public long insertUser(User user) {
        long result = -1;

        if (validateUser(user)) {
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
//            db.close();
        }

        if (result == -1) {
            Log.e(TAG, "Failed to insert user into the database.");
        } else {
            Log.d(TAG, "User inserted successfully with ID: " + result);
        }

        return result;
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
        if (dbHelper.userDao.checkUsernameExcludeUserId(user.getUsername(), user.getId())){
            Log.e(TAG, "EXISTS USERNAME");
            return -1;
        }
        if (dbHelper.userDao.validateUserNoPass(user)) {
            Log.e(TAG, "VALIDATE SUCCESSFUL");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.USER_USERNAME_FIELD, user.getUsername());
            contentValues.put(DatabaseHelper.USER_PHONE_NUMBER_FIELD, user.getPhoneNumber());
            contentValues.put(DatabaseHelper.USER_EMAIL_FIELD, user.getEmail());
            contentValues.put(DatabaseHelper.USER_FULL_NAME_FIELD, user.getFullName());
            contentValues.put(DatabaseHelper.USER_ROLE_FIELD, user.getRole().getId());
            contentValues.put(DatabaseHelper.ACTIVE_FIELD, user.isActived());
            contentValues.put(DatabaseHelper.UPDATED_DATE_FIELD, DateUtil.dateToTimestamp(new Date()));
            contentValues.put(DatabaseHelper.USER_AVATAR_FIELD, (user.getAvatar()));
            String whereClause = DatabaseHelper.ID_FIELD + " = ? ";
            String[] whereArgs = new String[]{String.valueOf(user.getId())};
            int rowAffected = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, whereClause, whereArgs);
            db.close();
            return rowAffected;
        }
        Log.e(TAG, "UPDATE FAILED");
        return -1;
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

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ACTIVE_FIELD, 0);

        long update = db.update(DatabaseHelper.TABLE_USER_NAME, contentValues, whereClause, whereArgs);
        db.close();
        if (update == -1) {
            return false;
        } return true;
    }

    public boolean deleteUserHard(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.ID_FIELD + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        long update = db.delete(DatabaseHelper.TABLE_USER_NAME, whereClause, whereArgs);
        db.close();
        if (update == -1) {
            return false;
        } return true;
    }

    public boolean hasRestaurant(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean result = false;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RESTAURANT_NAME
                    + " WHERE " + DatabaseHelper.RESTAURANT_USER_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                result = cursor.getCount() > 1;
            }
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
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? " +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
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

    public boolean checkUsernameExcludeUserId(String username, int excludeUserId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;
        Cursor cursor = null;

        try {
            String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_USER_NAME +
                    " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? " +
                    "AND " + DatabaseHelper.ID_FIELD + " <> ?";
            cursor = db.rawQuery(query, new String[]{username, String.valueOf(excludeUserId)});

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                exists = count > 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ? " +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                String pass = getString(cursor, DatabaseHelper.USER_PASSWORD_FIELD);
                isValid = BCrypt.checkpw(password, pass);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return isValid;
    }

    public long countUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        long total = 0;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM " + DatabaseHelper.TABLE_USER_NAME, null);
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

    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME + " WHERE " + DatabaseHelper.ACTIVE_FIELD + " = 1",
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

    public List<User> getAllUsersAdmin() {
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
                    + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " LIKE ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
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
                            + " WHERE " + DatabaseHelper.USER_FULL_NAME_FIELD + " LIKE ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
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
                            + " WHERE " + DatabaseHelper.USER_USERNAME_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
                    new String[]{userUsername});

            if (cursor != null && cursor.moveToFirst()) {
                user = getUserInfo(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
//            db.close();
        }

        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME
                            + " WHERE " + DatabaseHelper.ID_FIELD + " = ?" +
                            " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1",
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
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<User> users = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();

            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_NAME +
                    " INNER JOIN " + DatabaseHelper.TABLE_ROLE_NAME +
                    " ON " + DatabaseHelper.TABLE_USER_NAME + ".role_id = " + DatabaseHelper.TABLE_ROLE_NAME + ".id" +
                    " WHERE " + DatabaseHelper.TABLE_ROLE_NAME + ".name = ?";
            cursor = db.rawQuery(query, new String[]{roleName});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    User user = getUserInfo(cursor);
                    users.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
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