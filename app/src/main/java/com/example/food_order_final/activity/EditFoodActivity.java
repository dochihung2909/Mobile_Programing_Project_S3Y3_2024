package com.example.food_order_final.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_order_final.R;
import com.example.food_order_final.dao.FoodCategoryDao;
import com.example.food_order_final.dao.FoodDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.FoodCategory;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.RestaurantCategory;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.example.food_order_final.util.PriceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditFoodActivity extends AppCompatActivity {

    private int foodId = -1;
    private ImageView ivFoodAvatar;
    private ImageButton btnBack;
    private Button btnEditFoodImage, btnEditFoodSubmit;
    private EditText etFoodName, etFoodPrice, etFoodDiscount, etFoodDiscription;
    private Spinner snFoodCategory;
    private DatabaseHelper dbHelper = new DatabaseHelper(EditFoodActivity.this);

    private int SELECT_PICTURE = 200;

    private boolean isUpload = false;
    private String cloudinaryPath;

    private AwesomeValidation awesomeValidation;
    private Uri imagePath;
    private static boolean mediaManager = false;

    private static final String TAG = "EditRestaurantActivity Upload ###";
    private Food food;
    private FoodCategory foodCategory;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        initConfigCloudinary();

        ArrayList<FoodCategory> foodCategories = dbHelper.foodCateDao.getAllFoodCategories();
        ArrayList<String> foodCategoriesNames = new ArrayList<>();
        for (FoodCategory restaurantCategory: foodCategories) {
            foodCategoriesNames.add(restaurantCategory.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditFoodActivity.this, android.R.layout.simple_spinner_item, foodCategoriesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snFoodCategory.setAdapter(adapter);

        this.foodId = getIntent().getIntExtra("foodId", -1);
        if (foodId != -1) {
            food = dbHelper.foodDao.getFoodById(foodId);
            etFoodName.setText(food.getName());
            etFoodPrice.setText(String.valueOf(food.getPrice()));
            etFoodDiscount.setText(String.valueOf((food.getDiscount())));
            etFoodDiscription.setText(food.getDescription());

            for (int i = 0;i < foodCategories.size();i++) {
                if (food.getCategory().getId() == foodCategories.get(i).getId()) {
                    snFoodCategory.setSelection(i);
                }
            }
        }

        snFoodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodCategory = foodCategories.get(position);
                if (food != null) {
                    food.setCategory(foodCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditFoodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                isUpload = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
            }
        });

        btnEditFoodSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = etFoodName.getText().toString();
                String foodDescription = etFoodDiscription.getText().toString();
                double foodPrice = Double.parseDouble(etFoodPrice.getText().toString());
                double foodDiscount = Double.parseDouble(etFoodDiscount.getText().toString());
                if (foodId != -1) {
                    food.setName(foodName);
                    food.setDescription(foodDescription);
                    food.setPrice(foodPrice);
                    food.setDiscount(foodDiscount);
                    LoadImageUtil.loadImage(ivFoodAvatar, food.getAvatar());
                } else {
                    int restaurantId = getIntent().getIntExtra("restaurantId", -1);
                    if (restaurantId != -1) {
                        restaurant = dbHelper.resDao.getRestaurantById(restaurantId);
                        food = new Food(foodName, foodPrice, foodDiscount, cloudinaryPath, foodDescription, foodCategory, restaurant);
                    }
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
                            if (foodId != -1) {
                                food.setAvatar(cloudinaryPath);
                                updateFoodInfo();
                            } else {
                                createFood();
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
                    if (foodId == -1) {
                        createFood();
                    } else {
                        updateFoodInfo();
                    }
                }
            }
        });
    }

    private void init() {
        ivFoodAvatar = findViewById(R.id.ivFoodAvatar);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodDiscount = findViewById(R.id.etFoodDiscount);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        btnEditFoodImage = findViewById(R.id.btnEditFoodImage);
        btnEditFoodSubmit = findViewById(R.id.btnEditFoodSubmit);
        btnBack = findViewById(R.id.btnBack);
        snFoodCategory = findViewById(R.id.snFoodCategory);
        etFoodDiscription = findViewById(R.id.etFoodDiscription);
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(EditFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditFoodActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, SELECT_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == SELECT_PICTURE && data != null && data.getData() != null) {
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(ivFoodAvatar);

        }else {
            Toast.makeText(EditFoodActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    protected Boolean isValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etFoodName, RegexTemplate.NOT_EMPTY, R.string.invalid_empty);

        awesomeValidation.addValidation(this, R.id.etFoodDiscount, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                double discountPrice = Double.parseDouble(etFoodDiscount.getText().toString());
                double foodPrice = Double.parseDouble(etFoodPrice.getText().toString());
                return discountPrice < foodPrice;
            }
        }, R.string.invalid_discount_price);
        awesomeValidation.addValidation(this, R.id.etFoodPrice, RegexTemplate.NOT_EMPTY, R.string.invalid_empty);

        return awesomeValidation.validate();
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

    private void createFood() {
        if (isValidate()) {
            food.setAvatar(cloudinaryPath);
            boolean insert = dbHelper.foodDao.insertFood(food);
            if (insert) {
                Toast.makeText(EditFoodActivity.this,  "Create Restaurant Success", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditFoodActivity.this);
                alertDialog.setTitle("Thêm món ăn thành công");
                alertDialog.setMessage("Bạn có muốn tiếp tục thêm món ăn?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setPositiveButton("Thêm món ăn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetUI();
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
                Toast.makeText(EditFoodActivity.this,  "Create Restaurant Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFoodInfo() {
        if (isValidate()) {
            int statusCode = dbHelper.foodDao.updateFood(food);
            if (statusCode != -1) {
                Toast.makeText(EditFoodActivity.this,  " Update Success", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditFoodActivity.this);
                alertDialog.setTitle("Sửa thông tin món ăn thành công");
                alertDialog.setMessage("Bạn có muốn tiếp tục chỉnh sửa món ăn?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setPositiveButton("Tiếp tục chỉnh sửa", null)
                    .setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }).show();
            } else {

                Toast.makeText(EditFoodActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetUI() {
        food = null;
        ivFoodAvatar.setImageResource(R.drawable.ic_launcher_background);
        etFoodName.setText("");
        etFoodDiscount.setText("");
        etFoodDiscription.setText("");
        etFoodPrice.setText("");
        foodId = -1;
        cloudinaryPath = "";
        imagePath = null;
    }
}