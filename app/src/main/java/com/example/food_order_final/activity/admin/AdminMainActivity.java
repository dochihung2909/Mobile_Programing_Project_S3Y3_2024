package com.example.food_order_final.activity.admin;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_final.R;
import com.example.food_order_final.fragment.AdminHomeFragment;
import com.example.food_order_final.fragment.AdminStatisticFragment;
import com.example.food_order_final.fragment.HomeFragment;
import com.example.food_order_final.fragment.OrderFragment;
import com.example.food_order_final.fragment.UserFragment;
import com.google.android.material.tabs.TabLayout;

public class AdminMainActivity extends AppCompatActivity {
    TabLayout adminTabLayout;
    FrameLayout adminFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);
        initWidgets();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameLayout, new AdminHomeFragment())
                .addToBackStack(null)
                .commit();

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

    public void initWidgets() {
        adminTabLayout = (TabLayout) findViewById(R.id.adminTabLayout);
        adminFrameLayout = (FrameLayout) findViewById(R.id.adminFrameLayout);
    }
}