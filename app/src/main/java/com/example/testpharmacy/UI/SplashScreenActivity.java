package com.example.testpharmacy.UI; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testpharmacy.R;
import com.example.testpharmacy.UI.home.HomeActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1500; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity (Login/Signup in this case)
                Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);

//                Intent i = new Intent(SplashScreenActivity.this, AdminDashboardActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}