package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testpharmacy.Database.DatabaseHelper;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.User;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DatabaseHelper databaseHelper = new DatabaseHelper(this.getBaseContext());
        databaseHelper.open();

        // Initialize session manager and check login status
        sessionManager = UserSessionManager.getInstance(this);
        isLoggedIn = sessionManager.isLoggedIn();

        // Fix User Session
        userDao = new UserDao(this);
        userDao.open();
        User isUser = null;
        if(isLoggedIn) isUser = userDao.getUserById(sessionManager.getUserId());
        userDao.close();
        if(isUser == null || (isUser.getUserId() == 1)) isLoggedIn = false;


        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar); // Set Toolbar as ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Add this line to remove default title

        tabLayout = findViewById(R.id.home_category_tabs);
        viewPager = findViewById(R.id.home_view_pager);
        cartBadgeCountTextView = findViewById(R.id.cart_badge_count);
        cartIconContainer = findViewById(R.id.cart_icon_container);
        profileIconContainer = findViewById(R.id.profile_icon_container);

        // Initialize category names (replace with actual categories)
        categoryNames.add("All Items"); // Add "All Items" as the first category
        categoryNames.add("Pain Relievers");
        categoryNames.add("Antibiotics");
        categoryNames.add("Vitamins");
        categoryNames.add("Cold & Flu");
        categoryNames.add("First Aid");
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
                    // If logged in, go to ProfileActivity
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // If not logged in, go to LoginSignupActivity
                    Intent intent = new Intent(HomeActivity.this, LoginSignupActivity.class);
                    startActivity(intent);
                }
            }
        });

        updateCartBadgeCount(CartManager.getInstance().getCartItems().size());

        // Add a logout option in the toolbar if logged in
        if (isLoggedIn) {
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_logout) {
                        logout();
                        return true;
                    }
                    return false;
                }
            });
        }
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
        if (isLoggedIn) {
            getMenuInflater().inflate(R.menu.home_menu, menu);
        }
        return true;
    }

    // Add logout method
    private void logout() {
        sessionManager.logout();
        isLoggedIn = false;
        Intent intent = new Intent(HomeActivity.this, LoginSignupActivity.class);
        startActivity(intent);
        finish();
    }
}