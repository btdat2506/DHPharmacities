package com.example.testpharmacy.UI.admin.products;

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

import com.example.testpharmacy.Constants.CategoryConstants;
import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

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
    private Medicine product;
    private boolean isNewProduct;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        // Determine if this is a new product or editing existing one
        isNewProduct = getIntent().getBooleanExtra("is_new_product", false);
        if (!isNewProduct) {
            productId = getIntent().getLongExtra("product_id", -1);
            if (productId == -1) {
                Toast.makeText(this, getString(R.string.error_product_not_found), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        toolbar = findViewById(R.id.product_edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(isNewProduct ? getString(R.string.add_product) : getString(R.string.edit_product));

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
        // Get localized categories for the UI
        List<String> localizedCategories = new ArrayList<>();
        for (String dbCategory : CategoryConstants.getAllDatabaseCategories()) {
            localizedCategories.add(CategoryConstants.getLocalizedCategory(this, dbCategory));
        }

        // Create adapter with localized categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, localizedCategories);
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

            // Set category spinner position by converting the database category to its localized version
            String localizedCategory = CategoryConstants.getLocalizedCategory(this, product.getCategory());
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpinner.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(localizedCategory)) {
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
        String localizedCategory = categorySpinner.getSelectedItem().toString();
        String priceStr = priceEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();
        String stockQuantityStr = stockQuantityEditText.getText().toString().trim();
        String unit = unitEditText.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockQuantityStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, getString(R.string.product_fields_required), Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int stockQuantity;

        try {
            price = Double.parseDouble(priceStr);
            stockQuantity = Integer.parseInt(stockQuantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.product_price_invalid), Toast.LENGTH_SHORT).show();
            return;
        }

        if (price <= 0) {
            Toast.makeText(this, getString(R.string.product_price_positive), Toast.LENGTH_SHORT).show();
            return;
        }

        if (stockQuantity < 0) {
            Toast.makeText(this, getString(R.string.product_stock_negative), Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert localized category to database category
        String databaseCategory = CategoryConstants.getDatabaseCategory(this, localizedCategory);

        // Create or update product
        if (isNewProduct) {
            product = new Medicine();
        }

        product.setName(name);
        product.setDescription(description);
        product.setCategory(databaseCategory);
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
            Toast.makeText(this,
                    isNewProduct ? getString(R.string.product_added) : getString(R.string.product_updated),
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.product_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.product_delete_confirm_title))
                .setMessage(getString(R.string.product_delete_confirm_message))
                .setPositiveButton(getString(R.string.product_delete_confirm_button), (dialog, which) -> deleteProduct())
                .setNegativeButton(getString(R.string.product_delete_cancel_button), null)
                .show();
    }

    private void deleteProduct() {
        if (product == null) return;

        medicineDao.open();
        boolean success = medicineDao.delete(product);
        medicineDao.close();

        if (success) {
            Toast.makeText(this, getString(R.string.product_deleted), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.product_error), Toast.LENGTH_SHORT).show();
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