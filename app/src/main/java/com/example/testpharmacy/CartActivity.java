package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        cartItemList = createPlaceholderCartItems();

        cartItemAdapter = new CartItemAdapter(this, cartItemList);
        cartItemsRecyclerView.setAdapter(cartItemAdapter);

        updateCartSummary(); // Calculate and display cart summary
        updateShippingInfoPreview(); // Update shipping info preview on create


        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Checkout Activity
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                // Pass shipping information to CheckoutActivity (optional for this example, but good practice)
                intent.putExtra("shippingName", shippingNameEditText.getText().toString());
                intent.putExtra("shippingPhone", shippingPhoneEditText.getText().toString());
                intent.putExtra("shippingAddress", shippingAddressEditText.getText().toString());
                intent.putExtra("shippingNote", shippingNoteEditText.getText().toString());
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back when back button in toolbar is pressed
        return true;
    }

    private List<CartItem> createPlaceholderCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        // Add placeholder CartItems (replace with actual cart data)
        cartItems.add(new CartItem(new Medicine("Aspirin", 50.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100), 100)); // Updated constructor call, 2)); // 2 Aspirin
        cartItems.add(new CartItem(new Medicine("Paracetamol", 30.0, "box 50 pills", R.drawable.ic_placeholder_medicine, "","","", 100), 100)); // Updated constructor call, 1)); // 1 Paracetamol
        // ... add more placeholder cart items
        return cartItems;
    }

    protected void updateCartSummary() {
        double subtotal = 0;
        for (CartItem item : cartItemList) {
            subtotal += item.getTotalPrice();
        }
        double shippingCost = 0; // For now, shipping is free
        double total = subtotal + shippingCost;

        subtotalTextView.setText(String.format("%.3f", subtotal) + "đ");
        shippingTextView.setText(R.string.free); // "Free" from strings.xml
        totalTextView.setText(String.format("%.3f", total) + "đ");
    }

    // --- Method to toggle Shipping Info visibility ---
    private void toggleShippingInfoVisibility() {
        isShippingInfoExpanded = !isShippingInfoExpanded; // Toggle boolean state
        shippingInfoLayout.setVisibility(isShippingInfoExpanded ? View.VISIBLE : View.GONE); // Show/Hide Edit layout
        shippingInfoPreviewLayout.setVisibility(isShippingInfoExpanded ? View.GONE : View.VISIBLE); // Show/Hide Preview layout
        shippingInfoMoreButton.setText(isShippingInfoExpanded ? R.string.done_editing_shipping_info : R.string.edit_shipping_info); // Change button text

        if (isShippingInfoExpanded) {
            // When expanding to edit, populate EditTexts with preview values (or saved values)
            shippingNameEditText.setText(shippingNamePreviewTextView.getText().toString().substring(6)); // Remove "Name: " prefix
            shippingPhoneEditText.setText(shippingPhonePreviewTextView.getText().toString().substring(7)); // Remove "Phone: " prefix
            shippingAddressEditText.setText(shippingAddressPreviewTextView.getText().toString().substring(9)); // Remove "Address: " prefix
            shippingNoteEditText.setText(shippingNotePreviewTextView.getText().toString().substring(6)); // Remove "Note: " prefix
        } else {
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
        Toast.makeText(this, "Shipping info saved (simulated)", Toast.LENGTH_SHORT).show();
    }

    private void updateShippingInfoPreview() {
        // Update preview TextViews with saved shipping information
        shippingNamePreviewTextView.setText("Name: " + savedShippingName);
        shippingPhonePreviewTextView.setText("Phone: " + savedShippingPhone);
        shippingAddressPreviewTextView.setText("Address: " + savedShippingAddress);
        shippingNotePreviewTextView.setText("Note: " + savedShippingNote);

        // Conditionally show/hide preview layout based on whether any shipping info is entered
        boolean hasShippingInfo = !savedShippingName.isEmpty() || !savedShippingPhone.isEmpty() || !savedShippingAddress.isEmpty() || !savedShippingNote.isEmpty();
        shippingInfoPreviewLayout.setVisibility(hasShippingInfo ? View.VISIBLE : View.GONE);
    }
}