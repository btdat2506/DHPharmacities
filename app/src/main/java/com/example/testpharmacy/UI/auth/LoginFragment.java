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

import com.example.testpharmacy.UI.admin.AdminDashboardActivity;
import com.example.testpharmacy.Database.DatabaseHelper;
import com.example.testpharmacy.Database.UserDao;
import com.example.testpharmacy.Model.User;
import com.example.testpharmacy.R;
import com.example.testpharmacy.Manager.UserSessionManager;
import com.example.testpharmacy.UI.home.HomeActivity;

public class LoginFragment extends Fragment {

    private EditText emailPhoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button adminLoginButton;
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
//        adminLoginButton = view.findViewById(R.id.admin_login_button);
        forgotPasswordTextView = view.findViewById(R.id.login_forgot_password_text);
        errorTextView = view.findViewById(R.id.login_error_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

//        adminLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emailPhoneEditText.setText("admin");
//                passwordEditText.setText("admin");
//
//                Intent intent;
//                intent = new Intent(getActivity(), AdminDashboardActivity.class);
//                startActivity(intent);
//            }
//        });

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

        if (!emailPhone.isEmpty() && !password.isEmpty()) {
            userDao.open();
            Boolean checkUser = userDao.checkUserMailAndMk(emailPhone, password);

            if(checkUser) {
                long userId = userDao.getUserIdByEmail(emailPhone);
                User user = userDao.getUserById(userId);
                int numAdmin = userDao.getNumAdmin();

                UserSessionManager sessionManager = UserSessionManager.getInstance(getContext());
                sessionManager.setLogin(true);
                sessionManager.setUserId(userId);
                sessionManager.setUserEmail(emailPhone);
                //sessionManager.setAdmin(user.isAdmin());

                // Navigate based on user type
                Intent intent;
                if (userId <= numAdmin) {
                    intent = new Intent(getActivity(), AdminDashboardActivity.class);
                } else {
                    intent = new Intent(getActivity(), HomeActivity.class);
                }
                startActivity(intent);
                getActivity().finish();
            } else {
                errorTextView.setText("The email or password is incorrect.");
                errorTextView.setVisibility(View.VISIBLE);
            }
            userDao.close();
        } else {
            errorTextView.setText("Please enter email/phone and password.");
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}