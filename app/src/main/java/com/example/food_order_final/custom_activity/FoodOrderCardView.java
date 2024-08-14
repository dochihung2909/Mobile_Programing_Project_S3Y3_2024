package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;

public class FoodOrderCardView extends LinearLayout {

    private TextView tvFoodName, tvFoodDescription, foodQuantityOrder, tvFoodDiscountPrice, tvFoodDefaultPrice;
    private ImageView ivFoodAvatar;

    public FoodOrderCardView(Context context) {
        super(context);
        init(context);
    }

    public FoodOrderCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FoodOrderCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FoodOrderCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.food_order_card, this, true);
        tvFoodName = view.findViewById(R.id.tvFoodName);
        tvFoodDefaultPrice = view.findViewById(R.id.tvFoodDefaultPrice);
        tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
        foodQuantityOrder = view.findViewById(R.id.foodQuantityOrder);
        tvFoodDiscountPrice = view.findViewById(R.id.tvFoodDiscountPrice);
        ivFoodAvatar = view.findViewById(R.id.ivFoodAvatar);
    }

    public void setTvFoodName(String tvFoodName) {
        this.tvFoodName.setText(tvFoodName);
    }

    public void setTvFoodDescription(String tvFoodDescription) {
        this.tvFoodDescription.setText(tvFoodDescription);
    }

    public void setFoodQuantityOrder(String foodQuantityOrder) {
        this.foodQuantityOrder.setText(foodQuantityOrder);
    }

    public void setTvFoodDiscountPrice(String tvFoodDiscountPrice) {
        this.tvFoodDiscountPrice.setText(tvFoodDiscountPrice);
    }

    public void setTvFoodDefaultPrice(String tvFoodDefaultPrice) {
        this.tvFoodDefaultPrice.setText(tvFoodDefaultPrice);
    }

    public void setIvFoodAvatar(int ivFoodAvatar) {
        this.ivFoodAvatar.setImageResource(ivFoodAvatar);
    }
}
