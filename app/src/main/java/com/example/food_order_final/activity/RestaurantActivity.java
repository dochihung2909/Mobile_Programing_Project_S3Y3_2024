package com.example.food_order_final.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.example.food_order_final.util.PriceUtil;

import java.text.DecimalFormat;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    LinearLayout foodsContainer, restaurantRatingContainer;

    TextView tvRestaurantName, tvRestaurantTime, tvRestaurantAddress;
    ImageView ivRestaurantAvatar;

    ImageButton btnBackToMain;

    RelativeLayout mainLayout;

    Restaurant restaurant;

    DatabaseHelper dbhelper;

    CartCardView cartCardView;

    int REQUEST_CODE_CART = 1;

    CartDao cartDao;

    User currentUser;
    
    int restaurantId;
    private RatingBar ratingBar;



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

        restaurantId = getIntent().getIntExtra("restaurant", 0);

//
        if (restaurantId != 0) {
            dbhelper = new DatabaseHelper(RestaurantActivity.this);
            RestaurantDao restaurantDao = new RestaurantDao(
                    dbhelper,
                    new RestaurantCategoryDao(dbhelper));
            this.restaurant = restaurantDao.getRestaurantById((restaurantId));


            LoadImageUtil.loadImage(ivRestaurantAvatar, restaurant.getAvatar());

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "Guest");
            UserDao userDao = new UserDao(dbhelper, new RoleDao(dbhelper));
            currentUser = userDao.getUserByUsername(username);
            cartDao = new CartDao(dbhelper);
            cartCardView.setTvRestaurantName(restaurant.getName());
            if (!cartDao.isUserHasCart(currentUser.getId(), restaurantId)) {
                cartCardView.setVisibility(View.GONE);
            } else {
                Cart cart = cartDao.getCartByUserId(currentUser.getId(), restaurantId);
                cartCardView.setVisibility(View.VISIBLE);
                int totalDishes = cartDao.getTotalDishes(cart.getId());
                cartCardView.setTvTotalDishes(totalDishes);
                double totalAmount = cartDao.getTotalAmountByCartId(cart.getId());
                cartCardView.setTvTotalPrice(PriceUtil.formatNumber(totalAmount) + "đ");

                cartCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantActivity.this, CartActivity.class);
                        intent.putExtra("cartId", cart.getId());
                        startActivity(intent);
                    }
                });
            }


            tvRestaurantName.setText(restaurant.getName());
            tvRestaurantAddress.setText(restaurant.getAddress());

            ratingBar.setRating((float) restaurant.getRating());
//
//
//        // Get all foods of restaurant and render
            FoodDao foodDao = new FoodDao(dbhelper, new FoodCategoryDao(dbhelper), restaurantDao);
            List<Food> foods = foodDao.getFoodsByRestaurantId(restaurantId);
            for (Food food: foods) {
                FoodCardView foodCardView = new FoodCardView(RestaurantActivity.this);
                foodCardView.setFood(food);
                foodCardView.setTvFoodName(food.getName());
                if (food.getDiscount() > 0) {
                    TextView defaulPrice = foodCardView.findViewById(R.id.tvFoodDefaultPrice);
                    defaulPrice.setVisibility(View.VISIBLE);
                    defaulPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    foodCardView.setTvFoodDefaultPrice(PriceUtil.formatNumber(food.getPrice()));
                }
                foodCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(food.getPrice() - food.getDiscount()));

                foodCardView.setIvFoodAvatar(food.getAvatar());
                int numberSold = dbhelper.foodDao.getNumberSold(food.getId());
                foodCardView.setTvFoodName(food.getName());
                foodCardView.setTvFoodSold(numberSold + " đã bán");
                foodCardView.setIvFoodAvatar(food.getAvatar());
                DecimalFormat df = new DecimalFormat("#.#");
                foodCardView.setTvFoodLiked( df.format(food.getRating()));

                TextView btnAddToCart = foodCardView.findViewById(R.id.btnAddToCart);

                foodCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantActivity.this, FoodDetailActivity.class);
                        intent.putExtra("foodId", food.getId());
                        startActivity(intent);
                    }
                });

                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!username.equals("Guest")) {
                            Cart cart = cartDao.addToCard(currentUser, food.getRestaurant(), food, 1);
                            cartCardView.setVisibility(View.VISIBLE);
                            int totalDishes = cartDao.getTotalDishes(cart.getId());
                            cartCardView.setTvTotalDishes(totalDishes);
                            double totalAmount = cartDao.getTotalAmountByCartId(cart.getId());
                            cartCardView.setTvTotalPrice(PriceUtil.formatNumber(totalAmount) + "đ");
                            cartCardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(RestaurantActivity.this, CartActivity.class);
                                    intent.putExtra("cartId", cart.getId());
                                    SharedPreferences pref = getSharedPreferences("UserCart", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.apply();
                                    editor.putInt("currentCartId", cart.getId());
                                    startActivityForResult(intent, REQUEST_CODE_CART);
                                }
                            });
                        }

                    }
                });
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
        cartCardView = findViewById(R.id.cartCardView);
        ratingBar = findViewById(R.id.ratingBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CART) {
            if (resultCode == RESULT_OK) {
                if (!cartDao.isUserHasCart(currentUser.getId(), restaurantId)) {
                    cartCardView.setVisibility(View.GONE);
                }
            }
        }
    }
}