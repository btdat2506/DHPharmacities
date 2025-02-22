package com.example.testpharmacy; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testpharmacy.Database.DatabaseHelper;
import com.example.testpharmacy.Database.UserDao;

public class LoginFragment extends Fragment {

    private EditText emailPhoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private TextView errorTextView;

    private UserDao userDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDao = new UserDao(getContext()); // **1. Instantiate UserDao in onCreate**
    }

    @Override
    public void onPause() {
        super.onPause();
        userDao.close(); // **4. Close database in onPause**
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity().getBaseContext());
        databaseHelper.open();

        emailPhoneEditText = view.findViewById(R.id.login_email_phone_edit_text);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        forgotPasswordTextView = view.findViewById(R.id.login_forgot_password_text);
        errorTextView = view.findViewById(R.id.login_error_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement Forgot Password functionality (for future enhancement)
                Toast.makeText(getContext(), "Forgot Password clicked (Functionality to be implemented)", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void performLogin() {
        String emailPhone = emailPhoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // TODO: Implement actual Login authentication logic here
        // For now, let's simulate a successful login for demonstration

        if (!emailPhone.isEmpty() && !password.isEmpty()) {
            // Simulate successful login
            userDao.open();
            Boolean checkUser = userDao.checkUserMailAndMk(emailPhone, password);

            if(checkUser) {
                // Navigate to Home Activity after successful login
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                getActivity().finish(); // Optional: Close LoginSignupActivity after login
            } else {
                errorTextView.setText("The email or password is incorrect.");
                errorTextView.setVisibility(View.VISIBLE);
            }
        } else {
            errorTextView.setText("Please enter email/phone and password.");
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}