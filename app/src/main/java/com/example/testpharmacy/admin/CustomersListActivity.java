package com.example.testpharmacy.admin;

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

import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UserSessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomersListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView customersRecyclerView;
    private CustomerAdapter customerAdapter;
    private List<User> customerList;
    private List<User> filteredCustomerList;
    private EditText searchEditText;
    private Spinner sortSpinner;
    
    private UserDao userDao;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        // Initialize session manager and check admin status
        sessionManager = UserSessionManager.getInstance(this);
        if (!sessionManager.isAdmin()) {
            finish();
            return;
        }

        toolbar = findViewById(R.id.customers_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customers");

        customersRecyclerView = findViewById(R.id.customers_recycler_view);
        searchEditText = findViewById(R.id.customers_search_edit_text);
        sortSpinner = findViewById(R.id.customers_sort_spinner);

        // Set up RecyclerView
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize UserDao
        userDao = new UserDao(this);
        
        // Load customers from database
        loadCustomers();
        
        // Set up adapter
        customerAdapter = new CustomerAdapter(filteredCustomerList, new CustomerAdapter.OnCustomerClickListener() {
            @Override
            public void onCustomerClick(User customer) {
                Intent intent = new Intent(CustomersListActivity.this, CustomerDetailActivity.class);
                intent.putExtra("customer_id", customer.getUserId());
                startActivity(intent);
            }
        });
        customersRecyclerView.setAdapter(customerAdapter);
        
        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterCustomers(s.toString());
            }
        });
        
        // Set up sorting functionality
        setupSortSpinner();
    }

    private void loadCustomers() {
        userDao.open();
        customerList = userDao.getAllUsers();
        userDao.close();
        
        // Remove admin users from the list
        List<User> nonAdminUsers = new ArrayList<>();
        for (User user : customerList) {
            if (!user.isAdmin()) {
                nonAdminUsers.add(user);
            }
        }
        customerList = nonAdminUsers;
        
        // Initialize filtered list with all customers
        filteredCustomerList = new ArrayList<>(customerList);
    }

    private void filterCustomers(String query) {
        filteredCustomerList.clear();
        
        if (query.isEmpty()) {
            filteredCustomerList.addAll(customerList);
        } else {
            query = query.toLowerCase();
            for (User customer : customerList) {
                if (customer.getName().toLowerCase().contains(query) || 
                    customer.getEmail().toLowerCase().contains(query) ||
                    (customer.getPhoneNumber() != null && customer.getPhoneNumber().contains(query))) {
                    filteredCustomerList.add(customer);
                }
            }
        }
        
        customerAdapter.notifyDataSetChanged();
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.customer_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortCustomers(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void sortCustomers(int sortOption) {
        switch (sortOption) {
            case 0: // Name (A-Z)
                Collections.sort(filteredCustomerList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                break;
            case 1: // Name (Z-A)
                Collections.sort(filteredCustomerList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    }
                });
                break;
            case 2: // Email (A-Z)
                Collections.sort(filteredCustomerList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o1.getEmail().compareToIgnoreCase(o2.getEmail());
                    }
                });
                break;
            case 3: // ID (Ascending)
                Collections.sort(filteredCustomerList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return Long.compare(o1.getUserId(), o2.getUserId());
                    }
                });
                break;
            case 4: // ID (Descending)
                Collections.sort(filteredCustomerList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return Long.compare(o2.getUserId(), o1.getUserId());
                    }
                });
                break;
        }
        
        customerAdapter.notifyDataSetChanged();
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
        // Refresh customer list when returning to this activity
        loadCustomers();
        customerAdapter.setCustomers(filteredCustomerList);
        customerAdapter.notifyDataSetChanged();
    }
}
