package com.example.testpharmacy.UI.admin.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UI.admin.orders.OrderAdapter;
import com.example.testpharmacy.UI.admin.orders.OrderDetailActivity;
import com.example.testpharmacy.UI.auth.LoginSignupActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText medicalNoticeEditText;
    private Button saveButton;
    private Button viewOrdersButton;
    private RecyclerView ordersRecyclerView;
    private ScrollView detailScrollView;
    private TextView ordersHeaderTitle;
    private LinearLayout buttonContainer;

    private UserDao userDao;
    private BillDao billDao;
    private UserSessionManager sessionManager;
    private User userData;
    private long userId;
    private boolean isCurrentUser = false;
    private boolean showingOrders = false;
    private List<Bill> userBills;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        // Initialize session manager
        sessionManager = UserSessionManager.getInstance(this);

        toolbar = findViewById(R.id.customer_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find views
        nameEditText = findViewById(R.id.customer_detail_name_edit_text);
        emailEditText = findViewById(R.id.customer_detail_email_edit_text);
        phoneEditText = findViewById(R.id.customer_detail_phone_edit_text);
        addressEditText = findViewById(R.id.customer_detail_address_edit_text);
        medicalNoticeEditText = findViewById(R.id.customer_detail_medical_notice_edit_text);
        saveButton = findViewById(R.id.customer_detail_save_button);
        viewOrdersButton = findViewById(R.id.customer_detail_view_orders_button);
        ordersRecyclerView = findViewById(R.id.customer_orders_recycler_view);
        detailScrollView = findViewById(R.id.customer_detail_scroll_view);
        ordersHeaderTitle = findViewById(R.id.customer_orders_title);
        buttonContainer = findViewById(R.id.customer_detail_button_container);

        // Initialize DAOs
        userDao = new UserDao(this);
        billDao = new BillDao(this);

        // Determine if this is profile view (current user) or admin viewing customer
        if (getIntent().hasExtra("customer_id")) {
            // Admin is viewing a customer
            userId = getIntent().getLongExtra("customer_id", -1);
            isCurrentUser = false;

            if (userId == -1) {
                Toast.makeText(this, getString(R.string.error_customer_not_found), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            getSupportActionBar().setTitle(getString(R.string.customer_details));

            // Load orders for this customer
            loadUserOrders();

            // Set up ViewOrders button to toggle between information and orders
            viewOrdersButton.setText(getString(R.string.view_orders));

        } else {
            // Regular user viewing their own profile
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, getString(R.string.login_required_message), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            userId = sessionManager.getUserId();
            isCurrentUser = true;
            getSupportActionBar().setTitle(getString(R.string.profile_title));

            // Load orders for current user
            loadUserOrders();

            // Set up ViewOrders button to toggle between information and orders
            viewOrdersButton.setText(getString(R.string.view_orders));
        }

        // Load user data
        loadUserData();

        // Set up buttons
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView();
            }
        });

        // Setup RecyclerView
        setupOrdersRecyclerView();
    }

    private void setupOrdersRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(layoutManager);

        if (userBills != null && !userBills.isEmpty()) {
            orderAdapter = new OrderAdapter(userBills, new OrderAdapter.OnOrderClickListener() {
                @Override
                public void onOrderClick(Bill order) {
                    Intent intent = new Intent(CustomerDetailActivity.this, OrderDetailActivity.class);
                    intent.putExtra("order_number", order.getOrderNumber());
                    startActivity(intent);
                }
            });
            ordersRecyclerView.setAdapter(orderAdapter);
        }
    }

    private void toggleView() {
        showingOrders = !showingOrders;

        if (showingOrders) {
            // Show orders
            detailScrollView.setVisibility(View.GONE);
            ordersRecyclerView.setVisibility(View.VISIBLE);

            // Update button text
            viewOrdersButton.setText(getString(R.string.view_profile));

            // Check if we have orders to display
            if (userBills == null || userBills.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_orders), Toast.LENGTH_SHORT).show();
                // Toggle back to profile view if no orders
                toggleView();
            }
        } else {
            // Show profile info
            detailScrollView.setVisibility(View.VISIBLE);
            ordersRecyclerView.setVisibility(View.GONE);

            // Update button text
            viewOrdersButton.setText(getString(R.string.view_orders));
        }
    }

    private void loadUserData() {
        userDao.open();
        userData = userDao.getUserById(userId);
        userDao.close();

        if (userData != null) {
            nameEditText.setText(userData.getName());
            emailEditText.setText(userData.getEmail());
            phoneEditText.setText(userData.getPhoneNumber());
            addressEditText.setText(userData.getAddress());
            medicalNoticeEditText.setText(userData.getMedicalNotice());
        }
    }

    private void loadUserOrders() {
        billDao.open();
        userBills = billDao.getBillsByUserId(userId);
        billDao.close();
    }

    private void saveUserData() {
        if (userData == null) return;

        // Update user object with form data
        userData.setName(nameEditText.getText().toString());
        userData.setEmail(emailEditText.getText().toString());
        userData.setPhoneNumber(phoneEditText.getText().toString());
        userData.setAddress(addressEditText.getText().toString());
        userData.setMedicalNotice(medicalNoticeEditText.getText().toString());

        // Save to database
        userDao.open();
        boolean success = userDao.updateUser(userData);
        userDao.close();

        if (success) {
            Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.profile_update_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only add logout option for regular users viewing their profile
        if (isCurrentUser) {
            getMenuInflater().inflate(R.menu.logout_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(CustomerDetailActivity.this, LoginSignupActivity.class);
        startActivity(intent);
        finish();
    }
}