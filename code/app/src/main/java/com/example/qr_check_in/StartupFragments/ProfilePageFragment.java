package com.example.qr_check_in.StartupFragments;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    private static final String ARG_DEVICE_ID_KEY = "device_id";
    private static final String ARG_EVENT_ID_KEY = "event_id";

    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView profileImageView;

    private String deviceID;
    private String eventID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String fetchedName = "";
    private String fetchedAddress = "";
    private String fetchedPhoneNumber = "";
    private String fetchedImageURL = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided device ID.
     *
     * @param deviceId The device ID.
     * @return A new instance of fragment ProfilePageFragment.
     */
    public static ProfilePageFragment newInstance(String deviceId, String eventId) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID_KEY, deviceId);
        args.putString(ARG_EVENT_ID_KEY, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            deviceID = getArguments().getString(ARG_DEVICE_ID_KEY, "");
            eventID = getArguments().getString(ARG_EVENT_ID_KEY, "");
        }
        setActionBarTitle("Edit Profile");
    }

    private void fetchAttendeesDetails() {
        db.collection("events").document(eventID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                        Object attendeesObject = documentSnapshot.getData().get("attendees");

                        if (attendeesObject instanceof Map) {
                            Map<String, Object> attendees = (Map<String, Object>) attendeesObject;

                            for (Map.Entry<String, Object> entry : attendees.entrySet()) {
                                Object attendeeObject = entry.getValue();

                                if (attendeeObject instanceof Map) {
                                    Map<String, Object> attendeeDetails = (Map<String, Object>) attendeeObject;
                                    String fetchedName = (String) attendeeDetails.getOrDefault("Name", "");
                                    String fetchedAddress = (String) attendeeDetails.getOrDefault("Address", "");
                                    String fetchedPhoneNumber = (String) attendeeDetails.getOrDefault("Phone Number", "");
                                    String fetchedImageUrl = (String) attendeeDetails.getOrDefault("profileImageUrl", "");

                                    // Update the UI with fetched details
                                    updateUI(fetchedName, fetchedAddress, fetchedPhoneNumber, fetchedImageUrl);
                                }
                            }
                        }
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
        Button saveButton = view.findViewById(R.id.buttonSave);



        String currentName = nameEditView.getText().toString();
        String currentAddress = addressEditView.getText().toString();
        String currentPhoneNumber = phoneNumberEditView.getText().toString();
        String currentImageURL = fetchedImageURL;

        Context context = getContext();
        if (context != null) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray)));
            saveButton.setEnabled(false);
        }

        Map<String, Object> updatedAttendeeDetails = new HashMap<>();
        updatedAttendeeDetails.put("Name", currentName);
        updatedAttendeeDetails.put("Address", currentAddress);
        updatedAttendeeDetails.put("Phone Number", currentPhoneNumber);
        updatedAttendeeDetails.put("profileImageUrl", currentImageURL);

        // Wrap the updated details in the attendees map for the update
        Map<String, Object> updates = new HashMap<>();
        updates.put("attendees." + deviceID, updatedAttendeeDetails);

        // Update the Firestore document
        db.collection("events").document(eventID).update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Update the fetched values
                    fetchedName = currentName;
                    fetchedAddress = currentAddress;
                    fetchedPhoneNumber = currentPhoneNumber;
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
        db.collection("events").document(eventID).get()
         .addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                Object attendeesObject = documentSnapshot.getData().get("attendees");

                if (attendeesObject instanceof Map) {
                    Map<String, Object> updatedAttendeeDetails = new HashMap<>();
                    updatedAttendeeDetails.put("Name", fetchedName);
                    updatedAttendeeDetails.put("Address", fetchedAddress);
                    updatedAttendeeDetails.put("Phone Number", fetchedPhoneNumber);
                    updatedAttendeeDetails.put("profileImageUrl", imageUrl);
                    fetchedImageURL = imageUrl;
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("attendees." + deviceID, updatedAttendeeDetails);
                    db.collection("events").document(eventID).update(updates);
                }
            }
         });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            android.net.Uri selectedImageUri = data.getData();
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
        fetchAttendeesDetails();

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        Button saveButton = view.findViewById(R.id.buttonSave);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = getContext();
                if (context != null) {
                    saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_buttons)));
                    saveButton.setEnabled(true);
                }
            }

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
}