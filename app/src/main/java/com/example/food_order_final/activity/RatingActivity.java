package com.example.food_order_final.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.PaymentPending;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.ReviewFood;
import com.example.food_order_final.models.ReviewRestaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    ImageView ivRestaurantAvatar, ivUploadedFood;
    TextView tvRestaurantName, tvSubtitleFood, tvSubtitleRestaurant;
    LinearLayout ratingFoodLayout, ratingRestaurantLayout;
    RatingBar rbFood, rbRestaurant;
    EditText etCommentFood, etCommentRestaurant;
    Button btnUploadImageFood, btnSubmit;
    DatabaseHelper dbHelper;
    private int paymentPendingId = -1;
    private int SELECT_PICTURE = 200;
    private boolean isUpload = false;
    private String cloudinaryPath;
    private Uri imagePath;
    private static boolean mediaManager = false;

    private static final String TAG = "EditRestaurantActivity Upload ###";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating);
        initWidgets();
        initConfigCloudinary();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(RatingActivity.this);

        paymentPendingId = getIntent().getIntExtra("paymentPendingId", -1);
        if (paymentPendingId != -1) {
            PaymentPending paymentPending = dbHelper.paymentPendingDao.getPaymentPendingById(paymentPendingId);
            Cart cart = paymentPending.getCart();
            Restaurant restaurant = cart.getRestaurant();
            List<CartDetail> cartDetails = dbHelper.cartDetailDao.getAllCartDetailInCart(cart.getId());
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String currentUsername = sharedPreferences.getString("username", "Guest");
            User user = dbHelper.userDao.getUserByUsername(currentUsername);

            LoadImageUtil.loadImage(ivRestaurantAvatar, restaurant.getAvatar());
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUpload) {
                        MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                                Log.d(TAG, "onStart: " + "started");
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                                Log.d(TAG, "onProgress: " + "uploading");
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                cloudinaryPath = (String) resultData.get("url");
                                rating();
                                Log.d(TAG, "onSuccess: " + resultData.get("url"));
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                Log.d(TAG, "onError: " + error);
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                                Log.d(TAG, "onReschedule: " + error);
                            }
                        }).dispatch();
                    } else {
                        rating();
                    }
                }
            });

        }



    }

    private void initWidgets() {
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
        ivUploadedFood = findViewById(R.id.ivUploadedFood);

        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvSubtitleFood = findViewById(R.id.tvSubtitleFood);
        tvSubtitleRestaurant = findViewById(R.id.tvSubtitleRestaurant);

        ratingFoodLayout = findViewById(R.id.ratingFoodLayout);
        ratingRestaurantLayout = findViewById(R.id.ratingRestaurantLayout);

        rbFood = findViewById(R.id.rbFood);
        rbRestaurant = findViewById(R.id.rbRestaurant);

        etCommentFood = findViewById(R.id.etCommentFood);
        etCommentRestaurant = findViewById(R.id.etCommentRestaurant);

        btnUploadImageFood = findViewById(R.id.btnUploadImageFood);
        btnSubmit = findViewById(R.id.btnSubmit);
    }


    private void showThanksDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cảm ơn bạn !")
                .setMessage("Đánh giá của bạn đã thành công.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(RatingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RatingActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, SELECT_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == SELECT_PICTURE && data != null && data.getData() != null) {
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(ivUploadedFood);

        }else {
            Toast.makeText(RatingActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void initConfigCloudinary() {

        if (!mediaManager) {
            Map config = new HashMap();
            config.put("cloud_name", "dpacbtjva");
            config.put("api_key", "271295249232522");
            config.put("api_secret", "E5p9fqKGnvRRg-gtU8HxUpynWvI");
            mediaManager = true;
            MediaManager.init(this, config);
        }
    }

    private void rating() {
        String foodComment = String.valueOf(etCommentFood.getText());
        double foodRating = rbFood.getRating();


//        ReviewFood reviewFood = new ReviewFood(foodComment, foodRating, cloudinaryPath, );
    }

}