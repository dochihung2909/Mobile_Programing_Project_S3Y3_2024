package com.example.food_order_final.activity.admin;

import static com.android.volley.VolleyLog.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminCartAdapter;
import com.example.food_order_final.adapter.AdminUserAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminCartManagement extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ImageButton btnBackToMain;
    private Button btnAdminAddCart, btnFilter;
    private ListView lvAdminCart;
    private Spinner spnAdminCartUser, spnAdminCartCategory,
            spnAdminCartMonth, spnAdminCartDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_cart_management);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadAllCarts();
        loadAllUsers();
        loadAllCategories();
        loadMonths();
        loadDates();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadAllUsers() {
        List<User> users = dbHelper.userDao.getAllUsers();
        List<String> userUsernames = new ArrayList<>();
        userUsernames.add("All Users");
        for (User user : users) {
            userUsernames.add(user.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userUsernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAdminCartUser.setAdapter(adapter);
    }

    private void loadAllCategories() {
        List<RestaurantCategory> categories = dbHelper.resCateDao.getAllRestaurantCategories();
        List<String> resCateNames = new ArrayList<>();
        resCateNames.add("All Categories");
        for (RestaurantCategory restaurantCategory : categories) {
            resCateNames.add(restaurantCategory.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resCateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAdminCartCategory.setAdapter(adapter);
    }

    private void loadMonths() {
        List<String> months = new ArrayList<>();
        months.add("All Months");
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months.add("0" + i);
            } else {
                months.add(String.valueOf(i));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnAdminCartMonth.setAdapter(adapter);
    }

    private void loadDates() {
        List<String> dates = new ArrayList<>();
        dates.add("All Dates");
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                dates.add("0" + i);
            } else {
                dates.add(String.valueOf(i));
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnAdminCartDate.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);

        btnAdminAddCart = (Button) findViewById(R.id.btnAdminAddCart);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        lvAdminCart = (ListView) findViewById(R.id.lvAdminCart);

        spnAdminCartUser = (Spinner) findViewById(R.id.spnAdminCartUser);
        spnAdminCartCategory = (Spinner) findViewById(R.id.spnAdminCartCategory);
        spnAdminCartMonth = (Spinner) findViewById(R.id.spnAdminCartMonth);
        spnAdminCartDate = (Spinner) findViewById(R.id.spnAdminCartDate);
    }

    public void loadAllCarts() {
        List<Cart> carts = dbHelper.cartDao.getAllCarts();
        AdminCartAdapter adapter = new AdminCartAdapter(this, carts);
        lvAdminCart.setAdapter(adapter);
    }

    private void loadCartsByUsername(String username) {
        dbHelper = new DatabaseHelper(this);
        List<Cart> carts = dbHelper.cartDao.getCartsByUsername(username);
        AdminCartAdapter adapter = new AdminCartAdapter(this, carts);
        lvAdminCart.setAdapter(adapter);
    }

    private void loadCartsByResCate(String resCateName) {
        List<Cart> carts = dbHelper.cartDao.getCartsByResCateName(resCateName);
        AdminCartAdapter adapter = new AdminCartAdapter(this, carts);
        lvAdminCart.setAdapter(adapter);
    }

    private void loadCartsByMonth(int month) {
        List<Cart> carts = dbHelper.cartDao.getCartsByMonth(month);
        AdminCartAdapter adapter = new AdminCartAdapter(this, carts);
        lvAdminCart.setAdapter(adapter);
    }

    private void loadCartsByDate(int date) {
        List<Cart> carts = dbHelper.cartDao.getCartsByDate(date);
        AdminCartAdapter adapter = new AdminCartAdapter(this, carts);
        lvAdminCart.setAdapter(adapter);
    }

    private void setOnClickListener() {
        btnBackToMain.setOnClickListener(v -> finish());

        btnAdminAddCart.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCartManagement.this, AdminCartDetail.class);
            startActivity(intent);
        });

        lvAdminCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cart selectedCart = (Cart) parent.getItemAtPosition(position);
                Intent intent = new Intent(AdminCartManagement.this, AdminCartDetail.class);
                intent.putExtra("cart_id", selectedCart.getId());
                startActivity(intent);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUser = spnAdminCartUser.getSelectedItem().toString();
                String selectedCategory = spnAdminCartCategory.getSelectedItem().toString();
                String selectedMonth = spnAdminCartMonth.getSelectedItem().toString();
                String selectedDate = spnAdminCartDate.getSelectedItem().toString();
                boolean isAllUsers = selectedUser.equals("All Users");
                boolean isAllCategories = selectedCategory.equals("All Categories");
                boolean isAllMonths = selectedMonth.equals("All Months");
                boolean isAllDates = selectedDate.equals("All Dates");
                if (isAllUsers && isAllCategories && isAllMonths && isAllDates) {
                    loadAllCarts();
                } else {
                    if (!isAllUsers) {
                        loadCartsByUsername(selectedUser);
                    } else if (!isAllCategories){
                        Log.d(TAG, "Load cart by cate");
                        Log.d(TAG, "Load cart by cate: " + selectedCategory);
                        loadCartsByResCate(selectedCategory);
                    } else if (!isAllMonths) {
                        loadCartsByMonth(Integer.parseInt(selectedMonth));
                    } else {
                        loadCartsByDate(Integer.parseInt(selectedDate));
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllCarts();
    }
}