package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FoodCardView extends LinearLayout {

    TextView tvFoodName, tvFoodSold, tvFoodLiked, tvFoodDiscountPrice, tvFoodDefaultPrice, tvFoodDescription, btnAddToCard;
    ImageView ivFoodAvatar;

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
        btnAddToCard = view.findViewById(R.id.btnAddToCard);
        ivFoodAvatar = view.findViewById(R.id.ivFoodAvatar);
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
