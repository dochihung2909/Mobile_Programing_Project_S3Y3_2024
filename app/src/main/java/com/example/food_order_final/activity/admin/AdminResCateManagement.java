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
import com.example.food_order_final.adapter.AdminResCateAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.RestaurantCategory;

import java.util.List;

public class AdminResCateManagement extends AppCompatActivity {
    ImageButton btnBackToMain;
    Button btnAdminAddResCate;
    ListView lvAdminResCate;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_res_cate_management);

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
        btnAdminAddResCate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminResCateManagement.this, AdminResCateDetail.class);
            startActivity(intent);
        });

        lvAdminResCate.setOnItemClickListener((parent, view, position, id) -> {
            RestaurantCategory selectedCategory = (RestaurantCategory) parent.getItemAtPosition(position);
            Intent intent = new Intent(AdminResCateManagement.this, AdminResCateDetail.class);
            intent.putExtra("category_id", selectedCategory.getId());
            startActivity(intent);
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void loadCategories() {
        dbHelper = new DatabaseHelper(this);
        List<RestaurantCategory> categories = dbHelper.resCateDao.getAllRestaurantCategories();
        AdminResCateAdapter adapter = new AdminResCateAdapter(this, categories);
        lvAdminResCate.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnAdminAddResCate = findViewById(R.id.btnAdminAddResCate);
        lvAdminResCate = findViewById(R.id.lvAdminResCate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }
}
