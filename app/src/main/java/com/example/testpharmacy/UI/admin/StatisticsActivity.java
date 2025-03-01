package com.example.testpharmacy.UI.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testpharmacy.Database.BillDao;
import com.example.testpharmacy.Database.MedicineDao;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.Medicine;
import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.BillItem;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.Utils;
import com.example.testpharmacy.Constants.CategoryConstants;
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
    private BarChart dailySalesChart;

    private BillDao billDao;
    private UserDao userDao;
    private MedicineDao medicineDao;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize session manager
        sessionManager = UserSessionManager.getInstance(this);

        toolbar = findViewById(R.id.statistics_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.sales_statistics_title));

        totalOrdersTextView = findViewById(R.id.statistics_total_orders_text_view);
        totalRevenueTextView = findViewById(R.id.statistics_total_revenue_text_view);
        averageOrderValueTextView = findViewById(R.id.statistics_average_order_value_text_view);
        totalCustomersTextView = findViewById(R.id.statistics_total_customers_text_view);
        categorySalesChart = findViewById(R.id.statistics_category_sales_chart);
        dailySalesChart = findViewById(R.id.statistics_monthly_sales_chart);

        // Initialize DAOs
        billDao = new BillDao(this);
        userDao = new UserDao(this);
        medicineDao = new MedicineDao(this);

        // Load and display statistics
        loadStatistics();

        // Set chart theme based on current theme
        setupChartThemes();
    }

    private void setupChartThemes() {
        // Get the current theme's text color
        int textColor = getResources().getColor(
                isUsingDarkTheme() ? android.R.color.white : android.R.color.black
        );

        // Configure PieChart
        categorySalesChart.setEntryLabelColor(textColor);
        categorySalesChart.setCenterTextColor(textColor);
        categorySalesChart.getLegend().setTextColor(textColor);
        categorySalesChart.getDescription().setTextColor(textColor);

        // Configure BarChart
        dailySalesChart.getXAxis().setTextColor(textColor);
        dailySalesChart.getAxisLeft().setTextColor(textColor);
        dailySalesChart.getAxisRight().setTextColor(textColor);
        dailySalesChart.getLegend().setTextColor(textColor);
        dailySalesChart.getDescription().setTextColor(textColor);
    }

    private boolean isUsingDarkTheme() {
        // Check if night mode is active
        return (getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES;
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
        totalRevenueTextView.setText(Utils.formatVND(totalRevenue));
        averageOrderValueTextView.setText(Utils.formatVND(averageOrderValue));
        totalCustomersTextView.setText(String.valueOf(customerCount));

        // Create category sales chart
        createCategorySalesChart(allOrders);

        // Create daily sales chart
        createDailySalesChart(allOrders);
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

        // Create pie chart entries with localized category names
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categorySales.entrySet()) {
            // Convert database category to localized category
            String localizedCategory = CategoryConstants.getLocalizedCategory(this, entry.getKey());
            entries.add(new PieEntry(entry.getValue().floatValue(), localizedCategory));
        }

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.sales_by_category));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(isUsingDarkTheme() ? Color.WHITE : Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);

        categorySalesChart.setData(data);
        categorySalesChart.getDescription().setEnabled(false);
        categorySalesChart.setCenterText(getString(R.string.sales_by_category));
        categorySalesChart.animateY(1000);
        categorySalesChart.invalidate();
    }

    private void createDailySalesChart(List<Bill> orders) {
        // Calculate sales by day for the past 7 days
        Map<String, Double> dailySales = new HashMap<>();

        // Get the last 7 days
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        // Initialize with 0 values for the last 7 days
        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String dayName = dayFormat.format(calendar.getTime());
            dailySales.put(dayName, 0.0);
        }

        // Reset calendar to current
        calendar = Calendar.getInstance();

        // Calculate actual sales for each day
        for (Bill order : orders) {
            Date orderDate = order.getOrderDate();
            Calendar orderCalendar = Calendar.getInstance();
            orderCalendar.setTime(orderDate);

            // Only consider orders from the past 7 days
            Calendar sevenDaysAgo = Calendar.getInstance();
            sevenDaysAgo.add(Calendar.DAY_OF_YEAR, -7);

            if (orderDate.after(sevenDaysAgo.getTime())) {
                String dayName = dayFormat.format(orderDate);
                double currentSales = dailySales.getOrDefault(dayName, 0.0);
                dailySales.put(dayName, currentSales + order.getTotalAmount());
            }
        }

        // Create bar chart entries
        List<BarEntry> entries = new ArrayList<>();
        List<String> days = new ArrayList<>();

        int index = 0;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);

        for (int i = 0; i < 7; i++) {
            String dayName = dayFormat.format(calendar.getTime());
            double sales = dailySales.getOrDefault(dayName, 0.0);
            entries.add(new BarEntry(index++, (float) sales));
            days.add(dayName);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.daily_sales));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(isUsingDarkTheme() ? Color.WHITE : Color.BLACK);

        BarData data = new BarData(dataSet);

        dailySalesChart.setData(data);
        dailySalesChart.getDescription().setEnabled(false);
        dailySalesChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
        dailySalesChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        dailySalesChart.getXAxis().setGranularity(1f);
        dailySalesChart.getXAxis().setLabelCount(days.size());
        dailySalesChart.animateY(1000);
        dailySalesChart.invalidate();
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