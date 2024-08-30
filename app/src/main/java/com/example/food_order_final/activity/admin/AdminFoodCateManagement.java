package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminFoodCateAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;

import java.util.List;

public class AdminFoodCateManagement extends AppCompatActivity {
    ImageButton btnBackToMain;
    Button btnAdminAddFoodCate;
    ListView lvAdminFoodCate;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_food_cate_management);

        initWidgets();
        loadCategories();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setOnClickListener() {
        btnAdminAddFoodCate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminFoodCateManagement.this, AdminFoodCateDetail.class);
            startActivity(intent);
        });

        lvAdminFoodCate.setOnItemClickListener((parent, view, position, id) -> {
            FoodCategory selectedCategory = (FoodCategory) parent.getItemAtPosition(position);
            Intent intent = new Intent(AdminFoodCateManagement.this, AdminFoodCateDetail.class);
            intent.putExtra("food_category_id", selectedCategory.getId());
            startActivity(intent);
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    public void loadCategories() {
        dbHelper = new DatabaseHelper(this);
        List<FoodCategory> categories = dbHelper.foodCateDao.getAllFoodCategories();
        AdminFoodCateAdapter adapter = new AdminFoodCateAdapter(this, categories);
        lvAdminFoodCate.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnAdminAddFoodCate = findViewById(R.id.btnAdminAddFoodCate);
        lvAdminFoodCate = findViewById(R.id.lvAdminFoodCate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }
}
