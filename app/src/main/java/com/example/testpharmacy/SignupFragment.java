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
import androidx.fragment.app.Fragment;

import com.example.testpharmacy.Database.DatabaseHelper;

public class SignupFragment extends Fragment {

    private EditText emailPhoneEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signupButton;
    private TextView errorTextView;

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
            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity().getBaseContext());
            Boolean checkUserMail = databaseHelper.checkUserMail(emailPhone);

            if(checkUserMail) {
                errorTextView.setText("Tài khoản đã có người sử dụng");
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                if (password.equals(confirmPassword)) {
                    // Simulate successful signup
                    databaseHelper.addUser(emailPhone, password);

                    // Navigate to Home Activity after successful signup (or Login page if you prefer)
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // Optional: Close LoginSignupActivity after signup
                } else {
                    errorTextView.setText("Passwords do not match.");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            errorTextView.setText("Please fill in all fields.");
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}