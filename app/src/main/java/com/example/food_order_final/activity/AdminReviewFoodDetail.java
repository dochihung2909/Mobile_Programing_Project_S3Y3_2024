package com.example.food_order_final.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminReviewFoodDetail extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ImageButton btnBackToMain, btnEditReviewFoodSave;
    TextView tvEditReviewFoodTitle;
    TextInputLayout inputLayoutEditReviewFoodId;
    Spinner spnEditReviewFoodUser, spnEditReviewFoodFood;
    EditText edtEditReviewFoodComment, edtEditReviewFoodId;
    ReviewFood selectedReview;
    Button btnEditReviewFoodDelete;
    RatingBar rbEditReviewFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review_food_detail);
        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadAllUsers();
        loadAllFoods();
        checkForEditReviewFood();
        setOnClickListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditReviewFoodSave = findViewById(R.id.btnEditReviewFoodSave);

        tvEditReviewFoodTitle = findViewById(R.id.tvEditReviewFoodTitle);

        inputLayoutEditReviewFoodId = findViewById(R.id.inputLayoutEditReviewFoodId);

        spnEditReviewFoodUser = findViewById(R.id.spnEditReviewFoodUser);
        spnEditReviewFoodFood = findViewById(R.id.spnEditReviewFoodFood);

        edtEditReviewFoodComment = findViewById(R.id.edtEditReviewFoodComment);
        edtEditReviewFoodId = findViewById(R.id.edtEditReviewFoodId);

        btnEditReviewFoodDelete = findViewById(R.id.btnEditReviewFoodDelete);

        rbEditReviewFood = findViewById(R.id.rbEditReviewFood);
    }

    private void loadAllUsers() {
        List<User> users = dbHelper.userDao.getAllUsers();
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditReviewFoodUser.setAdapter(adapter);
    }

    private void loadAllFoods() {
        List<Food> Foods = dbHelper.foodDao.getAllFoods();
        List<String> FoodNames = new ArrayList<>();
        for (Food Food : Foods) {
            FoodNames.add(Food.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FoodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditReviewFoodFood.setAdapter(adapter);
    }

    private void checkForEditReviewFood() {
        Intent previousIntent = getIntent();
        int reviewFoodId = previousIntent.getIntExtra("review_Food_id", -1);
        selectedReview = dbHelper.reviewFoodDao.getReviewById(reviewFoodId);

        if (selectedReview != null) {
            inputLayoutEditReviewFoodId.setVisibility(View.VISIBLE);
            edtEditReviewFoodId.setText(String.valueOf(selectedReview.getId()));
            edtEditReviewFoodId.setEnabled(false);
            rbEditReviewFood.setRating((float) selectedReview.getRating());
            edtEditReviewFoodComment.setText(selectedReview.getComment());
            btnEditReviewFoodDelete.setVisibility(View.VISIBLE);

            if (selectedReview.getUser() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditReviewFoodUser.getAdapter();
                int position = adapter.getPosition(selectedReview.getUser().getUsername());
                spnEditReviewFoodUser.setSelection(position);
            }

            if (selectedReview.getFood() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditReviewFoodFood.getAdapter();
                int position = adapter.getPosition(selectedReview.getFood().getName());
                spnEditReviewFoodFood.setSelection(position);
            }
        } else {
            inputLayoutEditReviewFoodId.setVisibility(View.GONE);
            btnEditReviewFoodDelete.setVisibility(View.GONE);
        }
    }


    private void setOnClickListener() {
        btnEditReviewFoodSave.setOnClickListener(v -> {
            String userName = spnEditReviewFoodUser.getSelectedItem().toString();
            User user = dbHelper.userDao.getUserByUsername(userName);
            String FoodName = spnEditReviewFoodFood.getSelectedItem().toString();
            Food Food = dbHelper.foodDao.getFoodByName(FoodName);
            double rating = rbEditReviewFood.getRating();
            String reviewFoodComment = edtEditReviewFoodComment.getText().toString().trim();

            if (selectedReview == null) {
                ReviewFood newReviewFood = new ReviewFood(reviewFoodComment, rating, user, Food);
                dbHelper.reviewFoodDao.insertReview(newReviewFood);
                Toast.makeText(AdminReviewFoodDetail.this, "Tạo Đánh giá mới thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                selectedReview.setComment(reviewFoodComment);
                selectedReview.setUser(user);
                selectedReview.setFood(Food);
                dbHelper.reviewFoodDao.updateReview(selectedReview);
                Toast.makeText(AdminReviewFoodDetail.this, "Cập nhật Đánh giá thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditReviewFoodDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa Đánh giá này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(AdminReviewFoodDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dbHelper.reviewFoodDao.deleteReview(selectedReview.getId());
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}