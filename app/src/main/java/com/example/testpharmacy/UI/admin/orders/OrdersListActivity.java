package com.example.testpharmacy.UI.admin.orders;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdersListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Bill> orderList;
    private List<Bill> filteredOrderList;
    private EditText searchEditText;
    private Spinner sortSpinner;
    private Spinner statusFilterSpinner;
    
    private BillDao billDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        toolbar = findViewById(R.id.orders_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.orders));

        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        searchEditText = findViewById(R.id.orders_search_edit_text);
        sortSpinner = findViewById(R.id.orders_sort_spinner);
        statusFilterSpinner = findViewById(R.id.orders_status_filter_spinner);

        // Set up RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize BillDao
        billDao = new BillDao(this);
        
        // Load orders from database
        loadOrders();
        
        // Set up adapter
        orderAdapter = new OrderAdapter(filteredOrderList, new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(Bill order) {
                Intent intent = new Intent(OrdersListActivity.this, OrderDetailActivity.class);
                intent.putExtra("order_number", order.getOrderNumber());
                startActivity(intent);
            }
        });
        ordersRecyclerView.setAdapter(orderAdapter);
        
        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterOrders(s.toString());
            }
        });
        
        // Set up sorting and filtering functionality
        setupSortSpinner();
        setupStatusFilterSpinner();
    }

    private void loadOrders() {
        billDao.open();
        orderList = billDao.getAllBills();
        billDao.close();
        
        // Initialize filtered list with all orders
        filteredOrderList = new ArrayList<>(orderList);
    }

    private void filterOrders(String query) {
        // First apply status filter
        String statusFilter = statusFilterSpinner.getSelectedItem().toString();
        
        List<Bill> statusFilteredList = new ArrayList<>();
        
        if (statusFilter.equals("All Statuses")) {
            statusFilteredList.addAll(orderList);
        } else {
            for (Bill order : orderList) {
                if (order.getStatus().equalsIgnoreCase(statusFilter)) {
                    statusFilteredList.add(order);
                }
            }
        }
        
        // Then apply search query
        filteredOrderList.clear();
        
        if (query.isEmpty()) {
            filteredOrderList.addAll(statusFilteredList);
        } else {
            query = query.toLowerCase();
            for (Bill order : statusFilteredList) {
                if (order.getOrderNumber().toLowerCase().contains(query) || 
                    order.getShippingName().toLowerCase().contains(query) ||
                    order.getShippingPhone().contains(query)) {
                    filteredOrderList.add(order);
                }
            }
        }
        
        orderAdapter.notifyDataSetChanged();
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortOrders(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupStatusFilterSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_status_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusFilterSpinner.setAdapter(adapter);
        
        statusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Re-apply search filter with new status filter
                filterOrders(searchEditText.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void sortOrders(int sortOption) {
        switch (sortOption) {
            case 0: // Date (Newest First)
                Collections.sort(filteredOrderList, new Comparator<Bill>() {
                    @Override
                    public int compare(Bill o1, Bill o2) {
                        return o2.getOrderDate().compareTo(o1.getOrderDate());
                    }
                });
                break;
            case 1: // Date (Oldest First)
                Collections.sort(filteredOrderList, new Comparator<Bill>() {
                    @Override
                    public int compare(Bill o1, Bill o2) {
                        return o1.getOrderDate().compareTo(o2.getOrderDate());
                    }
                });
                break;
            case 2: // Total Amount (High to Low)
                Collections.sort(filteredOrderList, new Comparator<Bill>() {
                    @Override
                    public int compare(Bill o1, Bill o2) {
                        return Double.compare(o2.getTotalAmount(), o1.getTotalAmount());
                    }
                });
                break;
            case 3: // Total Amount (Low to High)
                Collections.sort(filteredOrderList, new Comparator<Bill>() {
                    @Override
                    public int compare(Bill o1, Bill o2) {
                        return Double.compare(o1.getTotalAmount(), o2.getTotalAmount());
                    }
                });
                break;
            case 4: // Order Number
                Collections.sort(filteredOrderList, new Comparator<Bill>() {
                    @Override
                    public int compare(Bill o1, Bill o2) {
                        return o1.getOrderNumber().compareTo(o2.getOrderNumber());
                    }
                });
                break;
        }
        
        orderAdapter.notifyDataSetChanged();
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
        // Refresh order list when returning to this activity
        loadOrders();
        orderAdapter.setOrders(filteredOrderList);
        orderAdapter.notifyDataSetChanged();
        //filterOrders(searchEditText.getText().toString());
    }
}
