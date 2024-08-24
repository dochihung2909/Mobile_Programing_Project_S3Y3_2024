package com.example.food_order_final.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminFoodCateManagement;
import com.example.food_order_final.activity.admin.AdminFoodManagement;
import com.example.food_order_final.activity.admin.AdminResCateManagement;
import com.example.food_order_final.activity.admin.AdminRestaurantManagement;
import com.example.food_order_final.activity.admin.AdminReviewFoodManagement;
import com.example.food_order_final.activity.admin.AdminReviewResManagement;
import com.example.food_order_final.activity.admin.AdminRoleManagement;
import com.example.food_order_final.activity.admin.AdminUserManagement;
import com.example.food_order_final.activity.LoginActivity;
import com.example.food_order_final.adapter.AdminHomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {
    private ListView lvAdminManagement;

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

        ListView listView = view.findViewById(R.id.lvAdminManagement);
        Button logoutBtn = view.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit().clear();
                editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        List<String> items = new ArrayList<>();
        items.add("Role");
        items.add("User");
        items.add("Restaurant Category");
        items.add("Restaurant");
        items.add("Review Restaurant");
        items.add("Food Category");
        items.add("Food");
        items.add("Review Food");

        AdminHomeAdapter adapter = new AdminHomeAdapter(getContext(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                if ("Role".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminRoleManagement.class);
                    startActivity(intent);
                } else if ("User".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminUserManagement.class);
                    startActivity(intent);
                } else if ("Restaurant Category".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminResCateManagement.class);
                    startActivity(intent);
                } else if ("Restaurant".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminRestaurantManagement.class);
                    startActivity(intent);
                } else if ("Review Restaurant".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminReviewResManagement.class);
                    startActivity(intent);
                } else if ("Food Category".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminFoodCateManagement.class);
                    startActivity(intent);
                } else if ("Food".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminFoodManagement.class);
                    startActivity(intent);
                } else if ("Review Food".equals(selectedItem)) {
                    Intent intent = new Intent(getContext(), AdminReviewFoodManagement.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}