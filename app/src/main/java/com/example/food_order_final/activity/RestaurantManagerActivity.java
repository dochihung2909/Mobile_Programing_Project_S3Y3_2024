package com.example.food_order_final.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Employee;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.example.food_order_final.util.PriceUtil;

public class RestaurantManagerActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RatingBar ratingBar;
    private ImageView ivRestaurantAvatar;
    private TextView tvRestaurantName, tvRestaurantTime;
    private LinearLayout restaurantRatingContainer;
    private Button btnRestaurantStatistical, btnEditRestaurantInfo, btnFoodManager, btnCheckOrder, btnEmployeeManager, btnLogout;
    private int REQUEST_EDIT_RESTAURANT_INFO = 202;

    private int ownerId;
    private int employeeId;
    private Restaurant restaurant;
    private Employee currentUser;
    private DatabaseHelper dbHelper = new DatabaseHelper(RestaurantManagerActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.ownerId = getIntent().getIntExtra("restaurantOwnerId", -1);
        this.employeeId = getIntent().getIntExtra("employeeId", -1);
        currentUser = dbHelper.employeeDao.getEmployeeByUserId(employeeId);
        RestaurantDao restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
        if (ownerId != -1 || employeeId != -1) {
            if (employeeId != -1) {
                Employee employee = dbHelper.employeeDao.getEmployeeByUserId(employeeId);
                restaurant = employee.getRestaurant();
                btnEmployeeManager.setVisibility(View.GONE);
                btnEditRestaurantInfo.setVisibility(View.GONE);
                btnFoodManager.setVisibility(View.GONE);
            } else {
                restaurant = restaurantDao.getRestaurantByUserId(ownerId);

            }
            updateUI();

            if (restaurant != null) {
                if (restaurant.getRating() > 0) {
                    ratingBar.setRating((float) restaurant.getRating());
                } else {
                    ratingBar.setVisibility(View.GONE);
                }

                tvRestaurantTime.setText(restaurant.getAddress());

                btnEmployeeManager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantManagerActivity.this, RestaurantEmployeeManagementActivity.class);
                        intent.putExtra("restaurantId", restaurant.getId());
                        startActivity(intent);
                    }
                });

                btnFoodManager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantManagerActivity.this, FoodManagerActivity.class);
                        intent.putExtra("restaurantId", restaurant.getId());
                        startActivity(intent);
                    }
                });

                btnEditRestaurantInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantManagerActivity.this, EditRestaurantActivity.class);
                        intent.putExtra("restaurantId", restaurant.getId());
                        startActivityForResult(intent, REQUEST_EDIT_RESTAURANT_INFO);

                    }
                });

                // Render Restaurant Avatar
                LoadImageUtil.loadImage(ivRestaurantAvatar, restaurant.getAvatar());

                // Statis of restaurant
                btnRestaurantStatistical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantManagerActivity.this);
                        alertDialog.setTitle("Thống kê")
                                .setMessage("Tổng doanh thu của cửa hàng là: " + PriceUtil.formatNumber(dbHelper.paymentPendingDao.getTotalRevenueOfRestaurantId(restaurant.getId())) + " đ")
                                .setPositiveButton("Ok", null).show();
                    }
                });

                btnCheckOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantManagerActivity.this, RestaurantOrderManagementActivity.class);
                        intent.putExtra("restaurantId", restaurant.getId());
                        startActivity(intent);
                    }
                });
            }

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit().clear();
                    editor.apply();

                    Toast.makeText(RestaurantManagerActivity.this, "Đăng xất", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_RESTAURANT_INFO) {
            if (resultCode == RESULT_OK) {
                restaurant = dbHelper.resDao.getRestaurantById(restaurant.getId());
                updateUI();
            }
        }
    }

    private void init() {
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        restaurantRatingContainer = findViewById(R.id.restaurantRatingContainer);
        btnRestaurantStatistical = findViewById(R.id.btnRestaurantStatistical);
        btnEditRestaurantInfo = findViewById(R.id.btnEditRestaurantInfo);
        btnFoodManager = findViewById(R.id.btnFoodManager);
        btnBack = findViewById(R.id.btnBack);
        btnCheckOrder = findViewById(R.id.btnCheckOrder);
        btnEmployeeManager = findViewById(R.id.btnEmployeeManager);
        btnLogout = findViewById(R.id.btnLogout);
        tvRestaurantTime = findViewById(R.id.tvRestaurantTime);
        ratingBar = findViewById(R.id.ratingBar);

    }

    private void updateUI() {
        if (restaurant != null) {
            if (restaurant.getAvatar() != null) {
                LoadImageUtil.loadImage(ivRestaurantAvatar, restaurant.getAvatar());
            }

            tvRestaurantName.setText(restaurant.getName());
            double ratting = restaurant.getRating();
            for (int i = 1; i<=ratting; i++ ) {
                ImageView rattingStar = new ImageView(RestaurantManagerActivity.this);
                rattingStar.setImageResource(R.drawable.baseline_star_24);
                restaurantRatingContainer.addView(rattingStar);
            }
        }




    }
}