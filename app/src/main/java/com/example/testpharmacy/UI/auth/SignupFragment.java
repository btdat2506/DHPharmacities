package com.example.testpharmacy.UI.auth; // Replace with your actual package name

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

import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.UI.home.HomeActivity;

public class SignupFragment extends Fragment {

    private EditText emailPhoneEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signupButton;
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

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailPhoneEditText = view.findViewById(R.id.signup_email_phone_edit_text);
        passwordEditText = view.findViewById(R.id.signup_password_edit_text);
        confirmPasswordEditText = view.findViewById(R.id.signup_confirm_password_edit_text);
        signupButton = view.findViewById(R.id.signup_button);
        errorTextView = view.findViewById(R.id.signup_error_text);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });

        return view;
    }

    private void performSignup() {
        String emailPhone = emailPhoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // TODO: Implement actual Signup authentication logic here
        // For now, let's simulate a successful signup if passwords match

        if (!emailPhone.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            userDao.open();
            Boolean checkUserMail = userDao.checkUserMail(emailPhone);

            if(checkUserMail) {
                errorTextView.setText(getString(R.string.signup_error_account_exists));
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                if (password.equals(confirmPassword)) {
                    // Simulate successful signup
                    User newUser = new User();
                    newUser.setEmail(emailPhone);
                    newUser.setPassword(password);
                    long userId = userDao.createUser(newUser);

                    if(userId != -1) {
                        Toast.makeText(getContext(), getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                    } else {
                        errorTextView.setText(getString(R.string.signup_failed));
                        errorTextView.setVisibility(View.VISIBLE);
                    }

                    // Navigate to Home Activity after successful signup (or Login page if you prefer)
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // Optional: Close LoginSignupActivity after signup
                } else {
                    errorTextView.setText(getString(R.string.signup_error_password_mismatch));
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            errorTextView.setText(getString(R.string.signup_error_fill_fields));
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}