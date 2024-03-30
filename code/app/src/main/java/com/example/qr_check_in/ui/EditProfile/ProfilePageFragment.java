package com.example.qr_check_in.ui.EditProfile;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePageFragment extends Fragment {

    private static final String ARG_DEVICE_ID_KEY = "device_id";

    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView profileImageView;

    private String deviceID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String fetchedName = "";
    private String fetchedAddress = "";
    private String fetchedPhoneNumber = "";
    private String fetchedImageURL = "";

    Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("Edit Profile");
    }

    private void findDeviceID() {
        deviceID = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void fetchUserDetails() {
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(getContext());

        db.collection("users").document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fetchedName = documentSnapshot.contains("Name") ?
                                documentSnapshot.getString("Name") : "";
                        String fetchedAddress = documentSnapshot.contains("Address") ?
                                documentSnapshot.getString("Address") : "";
                        String fetchedPhoneNumber = documentSnapshot.contains("Phone Number") ?
                                documentSnapshot.getString("Phone Number") : "";
                        String fetchedImageUrl = documentSnapshot.contains("profileImageUrl") ?
                                documentSnapshot.getString("profileImageUrl") : profileImageGenerator.generateImageUrlFromName(fetchedName);

                        updateUI(fetchedName, fetchedAddress, fetchedPhoneNumber, fetchedImageUrl);
                    }
                });
    }

    private void updateUI(String name, String address, String phoneNumber, String imageUrl) {
        View view = getView();
        if (view == null) return;

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        CircleImageView profileImageView = view.findViewById(R.id.profile_image);

        nameEditView.setText(name);
        addressEditView.setText(address);
        phoneNumberEditView.setText(phoneNumber);

        // Load image using Glide
        if (!imageUrl.isEmpty()) {
            Context context = getContext();
            if (context != null) {
                Glide.with(context)
                        .load(imageUrl)
                        .into(profileImageView);
            }
        }

        disableSaveButton();
    }

    private void setActionBarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    private void saveProfileChanges() {
        View view = getView();

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);

        String currentName = nameEditView.getText().toString();
        String currentAddress = addressEditView.getText().toString();
        String currentPhoneNumber = phoneNumberEditView.getText().toString();
        String currentImageURL = fetchedImageURL;

        Map<String, Object> updates = new HashMap<>();
        updates.put("Name", currentName);
        updates.put("Address", currentAddress);
        updates.put("Phone Number", currentPhoneNumber);
        updates.put("profileImageUrl", currentImageURL); // Update the image URL if necessary

        db.collection("users").document(deviceID).update(updates)
                .addOnSuccessListener(aVoid -> {
                    disableSaveButton();

                    fetchedName = currentName;
                    fetchedAddress = currentAddress;
                    fetchedPhoneNumber = currentPhoneNumber;
                    fetchedImageURL = currentImageURL;
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri != null) {
            // Create a reference to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + deviceID + ".jpg");

            // Upload the image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Save the download URL to Firestore under the event's attendees hashmap
                            saveImageUrlToFirestore(imageUrl);
                        });
                    });
        }
    }

    private void saveImageUrlToFirestore(String imageUrl) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", imageUrl);
        fetchedImageURL = imageUrl;

        saveProfileChanges();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
            uploadImageToFirebaseStorage(selectedImageUri);
        }

        View v = getView();
        Button saveButton = v.findViewById(R.id.buttonSave);

        Context context = getContext();
        if (context != null) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_buttons)));
            saveButton.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findDeviceID();
        fetchUserDetails();

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        saveButton = view.findViewById(R.id.buttonSave);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { enableSaveButton(); }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        saveButton.setOnClickListener(v -> saveProfileChanges());

        profileImageView = view.findViewById(R.id.profile_image);
        profileImageView.setOnClickListener(v -> openGallery());

        nameEditView.addTextChangedListener(textWatcher);
        addressEditView.addTextChangedListener(textWatcher);
        phoneNumberEditView.addTextChangedListener(textWatcher);

    }

    private void disableSaveButton() {
        Context context = getContext();
        if (context != null) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray)));
            saveButton.setEnabled(false);
        }
    }

    private void enableSaveButton() {
        Context context = getContext();
        if (context != null) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_buttons)));
            saveButton.setEnabled(true);
        }
    }
}