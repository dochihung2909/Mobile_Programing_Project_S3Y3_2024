package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.ReviewRestaurant;
import com.example.food_order_final.models.User;

public class RatingActivity extends AppCompatActivity {
    ImageView ivRestaurantAvatar, ivUploadedFood, ivUploadedRestaurant;
    TextView tvRestaurantName, tvSubtitleFood, tvSubtitleRestaurant;
    LinearLayout ratingFoodLayout, ratingRestaurantLayout;
    RatingBar rbFood, rbRestaurant;
    EditText etCommentFood, etCommentRestaurant;
    Button btnUploadImageFood, btnUploadImageRestaurant, btnSubmit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating);
        initWidgets();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(RatingActivity.this);

                // Test
//                int foodId = 1;
//                int resId = 1;
//                User user = dbHelper.userDao.getUserByUsername("ngynhg");
//                Food food = dbHelper.foodDao.getFoodById(foodId);
//                Restaurant res = dbHelper.resDao.getRestaurantById(resId);
//
//                String commentFood = String.valueOf(etCommentFood.getText());
//                String commentRestaurant = String.valueOf(etCommentRestaurant.getText());
//                double ratingFood = rbFood.getRating();
//                double ratingRestaurant = rbRestaurant.getRating();
//                ReviewFood reviewFood = new ReviewFood(commentFood, ratingFood, user, food);
//                ReviewRestaurant reviewRestaurant = new ReviewRestaurant(commentRestaurant, ratingFood, user, res);
//                dbHelper.reviewFoodDao.insertReview(reviewFood);
//                dbHelper.foodDao.updateFoodRatingByFoodId(food.getId());
//
//                showThanksDialog();
            }
        });
    }

    private void initWidgets() {
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
        ivUploadedFood = findViewById(R.id.ivUploadedFood);
        ivUploadedRestaurant = findViewById(R.id.ivUploadedRestaurant);

        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvSubtitleFood = findViewById(R.id.tvSubtitleFood);
        tvSubtitleRestaurant = findViewById(R.id.tvSubtitleRestaurant);

        ratingFoodLayout = findViewById(R.id.ratingFoodLayout);
        ratingRestaurantLayout = findViewById(R.id.ratingRestaurantLayout);

        rbFood = findViewById(R.id.rbFood);
        rbRestaurant = findViewById(R.id.rbRestaurant);

        etCommentFood = findViewById(R.id.etCommentFood);
        etCommentRestaurant = findViewById(R.id.etCommentRestaurant);

        btnUploadImageFood = findViewById(R.id.btnUploadImageFood);
        btnUploadImageRestaurant = findViewById(R.id.btnUploadImageRestaurant);
        btnSubmit = findViewById(R.id.btnSubmit);
    }


    private void showThanksDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cảm ơn bạn !")
                .setMessage("Đánh giá của bạn đã thành công.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

}