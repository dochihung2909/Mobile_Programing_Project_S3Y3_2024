package com.example.food_order_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoadScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        String password = sharedPreferences.getString("password", "Password");
        DatabaseHelper dbHelper = new DatabaseHelper(LoadScreenActivity.this);
        if (dbHelper.checkUsernamePassword(username, password)){
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
}