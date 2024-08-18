package com.example.food_order_final.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.CartActivity;
import com.example.food_order_final.activity.LoadScreenActivity;
import com.example.food_order_final.activity.LoginActivity;
import com.example.food_order_final.activity.RestaurantActivity;
import com.example.food_order_final.activity.RestaurantManagerActivity;
import com.example.food_order_final.activity.UserSettingActivity;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton btnSetting, btnCart;
    private TextView tvUserFullName, tvUsername, btnShopOwner;
    private int REQUEST_CODE_USER_SETTING = 400;
    private ImageView ivUserAvatar;
    private Button btnLogout;
    private User currentUser;
    private UserDao userDao;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = pref.getString("username", "Guest");

        if (!username.equals("Guest")) {
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
            currentUser = userDao.getUserByUsername(username);
            updateUI(currentUser);

            btnShopOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RestaurantManagerActivity.class);
                    intent.putExtra("restaurantOwnerId", currentUser.getId());
                    startActivity(intent);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
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
        }

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingActivity.class);

                startActivityForResult(intent, REQUEST_CODE_USER_SETTING);
            }
        });

        pref = getActivity().getSharedPreferences("UserCart", Context.MODE_PRIVATE);
        int currentCartId = pref.getInt("cartId", -1);

        if (currentCartId == -1 ){
            btnCart.setVisibility(View.GONE);
        } else {
            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

    private void init() {
        btnSetting = getView().findViewById(R.id.btnSetting);
        btnCart = getView().findViewById(R.id.btnCart);
        tvUserFullName = getView().findViewById(R.id.tvUserFullName);
        tvUsername = getView().findViewById(R.id.tvUsername);
        btnShopOwner = getView().findViewById(R.id.btnShopOwner);
        ivUserAvatar = getView().findViewById(R.id.ivUserAvatar);
        btnLogout = getView().findViewById(R.id.btnLogout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_USER_SETTING) {
            if (resultCode == Activity.RESULT_OK) {
                currentUser = userDao.getUserById(currentUser.getId());
                updateUI(currentUser);
            }
        }
    }

    private void updateUI(User currentUser) {
        tvUsername.setText(currentUser.getUsername());
        tvUserFullName.setText(currentUser.getFullName());
        LoadImageUtil.loadImage(ivUserAvatar, currentUser.getAvatar());
    }
}