package com.example.food_order_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.models.User;

import java.util.List;

public class AdminUserAdapter extends ArrayAdapter<User> {
    Context context;
    List<User> users;

    public AdminUserAdapter(Context context,List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_user_cell, parent, false);
        }

        User user = users.get(position);

        ImageView ivAdminUserAvatar = (ImageView) convertView.findViewById(R.id.ivAdminUserAvatar);
        TextView tvAdminUserId = (TextView) convertView.findViewById(R.id.tvAdminUserId);
        TextView tvAdminUserUsername = (TextView) convertView.findViewById(R.id.tvAdminUserUsername);
        TextView tvAdminUserFullName = (TextView) convertView.findViewById(R.id.tvAdminUserFullName);

        if (user != null) {
            tvAdminUserId.setText(String.valueOf(user.getId()));
            tvAdminUserUsername.setText(user.getUsername());
            tvAdminUserFullName.setText(user.getFullName());
        }

        return convertView;
    }
}
