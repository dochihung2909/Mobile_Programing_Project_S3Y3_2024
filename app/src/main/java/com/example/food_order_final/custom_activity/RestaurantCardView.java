package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.util.LoadImageUtil;

public class RestaurantCardView extends LinearLayout {

    public void setRestaurantImage(String image) {
        LoadImageUtil.loadImage(this.restaurantImage, image);
    }

    public void setRestaurantName(String name) {
        this.restaurantName.setText(name);
    }

    public void setRestaurantRating(String rating) {
        this.restaurantRating.setText(rating);
    }

    public void setRestaurantDistance(String distance) {
        this.restaurantDistance.setText(distance);
    }

    public void setRestaurantTime(String time) {
        this.restaurantTime.setText(time);
    }

    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantRating;
    private TextView restaurantDistance;
    private TextView restaurantTime;

    public RestaurantCardView(Context context) {
        super(context);
        init(context);
    }

    public RestaurantCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RestaurantCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RestaurantCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.restaurant_card, this, true);

        // Get references to the UI components
        restaurantImage = view.findViewById(R.id.imageRestaurant);
        restaurantName = view.findViewById(R.id.tvRestaurantName);
        restaurantRating = view.findViewById(R.id.tvRestaurantRating);
        restaurantDistance = view.findViewById(R.id.tvRestaurantDistance);
        restaurantTime = view.findViewById(R.id.tvRestaurantTime);
    }
}
