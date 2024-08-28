package com.example.food_order_final.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_final.R;
import com.example.food_order_final.custom_activity.UserCardView;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Employee;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class RestaurantEmployeeManagementActivity extends AppCompatActivity {

    private LinearLayout employeeContainer;
    private ImageButton btnBack;
    private DatabaseHelper dbHelper = new DatabaseHelper(RestaurantEmployeeManagementActivity.this);
    private int restaurantId = -1;
    private Button btnAddEmployee;

    private int REQUEST_CODE_USER_SETTING = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_employee_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        restaurantId = getIntent().getIntExtra("restaurantId", -1);
        if (restaurantId != -1) {
            updateUI();

            btnAddEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RestaurantEmployeeManagementActivity.this, UserSettingActivity.class);
                    intent.putExtra("restaurantId", restaurantId);
                    startActivityForResult(intent, REQUEST_CODE_USER_SETTING);
                }
            });
        }




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        employeeContainer = findViewById(R.id.employeeContainer);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
    }

    private void updateUI() {
        ArrayList<Employee> employees = dbHelper.employeeDao.getAllEmployeeByRestaurantId(restaurantId);
        if (employees.size() > 0) {
            employeeContainer.removeAllViews();
            for (Employee employee: employees) {
                UserCardView userCardView = new UserCardView(RestaurantEmployeeManagementActivity.this);
                userCardView.setTvEmployeeEmail(employee.getEmail());
                userCardView.setTvEmployeeName(employee.getFullName());
                userCardView.setTvEmployeePhoneNumber(employee.getPhoneNumber());
                if (employee.getAvatar() != null) {
                    userCardView.setIvEmployeeAvatar(employee.getAvatar());
                }

                userCardView.getBtnEditEmployee().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantEmployeeManagementActivity.this, UserSettingActivity.class);
                        intent.putExtra("employeeId", employee.getId());
                        startActivityForResult(intent, REQUEST_CODE_USER_SETTING);
                    }
                });

                userCardView.getBtnDeleteEmployee().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantEmployeeManagementActivity.this);
                        alertDialog.setTitle("Xoá nhân viên").
                                setMessage("Bạn có chắc muốn xoá nhân viên này?")
                                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        boolean userStatus = dbHelper.userDao.deleteUser(employee.getId());
                                        if (userStatus) {
                                            boolean employeeStatus = dbHelper.employeeDao.deleteEmployee(employee.getId(), restaurantId);
                                            if (employeeStatus) {
                                                AlertDialog.Builder newDialog = new AlertDialog.Builder(RestaurantEmployeeManagementActivity.this);
                                                newDialog.setTitle("Thành công")
                                                        .setMessage("Xoá thành công!")
                                                        .setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                updateUI();
                                                            }
                                                        }).show();
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("Huỷ", null).show();
                    }
                });

                employeeContainer.addView(userCardView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_USER_SETTING) {
            if (resultCode == RESULT_OK) {
                updateUI();
            }
        }
    }
}