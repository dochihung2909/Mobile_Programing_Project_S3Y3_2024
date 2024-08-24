package com.example.food_order_final.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminUserAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagement extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Spinner spnAdminRole;
    private ListView lvAdminUser;
    private Button btnAdminAddUser;
    private ImageButton btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_management);

        dbHelper = new DatabaseHelper(AdminUserManagement.this);
        initWidgets();
        loadAllRoles();
        loadAllUsers();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setOnClickListener() {
        spnAdminRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = (String) parent.getItemAtPosition(position);
                Log.d("AdminUserManagement", "Selected role: " + selectedRole);

                if ("All Users".equals(selectedRole))
                    loadAllUsers();
                else
                    loadUsersByRole(selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnAdminAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUserManagement.this, AdminUserDetail.class);
                startActivity(intent);
            }
        });

        lvAdminUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = (User) parent.getItemAtPosition(position);
                Intent intent = new Intent(AdminUserManagement.this, AdminUserDetail.class);
                intent.putExtra("user_id", selectedUser.getId());
                startActivity(intent);
            }
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void loadAllRoles() {
        dbHelper = new DatabaseHelper(this);
        List<Role> roles = dbHelper.roleDao.getAllRoles();
        List<String> roleNames = new ArrayList<>();

        roleNames.add("All Users");
        for (Role role : roles) {
            roleNames.add(role.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAdminRole.setAdapter(adapter);
    }

    private void loadAllUsers() {
        dbHelper = new DatabaseHelper(this);
        List<User> users = dbHelper.userDao.getAllUsers();
        AdminUserAdapter adapter = new AdminUserAdapter(this, users);
        lvAdminUser.setAdapter(adapter);
    }

    private void loadUsersByRole(String roleName) {
        List<User> users = dbHelper.userDao.getUsersByRole(roleName);
        AdminUserAdapter adapter = new AdminUserAdapter(this, users);
        lvAdminUser.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllUsers();
    }

    private void initWidgets() {
        lvAdminUser = (ListView) findViewById(R.id.lvAdminUser);

        spnAdminRole = (Spinner) findViewById(R.id.spnAdminUserRole);

        btnAdminAddUser = (Button) findViewById(R.id.btnAdminAddUser);

        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);
    }

}