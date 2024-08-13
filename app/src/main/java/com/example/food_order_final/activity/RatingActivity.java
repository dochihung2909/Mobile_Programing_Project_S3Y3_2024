package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.User;

public class RatingActivity extends AppCompatActivity {
    RatingBar ratingBar;
    EditText etComment;
    Button btnSubmit;
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
                int foodId = 1;
                User user = dbHelper.userDao.getUserByUsername("ngynhg");
                Food food = dbHelper.foodDao.getFoodById(foodId);

                String comment = String.valueOf(etComment.getText());
                double rating = ratingBar.getRating();
                ReviewFood reviewFood = new ReviewFood(comment, rating, user, food);
                dbHelper.reviewFoodDao.insertReview(reviewFood);
                dbHelper.foodDao.updateFoodRatingByFoodId(food.getId());

                showThanksDialog();
            }
        });
    }

    private void initWidgets() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        etComment = (EditText) findViewById(R.id.etComment);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
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