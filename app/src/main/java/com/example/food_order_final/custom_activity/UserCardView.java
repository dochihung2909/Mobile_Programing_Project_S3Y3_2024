package com.example.food_order_final.custom_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.food_order_final.R;
import com.example.food_order_final.util.LoadImageUtil;

public class UserCardView extends LinearLayout {
    private ImageView ivEmployeeAvatar;
    private Button btnDeleteEmployee, btnEditEmployee;
    private TextView tvEmployeeName, tvEmployeePhoneNumber, tvEmployeeEmail;

    public UserCardView(Context context) {
        super(context);
        init(context);
    }

    public UserCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public UserCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.employee_card, this, true);

        tvEmployeePhoneNumber = view.findViewById(R.id.tvEmployeePhoneNumber);
        tvEmployeeEmail = view.findViewById(R.id.tvEmployeeEmail);
        tvEmployeeName = view.findViewById(R.id.tvEmployeeName);
        ivEmployeeAvatar = view.findViewById(R.id.ivEmployeeAvatar);
        btnDeleteEmployee = view.findViewById(R.id.btnDeleteEmployee);
        btnEditEmployee = view.findViewById(R.id.btnEditEmployee);
    }

    public ImageView getIvEmployeeAvatar() {
        return ivEmployeeAvatar;
    }

    public void setIvEmployeeAvatar(String avatar) {
        LoadImageUtil.loadImage(ivEmployeeAvatar, avatar);
    }

    public Button getBtnDeleteEmployee() {
        return btnDeleteEmployee;
    }

    public void setBtnDeleteEmployee(Button btnDeleteEmployee) {
        this.btnDeleteEmployee = btnDeleteEmployee;
    }

    public Button getBtnEditEmployee() {
        return btnEditEmployee;
    }

    public void setBtnEditEmployee(Button btnEditEmployee) {
        this.btnEditEmployee = btnEditEmployee;
    }

    public TextView getTvEmployeeName() {
        return tvEmployeeName;
    }

    public void setTvEmployeeName(String tvEmployeeName) {
        this.tvEmployeeName.setText(tvEmployeeName);
    }

    public TextView getTvEmployeePhoneNumber() {
        return tvEmployeePhoneNumber;
    }

    public void setTvEmployeePhoneNumber(String tvEmployeePhoneNumber) {
        this.tvEmployeePhoneNumber.setText(tvEmployeePhoneNumber);
    }

    public TextView getTvEmployeeEmail() {
        return tvEmployeeEmail;
    }

    public void setTvEmployeeEmail(String tvEmployeeEmail) {
        this.tvEmployeeEmail.setText(tvEmployeeEmail);
    }
}
