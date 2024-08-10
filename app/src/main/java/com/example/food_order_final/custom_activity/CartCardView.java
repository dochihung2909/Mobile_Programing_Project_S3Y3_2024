package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;

public class CartCardView extends RelativeLayout {

    private TextView tvTotalPrice, tvTotalDishes, tvRestaurantName;

    private ImageButton btnDeleteCart;

    public CartCardView(Context context) {
        super(context);
        init(context);
    }

    public CartCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CartCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CartCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_cart_card, this, true);

        tvRestaurantName = view.findViewById(R.id.tvRestaurantName);
        tvTotalDishes = view.findViewById(R.id.tvTotalDishes);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnDeleteCart = view.findViewById(R.id.btnDeleteCart);

        btnDeleteCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setTvTotalPrice(double totalPrice) {
        this.tvTotalPrice.setText(String.valueOf(totalPrice));
    }

    public void setTvTotalDishes(int totalDishes) {
        this.tvTotalDishes.setText(String.valueOf(totalDishes));
    }

    public void setTvRestaurantName(String tvRestaurantName) {
        this.tvRestaurantName.setText(tvRestaurantName);
    }
}
