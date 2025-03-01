package com.example.testpharmacy.UI.home; // Replace with your actual package name

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.testpharmacy.UI.cart.CartManager;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Utils;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MEDICINE = "extra_medicine"; // Key for passing Medicine object in Intent

    private Toolbar toolbar;
    private ImageView medicineImageView;
    private TextView medicineNameTextView;
    private TextView medicinePriceTextView;
    private TextView medicineUnitTextView;
    private TextView medicineDescriptionTextView;
    private TextView medicineDosageTextView;
    private TextView medicineSideEffectsTextView;
    private TextView medicinePrecautionsTextView;
    private Button addToCartButton;

    // --- Quantity Controls UI Elements ---
    private EditText quantityEditText; // Quantity EditText
    private Button decreaseQuantityButton;
    private Button increaseQuantityButton;
    private TextView totalPriceTextView;

    private Medicine medicine; // To hold the Medicine object passed from HomeActivity
    private int currentQuantity = 1; // Track current quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        medicineImageView = findViewById(R.id.detail_medicine_image_view);
        medicineNameTextView = findViewById(R.id.detail_medicine_name_text_view);
        medicinePriceTextView = findViewById(R.id.detail_medicine_price_text_view);
        medicineUnitTextView = findViewById(R.id.detail_medicine_unit);
        medicineDescriptionTextView = findViewById(R.id.detail_medicine_description_text_view);
        medicineDosageTextView = findViewById(R.id.detail_medicine_dosage_text_view);
        medicineSideEffectsTextView = findViewById(R.id.detail_medicine_side_effects_text_view);
        medicinePrecautionsTextView = findViewById(R.id.detail_medicine_precautions_text_view);
        addToCartButton = findViewById(R.id.detail_add_to_cart_button);

        // --- Find Quantity Control UI Elements ---
        quantityEditText = findViewById(R.id.detail_quantity_edit_text);
        decreaseQuantityButton = findViewById(R.id.detail_quantity_decrease_button);
        increaseQuantityButton = findViewById(R.id.detail_quantity_increase_button);
        totalPriceTextView = findViewById(R.id.detail_total_price_text_view);


        // Get the Medicine object from the Intent
        medicine = getIntent().getParcelableExtra(EXTRA_MEDICINE);

        if (medicine != null) {
            populateUI(medicine);
        } else {
            Toast.makeText(this, getString(R.string.error_medicine_not_found), Toast.LENGTH_SHORT).show();
            finish();
        }

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicine != null) {
                    // TODO: Implement Add to Cart functionality
                    CartManager.getInstance().addToCart(medicine, currentQuantity);
                    Toast.makeText(ProductDetailActivity.this,  getString(R.string.added_to_cart_message) + medicine.getName(), Toast.LENGTH_SHORT).show();

                    // TODO: Update the number of carts
                    // You'll need to manage the cart data (e.g., using a CartManager class or similar)
                }
            }
        });

        // --- TextWatcher for Quantity EditText ---
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString();
                currentQuantity = 1; // Default quantity if input is invalid or empty
                try {
                    currentQuantity = Integer.parseInt(quantityStr);
                    if (currentQuantity <= 0) {
                        currentQuantity = 1; // Ensure quantity is at least 1
                        quantityEditText.setText(String.valueOf(currentQuantity)); // Reset EditText to 1
                    }
                } catch (NumberFormatException e) {
                    currentQuantity = 1; // Default to 1 if parsing fails
                    quantityEditText.setText(String.valueOf(currentQuantity)); // Reset EditText to 1
                }
                updateTotalPrice(); // Update total price based on quantity
            }
        });


        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuantity++;
                quantityEditText.setText(String.valueOf(currentQuantity)); // Update EditText
                updateTotalPrice(); // Update total price
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantityEditText.setText(String.valueOf(currentQuantity)); // Update EditText
                    updateTotalPrice(); // Update total price
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void populateUI(Medicine medicine) {
        medicineNameTextView.setText(medicine.getName());
        medicinePriceTextView.setText(Utils.formatVND(medicine.getPrice()));
        medicineUnitTextView.setText(medicine.getUnit());
        Glide.with(this)
                .load(medicine.getImageUrl())
                .into(medicineImageView);
        updateTotalPrice(); // Initial total price calculation

        // Placeholder descriptions, dosage, side effects, precautions
        medicineDescriptionTextView.setText(medicine.getDescription());
/*
        medicineDosageTextView.setText(getString(R.string.default_dosage));
        medicineSideEffectsTextView.setText(getString(R.string.default_side_effects));
        medicinePrecautionsTextView.setText(getString(R.string.default_precautions));
*/
    }

    private void updateTotalPrice() {
        double totalPrice = medicine.getPrice() * currentQuantity;
        totalPriceTextView.setText(Utils.formatVND(totalPrice));
    }
}