package com.example.food_order_final.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRestaurantActivity extends AppCompatActivity {

    private EditText etRestaurantName, etRestaurantAddress, etPhoneNumber, etRestaurantRating;
    private Spinner snRestaurantCategory;
    private ImageView ivRestaurantAvatar;
    private Button btnEditRestaurantSubmit, btnEditRestaurantImage;
    private ImageButton btnBack;
    private Restaurant restaurant;
    private static boolean mediaManager = false;
    private DatabaseHelper dbHelper = new DatabaseHelper(EditRestaurantActivity.this);
    private int SELECT_PICTURE = 200;

    private boolean isUpload = false;
    private String cloudinaryPath;

    private AwesomeValidation awesomeValidation;
    private RestaurantCategory restaurantCategory = null;
    private int ownerId;

    private Uri imagePath;

    private static final String TAG = "EditRestaurantActivity Upload ###";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        initConfigCloudinary();


        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        // set spinner restaurant category
        ArrayList<RestaurantCategory> restaurantCategoryArrayList = dbHelper.resCateDao.getAllRestaurantCategories();
        ArrayList<String> restaurantCategoryName = new ArrayList<>();
        for (RestaurantCategory restaurantCategory: restaurantCategoryArrayList) {
            restaurantCategoryName.add(restaurantCategory.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditRestaurantActivity.this, android.R.layout.simple_spinner_item, restaurantCategoryName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snRestaurantCategory.setAdapter(adapter);

        // Get restaurant Info
        int restaurantId = getIntent().getIntExtra("restaurantId", -1);

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        ownerId = preferences.getInt("userId", -1);

        if (restaurantId != -1) {
            restaurant = dbHelper.resDao.getRestaurantById(restaurantId);

            for (int i = 0;i < restaurantCategoryArrayList.size();i++) {
                if (restaurant.getCategory().getId() == restaurantCategoryArrayList.get(i).getId()) {
                     snRestaurantCategory.setSelection(i);
                }
            }
            initUi();
        }



        snRestaurantCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restaurantCategory = restaurantCategoryArrayList.get(position);
                if (restaurant != null) {
                    restaurant.setCategory(restaurantCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEditRestaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                isUpload = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
            }
        });

        btnEditRestaurantSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resName = etRestaurantName.getText().toString();
                String resPhoneNumber = etPhoneNumber.getText().toString();
                String resAddress = etRestaurantAddress.getText().toString();
                if (restaurantId != -1) {
                    restaurant.setAddress(resAddress);
                    restaurant.setPhoneNumber(resPhoneNumber);
                    restaurant.setName(resName);
                } else {
                    User owner = dbHelper.userDao.getUserById(ownerId);
                    restaurant = new Restaurant(resName, resAddress, resPhoneNumber, restaurantCategory, cloudinaryPath, owner);
                }


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
                            if (restaurantId != -1) {
                                restaurant.setAvatar(cloudinaryPath);
                                updateRestaurantInfo();
                            } else {
                                createRestaurant();
                            }
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
                    if (restaurantId == -1) {
                        createRestaurant();
                    } else {
                        updateRestaurantInfo();
                    }
                }
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(EditRestaurantActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditRestaurantActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, SELECT_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == SELECT_PICTURE && data != null && data.getData() != null) {
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(ivRestaurantAvatar);

        }else {
            Toast.makeText(EditRestaurantActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    protected Boolean isValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etPhoneNumber, "^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|8|9]|9[0-4|6-9])[0-9]{7}$", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.etRestaurantName, RegexTemplate.NOT_EMPTY, R.string.invalid_empty);

        awesomeValidation.addValidation(this, R.id.etRestaurantAddress, RegexTemplate.NOT_EMPTY, R.string.invalid_empty);

        return awesomeValidation.validate();
    }

    private void init() {
        etRestaurantName = findViewById(R.id.etRestaurantName);
        etRestaurantAddress = findViewById(R.id.etRestaurantAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etRestaurantRating = findViewById(R.id.etRestaurantRating);
        snRestaurantCategory = findViewById(R.id.snRestaurantCategory);
        ivRestaurantAvatar = findViewById(R.id.ivRestaurantAvatar);
        btnEditRestaurantImage = findViewById(R.id.btnEditRestaurantImage);
        btnEditRestaurantSubmit = findViewById(R.id.btnEditRestaurantSubmit);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initUi() {
        etRestaurantName.setText(restaurant.getName());
        etRestaurantAddress.setText(restaurant.getAddress());
        etPhoneNumber.setText(restaurant.getPhoneNumber());
        etRestaurantRating.setText(String.valueOf(restaurant.getRating()));
        LoadImageUtil.loadImage(ivRestaurantAvatar, restaurant.getAvatar());
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

    private void updateRestaurantInfo() {
        if (isValidate()) {
            int statusCode = dbHelper.resDao.updateRestaurant(restaurant);
            if (statusCode != -1) {
                Toast.makeText(EditRestaurantActivity.this,  " Update Success", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {

                Toast.makeText(EditRestaurantActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createRestaurant() {
        if (isValidate()) {
            restaurant.setAvatar(cloudinaryPath);
            Role role = dbHelper.roleDao.getRoleByName("Owner");
            boolean updateUser = dbHelper.userDao.updateUserRole(ownerId, role.getId());
            boolean insert = dbHelper.resDao.insertRestaurant(restaurant);
            if (insert && updateUser) {
                Toast.makeText(EditRestaurantActivity.this,  "Create Restaurant Success", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(EditRestaurantActivity.this,  "Create Restaurant Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
}