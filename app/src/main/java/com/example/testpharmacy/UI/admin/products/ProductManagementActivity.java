package com.example.testpharmacy.UI.admin.products;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Constants.CategoryConstants;
import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Medicine> productList;
    private List<Medicine> filteredProductList;
    private EditText searchEditText;
    private Spinner sortSpinner;
    private Spinner categoryFilterSpinner;
    private FloatingActionButton addProductFab;

    private MedicineDao medicineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        toolbar = findViewById(R.id.products_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.product_management));

        productsRecyclerView = findViewById(R.id.products_recycler_view);
        searchEditText = findViewById(R.id.products_search_edit_text);
        sortSpinner = findViewById(R.id.products_sort_spinner);
        categoryFilterSpinner = findViewById(R.id.products_category_filter_spinner);
        addProductFab = findViewById(R.id.add_product_fab);

        // Set up RecyclerView
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize MedicineDao
        medicineDao = new MedicineDao(this);

        // Load products from database
        loadProducts();

        // Set up adapter
        productAdapter = new ProductAdapter(filteredProductList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Medicine product) {
                Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
                intent.putExtra("product_id", product.getProductId());
                startActivity(intent);
            }
        });
        productsRecyclerView.setAdapter(productAdapter);

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }
        });

        // Set up sorting and filtering functionality
        setupSortSpinner();
        setupCategoryFilterSpinner();

        // Set up add product button
        addProductFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
                intent.putExtra("is_new_product", true);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        medicineDao.open();
        productList = medicineDao.getAllMedicines();
        medicineDao.close();

        // Initialize filtered list with all products
        filteredProductList = new ArrayList<>(productList);
    }

    private void filterProducts(String query) {
        // First apply category filter
        String categoryFilter = categoryFilterSpinner.getSelectedItem().toString();
        String databaseCategory = null;

        if (!categoryFilter.equals(getString(R.string.all_categories))) {
            // Convert UI category to database category
            databaseCategory = CategoryConstants.getDatabaseCategory(this, categoryFilter);
        }

        List<Medicine> categoryFilteredList = new ArrayList<>();

        if (databaseCategory == null) {
            categoryFilteredList.addAll(productList);
        } else {
            for (Medicine product : productList) {
                if (product.getCategory().equalsIgnoreCase(databaseCategory)) {
                    categoryFilteredList.add(product);
                }
            }
        }

        // Then apply search query
        filteredProductList.clear();

        if (query.isEmpty()) {
            filteredProductList.addAll(categoryFilteredList);
        } else {
            query = query.toLowerCase();
            for (Medicine product : categoryFilteredList) {
                if (product.getName().toLowerCase().contains(query) ||
                        product.getDescription().toLowerCase().contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }

        productAdapter.getProducts(filteredProductList);
        productAdapter.notifyDataSetChanged();
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortProducts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupCategoryFilterSpinner() {
        // Create a list of categories for the spinner, starting with "All Categories"
        List<String> categories = new ArrayList<>();
        categories.add(getString(R.string.all_categories));

        // Get database categories
        String[] databaseCategories = CategoryConstants.getAllDatabaseCategories();

        // Convert database categories to localized categories and add to spinner
        for (String dbCategory : databaseCategories) {
            String localizedCategory = CategoryConstants.getLocalizedCategory(this, dbCategory);
            categories.add(localizedCategory);
        }

        // Create custom adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(adapter);

        categoryFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Re-apply search filter with new category filter
                filterProducts(searchEditText.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void sortProducts(int sortOption) {
        switch (sortOption) {
            case 0: // Name (A-Z)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                break;
            case 1: // Name (Z-A)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    }
                });
                break;
            case 2: // Price (Low to High)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    }
                });
                break;
            case 3: // Price (High to Low)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return Double.compare(o2.getPrice(), o1.getPrice());
                    }
                });
                break;
            case 4: // Stock (Low to High)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return Integer.compare(o1.getStockQuantity(), o2.getStockQuantity());
                    }
                });
                break;
            case 5: // Stock (High to Low)
                Collections.sort(filteredProductList, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine o1, Medicine o2) {
                        return Integer.compare(o2.getStockQuantity(), o1.getStockQuantity());
                    }
                });
                break;
        }

        productAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh product list when returning to this activity
        loadProducts();
        productAdapter.getProducts(filteredProductList);
        productAdapter.notifyDataSetChanged();
    }
}