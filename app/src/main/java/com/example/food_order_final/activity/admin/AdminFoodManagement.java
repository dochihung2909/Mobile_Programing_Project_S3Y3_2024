package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminFoodAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodManagement extends AppCompatActivity {
    private ImageButton btnBackToMain;
    private Button btnAdminAddFood;
    private ListView lvAdminFood;
    private Spinner spnAdminFoodCate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_food_management);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadCategories();
        loadFoods();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setOnClickListener() {
        btnAdminAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(AdminFoodManagement.this, AdminFoodDetail.class);
            startActivity(intent);
        });

        lvAdminFood.setOnItemClickListener((parent, view, position, id) -> {
            Food selectedFood = (Food) parent.getItemAtPosition(position);
            Intent intent = new Intent(AdminFoodManagement.this, AdminFoodDetail.class);
            intent.putExtra("food_id", selectedFood.getId());
            startActivity(intent);
        });

        spnAdminFoodCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                if (selectedCategory.equals("All Categories")) {
                    loadFoods();
                } else {
                    loadFoodsByCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    public void loadFoods() {
        List<Food> foods = dbHelper.foodDao.getAllFoods();
        AdminFoodAdapter adapter = new AdminFoodAdapter(this, foods);
        lvAdminFood.setAdapter(adapter);
    }

    private void loadFoodsByCategory(String categoryName) {
        dbHelper = new DatabaseHelper(this);
        List<Food> foods = dbHelper.foodDao.getFoodsByCategory(categoryName);
        AdminFoodAdapter adapter = new AdminFoodAdapter(this, foods);
        lvAdminFood.setAdapter(adapter);
    }

    private void loadCategories() {
        List<FoodCategory> foodCategories = dbHelper.foodCateDao.getAllFoodCategories();
        List<String> foodCateNames = new ArrayList<>();
        foodCateNames.add("All Categories");
        for(FoodCategory foodCategory:foodCategories){
            foodCateNames.add(foodCategory.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodCateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAdminFoodCate.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnAdminAddFood = findViewById(R.id.btnAdminAddFood);
        lvAdminFood = findViewById(R.id.lvAdminFood);
        spnAdminFoodCate = findViewById(R.id.spnAdminFoodCate);

        if (btnBackToMain == null || btnAdminAddFood == null || lvAdminFood == null) {
            throw new NullPointerException("One or more views were not found. Check your layout XML.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoods();
    }
}
