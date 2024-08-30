package com.example.food_order_final.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.LoginActivity;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.fragment.AdminHomeFragment;
import com.example.food_order_final.fragment.AdminStatisticFragment;
import com.example.food_order_final.fragment.HomeFragment;
import com.example.food_order_final.fragment.OrderFragment;
import com.example.food_order_final.fragment.UserFragment;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.google.android.material.tabs.TabLayout;

public class AdminMainActivity extends AppCompatActivity {
    TabLayout adminTabLayout;
    FrameLayout adminFrameLayout;
    private Button logoutBtn;
    DatabaseHelper dbHelper;
    private TextView tvAdminHomeUserFullName,
            tvAdminHomeUserUsername;
    private ImageView ivAdminHomeUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadUserInfo();
        setOnClickListener();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameLayout, new AdminHomeFragment())
                .addToBackStack(null)
                .commit();

    }

    public void initWidgets() {
        adminTabLayout = (TabLayout) findViewById(R.id.adminTabLayout);
        adminFrameLayout = (FrameLayout) findViewById(R.id.adminFrameLayout);

        logoutBtn = findViewById(R.id.logoutBtn);

        tvAdminHomeUserFullName = findViewById(R.id.tvAdminHomeUserFullName);
        tvAdminHomeUserUsername = findViewById(R.id.tvAdminHomeUserUsername);

        ivAdminHomeUserAvatar = findViewById(R.id.ivAdminHomeUserAvatar);

    }

    private void setOnClickListener() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit().clear();
                editor.apply();

                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        adminTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new AdminHomeFragment();
                        break;
                    case 1:
                        fragment = new AdminStatisticFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameLayout, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Username");
        User user = dbHelper.userDao.getUserByUsername(username);
        tvAdminHomeUserFullName.setText(user.getFullName());
        tvAdminHomeUserUsername.setText(user.getUsername());
        LoadImageUtil.loadImage(ivAdminHomeUserAvatar, user.getAvatar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }
}