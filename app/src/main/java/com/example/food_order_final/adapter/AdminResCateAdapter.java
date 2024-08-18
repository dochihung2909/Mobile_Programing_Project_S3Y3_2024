package com.example.food_order_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
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

        if (category != null) {
            tvAdminResCateId.setText(String.valueOf(category.getId()));
            tvAdminResCateName.setText(category.getName());
        }

        return convertView;
    }
}
