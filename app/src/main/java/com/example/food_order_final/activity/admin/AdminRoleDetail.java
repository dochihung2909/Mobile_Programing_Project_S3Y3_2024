package com.example.food_order_final.activity.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.google.android.material.textfield.TextInputLayout;

public class AdminRoleDetail extends AppCompatActivity {
    EditText edtEditRoleId, edtEditRoleName;
    TextInputLayout inputLayoutEditRoleId;
    ImageButton btnBackToMain;
    ImageView btnEditRoleSave;
    Button btnEditRoleDelete;
    private DatabaseHelper dbHelper;
    private Role selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_role_detail);

        dbHelper = new DatabaseHelper(AdminRoleDetail.this);
        initWidgets();
        checkForEditRole();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkForEditRole() {
        Intent previousIntent = getIntent();

        int roleId = previousIntent.getIntExtra("role_id", -1);
        selectedRole = dbHelper.roleDao.getRoleById(roleId);

        if (selectedRole != null) {
            inputLayoutEditRoleId.setVisibility(View.VISIBLE);
            edtEditRoleId.setText(String.valueOf(selectedRole.getId()));
            edtEditRoleName.setText(selectedRole.getName());
            edtEditRoleId.setEnabled(false);
        } else {
            btnEditRoleDelete.setVisibility(View.GONE);
            inputLayoutEditRoleId.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        btnEditRoleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(AdminRoleDetail.this);
                String roleName = String.valueOf(edtEditRoleName.getText());
                if (selectedRole == null) {
                    if (!dbHelper.roleDao.isRoleNameExists(roleName)) {
                        Role newRole = new Role(roleName);
                        dbHelper.roleDao.insertRole(newRole);
                        Toast.makeText(AdminRoleDetail.this, "Tạo Role mới thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(AdminRoleDetail.this, "Tên Role đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    selectedRole.setName(roleName);
                    dbHelper.roleDao.updateRole(selectedRole);
                    Toast.makeText(AdminRoleDetail.this, "Cập nhật Role thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnEditRoleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(AdminRoleDetail.this);
                showDeleteConfirmDialog();
            }
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa vai trò này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AdminRoleDetail.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        dbHelper.roleDao.deleteRole(selectedRole.getId());
                        finish();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void initWidgets() {
        edtEditRoleId = (EditText) findViewById(R.id.edtEditRoleId);
        edtEditRoleName = (EditText) findViewById(R.id.edtEditRoleName);

        inputLayoutEditRoleId = (TextInputLayout) findViewById(R.id.inputLayoutEditRoleId);

        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);

        btnEditRoleDelete = (Button) findViewById(R.id.btnEditRoleDelete);
        btnEditRoleSave = (ImageView) findViewById(R.id.btnEditRoleSave);
    }
}