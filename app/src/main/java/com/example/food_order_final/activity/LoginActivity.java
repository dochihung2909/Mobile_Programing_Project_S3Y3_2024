package com.example.food_order_final.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.food_order_final.R;
import com.example.food_order_final.activity.admin.AdminMainActivity;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.hideSoftKeyboard;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private Button btnSubmitLogin,btnForgotPass,btnRegister,btnGoogleLogin;
    private TextInputEditText etUsername, etPassword;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new hideSoftKeyboard(this);
        initWidgets();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom);
            }
        });

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_username);
        //Get Validate from Server

        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate()){
                    DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);

                    String username = String.valueOf(etUsername.getText());
                    String password = String.valueOf(etPassword.getText());

                    if (dbHelper.userDao.isUserCredential(username, password)){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        User user = dbHelper.userDao.getUserByUsername(username);
                        String roleName = user.getRole().getName();





                        if (roleName.equals("Admin")) {
                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                        } else if (roleName.equals("User") || roleName.equals("Owner")) {
                            Intent intent;
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }


                        if (roleName.equals("Employee")) {
                            Toast.makeText(LoginActivity.this, "" + roleName, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, RestaurantManagerActivity.class);
                            intent.putExtra("employeeId", user.getId());
                            startActivity(intent);
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.putInt("userId", user.getId());
                        editor.putString("password", password);
                        editor.putString("currentUserRole", user.getRole().getName());
                        editor.apply();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Thông tin tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
//        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
//
//        awesomeValidation.addValidation(this, R.id.etUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_username);
//        awesomeValidation.addValidation(this, R.id.etPassword, RegexTemplate.NOT_EMPTY, R.string.invalid_password);

    }

    protected void initWidgets(){
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSubmitLogin = findViewById(R.id.btnSubmitLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    protected Boolean isValidate(){
        return awesomeValidation.validate();
    }
}