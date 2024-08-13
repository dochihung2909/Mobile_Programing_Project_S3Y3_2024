package com.example.food_order_final.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.food_order_final.hideSoftKeyboard;
import com.example.food_order_final.models.Food;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText etSearch;

    private LinearLayout foodsContainer;
    private TextView tvNoResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        DatabaseHelper dbHelper = new DatabaseHelper(SearchActivity.this);
        tvNoResult.setVisibility(View.GONE);

        etSearch.requestFocus();
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    RestaurantDao restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
                    FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper),restaurantDao );
                    List<Food> foods = foodDao.getFoodsByName(String.valueOf(etSearch.getText()));
                    foodsContainer.removeAllViews();
                    if (foods.size() > 0) {
                        for (Food food: foods) {
                            FoodCardView foodCardView = new FoodCardView(SearchActivity.this);

                            foodCardView.setTvFoodName(food.getName());
                            foodCardView.setFood(food);
                            foodCardView.setTvFoodDiscountPrice(food.getPrice());
                            foodCardView.findViewById(R.id.btnAddToCart).setVisibility(View.GONE);

                            foodCardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchActivity.this, RestaurantActivity.class);
                                    int restaurantId = restaurantDao.getRestaurantByFoodId(food.getId()).getId();
                                    intent.putExtra("restaurant", restaurantId);
                                    startActivity(intent);
                                }
                            });

                            foodsContainer.addView(foodCardView);
                        }
                        new hideSoftKeyboard(SearchActivity.this);
                        etSearch.clearFocus();
                    } else {
                        new hideSoftKeyboard(SearchActivity.this);
                        etSearch.clearFocus();
                        foodsContainer.addView(tvNoResult);
                        tvNoResult.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void init() {
        etSearch = findViewById(R.id.etSearch);
        foodsContainer = findViewById(R.id.foodsContainer);
        tvNoResult = findViewById(R.id.tvNoResult);
    }


}