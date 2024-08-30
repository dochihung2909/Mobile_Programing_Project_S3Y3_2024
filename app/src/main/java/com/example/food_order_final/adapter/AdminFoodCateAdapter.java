package com.example.food_order_final.adapter;

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
import androidx.appcompat.app.AlertDialog;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminFoodCateManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.FoodCategory;

import java.util.List;

public class AdminFoodCateAdapter extends ArrayAdapter<FoodCategory> {
    private Context context;
    private List<FoodCategory> categories;

    public AdminFoodCateAdapter(Context context, List<FoodCategory> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_food_cate_cell, parent, false);
        }

        FoodCategory category = categories.get(position);

        TextView tvAdminFoodCateId = convertView.findViewById(R.id.tvAdminFoodCateId);
        TextView tvAdminFoodCateName = convertView.findViewById(R.id.tvAdminFoodCateName);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (category != null) {
            tvAdminFoodCateId.setText(String.valueOf(category.getId()));
            tvAdminFoodCateName.setText(category.getName());
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(category));

        return convertView;
    }

    private void showConfirmDeleteDialog(FoodCategory foodCate) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục " + foodCate.getName() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteCate(foodCate);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCate(FoodCategory foodCate) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.foodCateDao.deleteFoodCategory(foodCate.getId());
        ((AdminFoodCateManagement) context).loadCategories();
        Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show();
    }
}
