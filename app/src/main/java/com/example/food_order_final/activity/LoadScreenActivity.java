package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cloudinary.android.MediaManager;
import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminMainActivity;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.User;

import java.util.HashMap;
import java.util.Map;

public class LoadScreenActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(LoadScreenActivity.this);

    SharedPreferences prefs = null;

    private static boolean mediaManager = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        dbHelper.initializeData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        String password = sharedPreferences.getString("password", "Password");
        String currentUserRole = sharedPreferences.getString("currentUserRole", "None");
        prefs = getSharedPreferences("com.example.food_order_final", MODE_PRIVATE);
        User user = dbHelper.userDao.getUserByUsername(username);
        if (dbHelper.userDao.isUserCredential(username, password)){
            if (!currentUserRole.equals("None")) {
                if (currentUserRole.equals("Admin")) {
                    Intent intent = new Intent(LoadScreenActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                } else if (currentUserRole.equals("User") || currentUserRole.equals("Owner")) {
                    Intent intent = new Intent(LoadScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoadScreenActivity.this, RestaurantManagerActivity.class);
                    intent.putExtra("employeeId", user.getId());
                    startActivity(intent);
                }
            }
        }
        Button startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        Intent mainIntent = new Intent(LoadScreenActivity.this,
                                LoginActivity.class);

                        startActivity(mainIntent);
                        finish();

                        overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom);
                    }
                }, 100);
            }
        });
    }
    private void initConfigCloudinary() {
        if (!mediaManager) {
            Map config = new HashMap();
            config.put("cloud_name", "dpacbtjva");
            config.put("api_key", "271295249232522");
            config.put("api_secret", "E5p9fqKGnvRRg-gtU8HxUpynWvI");
            mediaManager = true;
            MediaManager.init(this, config);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            dbHelper.initializeData();
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}