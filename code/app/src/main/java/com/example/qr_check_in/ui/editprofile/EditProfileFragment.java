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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button buttonSave;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef; // Reference to the current user's document in Firestore
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        buttonSave = root.findViewById(R.id.buttonSave);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Handle save button click
        buttonSave.setOnClickListener(v -> saveProfile());

        // Load existing user profile data
        loadUserProfile();

        return root;
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = db.collection("users").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    String email = documentSnapshot.getString("email");

                    // Set user's name in the EditText field
                    editTextName.setText(name);

                    // Populate phone and email fields if available
                    editTextPhone.setText(phone != null ? phone : "");
                    editTextEmail.setText(email != null ? email : "");
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(getContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Update the user document in Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);

        if (!phone.isEmpty()) {
            userData.put("phone", phone);
        }

        if (!email.isEmpty()) {
            userData.put("email", email);
        }

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = db.collection("users").document(userId);

            userRef.update(userData)
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
}
