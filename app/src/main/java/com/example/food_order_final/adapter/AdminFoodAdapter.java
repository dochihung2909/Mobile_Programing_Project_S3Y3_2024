package com.example.food_order_final.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;

import java.util.List;

public class AdminFoodAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> foods;
    private DatabaseHelper dbHelper;
    public AdminFoodAdapter(Context context, List<Food> foods) {
        super(context, 0, foods);
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_food_cell, parent, false);
        }
        Food food = foods.get(position);

        TextView tvAdminFoodId = convertView.findViewById(R.id.tvAdminFoodId);
        TextView tvAdminFoodName = convertView.findViewById(R.id.tvAdminFoodName);
        TextView tvAdminFoodRes = convertView.findViewById(R.id.tvAdminFoodRestaurant);
        ImageView ivAdminFoodAvatar = convertView.findViewById(R.id.ivAdminFoodAvatar);

        if (food != null) {
            if (tvAdminFoodId != null) {
                tvAdminFoodId.setText(String.valueOf(food.getId()));
            }
            if (tvAdminFoodName != null) {
                tvAdminFoodName.setText(food.getName());
            }
            if (tvAdminFoodRes != null) {
                tvAdminFoodRes.setText(food.getRestaurant().getName());
            }
            if (ivAdminFoodAvatar != null) {
                // Optional: Set default image or handle image setting
                // ivAdminFoodAvatar.setImageResource(R.drawable.default_image); // Example
            }
        }
        return convertView;
    }
}
