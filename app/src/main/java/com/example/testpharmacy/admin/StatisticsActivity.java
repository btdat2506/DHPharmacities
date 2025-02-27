package com.example.testpharmacy.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Medicine;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.BillItem;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UserSessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView totalOrdersTextView;
    private TextView totalRevenueTextView;
    private TextView averageOrderValueTextView;
    private TextView totalCustomersTextView;
    private PieChart categorySalesChart;
    private BarChart monthlySalesChart;
    
    private BillDao billDao;
    private UserDao userDao;
    private MedicineDao medicineDao;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize session manager and check admin status
        sessionManager = UserSessionManager.getInstance(this);
        if (!sessionManager.isAdmin()) {
            finish();
            return;
        }

        toolbar = findViewById(R.id.statistics_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Statistics");

        totalOrdersTextView = findViewById(R.id.statistics_total_orders_text_view);
        totalRevenueTextView = findViewById(R.id.statistics_total_revenue_text_view);
        averageOrderValueTextView = findViewById(R.id.statistics_average_order_value_text_view);
        totalCustomersTextView = findViewById(R.id.statistics_total_customers_text_view);
        categorySalesChart = findViewById(R.id.statistics_category_sales_chart);
        monthlySalesChart = findViewById(R.id.statistics_monthly_sales_chart);

        // Initialize DAOs
        billDao = new BillDao(this);
        userDao = new UserDao(this);
        medicineDao = new MedicineDao(this);
        
        // Load and display statistics
        loadStatistics();
    }

    private void loadStatistics() {
        // Get all orders
        billDao.open();
        List<Bill> allOrders = billDao.getAllBills();
        billDao.close();
        
        // Get all customers
        userDao.open();
        int customerCount = userDao.getCustomerCount();
        userDao.close();
        
        // Calculate total orders and revenue
        int totalOrders = allOrders.size();
        double totalRevenue = 0;
        
        for (Bill order : allOrders) {
            totalRevenue += order.getTotalAmount();
        }
        
        // Calculate average order value
        double averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        
        // Display summary statistics
        totalOrdersTextView.setText(String.valueOf(totalOrders));
        totalRevenueTextView.setText(String.format(Locale.getDefault(), "%.2f đ", totalRevenue));
        averageOrderValueTextView.setText(String.format(Locale.getDefault(), "%.2f đ", averageOrderValue));
        totalCustomersTextView.setText(String.valueOf(customerCount));
        
        // Create category sales chart
        createCategorySalesChart(allOrders);
        
        // Create monthly sales chart
        createMonthlySalesChart(allOrders);
    }

    private void createCategorySalesChart(List<Bill> orders) {
        // Calculate sales by category
        Map<String, Double> categorySales = new HashMap<>();
        
        medicineDao.open();
        List<Medicine> allProducts = medicineDao.getAllMedicines();
        medicineDao.close();
        
        // Create product ID to category map for quick lookup
        Map<Long, String> productCategories = new HashMap<>();
        for (Medicine product : allProducts) {
            productCategories.put(product.getProductId(), product.getCategory());
        }
        
        // Calculate sales by category
        for (Bill order : orders) {
            for (BillItem item : order.getBillItems()) {
                String category = productCategories.get(item.getProductId());
                if (category != null) {
                    double currentSales = categorySales.getOrDefault(category, 0.0);
                    categorySales.put(category, currentSales + item.getTotalPrice());
                }
            }
        }
        
        // Create pie chart entries
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categorySales.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "Sales by Category");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        
        PieData data = new PieData(dataSet);
        
        categorySalesChart.setData(data);
        categorySalesChart.getDescription().setEnabled(false);
        categorySalesChart.setCenterText("Sales by Category");
        categorySalesChart.animateY(1000);
        categorySalesChart.invalidate();
    }

    private void createMonthlySalesChart(List<Bill> orders) {
        // Calculate sales by month for the past 6 months
        Map<String, Double> monthlySales = new HashMap<>();
        
        // Get the last 6 months
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        
        // Initialize with 0 values
        for (int i = 5; i >= 0; i--) {
            calendar.add(Calendar.MONTH, -1);
            String monthName = monthFormat.format(calendar.getTime());
            monthlySales.put(monthName, 0.0);
        }
        
        // Reset calendar to current
        calendar = Calendar.getInstance();
        
        // Calculate actual sales for each month
        for (Bill order : orders) {
            Date orderDate = order.getOrderDate();
            Calendar orderCalendar = Calendar.getInstance();
            orderCalendar.setTime(orderDate);
            
            // Only consider orders from the past 6 months
            Calendar sixMonthsAgo = Calendar.getInstance();
            sixMonthsAgo.add(Calendar.MONTH, -6);
            
            if (orderDate.after(sixMonthsAgo.getTime())) {
                String monthName = monthFormat.format(orderDate);
                double currentSales = monthlySales.getOrDefault(monthName, 0.0);
                monthlySales.put(monthName, currentSales + order.getTotalAmount());
            }
        }
        
        // Create bar chart entries
        List<BarEntry> entries = new ArrayList<>();
        List<String> months = new ArrayList<>();
        
        int index = 0;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -5);
        
        for (int i = 0; i < 6; i++) {
            String monthName = monthFormat.format(calendar.getTime());
            double sales = monthlySales.getOrDefault(monthName, 0.0);
            entries.add(new BarEntry(index++, (float) sales));
            months.add(monthName);
            calendar.add(Calendar.MONTH, 1);
        }
        
        BarDataSet dataSet = new BarDataSet(entries, "Monthly Sales");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);
        
        BarData data = new BarData(dataSet);
        
        monthlySalesChart.setData(data);
        monthlySalesChart.getDescription().setEnabled(false);
        monthlySalesChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        monthlySalesChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        monthlySalesChart.getXAxis().setGranularity(1f);
        monthlySalesChart.getXAxis().setLabelCount(months.size());
        monthlySalesChart.animateY(1000);
        monthlySalesChart.invalidate();
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
