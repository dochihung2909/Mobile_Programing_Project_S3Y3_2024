package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;

public class AdminReviewManagement extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private LinearLayout linearLayoutAdminReviewRestaurant,
            linearLayoutAdminReviewFood;
    private ImageView btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review_management);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        linearLayoutAdminReviewRestaurant = findViewById(R.id.linearLayoutAdminReviewRestaurant);
        linearLayoutAdminReviewFood = findViewById(R.id.linearLayoutAdminReviewFood);

        btnBackToMain = findViewById(R.id.btnBackToMain);
    }

    private void setOnClickListener() {
        linearLayoutAdminReviewRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(AdminReviewManagement.this, AdminReviewResManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminReviewFood.setOnClickListener(v -> {
            Intent intent = new Intent(AdminReviewManagement.this, AdminReviewFoodManagement.class);
            startActivity(intent);
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }
}