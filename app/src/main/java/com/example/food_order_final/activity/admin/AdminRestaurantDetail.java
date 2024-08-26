package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminRestaurantDetail extends AppCompatActivity {
    private EditText edtEditRestaurantId, edtEditRestaurantName, edtEditRestaurantAddress,
            edtEditRestaurantPhone, edtEditRestaurantRating;
    private Spinner spnEditResCate, spnEditResOwner;
    private TextInputLayout inputLayoutEditRestaurantId, inputLayoutEditResRating;
    private ImageButton btnBackToMain;
    private ImageView btnEditRestaurantSave, imgEditResAvatar;
    private Button btnEditRestaurantDelete;
    private DatabaseHelper dbHelper;
    private Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_restaurant_detail);

        dbHelper = new DatabaseHelper(this);

        initWidgets();
        loadRestaurantCategories();
        loadUser();
        checkForEditRestaurant();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        edtEditRestaurantId = findViewById(R.id.edtEditResId);
        edtEditRestaurantName = findViewById(R.id.edtEditResName);
        edtEditRestaurantAddress = findViewById(R.id.edtEditResAddress);
        edtEditRestaurantPhone = findViewById(R.id.edtEditResPhone);
        edtEditRestaurantRating = findViewById(R.id.edtEditResRating);
        spnEditResCate = findViewById(R.id.spnEditResCate);
        spnEditResOwner = findViewById(R.id.spnEditResOwner);

        inputLayoutEditRestaurantId = findViewById(R.id.inputLayoutEditResId);
        inputLayoutEditResRating = findViewById(R.id.inputLayoutEditResRating);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditRestaurantDelete = findViewById(R.id.btnEditResDelete);
        btnEditRestaurantSave = findViewById(R.id.btnEditResSave);

        imgEditResAvatar = findViewById(R.id.imgEditResAvatar);

        if (edtEditRestaurantId == null || edtEditRestaurantName == null || edtEditRestaurantAddress == null ||
                edtEditRestaurantPhone == null || edtEditRestaurantRating == null || spnEditResCate == null ||
                spnEditResOwner == null || inputLayoutEditRestaurantId == null || btnBackToMain == null ||
                btnEditRestaurantDelete == null || btnEditRestaurantSave == null || imgEditResAvatar == null) {
            throw new NullPointerException("One or more views were not found. Check your layout XML.");
        }
    }

    private void loadRestaurantCategories() {
        List<RestaurantCategory> resCates = dbHelper.resCateDao.getAllRestaurantCategories();
        List<String> resCateNames = new ArrayList<>();

        for (RestaurantCategory resCate : resCates) {
            resCateNames.add(resCate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resCateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditResCate.setAdapter(adapter);
    }

    private void loadUser() {
        List<User> users = dbHelper.userDao.getAllUsers();
        List<String> userUsernames = new ArrayList<>();
        for(User user : users) {
            userUsernames.add(user.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userUsernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditResOwner.setAdapter(adapter);
    }

    private void checkForEditRestaurant() {
        Intent previousIntent = getIntent();
        int restaurantId = previousIntent.getIntExtra("restaurant_id", -1);
        selectedRestaurant = dbHelper.resDao.getRestaurantById(restaurantId);

        if (selectedRestaurant != null) {
            inputLayoutEditRestaurantId.setVisibility(View.VISIBLE);
            inputLayoutEditResRating.setVisibility(View.VISIBLE);
            edtEditRestaurantId.setText(String.valueOf(selectedRestaurant.getId()));
            edtEditRestaurantName.setText(selectedRestaurant.getName());
            edtEditRestaurantAddress.setText(selectedRestaurant.getAddress());
            edtEditRestaurantPhone.setText(selectedRestaurant.getPhoneNumber());
            edtEditRestaurantRating.setText(String.valueOf(selectedRestaurant.getRating()));
            String avatarUrl = selectedRestaurant.getAvatar();

            Picasso.get()
                    .load(avatarUrl)
                    .into(imgEditResAvatar);

            edtEditRestaurantId.setEnabled(false);

            if (selectedRestaurant.getCategory() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditResCate.getAdapter();
                int position = adapter.getPosition(selectedRestaurant.getCategory().getName());
                spnEditResCate.setSelection(position);
            }
            if (selectedRestaurant.getOwner() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditResOwner.getAdapter();
                int position = adapter.getPosition(selectedRestaurant.getOwner().getUsername());
                spnEditResOwner.setSelection(position);
            }
        } else {
            btnEditRestaurantDelete.setVisibility(View.GONE);
            inputLayoutEditRestaurantId.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        btnEditRestaurantSave.setOnClickListener(v -> {
            String restaurantName = edtEditRestaurantName.getText().toString().trim();
            String restaurantAddress = edtEditRestaurantAddress.getText().toString().trim();
            String restaurantPhone = edtEditRestaurantPhone.getText().toString().trim();
            String resCateName = spnEditResCate.getSelectedItem().toString();
            RestaurantCategory restaurantCategory = dbHelper.resCateDao.getRestaurantCategoryByName(resCateName);
            String ownerUsername = spnEditResOwner.getSelectedItem().toString();
            User owner = dbHelper.userDao.getUserByUsername(ownerUsername);
            String avatar = selectedRestaurant.getAvatar();

            if (selectedRestaurant == null) {
                Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, restaurantPhone, restaurantCategory, avatar, owner);
                if (!dbHelper.resDao.isRestaurantExists(newRestaurant)) {
                    dbHelper.resDao.insertRestaurant(newRestaurant);
                    Toast.makeText(AdminRestaurantDetail.this, "Tạo Nhà hàng mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminRestaurantDetail.this, "Tên Nhà hàng tại vị trí đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedRestaurant.setName(restaurantName);
                selectedRestaurant.setAddress(restaurantAddress);
                selectedRestaurant.setPhoneNumber(restaurantPhone);
                selectedRestaurant.setCategory(restaurantCategory);
                selectedRestaurant.setAvatar(avatar);
                dbHelper.resDao.updateRestaurant(selectedRestaurant);
                Toast.makeText(AdminRestaurantDetail.this, "Cập nhật Nhà hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnEditRestaurantDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());
    }


    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa nhà hàng này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(AdminRestaurantDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dbHelper.resDao.deleteRestaurant(selectedRestaurant.getId());
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
