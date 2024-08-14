package com.example.food_order_final.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.custom_activity.FoodOrderCardView;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LocationUtil;
import com.example.food_order_final.util.PriceUtil;

import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements LocationListener {

    private TextView tvFinalTotalPrice, tvTotalDishPrice, tvShipPrice, tvRestaurantName, tvUserFullName, tvUserPhoneNumber, tvUserAddress, tvNumberOfDishes;
    private LinearLayout foodsLayout;
    private EditText etOrderNote;
    private RadioGroup radioGroupPaymentMethod;
    private Button btnOrderSubmit, btnShowAllFood;
    private ImageButton btnBack;
    private int cartId = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        this.cartId = getIntent().getIntExtra("cartId", -1);

        if (cartId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(OrderActivity.this);
            CartDao cartDao = new CartDao(dbHelper);
            RestaurantDao restaurantDao = new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper));
            Cart cart = cartDao.getCartById(cartId);
            Toast.makeText(OrderActivity.this, "Cart ID = " + cartId, Toast.LENGTH_SHORT).show();
            Restaurant restaurant = cart.getRestaurant();
            CartDetailDao cartDetailDao = new CartDetailDao(dbHelper);
            List<CartDetail> cartDetails = cartDetailDao.getAllCartDetailInCart(cartId);

            FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), restaurantDao);

            double totalDishAmount = cartDao.getTotalAmountByCartId(cartId);
            double shipPrice = 16000;
            int totalDishes = cartDao.getTotalDishes(cartId);
            User currentUser = cart.getUser();
            tvUserFullName.setText(currentUser.getFullName());

            Toast.makeText(OrderActivity.this, "Cart ID = " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();

            LocationUtil.getLocation(OrderActivity.this);
            tvUserPhoneNumber.setText(currentUser.getPhoneNumber());
            tvNumberOfDishes.setText(String.format("Tổng số món ăn (%s món)", totalDishes));
            tvTotalDishPrice.setText(PriceUtil.formatNumber(totalDishAmount) + "đ");
            tvShipPrice.setText(PriceUtil.formatNumber(shipPrice) + "đ");
            tvRestaurantName.setText(restaurant.getName());
            double finalTotalPrice = totalDishAmount + shipPrice;
            tvFinalTotalPrice.setText(PriceUtil.formatNumber(finalTotalPrice) + "đ");

            if (cartDetails.size() > 1) {
                btnShowAllFood.setVisibility(View.VISIBLE);
            } else {
                addFoodToContainer(cartDetails.get(0));
            }

            btnShowAllFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CartDetail cartDetail: cartDetails) {
                        addFoodToContainer(cartDetail);
                    }
                }
            });

        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroupPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) radioGroupPaymentMethod.findViewById(checkedId);
            }
        });

        btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPaymentMethodId = radioGroupPaymentMethod.getCheckedRadioButtonId();
                Toast.makeText(OrderActivity.this, "" + selectedPaymentMethodId, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void init() {
        tvFinalTotalPrice = findViewById(R.id.tvFinalTotalPrice);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvShipPrice = findViewById(R.id.tvShipPrice);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvUserPhoneNumber = findViewById(R.id.tvUserPhoneNumber);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        foodsLayout = findViewById(R.id.foodsLayout);
        etOrderNote = findViewById(R.id.etOrderNote);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        btnOrderSubmit = findViewById(R.id.btnOrderSubmit);
        btnBack = findViewById(R.id.btnBack);
        tvNumberOfDishes = findViewById(R.id.tvNumberOfDishes);
        btnShowAllFood = findViewById(R.id.btnShowAllFood);
        tvTotalDishPrice = findViewById(R.id.tvTotalDishPrice);
    }

    private void addFoodToContainer(CartDetail cartDetail) {
        Food food = cartDetail.getFood();
        FoodOrderCardView foodOrderCardView = new FoodOrderCardView(OrderActivity.this);
        foodOrderCardView.setTvFoodName(food.getName());
        foodOrderCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(cartDetail.getPrice() * cartDetail.getQuantity()));
        foodOrderCardView.setFoodQuantityOrder(String.valueOf(cartDetail.getQuantity()) + "x");
        foodOrderCardView.setTvFoodDescription(food.getDescription());
        foodsLayout.addView(foodOrderCardView);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(MainActivity.this, "" + location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
            List<Address> addressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addressess.get(0).getAddressLine(0);
            tvUserAddress.setText(address);
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