package com.example.food_order_final.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminReviewFoodManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.ReviewFood;

import java.util.List;

public class AdminReviewFoodAdapter extends ArrayAdapter<ReviewFood> {
    private Context context;
    private List<ReviewFood> reviewFoods;
    public AdminReviewFoodAdapter(Context context, List<ReviewFood> reviewFoods) {
        super(context, 0, reviewFoods);
        this.context = context;
        this.reviewFoods = reviewFoods;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_review_food_cell, parent, false);
        }

        ReviewFood reviewFood = reviewFoods.get(position);

        TextView tvAdminReviewFoodId = convertView.findViewById(R.id.tvAdminReviewFoodId);
        TextView tvAdminReviewFoodUserId = convertView.findViewById(R.id.tvAdminReviewFoodUserId);
        TextView tvAdminReviewFoodFoodId = convertView.findViewById(R.id.tvAdminReviewFoodFoodId);
        TextView tvAdminReviewFoodComment = convertView.findViewById(R.id.tvAdminReviewFoodComment);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (reviewFood != null) {
            if (tvAdminReviewFoodId != null) {
                tvAdminReviewFoodId.setText("Review ID: " + reviewFood.getId());
            }

            if (tvAdminReviewFoodUserId != null) {
                if (reviewFood.getUser() != null) {
                    tvAdminReviewFoodUserId.setText("User ID: " + reviewFood.getUser().getId());
                } else {
                    tvAdminReviewFoodUserId.setText("User ID: N/A");
                }
            }

            if (tvAdminReviewFoodFoodId != null) {
                if (reviewFood.getFood() != null) {
                    tvAdminReviewFoodFoodId.setText("Food ID: " + reviewFood.getFood().getId());
                } else {
                    tvAdminReviewFoodFoodId.setText("Food ID: N/A");
                }
            }

            if (tvAdminReviewFoodComment != null) {
                tvAdminReviewFoodComment.setText(reviewFood.getComment() != null ? reviewFood.getComment() : "No Comment");
            }
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(reviewFood));

        return convertView;
    }

    private void showConfirmDeleteDialog(ReviewFood reviewFood) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa đánh giá")
                .setMessage("Bạn có chắc muốn xóa đánh giá không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteReview(reviewFood);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteReview(ReviewFood reviewFood) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.reviewFoodDao.deleteReview(reviewFood.getId());
        ((AdminReviewFoodManagement) context).loadAllReviews();
    }

}
