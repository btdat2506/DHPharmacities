package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No need to setContentView if you are immediately redirecting

        // Start SplashScreenActivity immediately
        Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity so user can't go back to it
    }
}