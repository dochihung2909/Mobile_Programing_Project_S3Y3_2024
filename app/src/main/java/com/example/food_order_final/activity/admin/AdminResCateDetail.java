package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.RestaurantCategory;
import com.google.android.material.textfield.TextInputLayout;

public class AdminResCateDetail extends AppCompatActivity {
    EditText edtEditCateId, edtEditCateName;
    TextInputLayout inputLayoutEditCateId;
    ImageButton btnBackToMain;
    ImageView btnEditCateSave;
    Button btnEditCateDelete;
    private DatabaseHelper dbHelper;
    private RestaurantCategory selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_res_cate_detail);

        dbHelper = new DatabaseHelper(AdminResCateDetail.this);
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

        int categoryId = previousIntent.getIntExtra("category_id", -1);
        selectedCategory = dbHelper.resCateDao.getRestaurantCategoryById(categoryId);

        if (selectedCategory != null) {
            inputLayoutEditCateId.setVisibility(View.VISIBLE);
            edtEditCateId.setText(String.valueOf(selectedCategory.getId()));
            edtEditCateName.setText(selectedCategory.getName());
            edtEditCateId.setEnabled(false);
        } else {
            btnEditCateDelete.setVisibility(View.GONE);
            inputLayoutEditCateId.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        btnEditCateSave.setOnClickListener(v -> {
            String categoryName = edtEditCateName.getText().toString().trim();
            if (selectedCategory == null) {
                if (!dbHelper.resCateDao.isCategoryNameExists(categoryName)) {
                    RestaurantCategory newCategory = new RestaurantCategory(categoryName);
                    dbHelper.resCateDao.insertRestaurantCategory(newCategory);
                    Toast.makeText(AdminResCateDetail.this, "Tạo loại mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminResCateDetail.this, "Tên loại đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedCategory.setName(categoryName);
                dbHelper.resCateDao.updateRestaurantCategory(selectedCategory);
                Toast.makeText(AdminResCateDetail.this, "Cập nhật loại thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditCateDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa loại này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.resCateDao.deleteRestaurantCategory(selectedCategory.getId());
                    Toast.makeText(AdminResCateDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void initWidgets() {
        edtEditCateId = findViewById(R.id.edtEditResCateId);
        edtEditCateName = findViewById(R.id.edtEditResCateName);

        inputLayoutEditCateId = findViewById(R.id.inputLayoutEditResCateId);

        btnBackToMain = findViewById(R.id.btnBackToMain);

        btnEditCateDelete = findViewById(R.id.btnEditResCateDelete);
        btnEditCateSave = findViewById(R.id.btnEditResCateSave);
    }
}
