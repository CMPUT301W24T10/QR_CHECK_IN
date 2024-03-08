package com.example.qr_check_in;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginFragment extends Fragment {

    private EditText passwordEditText;
    private Button submitButton;
    private Button backButton;

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    public static AdminLoginFragment newInstance() {
        AdminLoginFragment fragment = new AdminLoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        // Initialize the UI components
        passwordEditText = view.findViewById(R.id.etAdminPassword);
        submitButton = view.findViewById(R.id.btnSubmitPassword);
        backButton = view.findViewById(R.id.btnAdminBack);

        // Set the listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                if ("admin".equals(password)) {
                    // Password is correct, replace the fragment with the next fragment
                    Navigation.findNavController(view).navigate(R.id.action_adminLoginFragment_to_adminDashboardFragment);
                } else {
                    // Password is incorrect
                    Toast.makeText(getActivity(), "Incorrect password, please try again.", Toast.LENGTH_SHORT).show();
                    passwordEditText.setText(""); // Clear the incorrect password
                }
            }
        });

        // Set the listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the previous fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
