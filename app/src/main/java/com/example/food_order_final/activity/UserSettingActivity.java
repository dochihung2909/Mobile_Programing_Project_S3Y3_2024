package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_order_final.R;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.database.DbBitmapUtility;
import com.example.food_order_final.hideSoftKeyboard;
import com.example.food_order_final.models.Employee;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserSettingActivity extends AppCompatActivity {

    private EditText etUserFullName, etPhoneNumber, etUsername;
    private TextInputEditText etPassword, etRepeatPassword;
    private TextInputLayout etRepeatPasswordContainer, etPasswordContainer;
    private static final String TAG = "UserSettingActivity Upload ###";
    private Button btnEditUserAvatar, btnSubmitEdit;
    private ImageButton btnBack;
    private ImageView ivUserAvatar;

    private EditText etEmail;

    private static boolean mediaManager = false;

    private Bitmap imageSelected;

    private AwesomeValidation awesomeValidation;

    int SELECT_PICTURE = 200;

    private Uri imagePath;
    private String cloudinaryPath;
    private boolean isUpload = false;
    private User currentUser;
    private int restaurantId = -1;
    private int employeeId = -1;

    DatabaseHelper dbHelper = new DatabaseHelper(UserSettingActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new hideSoftKeyboard(this);
        init();
        initConfigCloudinary();

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString("username", "Guest");
        restaurantId = getIntent().getIntExtra("restaurantId", -1);
        employeeId = getIntent().getIntExtra("employeeId", -1);

        if (!username.equals("Guest")) {


            if (restaurantId == -1 || employeeId != -1) {

                if (employeeId != -1) {
                    currentUser = dbHelper.userDao.getUserById(employeeId);
                } else {
                    currentUser = dbHelper.userDao.getUserByUsername(username);
                }
                String email = currentUser.getEmail();
                etUserFullName.setText(currentUser.getFullName());
                etPhoneNumber.setText(currentUser.getPhoneNumber());
                if (currentUser.getAvatar() != null) {
                    LoadImageUtil.loadImage(ivUserAvatar, currentUser.getAvatar());
                }

                if (!Objects.equals(email, "")) {
                    etEmail.setText(email);
                } else {
                    etEmail.setText("Thiết lập Email");
                }
            } else {
                etUsername.setVisibility(View.VISIBLE);
                etRepeatPasswordContainer.setVisibility(View.VISIBLE);
                etPasswordContainer.setVisibility(View.VISIBLE);
            }

            btnSubmitEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newFullName = String.valueOf(etUserFullName.getText());
                    String newPhoneNumber = String.valueOf(etPhoneNumber.getText());
                    String newEmail = String.valueOf((etEmail.getText()));
                    if (restaurantId != -1) {
                        String password = String.valueOf(etPassword.getText());
                        String username = String.valueOf(etUsername.getText());
                        currentUser = new User(username, newPhoneNumber, newEmail, newFullName, password, dbHelper.roleDao.getRoleByName("Employee"), cloudinaryPath);
                    } else {
                        currentUser.setEmail(newEmail);
                        currentUser.setFullName(newFullName);
                        currentUser.setPhoneNumber(newPhoneNumber);
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
                                currentUser.setAvatar(cloudinaryPath);
                                if (restaurantId != -1) {
                                    createEmployee(currentUser);
                                } else {
                                    updateUserInfo(currentUser);
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
                        if (restaurantId != -1) {
                            createEmployee(currentUser);
                        } else {
                            updateUserInfo(currentUser);

                        }
                    }

                }
            });
        }

        btnEditUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                isUpload = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(UserSettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserSettingActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, SELECT_PICTURE);
        }
    }

    private void init() {
        etUserFullName = findViewById(R.id.etUserFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        btnEditUserAvatar = findViewById(R.id.btnEditUserAvatar);
        btnSubmitEdit = findViewById(R.id.btnSubmitEdit);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        btnBack = findViewById(R.id.btnBack);

        // Create User
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        etPasswordContainer = findViewById(R.id.etPasswordContainer);
        etRepeatPasswordContainer = findViewById(R.id.etRepeatPasswordContainer);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == SELECT_PICTURE && data != null && data.getData() != null) {
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(ivUserAvatar);

        }else {
            Toast.makeText(UserSettingActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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

    protected Boolean isValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etPhoneNumber, "^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|8|9]|9[0-4|6-9])[0-9]{7}$", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.etFullname, RegexTemplate.NOT_EMPTY, R.string.invalid_fullname);

        return awesomeValidation.validate();
    }

    private void updateUserInfo(User user) {
        if (isValidate()) {
            int statusCode = dbHelper.userDao.updateUserInfo(user);
            if (statusCode != -1) {
                Toast.makeText(UserSettingActivity.this,  " Update Success", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {

                Toast.makeText(UserSettingActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected Boolean isCreateValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_username);

        awesomeValidation.addValidation(this, R.id.etPhoneNumber, "^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|8|9]|9[0-4|6-9])[0-9]{7}$", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.etFullname, RegexTemplate.NOT_EMPTY, R.string.invalid_fullname);

        awesomeValidation.addValidation(this, R.id.etPassword, "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.etRepeatPassword, R.id.etPassword, R.string.invalid_repeat_password);

        return awesomeValidation.validate();
    }

    private void createEmployee(User user) {
        if (isCreateValidate()) {
            long statusCode = dbHelper.userDao.insertUser(user);
            if (statusCode != -1) {
                User u = dbHelper.userDao.getUserByUsername(user.getUsername());
                Restaurant restaurant = dbHelper.resDao.getRestaurantById(restaurantId);
                long employeeStatusCode = dbHelper.employeeDao.insertEmployee(new Employee(u, restaurant));
                if (employeeStatusCode != -1) {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        }
    }
}