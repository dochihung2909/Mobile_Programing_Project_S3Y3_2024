package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.food_order_final.adapter.AdminReviewResAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.ReviewRestaurant;

import java.util.List;

public class AdminReviewResManagement extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ImageButton btnBackToMain;
    TextView tvAdminReviewResTitle;
    Button btnAdminAddReviewRes;
    ListView lvAdminReviewRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review_res_management);
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

        tvAdminReviewResTitle = findViewById(R.id.tvAdminReviewResTitle);

        btnAdminAddReviewRes = findViewById(R.id.btnAdminAddReviewRes);

        lvAdminReviewRes = findViewById(R.id.lvAdminReviewRes);
    }

    private void loadAllReviews() {
        List<ReviewRestaurant> reviewRestaurantList = dbHelper.reviewRestaurantDao.getAllReviews();
        AdminReviewResAdapter adapter = new AdminReviewResAdapter(this, reviewRestaurantList);
        lvAdminReviewRes.setAdapter(adapter);
    }

    private void setOnClickListener() {
        btnBackToMain.setOnClickListener(v -> finish());

        btnAdminAddReviewRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminReviewResManagement.this, AdminReviewResDetail.class);
                startActivity(intent);
            }
        });

        lvAdminReviewRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewRestaurant reviewRestaurant = (ReviewRestaurant) parent.getItemAtPosition(position);
                Intent intent = new Intent(AdminReviewResManagement.this, AdminReviewResDetail.class);
                intent.putExtra("review_res_id", reviewRestaurant.getId());
                startActivity(intent);
            }
        });
    }
}