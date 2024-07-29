package com.example.food_order_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
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

                    if (dbHelper.checkUsernamePassword(username, password)){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

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