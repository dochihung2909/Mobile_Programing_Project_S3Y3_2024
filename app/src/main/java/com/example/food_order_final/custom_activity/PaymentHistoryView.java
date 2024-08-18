package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;

import java.util.zip.Inflater;

public class PaymentHistoryView extends LinearLayout {
    private TextView tvRestaurantName, tvPaymentStatus, tvPaymentFoodQuantity, tvPaymentTotal;
    private LinearLayout foodsContainer;

    public PaymentHistoryView(Context context) {
        super(context);
        init(context);
    }

    public PaymentHistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaymentHistoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PaymentHistoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payment_history_card, this, true);

        tvPaymentStatus = view.findViewById(R.id.tvPaymentStatus);
        tvPaymentTotal = view.findViewById(R.id.tvPaymentTotal);
        tvPaymentFoodQuantity = view.findViewById(R.id.tvPaymentFoodQuantity);
        tvRestaurantName = view.findViewById(R.id.tvRestaurantName);
        foodsContainer = view.findViewById(R.id.foodsContainer);
    }

    public void setTvRestaurantName(String tvRestaurantName) {
        this.tvRestaurantName.setText(tvRestaurantName);
    }

    public void setTvPaymentStatus(String tvPaymentStatus) {
        this.tvPaymentStatus.setText(tvPaymentStatus);
    }


    public void setTvPaymentFoodQuantity(String tvPaymentFoodQuantity) {
        this.tvPaymentFoodQuantity.setText(tvPaymentFoodQuantity);
    }

    public void setTvPaymentTotal(String tvPaymentTotal) {
        this.tvPaymentTotal.setText(tvPaymentTotal);
    }
}
