package com.example.food_order_final.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminReviewFoodAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.ReviewFood;

import java.util.List;

public class AdminReviewFoodManagement extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ImageButton btnBackToMain;
    TextView tvAdminReviewFoodTitle;
    Button btnAdminAddReviewFood;
    ListView lvAdminReviewFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review_food_management);
        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadAllReviews();
        setOnClickListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllReviews();
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);

        tvAdminReviewFoodTitle = findViewById(R.id.tvAdminReviewFoodTitle);

        btnAdminAddReviewFood = findViewById(R.id.btnAdminAddReviewFood);

        lvAdminReviewFood = findViewById(R.id.lvAdminReviewFood);
    }

    private void loadAllReviews() {
        List<ReviewFood> reviewFoodList = dbHelper.reviewFoodDao.getAllReviews();
        AdminReviewFoodAdapter adapter = new AdminReviewFoodAdapter(this, reviewFoodList);
        lvAdminReviewFood.setAdapter(adapter);
    }

    private void setOnClickListener() {
        btnBackToMain.setOnClickListener(v -> finish());

        btnAdminAddReviewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminReviewFoodManagement.this, AdminReviewFoodDetail.class);
                startActivity(intent);
            }
        });

        lvAdminReviewFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewFood reviewFood = (ReviewFood) parent.getItemAtPosition(position);
                Intent intent = new Intent(AdminReviewFoodManagement.this, AdminReviewFoodDetail.class);
                intent.putExtra("review_Food_id", reviewFood.getId());
                startActivity(intent);
            }
        });
    }
}