package com.example.food_order_final.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodOrderCardView;
import com.example.food_order_final.custom_activity.PaymentHistoryView;
import com.example.food_order_final.dao.PaymentPendingDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Payment;
import com.example.food_order_final.models.PaymentPending;
import com.example.food_order_final.models.PaymentStatus;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.util.PriceUtil;

import java.util.ArrayList;
import java.util.List;

public class RestaurantOrderManagementActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout paymentsContainer;
    private DatabaseHelper dbHelper = new DatabaseHelper(RestaurantOrderManagementActivity.this);
    private int restaurantId = -1;
    private int REQUEST_CHANGE_STATUS_PAYMENT = 204;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_order_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        restaurantId = getIntent().getIntExtra("restaurantId", -1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateUI();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHANGE_STATUS_PAYMENT) {
            if (resultCode == RESULT_OK) {
                updateUI();
            }
        }
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        paymentsContainer = findViewById(R.id.paymentsContainer);
    }

    private void updateUI() {
        paymentsContainer.removeAllViews();
        ArrayList<PaymentPending> paymentPendings = dbHelper.paymentPendingDao.getAllPaymentByRestaurantId(restaurantId);
        for (PaymentPending paymentPending: paymentPendings) {
            PaymentHistoryView paymentHistoryView = new PaymentHistoryView(RestaurantOrderManagementActivity.this);
            paymentHistoryView.setTvPaymentStatus(PaymentStatus.getNameFromStatus(paymentPending.getPaymentStatus().getStatus()));
            Restaurant restaurant = paymentPending.getCart().getRestaurant();
            LinearLayout foodsContainer = paymentHistoryView.findViewById(R.id.foodsContainer);
            Cart cart = paymentPending.getCart();
            List<CartDetail> cartDetails = dbHelper.cartDetailDao.getAllCartDetailInCart(cart.getId());
            for (CartDetail cartDetail: cartDetails) {
                FoodOrderCardView foodOrderCardView = new FoodOrderCardView(RestaurantOrderManagementActivity.this);
                foodOrderCardView.setFoodQuantityOrder(cartDetail.getQuantity() + "x");
                Food food = cartDetail.getFood();
                foodOrderCardView.setTvFoodDescription(food.getDescription());
                foodOrderCardView.setTvFoodName(food.getName());
                foodOrderCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(food.getPrice()) + "đ");
//                    foodOrderCardView.setIvFoodAvatar(cartDetail.getFood().getAvatar());

                foodsContainer.addView(foodOrderCardView);
            }
            paymentHistoryView.setTvRestaurantName(restaurant.getName());
            paymentHistoryView.setTvPaymentTotal(PriceUtil.formatNumber(paymentPending.getTotal()) + "đ");
            int totalDishes = dbHelper.cartDao.getTotalDishes(cart.getId());
            paymentHistoryView.setTvPaymentFoodQuantity(totalDishes + " món");
            paymentHistoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RestaurantOrderManagementActivity.this, OrderActivity.class);
                    intent.putExtra("paymentPendingId", paymentPending.getId());
                    startActivityForResult(intent, REQUEST_CHANGE_STATUS_PAYMENT);
                }
            });



            paymentsContainer.addView(paymentHistoryView);


        }
    }
}