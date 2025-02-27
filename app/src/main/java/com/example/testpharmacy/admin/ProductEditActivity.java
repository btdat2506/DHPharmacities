package com.example.testpharmacy.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Medicine;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UserSessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class ProductEditActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText nameEditText;
    private TextInputEditText descriptionEditText;
    private Spinner categorySpinner;
    private TextInputEditText priceEditText;
    private TextInputEditText imageUrlEditText;
    private TextInputEditText stockQuantityEditText;
    private TextInputEditText unitEditText;
    private Button saveButton;
    private Button deleteButton;
    
    private MedicineDao medicineDao;
//    private UserSessionManager sessionManager;
    private Medicine product;
    private boolean isNewProduct;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        // Initialize session manager and check admin status
//        sessionManager = UserSessionManager.getInstance(this);
//        if (!sessionManager.isAdmin()) {
//            finish();
//            return;
//        }

        // Determine if this is a new product or editing existing one
        isNewProduct = getIntent().getBooleanExtra("is_new_product", false);
        if (!isNewProduct) {
            productId = getIntent().getLongExtra("product_id", -1);
            if (productId == -1) {
                Toast.makeText(this, "Error: Product not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        toolbar = findViewById(R.id.product_edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(isNewProduct ? "Add New Product" : "Edit Product");

        nameEditText = findViewById(R.id.product_edit_name_edit_text);
        descriptionEditText = findViewById(R.id.product_edit_description_edit_text);
        categorySpinner = findViewById(R.id.product_edit_category_spinner);
        priceEditText = findViewById(R.id.product_edit_price_edit_text);
        imageUrlEditText = findViewById(R.id.product_edit_image_url_edit_text);
        stockQuantityEditText = findViewById(R.id.product_edit_stock_quantity_edit_text);
        unitEditText = findViewById(R.id.product_edit_unit_edit_text);
        saveButton = findViewById(R.id.product_edit_save_button);
        deleteButton = findViewById(R.id.product_edit_delete_button);

        // Initialize MedicineDao
        medicineDao = new MedicineDao(this);
        
        // Set up category spinner
        setupCategorySpinner();
        
        // Load product data if editing existing product
        if (!isNewProduct) {
            loadProductData();
        } else {
            // Hide delete button for new products
            deleteButton.setVisibility(View.GONE);
        }
        
        // Set up buttons
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProductData();
            }
        });
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    private void setupCategorySpinner() {
        // Populate spinner with predefined categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadProductData() {
        medicineDao.open();
        product = medicineDao.getMedicineById((int) productId);
        medicineDao.close();
        
        if (product != null) {
            nameEditText.setText(product.getName());
            descriptionEditText.setText(product.getDescription());
            
            // Set category spinner position
            String category = product.getCategory();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) categorySpinner.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(category)) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }
            
            priceEditText.setText(String.valueOf(product.getPrice()));
            imageUrlEditText.setText(product.getImageUrl());
            stockQuantityEditText.setText(String.valueOf(product.getStockQuantity()));
            unitEditText.setText(product.getUnit());
        }
    }

    private void saveProductData() {
        // Validate input
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String priceStr = priceEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();
        String stockQuantityStr = stockQuantityEditText.getText().toString().trim();
        String unit = unitEditText.getText().toString().trim();
        
        if (name.isEmpty() || priceStr.isEmpty() || stockQuantityStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double price;
        int stockQuantity;
        
        try {
            price = Double.parseDouble(priceStr);
            stockQuantity = Integer.parseInt(stockQuantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or stock quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (price <= 0) {
            Toast.makeText(this, "Price must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (stockQuantity < 0) {
            Toast.makeText(this, "Stock quantity cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create or update product
        if (isNewProduct) {
            product = new Medicine();
        }
        
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setStockQuantity(stockQuantity);
        product.setUnit(unit);
        
        medicineDao.open();
        boolean success;
        
        if (isNewProduct) {
            success = medicineDao.insert(product) != -1;
        } else {
            success = medicineDao.update(product);
        }
        
        medicineDao.close();
        
        if (success) {
            Toast.makeText(this, isNewProduct ? "Product added successfully" : "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProduct())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProduct() {
        if (product == null) return;
        
        medicineDao.open();
        boolean success = medicineDao.delete(product);
        medicineDao.close();
        
        if (success) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
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
