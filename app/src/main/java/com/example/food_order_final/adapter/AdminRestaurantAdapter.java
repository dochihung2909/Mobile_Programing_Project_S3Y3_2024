package com.example.food_order_final.adapter;

import android.app.AlertDialog;
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

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminRestaurantManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;

import java.util.List;

public class AdminRestaurantAdapter extends ArrayAdapter<Restaurant> {
    private Context context;
    private List<Restaurant> restaurants;
    public AdminRestaurantAdapter(Context context, List<Restaurant> restaurants) {
        super(context, 0, restaurants);
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_restaurant_cell, parent, false);
        }
        Restaurant res = restaurants.get(position);

        TextView tvAdminResName = convertView.findViewById(R.id.tvAdminResName);
        TextView tvAdminResAddress = convertView.findViewById(R.id.tvAdminResAddress);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (res != null) {
            if (tvAdminResName != null) {
                tvAdminResName.setText(res.getName());
            }
            if (tvAdminResAddress != null) {
                tvAdminResAddress.setText(res.getAddress());
            }
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(res));

        return convertView;
    }

    private void showConfirmDeleteDialog(Restaurant restaurant) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa nhà hàng")
                .setMessage("Bạn có chắc muốn xóa nhà hàng " + restaurant.getName() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteRestaurant(restaurant);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteRestaurant(Restaurant restaurant) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.resDao.deleteRestaurant(restaurant.getId());
        ((AdminRestaurantManagement) context).loadRestaurants();
        Toast.makeText(context, "Restaurant deleted", Toast.LENGTH_SHORT).show();
    }
}
