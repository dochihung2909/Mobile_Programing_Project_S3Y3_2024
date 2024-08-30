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
import com.example.food_order_final.activity.admin.AdminRoleManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;

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
        ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);

        if (role != null) {
            tvAdminRoleId.setText(String.valueOf(role.getId()));
            tvAdminRoleName.setText(role.getName());
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(role));

        return convertView;
    }

    private void showConfirmDeleteDialog(Role role) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa vai trò")
                .setMessage("Bạn có chắc muốn xóa vai trò " + role.getName() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteRole(role);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteRole(Role role) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.roleDao.deleteRole(role.getId());
        ((AdminRoleManagement) context).loadRoles();
        Toast.makeText(context, "Role deleted", Toast.LENGTH_SHORT).show();
    }
}
