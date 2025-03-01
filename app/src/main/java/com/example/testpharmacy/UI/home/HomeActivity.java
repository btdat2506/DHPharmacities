package com.example.testpharmacy.UI.home; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testpharmacy.UI.admin.AdminDashboardActivity;
import com.example.testpharmacy.UI.cart.CartActivity;
import com.example.testpharmacy.UI.cart.CartManager;
import com.example.testpharmacy.Database.DatabaseHelper;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.UI.auth.LoginSignupActivity;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView cartBadgeCountTextView;
    private View cartIconContainer;
    private View profileIconContainer;

    // At the class level:
    private UserSessionManager sessionManager;

    // Placeholder for medicine categories
    private List<String> categoryNames = new ArrayList<>();

    // --- Simulate User Login Status ---
    private boolean isLoggedIn = false; // Initially set to false (user not logged in)
    // In a real app, you would check actual authentication status here
    private UserDao userDao;
    private boolean isAdmin = false; // Initially set to false (user is not an admin)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DatabaseHelper databaseHelper = new DatabaseHelper(this.getBaseContext());
        databaseHelper.open();

        // Initialize session manager and check login status
        sessionManager = UserSessionManager.getInstance(this);
        isLoggedIn = sessionManager.isLoggedIn();
        isAdmin = sessionManager.isAdmin();

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar); // Set Toolbar as ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Add this line to remove default title

        tabLayout = findViewById(R.id.home_category_tabs);
        viewPager = findViewById(R.id.home_view_pager);
        cartBadgeCountTextView = findViewById(R.id.cart_badge_count);
        cartIconContainer = findViewById(R.id.cart_icon_container);
        profileIconContainer = findViewById(R.id.profile_icon_container);

        // Initialize category names (replace with actual categories)
        categoryNames.add(getString(R.string.category_all_items)); // Add "All Items" as the first category
        categoryNames.add(getString(R.string.category_pain_relievers));
        categoryNames.add(getString(R.string.category_antibiotics));
        categoryNames.add(getString(R.string.category_vitamins));
        categoryNames.add(getString(R.string.category_cold_flu));
        categoryNames.add(getString(R.string.category_first_aid));
        // ... add more categories

        // Set up ViewPager with Category Fragments
        CategoryPagerAdapter pagerAdapter = new CategoryPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager and set Tab Titles
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(categoryNames.get(position)); // Set tab title from category names
                }).attach();

        // Set click listener for the cart icon
        cartIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the profile icon - MODIFIED
        profileIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) { // Check login status
                    // If Admin, go to AdminDashboardActivity, else go to ProfileActivity
                    Intent intent;
                    if (isAdmin) {
                        intent = new Intent(HomeActivity.this, AdminDashboardActivity.class);
                    } else {
                        intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    }
                    startActivity(intent);
                } else {
                    // If not logged in, go to LoginSignupActivity
                    Intent intent = new Intent(HomeActivity.this, LoginSignupActivity.class);
                    startActivity(intent);
                }
            }
        });
        updateCartBadgeCount(CartManager.getInstance().getCartItems().size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the badge count when returning to this activity
        updateCartBadgeCount(CartManager.getInstance().getCartItems().size());
    }

    // Method to update the cart badge count
    public void updateCartBadgeCount(int count) {
        if (count > 0) {
            cartBadgeCountTextView.setText(String.valueOf(count));
            cartBadgeCountTextView.setVisibility(View.VISIBLE);
        } else {
            cartBadgeCountTextView.setVisibility(View.GONE);
        }
    }

    // PagerAdapter for Category Fragments
    private class CategoryPagerAdapter extends FragmentStateAdapter {
        public CategoryPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            String categoryName = categoryNames.get(position); // Get category name for this position
            return GenericCategoryFragment.newInstance(categoryName); // Create GenericCategoryFragment with category name
        }

        @Override
        public int getItemCount() {
            return categoryNames.size(); // Number of categories
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Find the search item and configure SearchView
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Configure search functionality
        searchView.setQueryHint(getString(R.string.search_medicine_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Perform search as user types
                performSearch(newText);
                return true;
            }
        });

        return true;
    }

    private void performSearch(String query) {
        // Get the current ViewPager fragment
        int currentPosition = viewPager.getCurrentItem();
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentByTag("f" + currentPosition);

        if (currentFragment instanceof GenericCategoryFragment) {
            GenericCategoryFragment categoryFragment = (GenericCategoryFragment) currentFragment;

            // Call a new method in GenericCategoryFragment to filter medicines
            categoryFragment.filterMedicines(query);
        }
    }
}