package com.example.food_order_final.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.FoodDetailActivity;
import com.example.food_order_final.activity.MainActivity;
import com.example.food_order_final.activity.RestaurantActivity;
import com.example.food_order_final.activity.SearchActivity;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.custom_activity.RestaurantCardView;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.util.LocationUtil;
import com.example.food_order_final.util.PriceUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Array;
import java.text.DecimalFormat;
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
    private LinearLayout lnHomeFoodContainer;
    private TextInputEditText etSearch;
    private Spinner spFoodCate;

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
        lnHomeFoodContainer = getView().findViewById(R.id.lnHomeFoodContainer);
        spFoodCate = getView().findViewById(R.id.spFoodCate);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }



        LinearLayout lnHomeContainer = getView().findViewById(R.id.lnHomeContainer);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());



        ArrayList<FoodCategory> foodCategories = databaseHelper.foodCateDao.getAllFoodCategories();
        ArrayList<String> foodCategoriesName = new ArrayList<>();
        foodCategoriesName.add("Tất cả");
        for (FoodCategory foodCategory: foodCategories) {
            foodCategoriesName.add(foodCategory.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, foodCategoriesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFoodCate.setAdapter(adapter);
        spFoodCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    List<Food> foods = databaseHelper.foodDao.getAllFoods();
                    updateUI(foods);
                } else {
                    FoodCategory foodCategory = foodCategories.get(position - 1);
                    List<Food> foods = databaseHelper.foodDao.getAllFoodsByCategory(foodCategory.getId());
                    updateUI(foods);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        RestaurantCategoryDao restaurantCategoryDao = new RestaurantCategoryDao(databaseHelper);
        RestaurantDao restaurantDao = new RestaurantDao(databaseHelper, restaurantCategoryDao);
        List<Restaurant> restaurants = restaurantDao.getAllRestaurants();
        Toast.makeText(getActivity(), "" + restaurants.size(), Toast.LENGTH_SHORT).show();
        for (Restaurant restaurant: restaurants) {
            RestaurantCardView restaurantCardView = new RestaurantCardView(getActivity());

            restaurantCardView.setRestaurantName(restaurant.getName());
//            restaurantCardView.setRestaurantDistance(restaurant.getAddress());
            restaurantCardView.setRestaurantImage(restaurant.getAvatar());
            DecimalFormat df = new DecimalFormat("#.#");
            double rating = restaurant.getRating();
            restaurantCardView.setRestaurantRating(df.format(restaurant.getRating()));

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
            LocationUtil.getLocation(getActivity());
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

    private void updateUI(List<Food> foods) {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        dbHelper.foodDao.getAllFoods();
        lnHomeFoodContainer.removeAllViews();

        for (Food food: foods) {
            FoodCardView foodCardView = new FoodCardView(getActivity());
            double discount = food.getDiscount();
            double defaultPrice = food.getPrice();
            if (food.getDiscount() > 0) {
                TextView defaulPrice = foodCardView.getTvFoodDefaultPrice();
                defaulPrice.setText(PriceUtil.formatNumber(defaultPrice) + "đ");
                defaulPrice.setVisibility(View.VISIBLE);
                defaulPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                foodCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(defaultPrice - discount));

            } else {
                foodCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(defaultPrice));
            }

            int numberSold = dbHelper.foodDao.getNumberSold(food.getId());
            foodCardView.setTvFoodName(food.getName());
            foodCardView.setTvFoodSold(numberSold + " đã bán");
            foodCardView.setIvFoodAvatar(food.getAvatar());
            DecimalFormat df = new DecimalFormat("#.#");
            foodCardView.setTvFoodLiked( df.format(food.getRating()));

            foodCardView.findViewById(R.id.btnAddToCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    Restaurant restaurant = dbHelper.resDao.getRestaurantByFoodId(food.getId());
                    intent.putExtra("restaurant", restaurant.getId());
                    startActivity(intent);
                }
            });

            foodCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
                    intent.putExtra("foodId", food.getId());
                    startActivity(intent);
                }
            });
            lnHomeFoodContainer.addView(foodCardView);
        }
    }

//    @SuppressLint("MissingPermission")
//    private void getLocation() {
//        try {
//            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5F, (LocationListener) getActivity());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}