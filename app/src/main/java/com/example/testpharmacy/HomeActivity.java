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

    // Placeholder for medicine categories
    private List<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar); // Set Toolbar as ActionBar

        tabLayout = findViewById(R.id.home_category_tabs);
        viewPager = findViewById(R.id.home_view_pager);
        cartBadgeCountTextView = findViewById(R.id.cart_badge_count);
        cartIconContainer = findViewById(R.id.cart_icon_container);

        // Initialize category names (replace with actual categories)
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

        // Initially set cart badge count to 0 (you'll update this dynamically)
        updateCartBadgeCount(0);
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
            // TODO: Create and return Fragment for each category based on position
            // For now, returning a generic category fragment (replace with actual category fragments)
            switch (position) {
                case 0:
                    return new CategoryPainRelieversFragment(); // Example: Pain Relievers Fragment
                // Add cases for other categories and return respective fragments
                default:
                    return new GenericCategoryFragment(); // Placeholder for other categories
            }
        }

        @Override
        public int getItemCount() {
            return categoryNames.size(); // Number of categories
        }
    }

    // Generic Placeholder Fragment for categories (Replace with actual category fragments)
    public static class GenericCategoryFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // You can inflate a default layout here if needed, or return null if the fragment is dynamically populated
            return inflater.inflate(R.layout.fragment_category_pain_relievers, container, false); // Reusing pain relievers layout as a placeholder
        }
    }
}