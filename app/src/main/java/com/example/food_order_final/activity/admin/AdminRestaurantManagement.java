package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminRestaurantAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;

import java.util.List;

public class AdminRestaurantManagement extends AppCompatActivity {
    private ImageButton btnBackToMain;
    private Button btnAdminAddRestaurant;
    private ListView lvAdminRestaurant;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_restaurant_management);

        dbHelper = new DatabaseHelper(this);
        initWidgets();
        loadRestaurants();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setOnClickListener() {
        btnAdminAddRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRestaurantManagement.this, AdminRestaurantDetail.class);
            startActivity(intent);
        });

        lvAdminRestaurant.setOnItemClickListener((parent, view, position, id) -> {
            Restaurant selectedRestaurant = (Restaurant) parent.getItemAtPosition(position);
            Intent intent = new Intent(AdminRestaurantManagement.this, AdminRestaurantDetail.class);
            intent.putExtra("restaurant_id", selectedRestaurant.getId());
            startActivity(intent);
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    public void loadRestaurants() {
        List<Restaurant> restaurants = dbHelper.resDao.getAllRestaurants();
        AdminRestaurantAdapter adapter = new AdminRestaurantAdapter(this, restaurants);
        lvAdminRestaurant.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnAdminAddRestaurant = findViewById(R.id.btnAdminAddRes);
        lvAdminRestaurant = findViewById(R.id.lvAdminRestaurant);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRestaurants();
    }
}
