package com.example.testpharmacy.UI.cart; // Replace with your actual package name

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
import com.example.testpharmacy.UI.auth.LoginSignupActivity;
import com.example.testpharmacy.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView cartItemsRecyclerView;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItemList; // Placeholder for cart items
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private Button checkoutButton;

    // --- Shipping Info UI Elements ---
    private TextView shippingInfoMoreButton;
    private LinearLayout shippingInfoLayout;
    private LinearLayout shippingInfoPreviewLayout; // New Preview Layout
    private TextInputEditText shippingNameEditText;
    private TextInputEditText shippingPhoneEditText;
    private TextInputEditText shippingAddressEditText;
    private TextInputEditText shippingNoteEditText;
    private TextView shippingNamePreviewTextView; // New Preview TextViews
    private TextView shippingPhonePreviewTextView;
    private TextView shippingAddressPreviewTextView;
    private TextView shippingNotePreviewTextView;
    private boolean isShippingInfoExpanded = false; // Track expansion state

    // --- Simulate Saved Shipping Information ---
    private String savedShippingName = ""; // Initially empty
    private String savedShippingPhone = "";
    private String savedShippingAddress = "";
    private String savedShippingNote = "";


    private UserDao userDao;
    private UserSessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize userDao and sessionManager
        userDao = new UserDao(this);
        sessionManager = UserSessionManager.getInstance(this);


        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Remove title

        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view);
        subtotalTextView = findViewById(R.id.cart_subtotal_text_view);
        shippingTextView = findViewById(R.id.cart_shipping_text_view);
        totalTextView = findViewById(R.id.cart_total_text_view);
        checkoutButton = findViewById(R.id.checkout_button);

        // --- Find Shipping Info UI Elements ---
        shippingInfoMoreButton = findViewById(R.id.cart_shipping_info_more_button);
        shippingInfoLayout = findViewById(R.id.cart_shipping_info_layout);
        shippingInfoPreviewLayout = findViewById(R.id.cart_shipping_info_preview_layout); // Find Preview Layout
        shippingNameEditText = findViewById(R.id.cart_shipping_name_edit_text);
        shippingPhoneEditText = findViewById(R.id.cart_shipping_phone_edit_text);
        shippingAddressEditText = findViewById(R.id.cart_shipping_address_edit_text);
        shippingNoteEditText = findViewById(R.id.cart_shipping_note_edit_text);
        shippingNamePreviewTextView = findViewById(R.id.cart_shipping_name_preview_text_view); // Find Preview TextViews
        shippingPhonePreviewTextView = findViewById(R.id.cart_shipping_phone_preview_text_view);
        shippingAddressPreviewTextView = findViewById(R.id.cart_shipping_address_preview_text_view);
        shippingNotePreviewTextView = findViewById(R.id.cart_shipping_note_preview_text_view);

        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize placeholder cart items
        cartItemList = CartManager.getInstance().getCartItems();

        cartItemAdapter = new CartItemAdapter(this, cartItemList);
        cartItemsRecyclerView.setAdapter(cartItemAdapter);

        updateCartSummary(); // Calculate and display cart summary

        // updateShippingInfoPreview(); // Update shipping info preview on create

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

        // Modify checkoutButton click listener:
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sessionManager.isLoggedIn()) {
                    Toast.makeText(CartActivity.this, getString(R.string.please_login_to_checkout), Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(CartActivity.this, LoginSignupActivity.class);
                    startActivity(loginIntent);
                    return;
                }

                // Navigate to Checkout Activity
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                // Pass shipping information and order number
                intent.putExtra("shippingName", savedShippingName);
                intent.putExtra("shippingPhone", savedShippingPhone);
                intent.putExtra("shippingAddress", savedShippingAddress);
                intent.putExtra("shippingNote", savedShippingNote);
                startActivity(intent);
            }
        });


        // --- Set click listener for "More Shipping Info" button ---
        shippingInfoMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShippingInfoVisibility();
            }
        });

        // For debug
        List<Long> productIdsInCart = CartManager.getInstance().getProductIdsInCart();
        for (Long productId : productIdsInCart) {
            Log.d("CartProductId", "Product ID: " + productId);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back when back button in toolbar is pressed
        return true;
    }


    // In CartActivity.java, update the updateCartSummary method
    protected void updateCartSummary() {
        double subtotal = CartManager.getInstance().getTotalPrice();
        double shippingCost = 0; // For now, shipping is free
        double total = subtotal + shippingCost;

        subtotalTextView.setText(Utils.formatVND(subtotal));
        shippingTextView.setText(getString(R.string.free)); // "Free" from strings.xml
        totalTextView.setText(Utils.formatVND(total));
    }


    // --- Method to toggle Shipping Info visibility ---
    private void toggleShippingInfoVisibility() {
        isShippingInfoExpanded = !isShippingInfoExpanded; // Toggle boolean state
        shippingInfoLayout.setVisibility(isShippingInfoExpanded ? View.VISIBLE : View.GONE); // Show/Hide Edit layout
        shippingInfoPreviewLayout.setVisibility(isShippingInfoExpanded ? View.GONE : View.VISIBLE); // Show/Hide Preview layout
        shippingInfoMoreButton.setText(isShippingInfoExpanded ? getString(R.string.done_editing_shipping_info) : getString(R.string.edit_shipping_info)); // Change button text

        if (isShippingInfoExpanded) {
            shippingNameEditText.setText(savedShippingName);
            shippingPhoneEditText.setText(savedShippingPhone);
            shippingAddressEditText.setText(savedShippingAddress);
            shippingNoteEditText.setText(savedShippingNote);

            // When collapsing after editing, hide checkout button
            checkoutButton.setVisibility(View.INVISIBLE);
        } else {
            // After finishing editing, show checkout button
            checkoutButton.setVisibility(View.VISIBLE);

            // When collapsing after editing ("Done Editing"), update preview TextViews and save info
            saveShippingInformation(); // Simulate saving
            updateShippingInfoPreview(); // Update preview TextViews
        }
    }

    private void saveShippingInformation() {
        // Simulate saving shipping information (replace with actual saving mechanism)
        savedShippingName = shippingNameEditText.getText().toString();
        savedShippingPhone = shippingPhoneEditText.getText().toString();
        savedShippingAddress = shippingAddressEditText.getText().toString();
        savedShippingNote = shippingNoteEditText.getText().toString();
        Toast.makeText(this, getString(R.string.shipping_info_saved), Toast.LENGTH_SHORT).show();
    }

    private void updateShippingInfoPreview() {
        // Update preview TextViews with saved shipping information
        shippingNamePreviewTextView.setText(getString(R.string.profile_name_label) + savedShippingName);
        shippingPhonePreviewTextView.setText(getString(R.string.profile_phone_label) + savedShippingPhone);
        shippingAddressPreviewTextView.setText(getString(R.string.profile_address_label) + savedShippingAddress);
        shippingNotePreviewTextView.setText(getString(R.string.shipping_note_display) + savedShippingNote);

        // Conditionally show/hide preview layout based on whether any shipping info is entered
        boolean hasShippingInfo = !savedShippingName.isEmpty() || !savedShippingPhone.isEmpty() || !savedShippingAddress.isEmpty() || !savedShippingNote.isEmpty();
        shippingInfoPreviewLayout.setVisibility(hasShippingInfo ? View.VISIBLE : View.GONE);
    }
}