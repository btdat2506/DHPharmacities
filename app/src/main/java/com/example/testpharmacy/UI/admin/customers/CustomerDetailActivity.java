package com.example.testpharmacy.UI.admin.customers;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText medicalNoticeEditText;
    private Button saveButton;
    private Button viewOrdersButton;
    
    private UserDao userDao;
    private BillDao billDao;
//    private UserSessionManager sessionManager;
    private User customer;
    private long customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        // Initialize session manager and check admin status
//        sessionManager = UserSessionManager.getInstance(this);
//        if (!sessionManager.isAdmin()) {
//            finish();
//            return;
//        }

        // Get customer ID from intent
        customerId = getIntent().getLongExtra("customer_id", -1);
        if (customerId == -1) {
            Toast.makeText(this, getString(R.string.error_customer_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.customer_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.customer_details));

        nameEditText = findViewById(R.id.customer_detail_name_edit_text);
        emailEditText = findViewById(R.id.customer_detail_email_edit_text);
        phoneEditText = findViewById(R.id.customer_detail_phone_edit_text);
        addressEditText = findViewById(R.id.customer_detail_address_edit_text);
        medicalNoticeEditText = findViewById(R.id.customer_detail_medical_notice_edit_text);
        saveButton = findViewById(R.id.customer_detail_save_button);
        viewOrdersButton = findViewById(R.id.customer_detail_view_orders_button);

        // Initialize DAOs
        userDao = new UserDao(this);
        billDao = new BillDao(this);
        
        // Load customer data
        loadCustomerData();
        
        // Set up buttons
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomerData();
            }
        });
        
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show orders for this customer
                showCustomerOrders();
            }
        });
    }

    private void loadCustomerData() {
        userDao.open();
        customer = userDao.getUserById(customerId);
        userDao.close();
        
        if (customer != null) {
            nameEditText.setText(customer.getName());
            emailEditText.setText(customer.getEmail());
            phoneEditText.setText(customer.getPhoneNumber());
            addressEditText.setText(customer.getAddress());
            medicalNoticeEditText.setText(customer.getMedicalNotice());
        }
    }

    private void saveCustomerData() {
        if (customer == null) return;
        
        // Update customer object with form data
        customer.setName(nameEditText.getText().toString());
        customer.setEmail(emailEditText.getText().toString());
        customer.setPhoneNumber(phoneEditText.getText().toString());
        customer.setAddress(addressEditText.getText().toString());
        customer.setMedicalNotice(medicalNoticeEditText.getText().toString());
        
        // Save to database
        userDao.open();
        boolean success = userDao.updateUser(customer);
        userDao.close();
        
        if (success) {
            Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.profile_update_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomerOrders() {
        // Get orders for this customer
        billDao.open();
        List<Bill> customerBills = billDao.getBillsByUserId(customerId);
        billDao.close();
        
        if (customerBills.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_orders), Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show orders dialog or navigate to orders list filtered for this customer
        CustomerOrdersDialog dialog = new CustomerOrdersDialog(this, customerBills);
        dialog.show();
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
