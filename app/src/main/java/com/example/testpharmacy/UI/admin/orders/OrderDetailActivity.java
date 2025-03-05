package com.example.testpharmacy.UI.admin.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Constants.OrderStatusConstants;
import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UI.home.HomeActivity;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView orderNumberTextView;
    private TextView orderDateTextView;
    private TextView customerNameTextView;
    private TextView customerEmailTextView;
    private TextView customerPhoneTextView;
    private TextView shippingNameTextView;
    private TextView shippingPhoneTextView;
    private TextView shippingAddressTextView;
    private TextView shippingNoteTextView;
    private RecyclerView orderItemsRecyclerView;
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private Spinner statusSpinner;
    private CardView order_detail_status_card_view;
    private Button updateStatusButton;
    private Button finishButton; // Added for user checkout flow

    private BillDao billDao;
    private UserDao userDao;
    private UserSessionManager sessionManager;
    private Bill order;
    private String orderNumber;
    private OrderItemAdapter orderItemAdapter;
    private boolean isNewOrder = false; // Flag to identify if this is a new order from checkout
    private boolean isAdmin = false; // Flag to identify if current user is admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Initialize session manager and check user role
        sessionManager = UserSessionManager.getInstance(this);
        isAdmin = sessionManager.isAdmin();

        // Get order number from intent
        orderNumber = getIntent().getStringExtra("order_number");
        isNewOrder = getIntent().getBooleanExtra("is_new_order", false);

        if (orderNumber == null) {
            Toast.makeText(this, getString(R.string.error_order_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.order_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title based on context (new order vs viewing existing)
        if (isNewOrder) {
            getSupportActionBar().setTitle(getString(R.string.checkout_title));
        } else {
            getSupportActionBar().setTitle(getString(R.string.order_details));
        }

        // Find all UI elements
        orderNumberTextView = findViewById(R.id.order_detail_number_text_view);
        orderDateTextView = findViewById(R.id.order_detail_date_text_view);
        customerNameTextView = findViewById(R.id.order_detail_customer_name_text_view);
        customerEmailTextView = findViewById(R.id.order_detail_customer_email_text_view);
        customerPhoneTextView = findViewById(R.id.order_detail_customer_phone_text_view);
        shippingNameTextView = findViewById(R.id.order_detail_shipping_name_text_view);
        shippingPhoneTextView = findViewById(R.id.order_detail_shipping_phone_text_view);
        shippingAddressTextView = findViewById(R.id.order_detail_shipping_address_text_view);
        shippingNoteTextView = findViewById(R.id.order_detail_shipping_note_text_view);
        orderItemsRecyclerView = findViewById(R.id.order_detail_items_recycler_view);
        subtotalTextView = findViewById(R.id.order_detail_subtotal_text_view);
        shippingTextView = findViewById(R.id.order_detail_shipping_text_view);
        totalTextView = findViewById(R.id.order_detail_total_text_view);
        order_detail_status_card_view = findViewById(R.id.order_detail_status_card_view);
        statusSpinner = findViewById(R.id.order_detail_status_spinner);
        updateStatusButton = findViewById(R.id.order_detail_update_status_button);

        if (isNewOrder || !isAdmin) {
            order_detail_status_card_view.setVisibility(View.GONE);
            // Find the parent view that contains the update button
            // ViewGroup parent = (ViewGroup) updateStatusButton.getParent();

            updateStatusButton.setText(R.string.finish);
            updateStatusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate back to home
                    Intent intent = new Intent(OrderDetailActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
                    startActivity(intent);
                    finish();
                }
            });
        }

        // Initialize RecyclerView
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DAOs
        billDao = new BillDao(this);
        userDao = new UserDao(this);

        // Load order details
        loadOrderDetails();

        // Set up status spinner and show/hide based on user role
        setupStatusSpinner();

        // Set up update button if showing
        if (isAdmin) {
            updateStatusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateOrderStatus();
                }
            });
        }
    }

    private void loadOrderDetails() {
        billDao.open();
        order = billDao.getBillByOrderNumber(orderNumber);
        billDao.close();

        if (order == null) {
            Toast.makeText(this, getString(R.string.error_order_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get customer information
        User customer = null;
        if (order.getUserId() > 0) {
            userDao.open();
            customer = userDao.getUserById(order.getUserId());
            userDao.close();
        }

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

        // Set order details
        orderNumberTextView.setText(getString(R.string.order_number, order.getOrderNumber()));
        orderDateTextView.setText(dateFormat.format(order.getOrderDate()));

        // Set customer details
        if (customer != null) {
            customerNameTextView.setText(getString(R.string.profile_name_label) + " " + customer.getName());
            customerEmailTextView.setText(getString(R.string.profile_email_label) + " " + customer.getEmail());
            customerPhoneTextView.setText(getString(R.string.profile_phone_label) + " " + customer.getPhoneNumber());
        } else {
            customerNameTextView.setText(getString(R.string.profile_name_label) + " " + order.getShippingName());
            customerEmailTextView.setText(getString(R.string.profile_email_label) + " N/A");
            customerPhoneTextView.setText(getString(R.string.profile_phone_label) + " " + order.getShippingPhone());
        }

        // Set shipping information
        shippingNameTextView.setText(getString(R.string.profile_name_label) + " " + order.getShippingName());
        shippingPhoneTextView.setText(getString(R.string.profile_phone_label) + " " + order.getShippingPhone());
        shippingAddressTextView.setText(getString(R.string.profile_address_label) + " " + order.getShippingAddress());
        shippingNoteTextView.setText(getString(R.string.shipping_note_display) + " " +
                (order.getShippingNote() != null ? order.getShippingNote() : ""));

        // Set financial information
        subtotalTextView.setText(Utils.formatVND(order.getTotalAmount()));
        shippingTextView.setText(getString(R.string.free));
        totalTextView.setText(Utils.formatVND(order.getTotalAmount()));

        // Set up order items
        orderItemAdapter = new OrderItemAdapter(order.getBillItems());
        orderItemsRecyclerView.setAdapter(orderItemAdapter);

        // If this is a new order from checkout, show confirmation message
        if (isNewOrder) {
            Toast.makeText(this, R.string.order_confirmation_message, Toast.LENGTH_LONG).show();
        }
    }

    private void setupStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Set current status
        if (order != null && order.getStatus() != null) {
            int position = OrderStatusConstants.getPositionForStatus(order.getStatus());
            statusSpinner.setSelection(position);
        }
    }

    private void updateOrderStatus() {
        int selectedPosition = statusSpinner.getSelectedItemPosition();
        String newStatus = OrderStatusConstants.getStatusFromPosition(selectedPosition);

        billDao.open();
        boolean success = billDao.updateOrderStatus(order.getOrderNumber(), newStatus);
        billDao.close();

        if (success) {
            Toast.makeText(this, getString(R.string.order_status_updated), Toast.LENGTH_SHORT).show();
            order.setStatus(newStatus);
        } else {
            Toast.makeText(this, getString(R.string.order_status_update_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}