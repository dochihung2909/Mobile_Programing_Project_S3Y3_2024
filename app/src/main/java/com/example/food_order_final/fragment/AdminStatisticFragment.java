package com.example.food_order_final.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;

import java.text.DecimalFormat;

public class AdminStatisticFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private LinearLayout linearLayoutAdminStatChart,
            linearLayoutAdminStatSum;
    private TextView tvAdminStatTotalUser, tvAdminStatTotalRestaurant,
            tvAdminStatTotalCart, tvAdminStatTotalRevenue;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminStatisticFragment() {
        // Required empty public constructor
    }

    public static AdminStatisticFragment newInstance(String param1, String param2) {
        AdminStatisticFragment fragment = new AdminStatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_statistic, container, false);

        dbHelper = new DatabaseHelper(getContext());
        initWidgets(view);
        loadData();
        setOnClickListener(view);

        return view;
    }

    private void initWidgets(View view) {
        tvAdminStatTotalUser = view.findViewById(R.id.tvAdminStatTotalUser);
        tvAdminStatTotalRestaurant = view.findViewById(R.id.tvAdminStatTotalRestaurant);
        tvAdminStatTotalCart = view.findViewById(R.id.tvAdminStatTotalCart);
        tvAdminStatTotalRevenue = view.findViewById(R.id.tvAdminStatTotalRevenue);
    }

    private void setOnClickListener(View view) {

    }

    private void loadData() {
        long totalUser = dbHelper.userDao.countUser();
        long totalRestaurant = dbHelper.resDao.countRestaurant();
        long totalCart = dbHelper.cartDao.countCart();
        tvAdminStatTotalUser.setText(String.valueOf(totalUser));
        tvAdminStatTotalRestaurant.setText(String.valueOf(totalRestaurant));
        tvAdminStatTotalCart.setText(String.valueOf(totalCart));

        double totalRevenue = dbHelper.cartDao.getTotalRevenue();
        DecimalFormat formatter = new DecimalFormat("#,###.0");
        String formattedRevenue = formatter.format(totalRevenue);
        tvAdminStatTotalRevenue.setText(formattedRevenue);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}