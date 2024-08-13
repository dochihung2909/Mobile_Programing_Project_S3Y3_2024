package com.example.food_order_final.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;

public class OrderActivity extends AppCompatActivity  {

    private TextView tvTotalPrice, tvTotalDishPrice, tvShipPrice, tvRestaurantName, tvUserFullName, tvUserPhoneNumber, tvUserAddress;
    private LinearLayout foodsLayout;
    private EditText etOrderNote;
    private RadioGroup radioGroupPaymentMethod;
    private Button btnOrderSubmit;
    private ImageButton btnBack;
    private int cartId = -1;

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
        if (cartId != -1 ) {
            DatabaseHelper dbHelper = new DatabaseHelper(OrderActivity.this);
            CartDao cartDao = new CartDao(dbHelper);
            Cart cart = cartDao.getCartById(cartId);
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
                RadioButton radioButton = (RadioButton) radioGroupPaymentMethod.findViewById(checkedId);
            }
        });

        btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPaymentMethodId = radioGroupPaymentMethod.getCheckedRadioButtonId();


            }
        });
    }

    private void init() {
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
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
    }
}