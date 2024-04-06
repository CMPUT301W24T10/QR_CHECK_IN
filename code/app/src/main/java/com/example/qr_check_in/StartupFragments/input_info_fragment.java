package com.example.qr_check_in.StartupFragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AppDatabase;

public class input_info_fragment extends Fragment {
    private EditText editTextOrganizerName, editTextEventName, editTextEventDescription;
    private RadioGroup radioGroupQRCode;
    private AppDatabase appDatabase;
    private String organizerId;
    private String eventId;
    private String deviceId;
    private ImageView posterPreview;
    private Uri posterUri;

    /**
     * ActivityResultLauncher to handle selecting an image from the device's gallery.
     * It launches an activity to select an image and handles the result, setting the selected image URI to the posterUri variable
     * and displaying the selected image in the posterPreview ImageView.
     */
    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), // Define the contract for starting an activity for result
            result -> {  // Define the callback to handle the result of the activity
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    posterUri = result.getData().getData(); // Retrieve the URI of the selected image from the result data
                    posterPreview.setImageURI(posterUri); // Set the selected image URI to the posterPreview ImageView
                    posterPreview.setVisibility(View.VISIBLE); // Make the posterPreview ImageView visible to display the selected image
                }
            });
    private int numberOfReusableQrCode;

    public input_info_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = new AppDatabase(); // Initialize AppDatabase
        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Called to create and return the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate
     *                           the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return                   The root View of the inflated layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_info_fragment, container, false);
// Initialize views
        editTextOrganizerName = view.findViewById(R.id.EnterOrganizerName);
        editTextEventName = view.findViewById(R.id.EnterEventName);
        editTextEventDescription = view.findViewById(R.id.EnterEventDescription);
        radioGroupQRCode = view.findViewById(R.id.read_status);

        posterPreview = view.findViewById(R.id.PosterPreview);

        Button uploadPosterButton = view.findViewById(R.id.uploadPosterButton);
        uploadPosterButton.setOnClickListener(v -> selectImage());

        Button confirmButton = view.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(v -> {
            boolean isNewQRCode = radioGroupQRCode.getCheckedRadioButtonId() == R.id.button_new_QRcode;
            // Check if the new QR code option is selected
            if(isNewQRCode)
                saveEventToFirestore(view); // creating a new event and save it to Firestore with a new QR code
            else{
                getNumberOfReusableQrCode(view);  // Retrieve the number of reusable QR codes
            }

        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        // Navigate back to the previous fragment
        cancelButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_checkin_createEvent_fragment2));

        return view;
    }

    /**
     * Saves the event information to Firestore database if organizer name, event name, and event description are not empty.
     *
     * @param view The current view.
     */
    private void saveEventToFirestore(View view) {
        // Get the text entered by the user for organizer name, event name, and event description
        String organizerName = editTextOrganizerName.getText().toString().trim();
        String eventName = editTextEventName.getText().toString().trim();
        String eventDescription = editTextEventDescription.getText().toString().trim();

// organizer name, event name, and event description are not empty
        if (!organizerName.isEmpty() && !eventName.isEmpty() && !eventDescription.isEmpty()) {
            appDatabase.saveOrganizer(organizerName, deviceId,getContext(), new AppDatabase.FirestoreCallback() { // Save the organizer information to Firestore database
                @Override
                public void onCallback(String documentId) {
                    organizerId = deviceId;
                    appDatabase.saveEvent(organizerId, eventName, eventDescription, posterUri, getContext(), new AppDatabase.FirestoreCallback() {  // Save the event information to Firestore database
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onCallback(String documentId) {
                            eventId = documentId;

                            if(eventId != null) {
                                Bundle bundle = new Bundle();  // Create a bundle with event ID and organizer ID
                                bundle.putString("eventId", eventId);
                                bundle.putString("organizerId", organizerId);
                                Log.d(TAG, "Navigation working fine.");
//                                idlingResource.decrement();

                                Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_displayQrCodeFragment, bundle); // Navigate to the display QR code fragment with the bundle
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Launches an intent to select an image from the device's external storage.
     */
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /**
     * Retrieves the number of reusable QR codes associated with the device ID and navigates to the appropriate fragment.
     *
     * @param view The current view.
     */
    public void getNumberOfReusableQrCode(View view) {
        appDatabase.fetchOrganizedEventIdsLength(deviceId, getContext(), new AppDatabase.FirestoreEventArrayLengthCallback() { // Fetch the length of organized event IDs associated with the device ID
            @Override
            public void onCallback(int arrayLength) {
                numberOfReusableQrCode = arrayLength; // Update the number of reusable QR codes
                if(numberOfReusableQrCode > 0) { // If there are reusable QR codes we navigate to the ReusableQRCodeFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("organizerName", editTextOrganizerName.getText().toString().trim());
                    if(organizerId == null) //if the organizer ID is null assign it to the device ID
                        organizerId = deviceId;
                    // Set the necessary data in the bundle for the ReusableQRCodeFragment
                    bundle.putString("organizerId", organizerId); // organizerId is the deviceId
                    bundle.putString("eventName", editTextEventName.getText().toString().trim());
                    bundle.putString("eventDescription", editTextEventDescription.getText().toString().trim());
                    Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_reuseQRcodeFragment, bundle);
                } else {
                    Toast.makeText(getContext(), "You don't have any reusable QR code", Toast.LENGTH_SHORT).show();
                    saveEventToFirestore(view); // creating a new event and save it to Firestore with a new QR code
                }
            }
            @Override
            public void onError(String message) {
//                numberOfReusableQrCode = 0;
            }
        });
    }
}

