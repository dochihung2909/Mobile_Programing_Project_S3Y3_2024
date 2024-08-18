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

        TextView tvAdminResId = convertView.findViewById(R.id.tvAdminResId);
        TextView tvAdminResName = convertView.findViewById(R.id.tvAdminResName);
        TextView tvAdminResAddress = convertView.findViewById(R.id.tvAdminResAddress);
        ImageView ivAdminResAvatar = convertView.findViewById(R.id.ivAdminResAvatar);

        if (res != null) {
            if (tvAdminResId != null) {
                tvAdminResId.setText(String.valueOf(res.getId()));
            }
            if (tvAdminResName != null) {
                tvAdminResName.setText(res.getName());
            }
            if (tvAdminResAddress != null) {
                tvAdminResAddress.setText(res.getAddress());
            }
            if (ivAdminResAvatar != null) {
                // Optional: Set default image or handle image setting
                // ivAdminResAvatar.setImageResource(R.drawable.default_image); // Example
            }
        }
        return convertView;
    }
}
