package com.example.food_order_final.activity.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUserDetail extends AppCompatActivity {
    ImageButton btnBackToMain, btnEditUserSave;
    TextView tvEditUserTitle;
    Button btnEditUserAvatar, btnEditUserDelete;
    ImageView imgEditUserAvatar;
    EditText edtEditUserUsername, edtEditUserPhone, edtEditUserEmail, edtEditUserFullName, edtEditUserPassword;
    Spinner spnEditUserRole;
    private DatabaseHelper dbHelper;
    private User selectedUser;
    private AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_detail);

        dbHelper = new DatabaseHelper(AdminUserDetail.this);
        initWidgets();
        loadRoles();
        checkForEditUser();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkForEditUser() {
        Intent previousIntent = getIntent();

        int userId = previousIntent.getIntExtra("user_id", -1);
        selectedUser = dbHelper.userDao.getUserById(userId);

        if (selectedUser != null) {
            edtEditUserUsername.setText(selectedUser.getUsername());
            edtEditUserPhone.setText(selectedUser.getPhoneNumber());
            edtEditUserEmail.setText(selectedUser.getEmail());
            edtEditUserFullName.setText(selectedUser.getFullName());
            edtEditUserPassword.setText(selectedUser.getPassword());

            List<Role> roles = dbHelper.roleDao.getAllRoles();
            List<String> roleNames = new ArrayList<>();

            for (Role role : roles) {
                roleNames.add(role.getName());
            }

            String userRole = selectedUser.getRole().getName();

            int position = roleNames.indexOf(userRole);

            if (position != -1)
                spnEditUserRole.setSelection(position);
            else
                spnEditUserRole.setSelection(0);

        } else
            btnEditUserDelete.setVisibility(View.INVISIBLE);
    }

    private void setOnClickListener() {
        btnEditUserSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(AdminUserDetail.this);
                String username = String.valueOf(edtEditUserUsername.getText());
                String phone = String.valueOf(edtEditUserPhone.getText());
                String email = String.valueOf(edtEditUserEmail.getText());
                String fullName = String.valueOf(edtEditUserFullName.getText());
                String password = String.valueOf(edtEditUserPassword.getText());
                String roleName = spnEditUserRole.toString();
                Role role = dbHelper.roleDao.getRoleByName(roleName);
                if (selectedUser == null) {
                    if (isValidate()) {
                        if (!dbHelper.userDao.checkUsername(username)) {
                            User newUser = new User(username, phone, email, fullName, password, role);
                            dbHelper.userDao.insertUser(newUser);
                            Toast.makeText(AdminUserDetail.this, "Tạo User mới thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(AdminUserDetail.this, "Tên Username đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminUserDetail.this, "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedUser.setUsername(username);
                    selectedUser.setPhoneNumber(phone);
                    selectedUser.setEmail(email);
                    selectedUser.setFullName(fullName);
                    dbHelper.userDao.updateUserPassword(selectedUser.getId(), password);
                    selectedUser.setRole(role);
                    dbHelper.userDao.updateUserInfo(selectedUser);
                    Toast.makeText(AdminUserDetail.this, "Cập nhật User thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnEditUserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(AdminUserDetail.this);
                showDeleteConfirmDialog();
            }
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa người dùng này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.userDao.deleteUser(selectedUser.getId());
                        Toast.makeText(AdminUserDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadRoles() {
        List<Role> roles = dbHelper.roleDao.getAllRoles();
        List<String> roleNames = new ArrayList<>();

        for (Role role : roles) {
            roleNames.add(role.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditUserRole.setAdapter(adapter);
    }

    protected Boolean isValidate(){
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_username);

        awesomeValidation.addValidation(this, R.id.etPhoneNumber, "(84|0[3|5|7|8|9])+([0-9]{8})\\b", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.etFullname, RegexTemplate.NOT_EMPTY, R.string.invalid_fullname);

        awesomeValidation.addValidation(this, R.id.etPassword, "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.etRepeatPassword, R.id.etPassword, R.string.invalid_repeat_password);

        return awesomeValidation.validate();
    }

    private void initWidgets() {
        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);

        tvEditUserTitle = (TextView) findViewById(R.id.tvEditUserTitle);

        btnEditUserSave = (ImageButton) findViewById(R.id.btnEditUserSave);
        btnEditUserDelete = (Button) findViewById(R.id.btnEditUserDelete);

        imgEditUserAvatar = (ImageView) findViewById(R.id.imgEditUserAvatar);

        edtEditUserUsername = (EditText) findViewById(R.id.edtEditUserUsername);
        edtEditUserPhone = (EditText) findViewById(R.id.edtEditUserPhone);
        edtEditUserEmail = (EditText) findViewById(R.id.edtEditUserEmail);
        edtEditUserFullName = (EditText) findViewById(R.id.edtEditUseFullName);
        edtEditUserPassword = (EditText) findViewById(R.id.edtEditUserPassword);

        spnEditUserRole = (Spinner) findViewById(R.id.spnEditUserRole);
    }
}