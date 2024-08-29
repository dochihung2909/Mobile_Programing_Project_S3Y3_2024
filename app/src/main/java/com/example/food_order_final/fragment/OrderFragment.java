package com.example.food_order_final.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.food_order_final.R;
import com.example.food_order_final.activity.OrderActivity;
import com.example.food_order_final.activity.RestaurantOrderManagementActivity;
import com.example.food_order_final.custom_activity.FoodOrderCardView;
import com.example.food_order_final.custom_activity.PaymentHistoryView;
import com.example.food_order_final.dao.CartDao;
import com.example.food_order_final.dao.CartDetailDao;
import com.example.food_order_final.dao.PaymentPendingDao;
import com.example.food_order_final.dao.RestaurantCategoryDao;
import com.example.food_order_final.dao.RestaurantDao;
import com.example.food_order_final.dao.RoleDao;
import com.example.food_order_final.dao.UserDao;
import com.example.food_order_final.database.DatabaseHelper;
import com.example.food_order_final.models.Cart;
import com.example.food_order_final.models.CartDetail;
import com.example.food_order_final.models.Food;
import com.example.food_order_final.models.PaymentPending;
import com.example.food_order_final.models.PaymentStatus;
import com.example.food_order_final.models.Restaurant;
import com.example.food_order_final.models.User;
import com.example.food_order_final.util.PriceUtil;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout paymentsContainer;

    private int REQUEST_CHANGE_STATUS_PAYMENT = 200;
    private User currentUser = null;


    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = pref.getString("username", "Guest");
        Toast.makeText(getActivity(), "" + username, Toast.LENGTH_SHORT).show();
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        if (!username.equals("Guest")) {
            currentUser = dbHelper.userDao.getUserByUsername(username);
            updateUI();
        }

    }

    private void init() {
        paymentsContainer = getView().findViewById(R.id.paymentsContainer);
    }

    private void updateUI() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        paymentsContainer.removeAllViews();
        List<PaymentPending> paymentPendings = dbHelper.paymentPendingDao.getPaymentHistoryByUserId(currentUser.getId());
        for (PaymentPending paymentPending: paymentPendings) {
            PaymentHistoryView paymentHistoryView = new PaymentHistoryView(getActivity());
            paymentHistoryView.setTvPaymentStatus(PaymentStatus.getNameFromStatus(paymentPending.getPaymentStatus().getStatus()));
            Restaurant restaurant = paymentPending.getCart().getRestaurant();
            LinearLayout foodsContainer = paymentHistoryView.findViewById(R.id.foodsContainer);
            Cart cart = paymentPending.getCart();
            List<CartDetail> cartDetails = dbHelper.cartDetailDao.getAllCartDetailInCart(cart.getId());
            for (CartDetail cartDetail: cartDetails) {
                FoodOrderCardView foodOrderCardView = new FoodOrderCardView(getActivity());
                foodOrderCardView.setFoodQuantityOrder(cartDetail.getQuantity() + "x");
                Food food = cartDetail.getFood();
                foodOrderCardView.setTvFoodDescription(food.getDescription());
                foodOrderCardView.setTvFoodName(food.getName());
                foodOrderCardView.setTvFoodDiscountPrice(PriceUtil.formatNumber(food.getPrice()) + "đ");
                foodOrderCardView.setIvFoodAvatar(cartDetail.getFood().getAvatar());

                foodsContainer.addView(foodOrderCardView);
            }
            paymentHistoryView.setTvRestaurantName(restaurant.getName());
            paymentHistoryView.setTvPaymentTotal(PriceUtil.formatNumber(paymentPending.getTotal()) + "đ");
            int totalDishes = dbHelper.cartDao.getTotalDishes(cart.getId());
            paymentHistoryView.setTvPaymentFoodQuantity(totalDishes + " món");

            paymentHistoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("paymentPendingId", paymentPending.getId());
                    startActivityForResult(intent, REQUEST_CHANGE_STATUS_PAYMENT);
                }
            });

            paymentsContainer.addView(paymentHistoryView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHANGE_STATUS_PAYMENT) {
            if (resultCode == getActivity().RESULT_OK) {
                updateUI();
            }
        }
    }
}