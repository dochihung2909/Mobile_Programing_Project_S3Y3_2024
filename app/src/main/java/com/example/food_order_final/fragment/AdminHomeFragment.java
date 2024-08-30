package com.example.food_order_final.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminCartManagement;
import com.example.food_order_final.activity.admin.AdminFoodCateManagement;
import com.example.food_order_final.activity.admin.AdminFoodManagement;
import com.example.food_order_final.activity.admin.AdminResCateManagement;
import com.example.food_order_final.activity.admin.AdminRestaurantManagement;
import com.example.food_order_final.activity.admin.AdminReviewManagement;
import com.example.food_order_final.activity.admin.AdminRoleManagement;
import com.example.food_order_final.activity.admin.AdminUserManagement;
import com.example.food_order_final.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView lvAdminManagement;
    private LinearLayout linearLayoutAdminHomeUser,
            linearLayoutAdminHomeRole,
            linearLayoutAdminHomeRestaurant,
            linearLayoutAdminHomeResCate,
            linearLayoutAdminHomeFood,
            linearLayoutAdminHomeFoodCate,
            linearLayoutAdminHomeCart,
            linearLayoutAdminHomeReview;
            ;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        dbHelper = new DatabaseHelper(getContext());
        initWidgets(view);
        setOnClickListener(view);

        return view;
    }
    private void initWidgets(View view) {
        linearLayoutAdminHomeUser = view.findViewById(R.id.linearLayoutAdminHomeUser);
        linearLayoutAdminHomeRole = view.findViewById(R.id.linearLayoutAdminHomeRole);
        linearLayoutAdminHomeRestaurant = view.findViewById(R.id.linearLayoutAdminHomeRestaurant);
        linearLayoutAdminHomeResCate = view.findViewById(R.id.linearLayoutAdminHomeResCate);
        linearLayoutAdminHomeFood = view.findViewById(R.id.linearLayoutAdminHomeFood);
        linearLayoutAdminHomeFoodCate = view.findViewById(R.id.linearLayoutAdminHomeFoodCate);
        linearLayoutAdminHomeReview = view.findViewById(R.id.linearLayoutAdminHomeReview);
        linearLayoutAdminHomeCart = view.findViewById(R.id.linearLayoutAdminHomeCart);
    }

    private void setOnClickListener(View view) {
        linearLayoutAdminHomeUser.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminUserManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeRole.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminRoleManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminRestaurantManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeResCate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminResCateManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeFood.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminFoodManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeFoodCate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminFoodCateManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminCartManagement.class);
            startActivity(intent);
        });

        linearLayoutAdminHomeReview.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminReviewManagement.class);
            startActivity(intent);
        });


    }

}