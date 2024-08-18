package com.example.food_order_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.food_order_final.R;

import java.util.List;

public class AdminHomeAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> items;

    public AdminHomeAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_home_cell, parent, false);
        }

        TextView tvItemName = convertView.findViewById(R.id.tvAdminCellManagement);
        String item = getItem(position);
        tvItemName.setText(item);

        return convertView;
    }
}
