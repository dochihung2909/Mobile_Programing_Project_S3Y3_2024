package com.example.food_order_final.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.custom_activity.RestaurantCardView;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;

import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    LinearLayout foodsContainer, restaurantRatingContainer;

    TextView tvRestaurantName, tvRestaurantTime;
    ImageView ivRestaurantAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        String restaurantId = getIntent().getStringExtra("restaurant");
//
        DatabaseHelper dbhelper = new DatabaseHelper(RestaurantActivity.this);
        RestaurantDao restaurantDao = new RestaurantDao(
                dbhelper,
                new RestaurantCategoryDao(dbhelper));
//        Restaurant restaurant = restaurantDao.getRestaurantById(Integer.parseInt(restaurantId));
//        tvRestaurantName.setText(restaurant.getName());

        for (int i = 1;i<=4;i++) {
            ImageView imageStar = new ImageView(RestaurantActivity.this);
            imageStar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageStar.setImageResource(R.drawable.baseline_star_24);
            restaurantRatingContainer.addView(imageStar);
        }
//
//
//        // Get all foods of restaurant and render
        FoodDao foodDao = new FoodDao(dbhelper, new FoodCategoryDao(dbhelper), restaurantDao);
        List<Food> foods = foodDao.findFoodByName("Pizza");
        for (Food food: foods) {
            FoodCardView foodCardView = new FoodCardView(RestaurantActivity.this);
            foodCardView.setTvFoodName(food.getName());
            foodCardView.setTvFoodDiscountPrice(food.getPrice());
            foodsContainer.addView(foodCardView);
        }



    }

    private void init() {
        foodsContainer = findViewById(R.id.foodsContainer);
        restaurantRatingContainer = findViewById(R.id.restaurantRatingContainer);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantTime = findViewById(R.id.tvRestaurantTime);
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
    }
}