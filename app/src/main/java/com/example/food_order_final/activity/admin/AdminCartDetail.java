package com.example.food_order_final.activity.admin;

import static com.android.volley.VolleyLog.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminCartDetailAdapter;
import com.example.food_order_final.adapter.AdminFoodAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCartDetail extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Cart selectedCart;
    private ImageButton btnBackToMain, btnEditCartSave;
    private TextInputLayout inputLayoutEditCartId;
    private Spinner spnEditCartUser, spnEditCartRestaurant;
    private LinearLayout btnEditCartAddFood;
    private ListView lvEditCartDetailEdit;
    private EditText edtEditCartId;
    private CheckBox cbEditCartStatus;
    private Map<String, Restaurant> restaurantMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_cart_detail);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadAllUsers();
        loadAllRestaurants();
        checkForEditCart();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);
        btnEditCartSave = (ImageButton) findViewById(R.id.btnEditCartSave);

        inputLayoutEditCartId = (TextInputLayout) findViewById(R.id.inputLayoutEditCartId);

        spnEditCartUser = (Spinner) findViewById(R.id.spnEditCartUser);
        spnEditCartRestaurant = (Spinner) findViewById(R.id.spnEditCartRestaurant);

        btnEditCartAddFood = (LinearLayout) findViewById(R.id.btnEditCartAddFood);

        lvEditCartDetailEdit = (ListView) findViewById(R.id.lvEditCartDetailEdit);

        edtEditCartId = (EditText) findViewById(R.id.edtEditCartId);

        cbEditCartStatus = (CheckBox) findViewById(R.id.cbEditCartStatus);
    }

    private void loadAllUsers() {
        List<User> users = dbHelper.userDao.getAllUsers();
        List<String> userNames = new ArrayList<>();

        for (User user : users) {
            userNames.add(user.getUsername());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditCartUser.setAdapter(adapter);
    }

    private void loadAllRestaurants() {
        List<Restaurant> restaurants = dbHelper.resDao.getAllRestaurants();
        List<String> restaurantInfo = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            String info = restaurant.getName() + " - " + restaurant.getAddress();
            restaurantInfo.add(info);
            restaurantMap.put(info, restaurant);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, restaurantInfo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditCartRestaurant.setAdapter(adapter);
    }

    private void loadCartDetail(Cart cart) {
        List<CartDetail> cartDetails = dbHelper.cartDetailDao.getAllCartDetailInCart(cart.getId());
        Log.d(TAG, "Number of CartDetails: " + cartDetails.size());
        AdminCartDetailAdapter adapter = new AdminCartDetailAdapter(this, cartDetails);
        lvEditCartDetailEdit.setAdapter(adapter);
    }


    private void setOnClickListener() {
        btnBackToMain.setOnClickListener(v -> finish());

        btnEditCartSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(AdminCartDetail.this);
                String username = spnEditCartUser.getSelectedItem().toString();
                User user = dbHelper.userDao.getUserByUsername(username);
                String restaurantInfo = spnEditCartRestaurant.getSelectedItem().toString();
                int status = cbEditCartStatus.isChecked() ?  1 : 0;
                Restaurant restaurant = restaurantMap.get(restaurantInfo);

                if (selectedCart == null) {
                    Cart cart = new Cart(user, restaurant, status);
                    dbHelper.cartDao.insertCart(cart);
                    Toast.makeText(AdminCartDetail.this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    selectedCart.setUser(user);
                    selectedCart.setRestaurant(restaurant);
                    selectedCart.setStatus(status);
                    loadCartDetail(selectedCart);
                    dbHelper.cartDao.updateCart(selectedCart);
                    Toast.makeText(AdminCartDetail.this, "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        });

        btnEditCartAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void checkForEditCart() {
        Intent previousIntent = getIntent();

        int cartId = previousIntent.getIntExtra("cart_id", -1);
        selectedCart = dbHelper.cartDao.getCartById(cartId);

        if (selectedCart != null) {
            loadCartDetail(selectedCart);
            inputLayoutEditCartId.setVisibility(View.VISIBLE);
            inputLayoutEditCartId.setEnabled(false);

            edtEditCartId.setText(String.valueOf(selectedCart.getId()));
            cbEditCartStatus.setChecked(selectedCart.getStatus() == 1);

            List<User> users = dbHelper.userDao.getAllUsers();
            List<String> userNames = new ArrayList<>();
            for (User user : users) {
                userNames.add(user.getUsername());
            }
            String cartUser = selectedCart.getUser().getUsername();
            int positionUser = userNames.indexOf(cartUser);
            if(positionUser != -1)
                spnEditCartUser.setSelection(positionUser);
            else
                spnEditCartUser.setSelection(0);

            List<Restaurant> restaurants = dbHelper.resDao.getAllRestaurants();
            List<String> restaurantInfo = new ArrayList<>();
            for (Restaurant restaurant : restaurants) {
                String info = restaurant.getName() + " - " + restaurant.getAddress();
                restaurantInfo.add(info);
            }
            Restaurant selectedRestaurant = selectedCart.getRestaurant();
            String cartRestaurantInfo = selectedRestaurant.getName() + " - " + selectedRestaurant.getAddress();
            int positionRestaurant = restaurantInfo.indexOf(cartRestaurantInfo);
            if (positionRestaurant != -1) {
                spnEditCartRestaurant.setSelection(positionRestaurant);
            } else {
                spnEditCartRestaurant.setSelection(0);
            }
        } else {
            inputLayoutEditCartId.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}