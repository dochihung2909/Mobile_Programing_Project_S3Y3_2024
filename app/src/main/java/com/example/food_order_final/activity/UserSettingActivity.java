package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.database.DbBitmapUtility;
import com.example.food_order_final.hideSoftKeyboard;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class UserSettingActivity extends AppCompatActivity {

    private EditText etUserFullName, etPhoneNumber;
    private Button btnEditUserAvatar, btnSubmitEdit;
    private ImageView ivUserAvatar;

    private EditText etEmail;

    private Bitmap imageSelected;

    int SELECT_PICTURE = 200;

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

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString("username", "Guest");


        if (!username.equals("Guest")) {
            DatabaseHelper dbHelper = new DatabaseHelper(UserSettingActivity.this);
            UserDao userDao = new UserDao(dbHelper, new RoleDao(dbHelper));
            User currentUser = userDao.getUserByUsername(username);
            String email = currentUser.getEmail();
            etUserFullName.setText(currentUser.getFullName());
            etPhoneNumber.setText(currentUser.getPhoneNumber());
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
                    currentUser.setAvatar(imageSelected);
                    currentUser.setEmail(newEmail);
                    currentUser.setFullName(newFullName);
                    currentUser.setPhoneNumber(newPhoneNumber);
                    int statusCode = userDao.updateUserInfo(currentUser);

                    if (statusCode != -1) {
                        Toast.makeText(UserSettingActivity.this, currentUser.getId() + " Update Success", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(UserSettingActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnEditUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
            }
        });


    }

    private void init() {
        etUserFullName = findViewById(R.id.etUserFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        btnEditUserAvatar = findViewById(R.id.btnEditUserAvatar);
        btnSubmitEdit = findViewById(R.id.btnSubmitEdit);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageSelected = selectedImage;
                ivUserAvatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UserSettingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(UserSettingActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}