package com.example.qr_check_in.ui.EditProfile;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePageFragment extends Fragment {

    private static final String ARG_DEVICE_ID_KEY = "device_id";

    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView profileImageView;

    private String deviceID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String fetchedName = "";
    private String fetchedEmailAddress = "";
    private String fetchedPhoneNumber = "";
    private String fetchedHomepage = "";
    private String fetchedImageURL = "";
    Uri selectedImageUri;
    private Button saveButton;
    private Button removeProfilePicButton;

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
                        fetchedName = documentSnapshot.contains("Name") ?
                                documentSnapshot.getString("Name") : "";
                        fetchedEmailAddress = documentSnapshot.contains("Email Address") ?
                                documentSnapshot.getString("Email Address") : "";
                        fetchedPhoneNumber = documentSnapshot.contains("Phone Number") ?
                                documentSnapshot.getString("Phone Number") : "";
                        fetchedHomepage = documentSnapshot.contains("Homepage") ?
                                documentSnapshot.getString("Homepage") : "";

                        if (documentSnapshot.contains("profileImageUrl") && !Objects.equals(documentSnapshot.getString("profileImageUrl"), "")) {
                            enableButton(removeProfilePicButton);
                            selectedImageUri = Uri.parse(documentSnapshot.getString("profileImageUrl"));
                            fetchedImageURL = documentSnapshot.getString("profileImageUrl");
                        } else {
                            selectedImageUri = null;
                            fetchedImageURL = profileImageGenerator.generateImageUrlFromName(fetchedName);
                        }


                        updateUI();
                    }
                });
    }

    private void updateUI() {
        View view = getView();
        if (view == null) return;

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText emailAddressEditView = view.findViewById(R.id.editTextEmailAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        EditText homepageEditView = view.findViewById(R.id.editTextHomepage);
        CircleImageView profileImageView = view.findViewById(R.id.profile_image);

        nameEditView.setText(fetchedName);
        emailAddressEditView.setText(fetchedEmailAddress);
        phoneNumberEditView.setText(fetchedPhoneNumber);
        homepageEditView.setText(fetchedHomepage);

        // Load image using Glide
        if (!fetchedImageURL.isEmpty()) {
            Context context = getContext();
            if (context != null) {
                Glide.with(context)
                        .load(fetchedImageURL)
                        .into(profileImageView);
            }
        }

        disableButton(saveButton);
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
        EditText emailAddressEditView = view.findViewById(R.id.editTextEmailAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        EditText homepageEditView = view.findViewById(R.id.editTextHomepage);

        String currentName = nameEditView.getText().toString();
        String currentEmailAddress = emailAddressEditView.getText().toString();
        String currentPhoneNumber = phoneNumberEditView.getText().toString();
        String currentHomepage = homepageEditView.getText().toString();

        Map<String, Object> updates = new HashMap<>();
        updates.put("Name", currentName);
        updates.put("Email Address", currentEmailAddress);
        updates.put("Phone Number", currentPhoneNumber);
        updates.put("Homepage", currentHomepage);

        uploadImageToFirebaseStorage(selectedImageUri);

        db.collection("users").document(deviceID).update(updates);

        disableButton(saveButton);
        fetchedName = currentName;
        fetchedEmailAddress = currentEmailAddress;
        fetchedPhoneNumber = currentPhoneNumber;
        fetchedHomepage = currentHomepage;

        if (selectedImageUri == null) {
            removeProfilePic();
        }
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
                            selectedImageUri = uri;
                            saveImageUrlToFirestore(uri.toString());
                        });
                    });
        }
    }

    private void saveImageUrlToFirestore(String imageUrl) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", imageUrl);
        db.collection("users").document(deviceID).update(updates);
        fetchedImageURL = imageUrl;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
            enableButton(removeProfilePicButton);
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
        EditText emailAddressEditView = view.findViewById(R.id.editTextEmailAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        EditText homepageEditView = view.findViewById(R.id.editTextHomepage);
        saveButton = view.findViewById(R.id.buttonSave);
        removeProfilePicButton = view.findViewById(R.id.buttonRemovePic);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { enableButton(saveButton); }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        saveButton.setOnClickListener(v -> saveProfileChanges());
        removeProfilePicButton.setOnClickListener(v -> removeProfilePic());

        profileImageView = view.findViewById(R.id.profile_image);
        profileImageView.setOnClickListener(v -> openGallery());

        nameEditView.addTextChangedListener(textWatcher);
        emailAddressEditView.addTextChangedListener(textWatcher);
        phoneNumberEditView.addTextChangedListener(textWatcher);
        homepageEditView.addTextChangedListener(textWatcher);

    }

    private void removeProfilePic() {
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(getContext());
        fetchedImageURL = profileImageGenerator.generateImageUrlFromName(fetchedName);
        selectedImageUri = null;

        if (!fetchedImageURL.isEmpty()) {
            Context context = getContext();
            if (context != null) {
                Glide.with(context)
                        .load(fetchedImageURL)
                        .into(profileImageView);
            }
        }

        db.collection("users").document(deviceID).update("profileImageUrl", "");

        disableButton(removeProfilePicButton);
    }

    private void disableButton(Button button) {
        Context context = getContext();
        if (context != null) {
            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray)));
            button.setEnabled(false);
        }
    }

    private void enableButton(Button button) {
        Context context = getContext();
        if (context != null) {
            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_buttons)));
            button.setEnabled(true);
        }
    }
}