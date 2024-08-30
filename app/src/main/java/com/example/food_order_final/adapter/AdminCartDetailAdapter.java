package com.example.food_order_final.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;

import java.util.List;

public class AdminCartDetailAdapter extends ArrayAdapter<CartDetail> {
    private Context context;
    private List<CartDetail> cartDetails;
    public AdminCartDetailAdapter(Context context, List<CartDetail> cartDetails) {
        super(context, 0, cartDetails);
        this.context = context;
        this.cartDetails = cartDetails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_cart_detail_cell, parent, false);
        }

        CartDetail cartDetail = cartDetails.get(position);

        TextView tvEditCartDetailName = convertView.findViewById(R.id.tvEditCartDetailName);
        ImageButton btnRemove = convertView.findViewById(R.id.btnRemove);
        ImageButton btnAdd = convertView.findViewById(R.id.btnAdd);
        ImageView btnDelete = convertView.findViewById(R.id.btnDelete);
        EditText edtQuantity = convertView.findViewById(R.id.edtQuantity);

        if (cartDetail != null) {
            tvEditCartDetailName.setText(cartDetail.getFood().getName());
            edtQuantity.setText(String.valueOf(cartDetail.getQuantity()));
        }

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = edtQuantity.getText().toString();
                if (!TextUtils.isEmpty(quantityStr)) {
                    int currentQuantity = Integer.parseInt(quantityStr);
                    if (currentQuantity > 1) {
                        currentQuantity--;
                        edtQuantity.setText(String.valueOf(currentQuantity));
                        cartDetail.setQuantity(currentQuantity);

                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.cartDetailDao.updateFoodQuantity(cartDetail.getQuantity(), cartDetail.getId());
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = edtQuantity.getText().toString();
                if (!TextUtils.isEmpty(quantityStr)) {
                    int currentQuantity = Integer.parseInt(quantityStr);
                    currentQuantity++;
                    edtQuantity.setText(String.valueOf(currentQuantity));
                    cartDetail.setQuantity(currentQuantity);

                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.cartDetailDao.updateFoodQuantity(cartDetail.getQuantity(), cartDetail.getId());
                }
            }
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa món")
                    .setMessage("Bạn có chắc muốn xóa món " + cartDetail.getFood().getName())
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.cartDetailDao.deleteCartDetail(cartDetail.getId());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        return convertView;
    }

}
