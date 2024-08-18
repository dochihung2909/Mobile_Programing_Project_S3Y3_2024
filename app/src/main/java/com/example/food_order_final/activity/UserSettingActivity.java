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
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.LoadImageUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserSettingActivity extends AppCompatActivity {

    private EditText etUserFullName, etPhoneNumber;
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
    private UserDao userDao;
    private User currentUser;

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


        if (!username.equals("Guest")) {
            DatabaseHelper dbHelper = new DatabaseHelper(UserSettingActivity.this);
            userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
            currentUser = userDao.getUserByUsername(username);
            String email = currentUser.getEmail();
            etUserFullName.setText(currentUser.getFullName());
            etPhoneNumber.setText(currentUser.getPhoneNumber());

//            Picasso.get().load((currentUser.getAvatar())).into(ivUserAvatar);
//            Picasso.get().load(currentUser.getAvatar()).placeholder(R.drawable.ic_launcher_background).into(ivUserAvatar);

            LoadImageUtil.loadImage(ivUserAvatar, currentUser.getAvatar());
            Toast.makeText(UserSettingActivity.this, currentUser.getAvatar(), Toast.LENGTH_SHORT).show();
            if (!Objects.equals(email, "")) {
                etEmail.setText(email);
            } else {
                etEmail.setText("Thiết lập Email");
            }
//            imageSelected = DbBitmapUtility.getImage(currentUser.getAvatar());
//            ivUserAvatar.setImageBitmap(imageSelected);

            btnSubmitEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newFullName = String.valueOf(etUserFullName.getText());
                    String newPhoneNumber = String.valueOf(etPhoneNumber.getText());
                    String newEmail = String.valueOf((etEmail.getText()));
                    currentUser.setEmail(newEmail);
                    currentUser.setFullName(newFullName);
                    currentUser.setPhoneNumber(newPhoneNumber);

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
                                updateUserInfo(currentUser);
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
                        updateUserInfo(currentUser);
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
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == SELECT_PICTURE && data != null && data.getData() != null) {
            imagePath = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Picasso.get().load(imagePath).into(ivUserAvatar);

//                imageSelected = selectedImage;

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
            int statusCode = userDao.updateUserInfo(user);
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
}