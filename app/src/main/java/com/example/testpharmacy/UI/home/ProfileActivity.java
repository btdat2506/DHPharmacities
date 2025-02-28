package com.example.testpharmacy.UI.home; // Replace with your actual package name

import android.os.Bundle;
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
            Toast.makeText(this, "You must log in to view your profile", Toast.LENGTH_LONG).show();
            finish();
        }

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

//    private void populateProfileFields() {
//        // Set placeholder data for EditTexts (replace with actual user profile data retrieval)
//        nameEditText.setText("John Doe");
//        emailEditText.setText("john.doe@example.com");
//        phoneEditText.setText("+1 123-456-7890");
//        addressEditText.setText("123 Main Street, City, State, ZIP");
//        medicalNoticeEditText.setText("No known allergies or medical conditions");
//        emergencyContactEditText.setText("Jane Doe, +1 987-654-3210");
//    }

    /*private void saveProfileChanges() {
        // Get the updated values from EditTexts
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String medicalNotice = medicalNoticeEditText.getText().toString();
        String emergencyContact = emergencyContactEditText.getText().toString();

        // TODO: Implement logic to save profile changes (e.g., to database, shared preferences, etc.)
        // For now, just show a Toast message confirming save
        Toast.makeText(this, "Profile changes saved!", Toast.LENGTH_SHORT).show();

        // Optionally, you could update the UI to reflect the saved changes if needed
        // For example, if you had TextViews to display the profile after editing.
    }*/

    private void saveProfileChanges() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please log in to save changes", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}