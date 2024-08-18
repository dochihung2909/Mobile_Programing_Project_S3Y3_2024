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
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.ReviewRestaurant;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminReviewResDetail extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ImageButton btnBackToMain, btnEditReviewResSave;
    TextView tvEditReviewResTitle;
    TextInputLayout inputLayoutEditReviewResId;
    Spinner spnEditReviewResUser, spnEditReviewResRes;
    EditText edtEditReviewResComment, edtEditReviewResId;
    ReviewRestaurant selectedReview;
    Button btnEditReviewResDelete;
    RatingBar rbEditReviewRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review_res_detail);
        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadAllUsers();
        loadAllRestaurants();
        checkForEditReviewRes();
        setOnClickListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditReviewResSave = findViewById(R.id.btnEditReviewResSave);

        tvEditReviewResTitle = findViewById(R.id.tvEditReviewResTitle);

        inputLayoutEditReviewResId = findViewById(R.id.inputLayoutEditReviewResId);

        spnEditReviewResUser = findViewById(R.id.spnEditReviewResUser);
        spnEditReviewResRes = findViewById(R.id.spnEditReviewResRes);

        edtEditReviewResComment = findViewById(R.id.edtEditReviewResComment);
        edtEditReviewResId = findViewById(R.id.edtEditReviewResId);

        btnEditReviewResDelete = findViewById(R.id.btnEditReviewResDelete);

        rbEditReviewRes = findViewById(R.id.rbEditReviewRes);
    }

    private void loadAllUsers() {
        List<User> users = dbHelper.userDao.getAllUsers();
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditReviewResUser.setAdapter(adapter);
    }

    private void loadAllRestaurants() {
        List<Restaurant> restaurants = dbHelper.resDao.getAllRestaurants();
        List<String> resNames = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            resNames.add(restaurant.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditReviewResRes.setAdapter(adapter);
    }

    private void checkForEditReviewRes() {
        Intent previousIntent = getIntent();
        int reviewResId = previousIntent.getIntExtra("review_res_id", -1);
        selectedReview = dbHelper.reviewRestaurantDao.getReviewById(reviewResId);

        if (selectedReview != null) {
            inputLayoutEditReviewResId.setVisibility(View.VISIBLE);
            edtEditReviewResId.setText(String.valueOf(selectedReview.getId()));
            edtEditReviewResId.setEnabled(false);
            rbEditReviewRes.setRating((float) selectedReview.getRating());
            edtEditReviewResComment.setText(selectedReview.getComment());
            btnEditReviewResDelete.setVisibility(View.VISIBLE);

            if (selectedReview.getUser() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditReviewResUser.getAdapter();
                int position = adapter.getPosition(selectedReview.getUser().getUsername());
                spnEditReviewResUser.setSelection(position);
            }

            if (selectedReview.getRestaurant() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditReviewResRes.getAdapter();
                int position = adapter.getPosition(selectedReview.getRestaurant().getName());
                spnEditReviewResRes.setSelection(position);
            }
        } else {
            inputLayoutEditReviewResId.setVisibility(View.GONE);
            btnEditReviewResDelete.setVisibility(View.GONE);
        }
    }


    private void setOnClickListener() {
        btnEditReviewResSave.setOnClickListener(v -> {
            String userName = spnEditReviewResUser.getSelectedItem().toString();
            User user = dbHelper.userDao.getUserByUsername(userName);
            String restaurantName = spnEditReviewResRes.getSelectedItem().toString();
            Restaurant restaurant = dbHelper.resDao.getRestaurantByName(restaurantName);
            double rating = rbEditReviewRes.getRating();
            String reviewResComment = edtEditReviewResComment.getText().toString().trim();

            if (selectedReview == null) {
                ReviewRestaurant newReviewRestaurant = new ReviewRestaurant(reviewResComment, rating, user, restaurant);
                dbHelper.reviewRestaurantDao.insertReview(newReviewRestaurant);
                Toast.makeText(AdminReviewResDetail.this, "Tạo Đánh giá mới thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                selectedReview.setComment(reviewResComment);
                selectedReview.setUser(user);
                selectedReview.setRestaurant(restaurant);
                dbHelper.reviewRestaurantDao.updateReview(selectedReview);
                Toast.makeText(AdminReviewResDetail.this, "Cập nhật Đánh giá thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditReviewResDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa Đánh giá này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(AdminReviewResDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dbHelper.reviewRestaurantDao.deleteReview(selectedReview.getId());
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}