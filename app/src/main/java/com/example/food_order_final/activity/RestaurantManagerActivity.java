package com.example.food_order_final.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

public class RestaurantManagerActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView ivRestaurantAvatar;
    private TextView tvRestaurantName;
    private LinearLayout restaurantRatingContainer;
    private Button btnRestaurantStatistical, btnEditRestaurantInfo, btnFoodManager, btnCheckOrder;
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
                restaurant = restaurantDao.getRestaurantByUserId(employeeId);
            } else {
                restaurant = restaurantDao.getRestaurantByUserId(ownerId);

            }
            updateUI();

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
    }

    private void updateUI() {
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