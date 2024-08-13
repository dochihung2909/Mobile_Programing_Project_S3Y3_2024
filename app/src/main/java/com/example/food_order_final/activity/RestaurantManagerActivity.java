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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;

public class RestaurantManagerActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView ivRestaurantAvatar;
    private TextView tvRestaurantName;
    private LinearLayout restaurantRatingContainer;
    private Button btnRestaurantStatistical, btnEditRestaurantInfo, btnFoodManager;

    private int ownerId;

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
        DatabaseHelper dbHelper = new DatabaseHelper(RestaurantManagerActivity.this);
        RestaurantDao restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
        if (ownerId != -1 ) {
            Restaurant restaurant = restaurantDao.getRestaurantByUserId(ownerId);
            tvRestaurantName.setText(restaurant.getName());
//           Set avatar for Restaurant ivRestaurantAvatar.set
            double ratting = restaurant.getRating();
            for (int i = 1; i<=ratting; i++ ) {
                ImageView rattingStar = new ImageView(RestaurantManagerActivity.this);
                rattingStar.setImageResource(R.drawable.baseline_star_24);
                restaurantRatingContainer.addView(rattingStar);
            }

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

                }
            });
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

    }
}