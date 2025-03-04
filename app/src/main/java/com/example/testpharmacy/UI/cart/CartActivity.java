package com.example.testpharmacy.UI.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.BillItem;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.UI.admin.orders.OrderDetailActivity;
import com.example.testpharmacy.UI.auth.LoginSignupActivity;
import com.example.testpharmacy.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView cartItemsRecyclerView;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItemList;
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private Button checkoutButton;

    // Shipping Info UI Elements
    private TextView shippingInfoMoreButton;
    private LinearLayout shippingInfoLayout;
    private LinearLayout shippingInfoPreviewLayout;
    private TextInputEditText shippingNameEditText;
    private TextInputEditText shippingPhoneEditText;
    private TextInputEditText shippingAddressEditText;
    private TextInputEditText shippingNoteEditText;
    private TextView shippingNamePreviewTextView;
    private TextView shippingPhonePreviewTextView;
    private TextView shippingAddressPreviewTextView;
    private TextView shippingNotePreviewTextView;
    private boolean isShippingInfoExpanded = false;

    // Saved Shipping Information
    private String savedShippingName = "";
    private String savedShippingPhone = "";
    private String savedShippingAddress = "";
    private String savedShippingNote = "";

    private UserDao userDao;
    private BillDao billDao;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize userDao, billDao, and sessionManager
        userDao = new UserDao(this);
        billDao = new BillDao(this);
        sessionManager = UserSessionManager.getInstance(this);

        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view);
        subtotalTextView = findViewById(R.id.cart_subtotal_text_view);
        shippingTextView = findViewById(R.id.cart_shipping_text_view);
        totalTextView = findViewById(R.id.cart_total_text_view);
        checkoutButton = findViewById(R.id.checkout_button);

        // Find Shipping Info UI Elements
        shippingInfoMoreButton = findViewById(R.id.cart_shipping_info_more_button);
        shippingInfoLayout = findViewById(R.id.cart_shipping_info_layout);
        shippingInfoPreviewLayout = findViewById(R.id.cart_shipping_info_preview_layout);
        shippingNameEditText = findViewById(R.id.cart_shipping_name_edit_text);
        shippingPhoneEditText = findViewById(R.id.cart_shipping_phone_edit_text);
        shippingAddressEditText = findViewById(R.id.cart_shipping_address_edit_text);
        shippingNoteEditText = findViewById(R.id.cart_shipping_note_edit_text);
        shippingNamePreviewTextView = findViewById(R.id.cart_shipping_name_preview_text_view);
        shippingPhonePreviewTextView = findViewById(R.id.cart_shipping_phone_preview_text_view);
        shippingAddressPreviewTextView = findViewById(R.id.cart_shipping_address_preview_text_view);
        shippingNotePreviewTextView = findViewById(R.id.cart_shipping_note_preview_text_view);

        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize cart items from CartManager
        cartItemList = CartManager.getInstance().getCartItems();

        cartItemAdapter = new CartItemAdapter(this, cartItemList);
        cartItemsRecyclerView.setAdapter(cartItemAdapter);

        updateCartSummary();

        // Load shipping info if user is logged in
        if (sessionManager.isLoggedIn()) {
            userDao.open();
            User user = userDao.getUserById(sessionManager.getUserId());
            userDao.close();

            if (user != null) {
                // Populate shipping info with user data
                savedShippingName = user.getName() != null ? user.getName() : "";
                savedShippingPhone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
                savedShippingAddress = user.getAddress() != null ? user.getAddress() : "";
                // Note field stays empty
                updateShippingInfoPreview();
            }
        }

        // Setup checkout button
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCheckout();
            }
        });

        // Set up shipping info button
        shippingInfoMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShippingInfoVisibility();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Process checkout and create order
    private void processCheckout() {
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(CartActivity.this, getString(R.string.please_login_to_checkout), Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(CartActivity.this, LoginSignupActivity.class);
            startActivity(loginIntent);
            return;
        }

        // Validate cart is not empty
        if (cartItemList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate shipping information
        if (savedShippingName.isEmpty() || savedShippingPhone.isEmpty() || savedShippingAddress.isEmpty()) {
            Toast.makeText(this, "Please provide shipping information", Toast.LENGTH_SHORT).show();
            if (!isShippingInfoExpanded) {
                toggleShippingInfoVisibility(); // Show shipping info form
            }
            return;
        }

        // Create a new order (Bill)
        Bill bill = createOrder();

        if (bill != null && bill.getOrderNumber() != null && !bill.getOrderNumber().isEmpty()) {
            // Navigate to OrderDetailActivity to display the order confirmation
            Intent intent = new Intent(CartActivity.this, OrderDetailActivity.class);
            intent.putExtra("order_number", bill.getOrderNumber());
            intent.putExtra("is_new_order", true); // Flag to indicate this is a newly created order

            // Clear the cart
            CartManager.getInstance().clearCart();

            startActivity(intent);
            finish(); // Finish CartActivity
        } else {
            Toast.makeText(this, "Failed to create order. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Create an order in the database
    private Bill createOrder() {
        long userId = sessionManager.getUserId();

        // Create a new Bill object
        Bill bill = new Bill();
        bill.setUserId(userId);
        bill.setShippingName(savedShippingName);
        bill.setShippingPhone(savedShippingPhone);
        bill.setShippingAddress(savedShippingAddress);
        bill.setShippingNote(savedShippingNote);

        // Create bill items from cart items
        for (CartItem cartItem : cartItemList) {
            BillItem billItem = new BillItem(cartItem);
            bill.addBillItem(billItem);
        }

        // Calculate total
        bill.calculateTotal();

        // Open connection to database
        billDao.open();

        // Generate order number
        String orderNumber = billDao.generateOrderNumber();
        bill.setOrderNumber(orderNumber);

        // Create the bill with all its items
        boolean success = billDao.createBill(bill);

        // Close database connection
        billDao.close();

        if (success) {
            return bill;
        } else {
            return null;
        }
    }

    // Update cart summary totals
    protected void updateCartSummary() {
        double subtotal = CartManager.getInstance().getTotalPrice();
        double shippingCost = 0; // Free shipping
        double total = subtotal + shippingCost;

        subtotalTextView.setText(Utils.formatVND(subtotal));
        shippingTextView.setText(getString(R.string.free));
        totalTextView.setText(Utils.formatVND(total));
    }

    // Toggle shipping info form visibility
    private void toggleShippingInfoVisibility() {
        isShippingInfoExpanded = !isShippingInfoExpanded;
        shippingInfoLayout.setVisibility(isShippingInfoExpanded ? View.VISIBLE : View.GONE);
        shippingInfoPreviewLayout.setVisibility(isShippingInfoExpanded ? View.GONE : View.VISIBLE);
        shippingInfoMoreButton.setText(isShippingInfoExpanded ?
                getString(R.string.done_editing_shipping_info) :
                getString(R.string.edit_shipping_info));

        if (isShippingInfoExpanded) {
            // Populate the form with saved values
            shippingNameEditText.setText(savedShippingName);
            shippingPhoneEditText.setText(savedShippingPhone);
            shippingAddressEditText.setText(savedShippingAddress);
            shippingNoteEditText.setText(savedShippingNote);

            // Hide checkout button while editing
            checkoutButton.setVisibility(View.INVISIBLE);
        } else {
            // Save the info and update preview
            saveShippingInformation();
            updateShippingInfoPreview();

            // Show checkout button again
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }

    // Save shipping information from form
    private void saveShippingInformation() {
        savedShippingName = shippingNameEditText.getText().toString();
        savedShippingPhone = shippingPhoneEditText.getText().toString();
        savedShippingAddress = shippingAddressEditText.getText().toString();
        savedShippingNote = shippingNoteEditText.getText().toString();
        Toast.makeText(this, getString(R.string.shipping_info_saved), Toast.LENGTH_SHORT).show();
    }

    // Update shipping info preview text
    private void updateShippingInfoPreview() {
        shippingNamePreviewTextView.setText(getString(R.string.profile_name_label) + " " + savedShippingName);
        shippingPhonePreviewTextView.setText(getString(R.string.profile_phone_label) + " " + savedShippingPhone);
        shippingAddressPreviewTextView.setText(getString(R.string.profile_address_label) + " " + savedShippingAddress);
        shippingNotePreviewTextView.setText(getString(R.string.shipping_note_display) + " " + savedShippingNote);

        // Show/hide preview based on whether any info is entered
        boolean hasShippingInfo = !savedShippingName.isEmpty() || !savedShippingPhone.isEmpty() ||
                !savedShippingAddress.isEmpty() || !savedShippingNote.isEmpty();
        shippingInfoPreviewLayout.setVisibility(hasShippingInfo ? View.VISIBLE : View.GONE);
    }
}