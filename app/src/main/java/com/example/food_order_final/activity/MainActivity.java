package com.example.food_order_final.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.RestaurantCardView;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.fragment.FavouriteFragment;
import com.example.food_order_final.fragment.HomeFragment;
import com.example.food_order_final.fragment.OrderFragment;
import com.example.food_order_final.models.Restaurant;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private FrameLayout frameLayout;
    private TabLayout tabLayout;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initWidgets();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        tabLayout.getTabAt(2).setText(username);


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment())
                .addToBackStack(null)
                .commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new OrderFragment();
                        break;
                    case 2:
                        fragment = new FavouriteFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment)
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

    private void initWidgets() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(MainActivity.this, "" + location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addressess.get(0).getAddressLine(0);
            TextView tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
            tvCurrentLocation.setText(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}