package com.example.food_order_final.util;

import android.util.Log;
import android.widget.ImageView;

import com.example.food_order_final.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class LoadImageUtil {

    public static void loadImage(final ImageView imageView, final String imageUrl){
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView , new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        String updatedImageUrl;
                        if (imageUrl.contains("https")){
                            updatedImageUrl = imageUrl.replace("https", "http");
                        }else{
                            updatedImageUrl = imageUrl.replace("http", "https");
                        }
                        loadImage(imageView, updatedImageUrl);
                    }
                });
    }
}
