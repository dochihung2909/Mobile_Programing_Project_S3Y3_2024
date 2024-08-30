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
import com.example.food_order_final.activity.admin.AdminResCateManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.RestaurantCategory;

import java.util.List;

public class AdminResCateAdapter extends ArrayAdapter<RestaurantCategory> {
    private Context context;
    private List<RestaurantCategory> categories;

    public AdminResCateAdapter(Context context, List<RestaurantCategory> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_res_cate_cell, parent, false);
        }

        RestaurantCategory category = categories.get(position);

        TextView tvAdminResCateId = convertView.findViewById(R.id.tvAdminResCateId);
        TextView tvAdminResCateName = convertView.findViewById(R.id.tvAdminResCateName);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (category != null) {
            tvAdminResCateId.setText(String.valueOf(category.getId()));
            tvAdminResCateName.setText(category.getName());
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(category));

        return convertView;
    }

    private void showConfirmDeleteDialog(RestaurantCategory resCate) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục " + resCate.getName() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteCate(resCate);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCate(RestaurantCategory resCate) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.resCateDao.deleteRestaurantCategory(resCate.getId());
        ((AdminResCateManagement) context).loadCategories();
    }
}
