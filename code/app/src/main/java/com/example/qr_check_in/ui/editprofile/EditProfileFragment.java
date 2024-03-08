package com.example.qr_check_in.ui.editprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.qr_check_in.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button buttonSave;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        buttonSave = root.findViewById(R.id.buttonSave);

        // Initialize Firebase Firestore and Firebase Authentication
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where the user is not authenticated
            // Redirect the user to the login page or handle it as necessary
            // For simplicity, let's display a toast message
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return root;
        }

        // Handle save button click
        buttonSave.setOnClickListener(v -> saveProfile());

        // Load existing user profile data
        loadUserProfile();

        return root;
    }

    private void loadUserProfile() {
        // Check if currentUser is null before accessing its properties
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve user document from Firestore
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Retrieve user data from document
                    String name = document.getString("name");
                    String phone = document.getString("phone");
                    String email = document.getString("email");

                    // Set retrieved user data to EditText fields
                    editTextName.setText(name);
                    editTextPhone.setText(phone);
                    editTextEmail.setText(email);
                } else {
                    Toast.makeText(getContext(), "User document does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to retrieve user document", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        // Get the edited profile information
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Update user document in Firestore with the new information
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.update("name", name, "phone", phone, "email", email)
                .addOnSuccessListener(aVoid -> {
                    // Display success message
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Display failure message
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}
