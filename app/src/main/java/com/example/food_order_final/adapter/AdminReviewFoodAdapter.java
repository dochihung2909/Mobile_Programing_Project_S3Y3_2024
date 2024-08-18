package com.example.food_order_final.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.models.ReviewFood;
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
        return convertView;
    }

}
