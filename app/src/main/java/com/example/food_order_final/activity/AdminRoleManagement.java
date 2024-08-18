package com.example.food_order_final.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.adapter.AdminRoleAdapter;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Role;
import com.example.food_order_final.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminRoleManagement extends AppCompatActivity {
    ImageButton btnBackToMain;
    Button btnAdminAddRole;
    ListView lvAdminRole;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_role_management);

        initWidgets();
        loadRoles();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setOnClickListener() {
        btnAdminAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRoleManagement.this, AdminRoleDetail.class);
                startActivity(intent);
            }
        });

        lvAdminRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role selectedRole = (Role) parent.getItemAtPosition(position);
                Intent intent = new Intent(AdminRoleManagement.this, AdminRoleDetail.class);
                intent.putExtra("role_id", selectedRole.getId());
                startActivity(intent);
            }
        });

        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void loadRoles() {
        dbHelper = new DatabaseHelper(this);
        List<Role> roles = dbHelper.roleDao.getAllRoles();
        AdminRoleAdapter adapter = new AdminRoleAdapter(this, roles);
        lvAdminRole.setAdapter(adapter);
    }

    private void initWidgets() {
        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);
        btnAdminAddRole = (Button) findViewById(R.id.btnAdminAddRole);
        lvAdminRole = (ListView) findViewById(R.id.lvAdminRole);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRoles();
    }
}