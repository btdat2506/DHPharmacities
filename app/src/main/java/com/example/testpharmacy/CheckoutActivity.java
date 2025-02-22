package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Add this line to remove default title

        checkoutItemsRecyclerView = findViewById(R.id.checkout_items_recycler_view);
        subtotalTextView = findViewById(R.id.checkout_subtotal_text_view);
        shippingTextView = findViewById(R.id.checkout_shipping_text_view);
        totalTextView = findViewById(R.id.checkout_total_text_view);
        confirmationTextView = findViewById(R.id.checkout_confirmation_text_view);
        continueShoppingButton = findViewById(R.id.continue_shopping_button);

        checkoutItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Placeholder: Get cart items from CartActivity (or pass via Intent)
        cartItemList = getCartItemsFromSomewhere(); // Replace with actual cart data retrieval

        cartItemCheckoutAdapter = new CartItemCheckoutAdapter(this, cartItemList);
        checkoutItemsRecyclerView.setAdapter(cartItemCheckoutAdapter);

        updateCheckoutSummary();

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home Activity
                Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
                startActivity(intent);
                finish(); // Close CheckoutActivity
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back
        return true;
    }

    // Placeholder: Replace with actual cart data retrieval logic (e.g., from Intent, CartManager, etc.)
    private List<CartItem> getCartItemsFromSomewhere() {
        // For now, returning the same placeholder cart items as in CartActivity
        return createPlaceholderCartItems(); // Reusing placeholder method from CartActivity for simplicity
    }


    // Reusing the same placeholder method from CartActivity for simplicity in Checkout
    private List<CartItem> createPlaceholderCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Medicine("Aspirin", 50.0, R.drawable.ic_placeholder_medicine), 2));
        cartItems.add(new CartItem(new Medicine("Paracetamol", 30.0, R.drawable.ic_placeholder_medicine), 1));
        return cartItems;
    }

    private void updateCheckoutSummary() {
        double subtotal = 0;
        for (CartItem item : cartItemList) {
            subtotal += item.getTotalPrice();
        }
        double shippingCost = 0; // Free shipping
        double total = subtotal + shippingCost;

        subtotalTextView.setText("₹" + String.format("%.2f", subtotal));
        shippingTextView.setText(R.string.free);
        totalTextView.setText("₹" + String.format("%.2f", total));
    }
}