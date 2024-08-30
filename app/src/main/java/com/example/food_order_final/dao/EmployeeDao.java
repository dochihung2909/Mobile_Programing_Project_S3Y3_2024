package com.example.food_order_final.dao;

import static com.android.volley.VolleyLog.TAG;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Employee;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.DateUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;

public class EmployeeDao extends BaseDao {
    private DatabaseHelper dbHelper;

    public EmployeeDao(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper;
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
//            throw new IllegalArgumentException("Invalid username: Username already exists.");
            }
            if (!isUsername) {
                Log.d(TAG, "Username too short!");
//            throw new IllegalArgumentException("Invalid username: Must be at least 3 non-whitespace characters.");
            }
            if (!isEmail) {
                Log.d(TAG, "Email not valid!");
//            throw new IllegalArgumentException("Invalid email address.");
            }
            if (!isPhoneNumber) {
                Log.d(TAG, "Phone number not available!");
//            throw new IllegalArgumentException("Invalid phone number format.");
            }
            if (!isFullname) {
                Log.d(TAG, "Full name can not be null or contain only spaces!");
//            throw new IllegalArgumentException("Full name can not be null or contain only spaces!");
            }
            if (!isPassword) {
                Log.d(TAG, "Invalid password: Must be at least 8 characters, including letters, numbers, and special characters.");
//            throw new IllegalArgumentException("Invalid password: Must be at least 8 characters, including letters, numbers, and special characters.");
            }

            return false;
        }

        return true;
    }



    public long insertEmployee(Employee employee) {
        long result = -1;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValuesEmployee = new ContentValues();
        contentValuesEmployee.put(DatabaseHelper.ACTIVE_FIELD, employee.isActived());
        contentValuesEmployee.put(DatabaseHelper.CREATED_DATE_FIELD, DateUtil.dateToTimestamp(employee.getCreatedDate()));
        contentValuesEmployee.put(DatabaseHelper.UPDATED_DATE_FIELD,DateUtil.dateToTimestamp(employee.getUpdatedDate()));
        contentValuesEmployee.put(DatabaseHelper.EMPLOYEE_USER_FILED, employee.getId());
        contentValuesEmployee.put(DatabaseHelper.EMPLOYEE_RESTAURANT_FIELD, employee.getRestaurant().getId());
        Log.d(TAG, "" +  employee.getId() + " " + employee.getRestaurant().getId());
        result = db.insert(DatabaseHelper.TABLE_EMPLOYEE_NAME, null, contentValuesEmployee);
        db.close();

        if (result == -1) {
            Log.e(TAG, "Failed to insert user into the database.");
//            throw new IllegalArgumentException("Failed to insert user into the database.");
        } else {
            Log.d(TAG, "User inserted successfully with ID: " + result);
        }

        return result;
    }

    public Employee getEmployeeByUserId(int userId) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Employee employee = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_EMPLOYEE_NAME +
                    " WHERE " + DatabaseHelper.EMPLOYEE_USER_FILED + " = ?" +
                    " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1", new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                employee = getEmployeeInfo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return employee;
    }

    public ArrayList<Employee> getAllEmployeeByRestaurantId(int resId) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_EMPLOYEE_NAME +
                    " WHERE " + DatabaseHelper.EMPLOYEE_RESTAURANT_FIELD + " = ?" +
                    " AND " + DatabaseHelper.ACTIVE_FIELD + " = 1", new String[]{String.valueOf(resId)});

            if (cursor.moveToFirst()) {
                do {
                    employees.add(getEmployeeInfo(cursor));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return employees;
    }

    public boolean deleteEmployee(int employeeId, int resId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ACTIVE_FIELD, 0);
        long update = db.update(DatabaseHelper.TABLE_EMPLOYEE_NAME, contentValues,
                DatabaseHelper.EMPLOYEE_USER_FILED + " = ? AND " + DatabaseHelper.EMPLOYEE_RESTAURANT_FIELD + " = ?", new String[]{String.valueOf(employeeId), String.valueOf(resId)});
        if (update == -1) {
            return false;
        }
        return true;
    }

    private Employee getEmployeeInfo(Cursor cursor) {
        int id = getInt(cursor, DatabaseHelper.EMPLOYEE_USER_FILED);
        User user = dbHelper.userDao.getUserById(id);
        int restaurantId = getInt(cursor, DatabaseHelper.EMPLOYEE_RESTAURANT_FIELD);
        Restaurant restaurant = dbHelper.resDao.getRestaurantById(restaurantId);

        return (new Employee(user, restaurant));
    }
}
