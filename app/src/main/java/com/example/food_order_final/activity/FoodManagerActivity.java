package com.example.food_order_final.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.food_order_final.util.PriceUtil;

import org.w3c.dom.Text;

import java.util.List;

public class FoodManagerActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnAddFoodToRestaurant;
    private LinearLayout foodsContainer;
    private TextView tvEmptyFoods;

    private int restaurantId;
    private DatabaseHelper dbHelper = new DatabaseHelper(FoodManagerActivity.this);
    private int REQUEST_EDIT_FOOD = 102;


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
                intent.putExtra("restaurantId", restaurantId);
                startActivityForResult(intent, REQUEST_EDIT_FOOD);
            }
        });

        loadUI();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_FOOD) {
            if (resultCode == RESULT_OK) {
                loadUI();
            }
        }
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnAddFoodToRestaurant = findViewById(R.id.btnAddFoodToRestaurant);
        foodsContainer = findViewById(R.id.foodsContainer);
        tvEmptyFoods = findViewById(R.id.tvEmptyFoods);
    }

    private void loadUI() {
        List<Food> foods = dbHelper.foodDao.getFoodsByRestaurantId(restaurantId);
        Toast.makeText(this, "" + foods.size(), Toast.LENGTH_SHORT).show();
        if (foods.size() > 0) {
            foodsContainer.removeAllViews();
            for (Food food: foods) {
                FoodCardView foodCardView = new FoodCardView(FoodManagerActivity.this);
                //set food card info
                foodCardView.setTvFoodName(food.getName());
                foodCardView.setTvFoodDescription(food.getDescription());
                foodCardView.setIvFoodAvatar(food.getAvatar());
                if (food.getDiscount() > 0) {
                    TextView defaulPrice = foodCardView.findViewById(R.id.tvFoodDefaultPrice);
                    defaulPrice.setVisibility(View.VISIBLE);
                    defaulPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    foodCardView.setTvFoodDefaultPrice(PriceUtil.formatNumber(food.getPrice()));
                }
                foodCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(food.getPrice() - food.getDiscount()));

                foodCardView.findViewById(R.id.btnAddToCart).setVisibility(View.GONE);
                foodCardView.findViewById(R.id.foodOwnerLayout).setVisibility(View.VISIBLE);
                TextView btnEditFoodInfo = foodCardView.findViewById(R.id.btnEditFoodInfo);
                TextView btnDeleteFood = foodCardView.findViewById(R.id.btnDeleteFood);

                foodsContainer.addView(foodCardView);

                btnEditFoodInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodManagerActivity.this, EditFoodActivity.class);
                        intent.putExtra("foodId", food.getId());
                        startActivityForResult(intent, REQUEST_EDIT_FOOD);
                    }
                });

                btnDeleteFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodManagerActivity.this);
                        alertDialog.setTitle("Xác nhận xoá món ăn");
                        alertDialog.setMessage("Bạn có muốn xoá món ăn này");
                        alertDialog.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dbHelper.foodDao.deleteFood(food.getId());
                                // Soft Delete
                            }
                        }).setNegativeButton("Huỷ", null);

                    }
                });
            }
        } else {
            tvEmptyFoods.setVisibility(View.GONE);
        }
    }
}