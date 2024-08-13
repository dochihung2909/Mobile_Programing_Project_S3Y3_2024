package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

public class LoadScreenActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(LoadScreenActivity.this);

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        String password = sharedPreferences.getString("password", "Password");
        prefs = getSharedPreferences("com.example.food_order_final", MODE_PRIVATE);
        if (dbHelper.userDao.isUserCredential(username, password)){
            Intent intent = new Intent(LoadScreenActivity.this, MainActivity.class);
            startActivity(intent);
        }
        Button startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        /* Create an intent that will start the main activity. */
                        Intent mainIntent = new Intent(LoadScreenActivity.this,
                                LoginActivity.class);
                        mainIntent.putExtra("id", "1");

                        //SplashScreen.this.startActivity(mainIntent);
                        startActivity(mainIntent);
                        /* Finish splash activity so user cant go back to it. */
                        LoadScreenActivity.this.finish();

                     /* Apply our splash exit (fade out) and main
                        entry (fade in) animation transitions. */
                        overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom);
                    }
                }, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            dbHelper.initializeData();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
}