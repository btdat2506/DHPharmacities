package com.example.testpharmacy.UI.auth; // Replace with your actual package name

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testpharmacy.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginSignupActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        viewPager = findViewById(R.id.login_signup_view_pager);
        tabLayout = findViewById(R.id.login_signup_tab_layout);

        // Set up ViewPager with Fragments
        LoginSignupPagerAdapter pagerAdapter = new LoginSignupPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText(getString(R.string.login_tab_text)); // "Login"
                    } else {
                        tab.setText(getString(R.string.signup_tab_text)); // "Sign Up"
                    }
                }).attach();
    }

    // PagerAdapter for Login and Signup Fragments
    private class LoginSignupPagerAdapter extends FragmentStateAdapter {

        public LoginSignupPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new LoginFragment(); // Login Fragment
            } else {
                return new SignupFragment(); // Signup Fragment
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Number of tabs/fragments (Login and Signup)
        }
    }
}