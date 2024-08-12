package com.example.food_order_final.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private LinearLayout foodsContainer;

    private TextView tvFoodDefaultPrice, tvFoodDiscountPrice;
    private Button btnOrderSubmit;
    private ImageButton btnBackToMain;
    private DatabaseHelper dbHelper;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        int cartId = getIntent().getIntExtra("cartId", 0);
        dbHelper = new DatabaseHelper(CartActivity.this);


        if (cartId != 0) {
            Toast.makeText(CartActivity.this, "" + cartId, Toast.LENGTH_LONG).show();
            FoodDao foodDao = new FoodDao(dbHelper, new FoodCategoryDao(dbHelper), new RestaurantDao(dbHelper, new RestaurantCategoryDao(dbHelper)));
            CartDetailDao cartDetailDao = new CartDetailDao(dbHelper);
            List<CartDetail> cartDetails = cartDetailDao.getAllCartDetailInCart(cartId);
            CartDao cartDao = new CartDao(dbHelper);

            for (CartDetail cartDetail: cartDetails) {
                FoodCardView foodCardView = new FoodCardView(CartActivity.this);
                Food food = foodDao.getFoodById(cartDetail.getFood().getId());
                foodCardView.setTvFoodName(food.getName());
                foodCardView.findViewById(R.id.layoutInCart).setVisibility(FoodCardView.VISIBLE);
                foodCardView.findViewById(R.id.btnAddToCart).setVisibility(FoodCardView.GONE);
                foodCardView.setTvFoodDiscountPrice(cartDetail.getPrice());
                TextView tvQuantity = foodCardView.findViewById(R.id.tvQuantity);
                tvQuantity.setText(String.valueOf(cartDetail.getQuantity()));
                foodCardView.setQuantity(cartDetail.getQuantity());
                TextView btnMinusQuantity = foodCardView.findViewById(R.id.btnMinus);

                double totalAmount = cartDao.getTotalAmountByCartId(cartId);
                tvFoodDiscountPrice.setText(formatNumber(totalAmount) + "đ");

                Toast.makeText(CartActivity.this, "" + foodCardView.getQuantity(), Toast.LENGTH_SHORT).show();
                btnMinusQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity = foodCardView.getQuantity();
                        if (quantity > 1) {
                            quantity = quantity - 1;
                            resetUI(quantity, cartDetail, cartDao, foodCardView, cartDetailDao, tvQuantity, cartId);
                        } else {
                            new AlertDialog.Builder(CartActivity.this)
                                    .setTitle("Xoá khỏi giỏ hàng")
                                    .setMessage("Bạn có muốn xoá món ăn này khỏi giỏ hàng")
                                    .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cartDetailDao.deleteCartDetail(cartDetail.getId());
                                            foodsContainer.removeView(foodCardView);
                                            double totalAmount = cartDao.getTotalAmount(cartId);
                                            tvFoodDiscountPrice.setText(formatNumber(totalAmount) + "đ");
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                });


                TextView btnPlusQuantity = foodCardView.findViewById(R.id.btnPlus);
                btnPlusQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity = foodCardView.getQuantity();
                        quantity = quantity + 1;
                        resetUI(quantity, cartDetail, cartDao, foodCardView, cartDetailDao, tvQuantity, cartId);

                    }
                });

                foodsContainer.addView(foodCardView);

                btnBackToMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, RestaurantActivity.class);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    private void init() {
        foodsContainer = findViewById(R.id.foodsContainer);
        tvFoodDefaultPrice = findViewById(R.id.tvFoodDefaultPrice);
        tvFoodDiscountPrice = findViewById(R.id.tvFoodDiscountPrice);
        btnOrderSubmit = findViewById(R.id.btnOrderSubmit);
        btnBackToMain = findViewById(R.id.btnBackToMain);
    }

    @SuppressLint("SetTextI18n")
    public void resetUI(int quantity, CartDetail cartDetail, CartDao cartDao, FoodCardView foodCardView, CartDetailDao cartDetailDao, TextView tvQuantity, int cartId) {
        foodCardView.setQuantity(quantity);
        cartDetailDao.updateFoodQuantity(quantity, cartDetail.getId());
        tvQuantity.setText(String.valueOf(quantity));
        double totalAmount = cartDetail.getPrice() * quantity;
        tvFoodDiscountPrice.setText(formatNumber(totalAmount) + "đ");
    }

    public String formatNumber(double totalAmount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        return decimalFormat.format(totalAmount);
    }
}