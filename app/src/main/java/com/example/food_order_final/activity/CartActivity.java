package com.example.food_order_final.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private LinearLayout foodsContainer;

    private TextView tvFoodDefaultPrice, tvFoodDiscountPrice;
    private Button btnOrderSubmit;
    private ImageButton btnBackToMain;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        int cartId = getIntent().getIntExtra("cartId", 0);
        dbHelper = new DatabaseHelper(CartActivity.this);

        if (cartId != 0) {
            FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
            List<Food> foods = foodDao.getAllFoodsInCart(cartId);

            for (Food food: foods) {
                FoodCardView foodCardView = new FoodCardView(CartActivity.this);
                foodCardView.setTvFoodName(food.getName());
                foodsContainer.addView(foodCardView);
            }

        }


    }

    private void init() {
        foodsContainer = findViewById(R.id.foodsContainer);
        tvFoodDefaultPrice = findViewById(R.id.tvFoodDefaultPrice);
        tvFoodDiscountPrice = findViewById(R.id.tvFoodDiscountPrice);
        btnOrderSubmit = findViewById(R.id.btnOrderSubmit);
        btnBackToMain = findViewById(R.id.btnBackToMain);
    }
}