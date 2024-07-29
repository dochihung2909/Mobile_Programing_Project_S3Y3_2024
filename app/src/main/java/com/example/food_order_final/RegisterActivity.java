package com.example.food_order_final;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.food_order_final.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {


    private Button btnSubmitRegister, btnGoogleLogin;

    private TextInputEditText etUsername,etPhoneNumber, etEmail, etFullname, etPassword, etRepeatPassword;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidgets();

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);

                    String fullName = String.valueOf(etFullname.getText());
                    String phoneNumber = String.valueOf(etPhoneNumber.getText());
                    String email = String.valueOf(etEmail.getText());
                    String username = String.valueOf(etUsername.getText());
                    String password = String.valueOf(etPassword.getText());
                    if (!dbHelper.checkUsername(username)) {
                        User newUser = new User(username, phoneNumber, email, fullName, password);
                        dbHelper.addUserToDatabase(newUser);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tên username đã tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Vui lòng kiểm tra lại thông tin !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void initWidgets(){
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etFullname = findViewById(R.id.etFullname);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
    }

    protected Boolean isValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_username);

        awesomeValidation.addValidation(this, R.id.etPhoneNumber, "(84|0[3|5|7|8|9])+([0-9]{8})\\b", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.etEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.etFullname, RegexTemplate.NOT_EMPTY, R.string.invalid_fullname);

        awesomeValidation.addValidation(this, R.id.etPassword, "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.etRepeatPassword, R.id.etPassword, R.string.invalid_repeat_password);

        return awesomeValidation.validate();
    }
}