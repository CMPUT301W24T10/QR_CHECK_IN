package com.example.qr_check_in.ui.EditProfile;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import com.bumptech.glide.Glide;

/**
 * This class represents the Edit Profile Fragment in the QR Check-In application.
 @@ -30,102 +19,52 @@
 */
public class EditProfileFragment extends Fragment {

    private EditText editTextName, editTextPhone, editTextEmail;
    private Button buttonSave;
    private ImageView imageViewProfile;

    private FirebaseFirestore db;
    private ProfileImageGenerator profileImageGenerator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        db = FirebaseFirestore.getInstance();
        profileImageGenerator = new ProfileImageGenerator(getContext());

        editTextName = root.findViewById(R.id.editTextName);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        buttonSave = root.findViewById(R.id.buttonSave);
        imageViewProfile = root.findViewById(R.id.imageViewProfile);

        buttonSave.setOnClickListener(v -> saveProfile());
        loadUserProfile();

        return root;
    }

    private void loadUserProfile() {
        String deviceId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("Name");
                        String phone = documentSnapshot.getString("Phone");
                        String email = documentSnapshot.getString("Email");
                        String profileImageURL = documentSnapshot.getString("profileImageURL");

                        editTextName.setText(name);
                        editTextPhone.setText(phone != null ? phone : "");
                        editTextEmail.setText(email != null ? email : "");

                        if (profileImageURL == null || profileImageURL.isEmpty()) {
                            if (name != null && !name.isEmpty()) {
                                // Now using ProfileImageGenerator to get the URL
                                profileImageURL = profileImageGenerator.generateImageUrlFromName(name);
                                // Immediately update Firestore with the new URL and defaultProfileImage
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("defaultProfileImage", profileImageURL);
                                String ProfileImageURL = profileImageURL;
                                db.collection("users").document(deviceId)
                                        .set(userData, SetOptions.merge())
                                        .addOnSuccessListener(aVoid -> {
                                            // Update imageViewProfile using Glide
                                            Glide.with(requireContext()).load(ProfileImageURL).into(imageViewProfile);
                                        })
                                        .addOnFailureListener(e -> Log.e("EditProfileFragment", "Error updating document", e));
                            }
                        } else {
                            // Update imageViewProfile using Glide
                            Glide.with(requireContext()).load(profileImageURL).into(imageViewProfile);
                        }
                    } else {
                        Toast.makeText(getContext(), "User details not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



    private void saveProfile() {
        // Get the edited profile information
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Create a data object to update the document
        Map<String, Object> userData = new HashMap<>();
        userData.put("Name", name);
        userData.put("Phone", phone);
        userData.put("Email", email);

        // Get the current device ID
        String deviceId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Update the document in Firestore
        db.collection("users").document(deviceId)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Document updated successfully
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error occurred while updating document
                    Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}