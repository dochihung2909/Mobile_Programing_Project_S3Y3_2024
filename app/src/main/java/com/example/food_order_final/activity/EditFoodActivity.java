package com.example.food_order_final.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.util.PriceUtil;

public class EditFoodActivity extends AppCompatActivity {

    private int foodId;
    private ImageView ivFoodAvatar;
    private ImageButton btnBack;
    private Button btnEditFoodImage, btnEditFoodSubmit;
    private EditText etFoodName, etFoodPrice, etFoodDiscount;
    private Spinner snFoodCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        this.foodId = getIntent().getIntExtra("foodId", -1);
        if (foodId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(EditFoodActivity.this);
            FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
            Food food = foodDao.getFoodById(foodId);
            etFoodName.setText(food.getName());
            etFoodPrice.setText(PriceUtil.formatNumber(food.getPrice()));
            etFoodDiscount.setText(PriceUtil.formatNumber(food.getDiscount()));
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        ivFoodAvatar = findViewById(R.id.ivFoodAvatar);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodDiscount = findViewById(R.id.etFoodDiscount);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        btnEditFoodImage = findViewById(R.id.btnEditFoodImage);
        btnEditFoodSubmit = findViewById(R.id.btnEditFoodSubmit);
        btnBack = findViewById(R.id.btnBack);
    }

}