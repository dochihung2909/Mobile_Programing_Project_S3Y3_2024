package com.example.food_order_final.activity.admin;

import static com.android.volley.VolleyLog.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminFoodAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
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
    private LinearLayout btnEditResAddFood, linearLayoutEditResFood;
    private ListView lvEditResFoods;
    private String avatarUrl = null;
    private ScrollView scrollViewEditResInfo;

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
        btnEditResAddFood = findViewById(R.id.btnEditResAddFood);

        imgEditResAvatar = findViewById(R.id.imgEditResAvatar);

        lvEditResFoods = findViewById(R.id.lvEditResFoods);

        linearLayoutEditResFood = findViewById(R.id.linearLayoutEditResFood);

        scrollViewEditResInfo = findViewById(R.id.scrollViewEditResInfo);
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
        List<User> users = dbHelper.userDao.getUsersByRole("Owner");
//        List<User> users = dbHelper.userDao.getAllUsers();
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
            linearLayoutEditResFood.setVisibility(View.VISIBLE);
            edtEditRestaurantId.setText(String.valueOf(selectedRestaurant.getId()));
            edtEditRestaurantName.setText(selectedRestaurant.getName());
            edtEditRestaurantAddress.setText(selectedRestaurant.getAddress());
            edtEditRestaurantPhone.setText(selectedRestaurant.getPhoneNumber());
            edtEditRestaurantRating.setText(String.valueOf(selectedRestaurant.getRating()));
            String avatarUrl = selectedRestaurant.getAvatar();
            if (avatarUrl != null) {
                LoadImageUtil.loadImage(imgEditResAvatar, avatarUrl); // Load avatar
            }
            loadAllFoods(selectedRestaurant.getId()); // Load foods

            edtEditRestaurantId.setEnabled(false);

            Log.d(TAG, "Category: " + selectedRestaurant.getCategory().getName());
            Log.d(TAG, "Owner: " + selectedRestaurant.getOwner().getUsername());
            if (selectedRestaurant.getCategory() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditResCate.getAdapter();
                int position = adapter.getPosition(selectedRestaurant.getCategory().getName());
                spnEditResCate.setSelection(position);
                Log.d(TAG, "Position: " + position);
            }
            if (selectedRestaurant.getOwner() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnEditResOwner.getAdapter();
                int position = adapter.getPosition(selectedRestaurant.getOwner().getUsername());
                spnEditResOwner.setSelection(position);
                Log.d(TAG, "Position: " + position);

            }
        } else {
            btnEditRestaurantDelete.setVisibility(View.GONE);
            inputLayoutEditRestaurantId.setVisibility(View.GONE);
            linearLayoutEditResFood.setVisibility(View.GONE);
            scrollViewEditResInfo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private void setOnClickListener() {
        btnEditRestaurantSave.setOnClickListener(v -> {
            String restaurantName = edtEditRestaurantName.getText().toString().trim();
            String restaurantAddress = edtEditRestaurantAddress.getText().toString().trim();
            String restaurantPhone = edtEditRestaurantPhone.getText().toString().trim();
            String resCateName = spnEditResCate.getSelectedItem().toString();
            RestaurantCategory restaurantCategory = dbHelper.resCateDao.getRestaurantCategoryByName(resCateName);
            Log.d("AdminRestaurantDetail", "Category: " + restaurantCategory);
            Log.d("AdminRestaurantDetail", "Category name: " + restaurantCategory.getName());
            String ownerUsername = spnEditResOwner.getSelectedItem().toString();
            User owner = dbHelper.userDao.getUserByUsername(ownerUsername);
            Log.d("AdminRestaurantDetail", "Owner: " + owner);
            Log.d("AdminRestaurantDetail", "Owner username: " + owner.getUsername());

            Log.d(TAG, "Selected owner: " + owner.getUsername());
            Log.d(TAG, "Selected category: " + restaurantCategory.getName());
            if (selectedRestaurant == null) {
                Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, restaurantPhone, restaurantCategory, avatarUrl, owner);
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
                Log.d(TAG, "Category (Before save): " + selectedRestaurant.getCategory().getName());
                selectedRestaurant.setCategory(restaurantCategory);
                Log.d(TAG, "Category (After save): " + selectedRestaurant.getCategory().getName());
                Log.d(TAG, "Avatar (Before save): " + selectedRestaurant.getAvatar());
                selectedRestaurant.setAvatar(avatarUrl);
                Log.d(TAG, "Avatar (After save): " + selectedRestaurant.getAvatar());

                Log.d(TAG, "Owner (Before save): " + selectedRestaurant.getOwner().getUsername());
                selectedRestaurant.setOwner(owner);
                Log.d(TAG, "Owner (After save): " + selectedRestaurant.getOwner().getUsername());

                int result = dbHelper.resDao.updateRestaurant(selectedRestaurant);
                Log.d("AdminRestaurantDetail", "Restaurannt Name: " + selectedRestaurant.getName());
                Log.d("AdminRestaurantDetail", "Restaurannt Address: " + selectedRestaurant.getAddress());
                Log.d("AdminRestaurantDetail", "Restaurannt Phone: " + selectedRestaurant.getPhoneNumber());
                Log.d("AdminRestaurantDetail", "Restaurannt Category: " + selectedRestaurant.getCategory().getName());
                Log.d("AdminRestaurantDetail", "Restaurannt Owner: " + selectedRestaurant.getOwner().getUsername());
                Log.d("AdminRestaurantDetail", "Restaurannt Avatar: " + selectedRestaurant.getAvatar());
                if (result != -1) {
                    Toast.makeText(AdminRestaurantDetail.this, "Cập nhật Nhà hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(AdminRestaurantDetail.this, "Cập nhật Nhà hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEditResAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRestaurantDetail.this, AdminFoodDetail.class);
                intent.putExtra("restaurant_id_fromResDetail", selectedRestaurant.getId());
                startActivity(intent);
            }
        });

        btnEditRestaurantDelete.setOnClickListener(v -> showDeleteConfirmDialog());

        btnBackToMain.setOnClickListener(v -> finish());

        imgEditResAvatar.setOnClickListener(v -> showInputAvatarUrl());
    }

    private void showInputAvatarUrl() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_avatar_url, null);
        final EditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter image URL");
        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            avatarUrl = edtImageUrl.getText().toString();
            LoadImageUtil.loadImage(imgEditResAvatar, avatarUrl);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
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

    private void loadAllFoods(int restaurantId) {
        List<Food> foods = dbHelper.resDao.getAllFoodsInRestaurant(restaurantId);
        AdminFoodAdapter adapter = new AdminFoodAdapter(this, foods);
        lvEditResFoods.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent previousIntent = getIntent();
        int restaurantId = previousIntent.getIntExtra("restaurant_id", -1);
        selectedRestaurant = dbHelper.resDao.getRestaurantById(restaurantId);
        if (selectedRestaurant != null)
            loadAllFoods(selectedRestaurant.getId());
    }
}
