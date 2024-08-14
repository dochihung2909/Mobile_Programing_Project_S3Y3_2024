package com.example.food_order_final.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationUtil {


    private static LocationManager locationManager;
    @SuppressLint("MissingPermission")
    public static void getLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5F, (LocationListener) context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
