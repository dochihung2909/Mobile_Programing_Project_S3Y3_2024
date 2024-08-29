package com.example.food_order_final.activity;

import android.os.Bundle;
import android.view.View;
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
import com.example.food_order_final.custom_activity.RattingCardView;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.PriceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView ivFoodAvatar;
    private TextView tvFoodName, tvFoodDescription, tvFoodSold, tvFoodLiked, tvFoodDefaultPrice, tvFoodDiscountPrice, tvQuantity, btnMinus, btnPlus;
    private int foodId;
    private LinearLayout commentLayout;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);
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

        foodId = getIntent().getIntExtra("foodId", -1);
        if (foodId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(FoodDetailActivity.this);
            FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
            Food food = foodDao.getFoodById(foodId);

            tvFoodName.setText(food.getName());
            tvFoodDiscountPrice.setText(PriceUtil.formatNumber(food.getPrice()) + "đ");

            List<ReviewFood> reviewFoods = dbHelper.reviewFoodDao.getReviewsByFoodId(foodId);
            for (ReviewFood reviewFood: reviewFoods) {
                RattingCardView rattingCardView = new RattingCardView(FoodDetailActivity.this);
                User user = reviewFood.getUser();
                rattingCardView.setIvUserAvatar(user.getAvatar());
                rattingCardView.setTvUsername(user.getFullName());
                rattingCardView.setTvComment(reviewFood.getComment());
                double rating = reviewFood.getRating();
                rattingCardView.getRatingBar().setRating((float) (rating));

                String pattern = "MM/dd/yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(reviewFood.getUpdatedDate());
                rattingCardView.setTvTime(todayAsString);
                commentLayout.addView(rattingCardView);
            }
        }

    }

    private void init() {
        ivFoodAvatar = findViewById(R.id.ivFoodAvatar);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodDescription = findViewById(R.id.tvFoodDescription);
        tvFoodSold = findViewById(R.id.tvFoodSold);
        tvFoodLiked = findViewById(R.id.tvFoodLiked);
        tvFoodDiscountPrice = findViewById(R.id.tvFoodDiscountPrice);
        tvFoodDefaultPrice = findViewById(R.id.tvFoodDefaultPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        commentLayout = findViewById(R.id.commentLayout);
        btnBack = findViewById(R.id.btnBack);
    }
}