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

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView cartItemsRecyclerView;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItemList; // Placeholder for cart items
    private TextView subtotalTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Add this line to remove default title

        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view);
        subtotalTextView = findViewById(R.id.cart_subtotal_text_view);
        shippingTextView = findViewById(R.id.cart_shipping_text_view);
        totalTextView = findViewById(R.id.cart_total_text_view);
        checkoutButton = findViewById(R.id.checkout_button);

        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize placeholder cart items
        cartItemList = createPlaceholderCartItems();

        cartItemAdapter = new CartItemAdapter(this, cartItemList);
        cartItemsRecyclerView.setAdapter(cartItemAdapter);

        updateCartSummary(); // Calculate and display cart summary

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Checkout Activity
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
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
        cartItems.add(new CartItem(new Medicine("Aspirin", 50.0, R.drawable.ic_placeholder_medicine), 2)); // 2 Aspirin
        cartItems.add(new CartItem(new Medicine("Paracetamol", 30.0, R.drawable.ic_placeholder_medicine), 1)); // 1 Paracetamol
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

        subtotalTextView.setText("₹" + String.format("%.2f", subtotal));
        shippingTextView.setText(R.string.free); // "Free" from strings.xml
        totalTextView.setText("₹" + String.format("%.2f", total));
    }
}