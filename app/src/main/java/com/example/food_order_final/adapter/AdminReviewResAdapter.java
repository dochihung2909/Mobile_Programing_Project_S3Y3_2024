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
import com.example.food_order_final.models.ReviewRestaurant;

import java.util.List;

public class AdminReviewResAdapter extends ArrayAdapter<ReviewRestaurant> {
    private Context context;
    private List<ReviewRestaurant> reviewRestaurants;
    public AdminReviewResAdapter(Context context, List<ReviewRestaurant> reviewRestaurants) {
        super(context, 0, reviewRestaurants);
        this.context = context;
        this.reviewRestaurants = reviewRestaurants;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_review_res_cell, parent, false);
        }

        ReviewRestaurant reviewRestaurant = reviewRestaurants.get(position);

        TextView tvAdminReviewResId = convertView.findViewById(R.id.tvAdminReviewResId);
        TextView tvAdminReviewResUserId = convertView.findViewById(R.id.tvAdminReviewResUserId);
        TextView tvAdminReviewResResId = convertView.findViewById(R.id.tvAdminReviewResResId);
        TextView tvAdminReviewResComment = convertView.findViewById(R.id.tvAdminReviewResComment);

        if (reviewRestaurant != null) {
            if (tvAdminReviewResId != null) {
                tvAdminReviewResId.setText("Review ID: " + reviewRestaurant.getId());
            }

            if (tvAdminReviewResUserId != null) {
                if (reviewRestaurant.getUser() != null) {
                    tvAdminReviewResUserId.setText("User ID: " + reviewRestaurant.getUser().getId());
                } else {
                    tvAdminReviewResUserId.setText("User ID: N/A");
                }
            }

            if (tvAdminReviewResResId != null) {
                if (reviewRestaurant.getRestaurant() != null) {
                    tvAdminReviewResResId.setText("Restaurant ID: " + reviewRestaurant.getRestaurant().getId());
                } else {
                    tvAdminReviewResResId.setText("Restaurant ID: N/A");
                }
            }

            if (tvAdminReviewResComment != null) {
                tvAdminReviewResComment.setText(reviewRestaurant.getComment() != null ? reviewRestaurant.getComment() : "No Comment");
            }
        }
        return convertView;
    }

}
