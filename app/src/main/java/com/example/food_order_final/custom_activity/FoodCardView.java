package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FoodCardView extends LinearLayout {

    TextView tvFoodName, tvFoodSold, tvFoodLiked, tvFoodDiscountPrice, tvFoodDefaultPrice, tvFoodDescription, btnAddToCart;
    int foodId;

    private Food food;
    ImageView ivFoodAvatar;

    private CartDao cartDao;

    private UserDao userDao;

    private RestaurantDao restaurantDao;

    public FoodCardView(Context context) {
        super(context);
        init(context);
    }

    public FoodCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public FoodCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public FoodCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_card, this, true);

        tvFoodName = view.findViewById(R.id.tvFoodName);
        tvFoodSold = view.findViewById(R.id.tvFoodSold);
        tvFoodLiked = view.findViewById(R.id.tvFoodLiked);
        tvFoodDiscountPrice = view.findViewById(R.id.tvFoodDiscountPrice);
        tvFoodDefaultPrice = view.findViewById(R.id.tvFoodDefaultPrice);
        tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        ivFoodAvatar = view.findViewById(R.id.ivFoodAvatar);

        btnAddToCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                cartDao = new CartDao(dbHelper, userDao, restaurantDao);
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE); 
                String username = sharedPreferences.getString("username", "Guest");
                userDao = new UserDao(dbHelper, new RoleDao(dbHelper));

                if (username != "Guest") { 
                    User currentUser = userDao.getUserByUsername(username);
                    Cart cart = cartDao.addToCard(currentUser, food.getRestaurant(), food, 1);
                    Toast.makeText(context, "" + cart.getId(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getTvFoodName() {
        return tvFoodName.getText().toString();
    }

    public void setTvFoodName(String tvFoodName) {
        this.tvFoodName.setText(tvFoodName);
    }

    public String getTvFoodSold() {
        return tvFoodSold.getText().toString();
    }

    public void setTvFoodSold(String tvFoodSold) {
        this.tvFoodSold.setText(tvFoodSold);
    }

    public String getTvFoodLiked() {
        return tvFoodLiked.getText().toString();
    }

    public void setTvFoodLiked(String tvFoodLiked) {
        this.tvFoodLiked.setText(tvFoodLiked);
    }

    public String getTvFoodDiscountPrice() {
        return (tvFoodDiscountPrice.getText().toString());
    }

    public void setTvFoodDiscountPrice(double tvFoodDiscountPrice) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        String formattedNumber = decimalFormat.format(tvFoodDiscountPrice);
        this.tvFoodDiscountPrice.setText(formattedNumber);
    }

    public String getTvFoodDefaultPrice() {
        return tvFoodDefaultPrice.getText().toString();
    }

    public void setTvFoodDefaultPrice(int tvFoodDefaultPrice) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        String formattedNumber = decimalFormat.format(tvFoodDefaultPrice);
        this.tvFoodDefaultPrice.setText(formattedNumber);
    }

    public String getTvFoodDescription() {
        return tvFoodDescription.getText().toString();
    }

    public void setTvFoodDescription(String tvFoodDescription) {
        this.tvFoodDescription.setText(tvFoodDescription);
    }

    public void setIvFoodAvatar(int ivFoodAvatar) {
        this.ivFoodAvatar.setImageDrawable(getResources().getDrawable(ivFoodAvatar));
    }
}
