package com.example.food_order_final.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminCartManagement;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;

import java.util.List;

public class AdminCartAdapter extends ArrayAdapter<Cart> {
    Context context;
    List<Cart> carts;
    public AdminCartAdapter(Context context, List<Cart> carts) {
        super(context, 0, carts);
        this.context = context;
        this.carts = carts;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_cart_cell, parent, false);
        }

        Cart cart = carts.get(position);

        TextView tvAdminCartId = convertView.findViewById(R.id.tvAdminCartId);
        TextView tvAdminCartUserId = convertView.findViewById(R.id.tvAdminCartUserId);
        TextView tvAdminCartResId = convertView.findViewById(R.id.tvAdminCartResId);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);

        if (cart != null) {
            tvAdminCartId.setText("Cart ID: " + cart.getId());
            tvAdminCartUserId.setText("User ID: " + cart.getUser().getId());
            tvAdminCartResId.setText("Restaurant ID: " + cart.getRestaurant().getId());
        }

        btnDelete.setOnClickListener(v -> showDialogConfirmDelete(cart));

        return convertView;
    }

    private void showDialogConfirmDelete(Cart cart) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa đơn hàng")
                .setMessage("Bạn có chắc muốn xóa đơn hàng không?")
                .setPositiveButton("Có", ((dialog, which) -> {
                    deleteCart(cart);
                }))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCart(Cart cart) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.cartDao.deleteCart(cart.getId());
        ((AdminCartManagement) context).loadAllCarts();
        Toast.makeText(context, "Cart deleted", Toast.LENGTH_SHORT).show();
    }
}
