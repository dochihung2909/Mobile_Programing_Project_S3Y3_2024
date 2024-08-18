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
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import java.util.List;

public class AdminRoleAdapter extends ArrayAdapter<Role> {
    Context context;
    List<Role> roles;

    public AdminRoleAdapter(Context context,List<Role> roles) {
        super(context, 0, roles);
        this.context = context;
        this.roles = roles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_role_cell, parent, false);
        }

        Role role = roles.get(position);

        TextView tvAdminRoleId = (TextView) convertView.findViewById(R.id.tvAdminRoleId);
        TextView tvAdminRoleName = (TextView) convertView.findViewById(R.id.tvAdminRoleName);

        if (role != null) {
            tvAdminRoleId.setText(String.valueOf(role.getId()));
            tvAdminRoleName.setText(role.getName());
        }

        return convertView;
    }
}
