package com.example.testpharmacy.UI.admin.orders;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView orderNumberTextView;
    private TextView orderDateTextView;
    private TextView customerNameTextView;
    private TextView customerEmailTextView;
    private TextView customerPhoneTextView;
    private TextView shippingAddressTextView;
    private TextView shippingNoteTextView;
    private RecyclerView orderItemsRecyclerView;
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private Spinner statusSpinner;
    private Button updateStatusButton;
    
    private BillDao billDao;
    private UserDao userDao;
    private Bill order;
    private String orderNumber;
    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get order number from intent
        orderNumber = getIntent().getStringExtra("order_number");
        if (orderNumber == null) {
            Toast.makeText(this, "Error: Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.order_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");

        orderNumberTextView = findViewById(R.id.order_detail_number_text_view);
        orderDateTextView = findViewById(R.id.order_detail_date_text_view);
        customerNameTextView = findViewById(R.id.order_detail_customer_name_text_view);
        customerEmailTextView = findViewById(R.id.order_detail_customer_email_text_view);
        customerPhoneTextView = findViewById(R.id.order_detail_customer_phone_text_view);
        shippingAddressTextView = findViewById(R.id.order_detail_shipping_address_text_view);
        shippingNoteTextView = findViewById(R.id.order_detail_shipping_note_text_view);
        orderItemsRecyclerView = findViewById(R.id.order_detail_items_recycler_view);
        subtotalTextView = findViewById(R.id.order_detail_subtotal_text_view);
        shippingTextView = findViewById(R.id.order_detail_shipping_text_view);
        totalTextView = findViewById(R.id.order_detail_total_text_view);
        statusSpinner = findViewById(R.id.order_detail_status_spinner);
        updateStatusButton = findViewById(R.id.order_detail_update_status_button);

        // Set up RecyclerView
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize DAOs
        billDao = new BillDao(this);
        userDao = new UserDao(this);
        
        // Load order details
        loadOrderDetails();
        
        // Set up status spinner
        setupStatusSpinner();
        
        // Set up update button
        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus();
            }
        });
    }

    private void loadOrderDetails() {
        billDao.open();
        order = billDao.getBillByOrderNumber(orderNumber);
        billDao.close();
        
        if (order == null) {
            Toast.makeText(this, "Error: Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get customer information
        userDao.open();
        User customer = userDao.getUserById(order.getUserId());
        userDao.close();
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        
        // Set order details
        orderNumberTextView.setText("Order #" + order.getOrderNumber());
        orderDateTextView.setText(dateFormat.format(order.getOrderDate()));
        
        // Set customer details
        if (customer != null) {
            customerNameTextView.setText(customer.getName());
            customerEmailTextView.setText(customer.getEmail());
            customerPhoneTextView.setText(customer.getPhoneNumber());
        } else {
            customerNameTextView.setText(order.getShippingName());
            customerEmailTextView.setText("N/A");
            customerPhoneTextView.setText(order.getShippingPhone());
        }
        
        // Set shipping information
        shippingAddressTextView.setText(order.getShippingAddress());
        shippingNoteTextView.setText(order.getShippingNote());
        
        // Set financial information
        subtotalTextView.setText(String.format(Locale.getDefault(), "%.2f đ", order.getTotalAmount()));
        shippingTextView.setText("Free");
        totalTextView.setText(String.format(Locale.getDefault(), "%.2f đ", order.getTotalAmount()));
        
        // Set up order items
        orderItemAdapter = new OrderItemAdapter(order.getBillItems());
        orderItemsRecyclerView.setAdapter(orderItemAdapter);
    }

    private void setupStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        
        // Set current status
        String currentStatus = order.getStatus();
        if (currentStatus != null) {
            int position = 0;
            switch (currentStatus.toLowerCase()) {
                case "pending":
                    position = 0;
                    break;
                case "processing":
                    position = 1;
                    break;
                case "shipping":
                    position = 2;
                    break;
                case "delivered":
                    position = 3;
                    break;
                case "cancelled":
                    position = 4;
                    break;
            }
            statusSpinner.setSelection(position);
        }
    }

    private void updateOrderStatus() {
        String newStatus = statusSpinner.getSelectedItem().toString().toLowerCase();
        
        billDao.open();
        boolean success = billDao.updateOrderStatus(order.getOrderNumber(), newStatus);
        billDao.close();
        
        if (success) {
            Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
            order.setStatus(newStatus);
        } else {
            Toast.makeText(this, "Failed to update order status", Toast.LENGTH_SHORT).show();
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
