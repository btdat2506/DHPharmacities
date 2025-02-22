package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MEDICINE = "extra_medicine"; // Key for passing Medicine object in Intent

    private Toolbar toolbar;
    private ImageView medicineImageView;
    private TextView medicineNameTextView;
    private TextView medicinePriceTextView;
    private TextView medicineDescriptionTextView; // Placeholder for description
    private TextView medicineDosageTextView; // Placeholder for dosage
    private TextView medicineSideEffectsTextView; // Placeholder for side effects
    private TextView medicinePrecautionsTextView; // Placeholder for precautions
    private Button addToCartButton;

    private Medicine medicine; // To hold the Medicine object passed from HomeActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Add this line to remove default title

        medicineImageView = findViewById(R.id.detail_medicine_image_view);
        medicineNameTextView = findViewById(R.id.detail_medicine_name_text_view);
        medicinePriceTextView = findViewById(R.id.detail_medicine_price_text_view);
        medicineDescriptionTextView = findViewById(R.id.detail_medicine_description_text_view);
        medicineDosageTextView = findViewById(R.id.detail_medicine_dosage_text_view);
        medicineSideEffectsTextView = findViewById(R.id.detail_medicine_side_effects_text_view);
        medicinePrecautionsTextView = findViewById(R.id.detail_medicine_precautions_text_view);
        addToCartButton = findViewById(R.id.detail_add_to_cart_button);

        // Get the Medicine object from the Intent
        medicine = getIntent().getParcelableExtra(EXTRA_MEDICINE); // Retrieve using the key

        if (medicine != null) {
            populateUI(medicine); // Populate UI elements with medicine data
        } else {
            // Handle error: Medicine object not passed correctly
            Toast.makeText(this, "Error: Medicine details not found.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if medicine data is missing
        }

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicine != null) {
                    // TODO: Implement Add to Cart functionality (similar to MedicineAdapter)
                    Toast.makeText(ProductDetailActivity.this, "Added " + medicine.getName() + " to cart!", Toast.LENGTH_SHORT).show();
                    // You'll need to manage the cart data (e.g., using a CartManager class or similar)
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back when back button in toolbar is pressed
        return true;
    }

    private void populateUI(Medicine medicine) {
        medicineNameTextView.setText(medicine.getName());
        medicinePriceTextView.setText("₹" + String.format("%.2f", medicine.getPrice()));
        medicineImageView.setImageResource(medicine.getImageResourceId());

        // Placeholder descriptions, dosage, side effects, precautions
        medicineDescriptionTextView.setText("This is a placeholder description for " + medicine.getName() + ".  More detailed information will be added here in a real application.");
        medicineDosageTextView.setText("Dosage: As directed by physician. (Placeholder)");
        medicineSideEffectsTextView.setText("Side Effects: May cause drowsiness. (Placeholder)");
        medicinePrecautionsTextView.setText("Precautions: Consult your doctor before use if pregnant. (Placeholder)");
    }
}