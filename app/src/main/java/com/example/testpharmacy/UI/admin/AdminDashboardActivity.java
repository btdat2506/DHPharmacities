package com.example.testpharmacy.UI.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.testpharmacy.R;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.UI.admin.customers.CustomersListActivity;
import com.example.testpharmacy.UI.admin.orders.OrdersListActivity;
import com.example.testpharmacy.UI.admin.products.ProductManagementActivity;
import com.example.testpharmacy.UI.auth.LoginSignupActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView adminNameTextView;
    private CardView customersCard;
    private CardView ordersCard;
    private CardView productsCard;
    private CardView statisticsCard;
    
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize session manager
        sessionManager = UserSessionManager.getInstance(this);

        toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.admin_dashboard));

        adminNameTextView = findViewById(R.id.admin_name_text_view);
        customersCard = findViewById(R.id.customers_card);
        ordersCard = findViewById(R.id.orders_card);
        productsCard = findViewById(R.id.products_card);
        statisticsCard = findViewById(R.id.statistics_card);

        // Display admin name
        adminNameTextView.setText(getString(R.string.hello_admin));

        // Set click listeners for cards
        customersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, CustomersListActivity.class);
                startActivity(intent);
            }
        });

        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, OrdersListActivity.class);
                startActivity(intent);
            }
        });

        productsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ProductManagementActivity.class);
                startActivity(intent);
            }
        });

        statisticsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_logout) {
            sessionManager.logout();
            Intent intent = new Intent(AdminDashboardActivity.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
