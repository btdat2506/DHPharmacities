package com.example.testpharmacy.UI.home; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UI.auth.LoginSignupActivity;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView profileImageView;
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText medicalNoticeEditText;
    private TextInputEditText emergencyContactEditText;
    private Button saveButton;

    private UserDao userDao;
    private UserSessionManager sessionManager;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setTitle(R.string.profile_title); // Set title from string resource

        profileImageView = findViewById(R.id.profile_image_view);
        nameEditText = findViewById(R.id.profile_name_edit_text);
        emailEditText = findViewById(R.id.profile_email_edit_text);
        phoneEditText = findViewById(R.id.profile_phone_edit_text);
        addressEditText = findViewById(R.id.profile_address_edit_text);
        medicalNoticeEditText = findViewById(R.id.profile_medical_notice_edit_text);
        emergencyContactEditText = findViewById(R.id.profile_emergency_contact_edit_text);
        saveButton = findViewById(R.id.profile_save_button);

        // Initialize UserDao and session manager
        userDao = new UserDao(this);
        sessionManager = UserSessionManager.getInstance(this);

        // Check if user is logged in
        if (sessionManager.isLoggedIn()) {
            currentUserId = sessionManager.getUserId();
            userDao.open();
            User user = userDao.getUserById(currentUserId);
            userDao.close();

            if (user != null) {
                // Populate form with user data
                nameEditText.setText(user.getName());
                emailEditText.setText(user.getEmail());
                phoneEditText.setText(user.getPhoneNumber());
                addressEditText.setText(user.getAddress());
                medicalNoticeEditText.setText(user.getMedicalNotice());
            }
        } else {
            // Not logged in, show message and disable editing
            Toast.makeText(this, R.string.login_required_message, Toast.LENGTH_LONG).show();
            finish();
        }

        // Add a logout option in the toolbar if logged in
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

        // Setup save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back when back button in toolbar is pressed
        return true;
    }

    // Add logout method
    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(ProfileActivity.this, LoginSignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveProfileChanges() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.please_login_to_save, Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String medicalNotice = medicalNoticeEditText.getText().toString();

        User user = new User();
        user.setUserId(currentUserId);
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setMedicalNotice(medicalNotice);

        userDao.open();
        boolean success = userDao.updateUser(user);
        userDao.close();

        if (success) {
            Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.profile_update_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }
}