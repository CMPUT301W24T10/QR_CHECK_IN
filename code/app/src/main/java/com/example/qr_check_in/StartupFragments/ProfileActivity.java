
package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_check_in.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Profile Activity in the QR Check-In application. It allows users to view and edit their profile information.
 * Users can save changes to their profile, which are then updated in Firebase Authentication.
 * Outstanding issues: The updateProfile method in saveProfileChanges() is not implemented correctly, and it needs to be fixed to update user profile information in Firebase Authentication.
 */
public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, contactEditText, addressEditText;
    private Button saveButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        contactEditText = findViewById(R.id.contactEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set onClickListener for Save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes to profile
                saveProfileChanges();
            }
        });

        // Load user's profile data
        loadProfileData();
    }

    private void saveProfileChanges() {
        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get updated profile information
            String name = nameEditText.getText().toString().trim();
            String contact = contactEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            // Create a map to update the profile
            Map<String, Object> profileUpdates = new HashMap<>();
            profileUpdates.put("name", name);
            profileUpdates.put("contact", contact);
            profileUpdates.put("address", address);

            // Update the user's profile
            mAuth.getCurrentUser().updateProfile((UserProfileChangeRequest) profileUpdates);
        }
    }

    private void loadProfileData() {
        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set retrieved profile data to EditText fields
            nameEditText.setText(currentUser.getDisplayName());
            contactEditText.setText(currentUser.getPhoneNumber());
            addressEditText.setText(currentUser.getEmail());
        }
    }
}
