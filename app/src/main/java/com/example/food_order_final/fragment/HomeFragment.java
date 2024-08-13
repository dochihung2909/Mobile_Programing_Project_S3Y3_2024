package com.example.food_order_final.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.MainActivity;
import com.example.food_order_final.activity.RestaurantActivity;
import com.example.food_order_final.activity.SearchActivity;
import com.example.food_order_final.custom_activity.RestaurantCardView;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout lnHomeContainer;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvCurrentLocation;
    private LocationManager locationManager;
    private TextInputEditText etSearch;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
    }

    public void initVariable() {

        tvCurrentLocation = getView().findViewById(R.id.tvCurrentLocation);
        etSearch = getView().findViewById(R.id.etSearch);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        LinearLayout lnHomeContainer = getView().findViewById(R.id.lnHomeContainer);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        RestaurantCategoryDao restaurantCategoryDao = new RestaurantCategoryDao(databaseHelper);
        RestaurantDao restaurantDao = new RestaurantDao(databaseHelper, restaurantCategoryDao);
        List<Restaurant> restaurants = restaurantDao.getAllRestaurants();
        Toast.makeText(getActivity(), "" + restaurants.size(), Toast.LENGTH_SHORT).show();
        for (Restaurant restaurant: restaurants) {
            RestaurantCardView restaurantCardView = new RestaurantCardView(getActivity());

            restaurantCardView.setRestaurantName(restaurant.getName());
            restaurantCardView.setRestaurantDistance(restaurant.getAddress());

            restaurantCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    intent.putExtra("restaurant", restaurant.getId());
                    startActivity(intent);
                }
            });
            lnHomeContainer.addView(restaurantCardView);
        }

        Button btnGetLocation = getView().findViewById(R.id.btnGetLocation);

        btnGetLocation.setOnClickListener(view -> {
            getLocation();
        });

        btnGetLocation.callOnClick();

//        Search function
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5F, (LocationListener) getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}