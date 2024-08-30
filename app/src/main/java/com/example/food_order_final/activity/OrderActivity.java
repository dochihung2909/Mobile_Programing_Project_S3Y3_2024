package com.example.food_order_final.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.FoodCardView;
import com.example.food_order_final.custom_activity.FoodOrderCardView;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.PaymentPendingDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.PaymentMethod;
import com.example.food_order_final.models.PaymentPending;
import com.example.food_order_final.models.PaymentStatus;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LocationUtil;
import com.example.food_order_final.util.PriceUtil;

import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements LocationListener {

    private TextView tvPaymentMethod, tvFinalTotalPrice, tvTotalDishPrice, tvShipPrice, tvRestaurantName, tvUserFullName, tvUserPhoneNumber, tvUserAddress, tvNumberOfDishes;
    private LinearLayout foodsLayout;
    private EditText etOrderNote;
    private RadioGroup radioGroupPaymentMethod;
    private Button btnOrderSubmit, btnShowAllFood;
    private ImageButton btnBack;
    private int cartId = -1;
    private int paymentPendingId = -1;
    private RadioButton  cashPaymentMethod, momoPaymentMethod;

    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private PaymentPending paymentPending;
    private String[] statusString = {"Nhận đơn", "Lấy hàng", "Giao hàng", "Thành công", "Thất bại"};
    private String[] methodString = {"Tiền mặt", "Momo"};

    private Cart cart;

    private double finalTotalPrice = 0;
    private boolean isShowAll = false;
    private int currentStatus = -1;
    private DatabaseHelper dbHelper = new DatabaseHelper(OrderActivity.this);
    private boolean isOwner = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        this.cartId = getIntent().getIntExtra("cartId", -1);
        paymentPendingId = getIntent().getIntExtra("paymentPendingId", -1);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUserRole = sharedPreferences.getString("currentUserRole", "None");
        Toast.makeText(this, "" + currentUserRole, Toast.LENGTH_SHORT).show();
        String currentUsername = sharedPreferences.getString("username", "Guest");

        if (paymentPendingId != -1) {
            paymentPending = dbHelper.paymentPendingDao.getPaymentPendingById(paymentPendingId);
            etOrderNote.setText(paymentPending.getNote());
            etOrderNote.setEnabled(false);
            cartId = paymentPending.getCart().getId();
            radioGroupPaymentMethod.setVisibility(View.GONE);
            tvPaymentMethod.setVisibility(View.VISIBLE);
            tvPaymentMethod.setText(methodString[paymentPending.getPaymentMethod().getMethod()]);
            currentStatus = paymentPending.getPaymentStatus().getStatus();
            btnOrderSubmit.setText(statusString[currentStatus]);

            if (currentUserRole.equals("User") || currentUserRole.equals("Owner")) {
                User currentUser = dbHelper.userDao.getUserByUsername(currentUsername);
                if (currentUser.getId() != paymentPending.getCart().getRestaurant().getOwner().getId()) {
                    if (paymentPending.getPaymentStatus().getStatus() == 3) {
                        btnOrderSubmit.setText("Đánh giá đơn hàng");
                    } else {
                        btnOrderSubmit.setText("Huỷ đơn");
                    }
                    isOwner = false;
                }
            }
        }
        if (cartId != -1) {
            if (paymentPendingId != -1) {
                cart = paymentPending.getCart();
            } else {
                cart = dbHelper.cartDao.getCartById(cartId);
            }
            Restaurant restaurant = cart.getRestaurant();
            List<CartDetail> cartDetails = dbHelper.cartDetailDao.getAllCartDetailInCart(cartId);

            double totalDishAmount = dbHelper.cartDao.getTotalAmountByCartId(cartId);
            double shipPrice = 16000;
            int totalDishes = dbHelper.cartDao.getTotalDishes(cartId);
            User currentUser = cart.getUser();
            tvUserFullName.setText(currentUser.getFullName());

            LocationUtil.getLocation(OrderActivity.this);
            tvUserPhoneNumber.setText(currentUser.getPhoneNumber());
            tvNumberOfDishes.setText(String.format("Tổng số món ăn (%s món)", totalDishes));
            tvTotalDishPrice.setText(PriceUtil.formatNumber(totalDishAmount) + "đ");
            tvShipPrice.setText(PriceUtil.formatNumber(shipPrice) + "đ");
            tvRestaurantName.setText(restaurant.getName());
            finalTotalPrice = totalDishAmount + shipPrice;
            tvFinalTotalPrice.setText(PriceUtil.formatNumber(finalTotalPrice) + "đ");
            Toast.makeText(this, "" + cartDetails.size(), Toast.LENGTH_SHORT).show();
            if (cartDetails.size() > 1) {
                btnShowAllFood.setVisibility(View.VISIBLE);
            }
            addFoodToContainer(cartDetails.get(0));

            btnShowAllFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShowAll = !isShowAll;
                    if (isShowAll) {
                        foodsLayout.removeAllViews();
                        addFoodToContainer(cartDetails.get(0));
                        btnShowAllFood.setText("Hiển thị thêm");
                    } else {
                        btnShowAllFood.setText("Thu gọn");
                        foodsLayout.removeAllViews();
                        for (CartDetail cartDetail: cartDetails) {
                            addFoodToContainer(cartDetail);
                        }
                    }

                }
            });

            if (paymentPendingId != -1) {
                if (currentStatus < 3) {
                    btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // change payment pending status to confirm
                            // if confirm change to delivery
                            // if delivery change to finish
                            if (isOwner) {
                                PaymentStatus status = paymentPending.getPaymentStatus();
                                if (currentStatus < 3) {
                                    status = PaymentStatus.fromStatus(status.getStatus() + 1);
                                    boolean update = dbHelper.paymentPendingDao.changePaymentPendingStatus(paymentPendingId, status);
                                    if (update) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
                                        alertDialog.setTitle(statusString[currentStatus] + " Thành công");
                                        alertDialog.setMessage("Trở về trang đơn hàng");
                                        alertDialog.setPositiveButton("Trở về", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent returnIntent = new Intent();
                                                setResult(RESULT_OK, returnIntent);
                                                finish();
                                            }
                                        }).show();
                                    }
                                }
                            } else {
                                if (paymentPending.getPaymentStatus().getStatus() != 3) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
                                    alertDialog.setTitle("Huỷ đơn hàng");
                                    alertDialog.setMessage("Bạn có chắc muốn huỷ đơn hàng này");
                                    alertDialog.setPositiveButton("Huỷ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PaymentStatus status = PaymentStatus.fromStatus(4);
                                            boolean update = dbHelper.paymentPendingDao.changePaymentPendingStatus(paymentPendingId, status);
                                            if (update) {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
                                                alertDialog.setTitle("Huỷ Thành công");
                                                alertDialog.setMessage("Trở về trang đơn hàng");
                                                alertDialog.setPositiveButton("Trở về", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent returnIntent = new Intent();
                                                        setResult(RESULT_OK, returnIntent);
                                                        finish();
                                                    }
                                                }).show();
                                            }

                                        }
                                    }).setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent returnIntent = new Intent();
                                            setResult(RESULT_OK, returnIntent);
                                            finish();
                                        }
                                    }).show();
                                } else {
                                    Intent intent = new Intent(OrderActivity.this, RatingActivity.class);
                                    intent.putExtra("paymentPendingId", paymentPendingId);
                                    startActivity(intent);
                                }
                            }

                        }
                    });
                } else {
                    btnOrderSubmit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
                }

            } else {
                btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note = String.valueOf(etOrderNote.getText());
                        PaymentPending paymentPending = new PaymentPending(PaymentStatus.PENDING, cart, finalTotalPrice, paymentMethod, note);
                        PaymentPendingDao paymentPendingDao = new PaymentPendingDao(dbHelper);
                        paymentPendingDao.insertPaymentPending(paymentPending);
                        dbHelper.cartDao.updateCartStatus(cartId);
                        Intent intent = new Intent(OrderActivity.this, PaymentSuccess.class);
                        startActivity(intent);
                    }
                });
            }

        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroupPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (cashPaymentMethod.isChecked()) {
                    paymentMethod = PaymentMethod.CASH;
                } else if (momoPaymentMethod.isChecked()) {
                    paymentMethod = PaymentMethod.E_WALLET;
                }
            }
        });
    }

    private void init() {
        tvFinalTotalPrice = findViewById(R.id.tvFinalTotalPrice);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvShipPrice = findViewById(R.id.tvShipPrice);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvUserPhoneNumber = findViewById(R.id.tvUserPhoneNumber);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        foodsLayout = findViewById(R.id.foodsLayout);
        etOrderNote = findViewById(R.id.etOrderNote);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        btnOrderSubmit = findViewById(R.id.btnOrderSubmit);
        btnBack = findViewById(R.id.btnBack);
        tvNumberOfDishes = findViewById(R.id.tvNumberOfDishes);
        btnShowAllFood = findViewById(R.id.btnShowAllFood);
        tvTotalDishPrice = findViewById(R.id.tvTotalDishPrice);
        cashPaymentMethod = findViewById(R.id.cashPaymentMethod);
        momoPaymentMethod = findViewById(R.id.momoPaymentMethod);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
    }

    private void addFoodToContainer(CartDetail cartDetail) {
        Food food = cartDetail.getFood();
        FoodOrderCardView foodOrderCardView = new FoodOrderCardView(OrderActivity.this);
        foodOrderCardView.setTvFoodName(food.getName());
        foodOrderCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(cartDetail.getPrice() * cartDetail.getQuantity()));
        foodOrderCardView.setFoodQuantityOrder(String.valueOf(cartDetail.getQuantity()) + "x");
        double discount = food.getDiscount();
        if (discount > 0) {
            TextView defaulPrice = foodOrderCardView.findViewById(R.id.tvFoodDefaultPrice);
            defaulPrice.setVisibility(View.VISIBLE);
            defaulPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            foodOrderCardView.setTvFoodDefaultPrice(PriceUtil.formatNumber(food.getPrice()) + "đ");
        }
        foodOrderCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(food.getPrice() - discount) + "đ");
        foodOrderCardView.setIvFoodAvatar(food.getAvatar());
        foodOrderCardView.setTvFoodDescription(food.getDescription());
        foodsLayout.addView(foodOrderCardView);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(MainActivity.this, "" + location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
            List<Address> addressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addressess.get(0).getAddressLine(0);
            tvUserAddress.setText(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}