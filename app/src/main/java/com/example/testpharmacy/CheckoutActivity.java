package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.BillDao;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView checkoutItemsRecyclerView;
    private CartItemCheckoutAdapter cartItemCheckoutAdapter;
    private List<CartItem> cartItemList; // Placeholder - Get from Cart (or pass via intent)
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private TextView confirmationTextView;
    private Button continueShoppingButton;

    // --- Shipping Info UI Elements (Preview Only - No Edit Texts) ---
    private LinearLayout shippingInfoLayout;
    private TextView shippingNameTextView;
    private TextView shippingPhoneTextView;
    private TextView shippingAddressTextView;
    private TextView shippingNoteTextView;

    private TextView orderNumberTextView; // New Order Number TextView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Remove title

        checkoutItemsRecyclerView = findViewById(R.id.checkout_items_recycler_view);
        subtotalTextView = findViewById(R.id.checkout_subtotal_text_view);
        shippingTextView = findViewById(R.id.checkout_shipping_text_view);
        totalTextView = findViewById(R.id.checkout_total_text_view);
        confirmationTextView = findViewById(R.id.checkout_confirmation_text_view);
        continueShoppingButton = findViewById(R.id.continue_shopping_button);

        // --- Find Shipping Info UI Elements (TextViews for Preview) ---
        shippingInfoLayout = findViewById(R.id.checkout_shipping_info_layout);
        shippingNameTextView = findViewById(R.id.checkout_shipping_name_text_view);
        shippingPhoneTextView = findViewById(R.id.checkout_shipping_phone_text_view);
        shippingAddressTextView = findViewById(R.id.checkout_shipping_address_text_view);
        shippingNoteTextView = findViewById(R.id.checkout_shipping_note_text_view);

        orderNumberTextView = findViewById(R.id.order_number_text_view); // Find Order Number TextView

        checkoutItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- Retrieve Shipping Information from Intent (passed from CartActivity) ---
        Intent intent = getIntent();
        String orderNumber = intent.getStringExtra("orderNumber");
        String shippingName = intent.getStringExtra("shippingName");
        String shippingPhone = intent.getStringExtra("shippingPhone");
        String shippingAddress = intent.getStringExtra("shippingAddress");
        String shippingNote = intent.getStringExtra("shippingNote");

        cartItemList = CartManager.getInstance().getCartItems();
        cartItemCheckoutAdapter = new CartItemCheckoutAdapter(this, cartItemList);
        checkoutItemsRecyclerView.setAdapter(cartItemCheckoutAdapter);

        updateCheckoutSummary();

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the cart after checkout
                CartManager.getInstance().clearCart();

                // Navigate to Home Activity
                Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
                startActivity(intent);
                finish(); // Close CheckoutActivity
            }
        });

        // --- Populate Shipping Info TextViews ---
        shippingNameTextView.setText("Name: " + shippingName);
        shippingPhoneTextView.setText("Phone: " + shippingPhone);
        shippingAddressTextView.setText("Address: " + shippingAddress);
        shippingNoteTextView.setText("Note: " + shippingNote);
        orderNumberTextView.setText("Order Number: " + orderNumber); // Set Order Number with placeholder XXX
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back
        return true;
    }

    private void updateCheckoutSummary() {
        double subtotal = 0;
        for (CartItem item : cartItemList) {
            subtotal += item.getTotalPrice();
        }
        double shippingCost = 0; // Free shipping
        double total = subtotal + shippingCost;

        subtotalTextView.setText(String.format("%.3f", subtotal) + "đ");
        shippingTextView.setText(R.string.free);
        totalTextView.setText(String.format("%.3f", total) + "đ");
    }

}