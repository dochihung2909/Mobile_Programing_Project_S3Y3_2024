package com.example.food_order_final.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminFoodManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;

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

        TextView tvAdminFoodName = convertView.findViewById(R.id.tvAdminFoodName);
        TextView tvAdminFoodRes = convertView.findViewById(R.id.tvAdminFoodRestaurant);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (food != null) {
            if (tvAdminFoodName != null) {
                tvAdminFoodName.setText(food.getName());
            } else {
                tvAdminFoodName.setText("Name: N/A");
            }
            if (tvAdminFoodRes != null) {
                tvAdminFoodRes.setText(food.getRestaurant().getName());
            } else {
                tvAdminFoodRes.setText("Cate: N/A");
            }
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(food));

        return convertView;
    }

    private void showConfirmDeleteDialog(Food food) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa món ăn")
                .setMessage("Bạn có chắc muốn xóa món ăn " + food.getName() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteFood(food);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteFood(Food food) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.foodDao.deleteFood(food.getId());
        ((AdminFoodManagement) context).loadFoods(); 
    }
}
