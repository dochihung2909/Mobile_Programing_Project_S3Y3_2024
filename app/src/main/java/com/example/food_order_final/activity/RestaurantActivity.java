package com.example.food_order_final.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.CartCardView;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.custom_activity.RestaurantCardView;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;

import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    LinearLayout foodsContainer, restaurantRatingContainer;

    TextView tvRestaurantName, tvRestaurantTime, tvRestaurantAddress;
    ImageView ivRestaurantAvatar;

    ImageButton btnBackToMain;

    RelativeLayout mainLayout;

    Restaurant restaurant;

    DatabaseHelper dbhelper;



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

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int restaurantId = getIntent().getIntExtra("restaurant", 0);

        Toast.makeText(this, "" + restaurantId, Toast.LENGTH_SHORT).show();
//
        if (restaurantId != 0) {
            dbhelper = new DatabaseHelper(RestaurantActivity.this);
            RestaurantDao restaurantDao = new RestaurantDao(
                    dbhelper,
                    new RestaurantCategoryDao(dbhelper));
            this.restaurant = restaurantDao.getRestaurantById((restaurantId));
            Log.d("Restaurant", this.restaurant.getName());
            tvRestaurantName.setText(restaurant.getName());
            tvRestaurantAddress.setText(restaurant.getAddress());

            for (int i = 1; i <= restaurant.getId();i++) {
                ImageView imageStar = new ImageView(RestaurantActivity.this);
                imageStar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageStar.setImageResource(R.drawable.baseline_star_24);
                restaurantRatingContainer.addView(imageStar);
            }
//
//
//        // Get all foods of restaurant and render
            FoodDao foodDao = new FoodDao(dbhelper, new FoodCategoryDao(dbhelper), restaurantDao);
            List<Food> foods = foodDao.getFoodsByRestaurantId(restaurantId);
            for (Food food: foods) {
                FoodCardView foodCardView = new FoodCardView(RestaurantActivity.this);

                foodCardView.setFood(food);
                foodCardView.setTvFoodName(food.getName());
                foodCardView.setTvFoodDiscountPrice(food.getPrice());
                foodsContainer.addView(foodCardView);
            }
        }
    }

    private void init() {
        foodsContainer = findViewById(R.id.foodsContainer);
        restaurantRatingContainer = findViewById(R.id.restaurantRatingContainer);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantTime = findViewById(R.id.tvRestaurantTime);
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
        tvRestaurantAddress = findViewById(R.id.tvRestaurantAddress);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        mainLayout = findViewById(R.id.main);
    }

//    @Override
//    public void onActionCompleted(Cart cart) {
//        CartCardView cartCardView = new CartCardView(RestaurantActivity.this);
//        cartCardView.setTvRestaurantName(this.restaurant.getName());
//        dbhelper = new DatabaseHelper(RestaurantActivity.this);
//        CartDao cartDao = new CartDao(dbhelper);
//        cartCardView.setTvTotalDishes(cartDao.getTotalDishes(cart.getId()));
//        cartCardView.setTvTotalPrice(cartDao.getTotalAmount(cart.getId()));
//        mainLayout.addView(cartCardView);
//    }
}