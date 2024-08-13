package com.example.food_order_final.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;

import org.w3c.dom.Text;

import java.util.List;

public class FoodManagerActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnAddFoodToRestaurant;
    private LinearLayout foodsContainer;

    private int restaurantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        this.restaurantId = getIntent().getIntExtra("restaurantId", 0);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddFoodToRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodManagerActivity.this,EditFoodActivity.class);
                startActivity(intent);
            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(FoodManagerActivity.this);
        FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
        List<Food> foods = foodDao.getFoodsByRestaurantId(restaurantId);
        for (Food food: foods) {
            FoodCardView foodCardView = new FoodCardView(FoodManagerActivity.this);
            foodCardView.findViewById(R.id.btnAddToCart).setVisibility(View.GONE);
            foodCardView.findViewById(R.id.foodOwnerLayout).setVisibility(View.VISIBLE);
            TextView btnEditFoodInfo = foodCardView.findViewById(R.id.btnEditFoodInfo);
            TextView btnDeleteFood = foodCardView.findViewById(R.id.btnDeleteFood);

            btnEditFoodInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodManagerActivity.this, EditFoodActivity.class);
                }
            });
        }

    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnAddFoodToRestaurant = findViewById(R.id.btnAddFoodToRestaurant);
        foodsContainer = findViewById(R.id.foodsContainer);
    }
}