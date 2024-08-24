package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodDetail extends AppCompatActivity {
    private EditText edtEditFoodId, edtEditFoodName, edtEditFoodPrice,
            edtEditFoodDiscount, edtEditFoodRating;
    private Spinner spnEditFoodCate, spnEditFoodRes;
    private TextInputLayout inputLayoutEditFoodId, inputLayoutEditFoodRating;
    private ImageButton btnBackToMain;
    private ImageView btnEditFoodSave;
    private Button btnEditFoodDelete;
    private DatabaseHelper dbHelper;
    private Food selectedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_food_detail);

        dbHelper = new DatabaseHelper(this);

        initWidgets();
        loadFoodCategories();
        loadRestaurants();
        checkForEditFood();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        edtEditFoodId = findViewById(R.id.edtEditFoodId);
        edtEditFoodName = findViewById(R.id.edtEditFoodName);
        edtEditFoodPrice = findViewById(R.id.edtEditFoodPrice);
        edtEditFoodDiscount = findViewById(R.id.edtEditFoodDiscount);
        edtEditFoodRating = findViewById(R.id.edtEditFoodRating);
        spnEditFoodCate = findViewById(R.id.spnEditFoodCate);
        spnEditFoodRes = findViewById(R.id.spnEditFoodRes);

        inputLayoutEditFoodId = findViewById(R.id.inputLayoutEditFoodId);
        inputLayoutEditFoodRating = findViewById(R.id.inputLayoutEditFoodRating);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditFoodDelete = findViewById(R.id.btnEditFoodDelete);
        btnEditFoodSave = findViewById(R.id.btnEditFoodSave);

        if (edtEditFoodId == null || edtEditFoodName == null || edtEditFoodPrice == null ||
                edtEditFoodDiscount == null || edtEditFoodRating == null || spnEditFoodCate == null ||
                spnEditFoodRes == null || inputLayoutEditFoodId == null || btnBackToMain == null ||
                btnEditFoodDelete == null || btnEditFoodSave == null) {
            throw new NullPointerException("One or more views were not found. Check your layout XML.");
        }
    }

    private void loadFoodCategories() {
        List<FoodCategory> FoodCates = dbHelper.foodCateDao.getAllFoodCategories();
        List<String> FoodCateNames = new ArrayList<>();

        for (FoodCategory FoodCate : FoodCates) {
            FoodCateNames.add(FoodCate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FoodCateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditFoodCate.setAdapter(adapter);
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = dbHelper.resDao.getAllRestaurants();
        List<String> resNames = new ArrayList<>();
        for (Restaurant res : restaurants) {
            resNames.add(res.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditFoodRes.setAdapter(adapter);
    }

    private void checkForEditFood() {
        Intent previousIntent = getIntent();
        int FoodId = previousIntent.getIntExtra("food_id", -1);
        selectedFood = dbHelper.foodDao.getFoodById(FoodId);

        if (selectedFood != null) {
            inputLayoutEditFoodId.setVisibility(View.VISIBLE);
            inputLayoutEditFoodRating.setVisibility(View.VISIBLE);
            edtEditFoodId.setText(String.valueOf(selectedFood.getId()));
            edtEditFoodName.setText(selectedFood.getName());
            edtEditFoodPrice.setText(String.valueOf(selectedFood.getPrice()));
            edtEditFoodDiscount.setText(String.valueOf(selectedFood.getDiscount()));
            edtEditFoodRating.setText(String.valueOf(selectedFood.getRating()));
            edtEditFoodId.setEnabled(false);

            if (selectedFood.getCategory() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditFoodCate.getAdapter();
                int position = adapter.getPosition(selectedFood.getCategory().getName());
                spnEditFoodCate.setSelection(position);
            }
        } else {
            btnEditFoodDelete.setVisibility(View.GONE);
            inputLayoutEditFoodId.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        btnEditFoodSave.setOnClickListener(v -> {
            String FoodName = edtEditFoodName.getText().toString().trim();
            double FoodPrice = Double.parseDouble(edtEditFoodPrice.getText().toString().trim());
            double FoodDiscount = Double.parseDouble(edtEditFoodDiscount.getText().toString().trim());
            String FoodCateName = spnEditFoodCate.getSelectedItem().toString();
            FoodCategory FoodCategory = dbHelper.foodCateDao.getFoodCategoryByName(FoodCateName);
            String FoodResName = spnEditFoodRes.getSelectedItem().toString();
            Restaurant FoodRes = dbHelper.resDao.getRestaurantByName(FoodResName);

            if (selectedFood == null) {
                if (!dbHelper.foodDao.isFoodExists(FoodName, FoodRes)) {
                    Food newFood = new Food(FoodName, FoodPrice, "", FoodCategory, FoodRes);
                    newFood.setDiscount(FoodDiscount);
                    dbHelper.foodDao.insertFood(newFood);
                    Toast.makeText(AdminFoodDetail.this, "Tạo Món ăn mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminFoodDetail.this, "Tên Món ăn trong nhà hàng đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedFood.setName(FoodName);
                selectedFood.setPrice(FoodPrice);
                selectedFood.setDiscount(FoodDiscount);
                selectedFood.setCategory(FoodCategory);
                selectedFood.setRestaurant(FoodRes);
                dbHelper.foodDao.updateFood(selectedFood);
                Toast.makeText(AdminFoodDetail.this, "Cập nhật Món ăn thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditFoodDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa món ăn này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(AdminFoodDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dbHelper.foodDao.deleteFood(selectedFood.getId());
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
