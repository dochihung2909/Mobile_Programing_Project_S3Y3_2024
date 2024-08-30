package com.example.food_order_final.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.food_order_final.activity.admin.AdminUserManagement;
import com.example.food_order_final.database.DatabaseHelper;
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

        TextView tvAdminUserUsername = convertView.findViewById(R.id.tvAdminUserUsername);
        TextView tvAdminUserFullName = convertView.findViewById(R.id.tvAdminUserFullName);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);
        if (user != null) {
            tvAdminUserUsername.setText(user.getUsername());
            tvAdminUserFullName.setText(user.getFullName());
        }

        btnDelete.setOnClickListener(v -> showConfirmDeleteDialog(user));

        return convertView;
    }

    private void showConfirmDeleteDialog(User user) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa người dùng")
                .setMessage("Bạn có chắc muốn xóa người dùng " + user.getUsername() + " không ?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    deleteUser(user);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteUser(User user) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.userDao.deleteUser(user.getId());
        ((AdminUserManagement) context).loadAllUsers();
    }
}
