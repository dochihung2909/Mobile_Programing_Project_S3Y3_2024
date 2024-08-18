package com.example.food_order_final.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;
import com.google.android.material.textfield.TextInputLayout;

public class AdminFoodCateDetail extends AppCompatActivity {
    EditText edtEditFoodCateId, edtEditFoodCateName;
    TextInputLayout inputLayoutEditFoodCateId;
    ImageButton btnBackToMain;
    ImageView btnEditFoodCateSave;
    Button btnEditFoodCateDelete;
    private DatabaseHelper dbHelper;
    private FoodCategory selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_food_cate_detail);

        dbHelper = new DatabaseHelper(AdminFoodCateDetail.this);
        initWidgets();
        checkForEditCategory();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkForEditCategory() {
        Intent previousIntent = getIntent();

        int categoryId = previousIntent.getIntExtra("food_category_id", -1);
        selectedCategory = dbHelper.foodCateDao.getFoodCategoryById(categoryId);

        if (selectedCategory != null) {
            inputLayoutEditFoodCateId.setVisibility(View.VISIBLE);
            edtEditFoodCateId.setText(String.valueOf(selectedCategory.getId()));
            edtEditFoodCateName.setText(selectedCategory.getName());
            edtEditFoodCateId.setEnabled(false);
        } else {
            btnEditFoodCateDelete.setVisibility(View.GONE);
            inputLayoutEditFoodCateId.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        btnEditFoodCateSave.setOnClickListener(v -> {
            String categoryName = edtEditFoodCateName.getText().toString().trim();
            if (selectedCategory == null) {
                if (!dbHelper.foodCateDao.isCategoryNameExists(categoryName)) {
                    FoodCategory newCategory = new FoodCategory(categoryName);
                    dbHelper.foodCateDao.insertFoodCategory(newCategory);
                    Toast.makeText(AdminFoodCateDetail.this, "Tạo loại mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminFoodCateDetail.this, "Tên loại đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedCategory.setName(categoryName);
                dbHelper.foodCateDao.updateFoodCategory(selectedCategory);
                Toast.makeText(AdminFoodCateDetail.this, "Cập nhật loại thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditFoodCateDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa loại này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.foodCateDao.deleteFoodCategory(selectedCategory.getId());
                    Toast.makeText(AdminFoodCateDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void initWidgets() {
        edtEditFoodCateId = findViewById(R.id.edtEditFoodCateId);
        edtEditFoodCateName = findViewById(R.id.edtEditFoodCateName);

        inputLayoutEditFoodCateId = findViewById(R.id.inputLayoutEditFoodCateId);

        btnBackToMain = findViewById(R.id.btnBackToMain);

        btnEditFoodCateDelete = findViewById(R.id.btnEditFoodCateDelete);
        btnEditFoodCateSave = findViewById(R.id.btnEditFoodCateSave);
    }
}
