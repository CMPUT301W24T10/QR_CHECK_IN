package com.example.qr_check_in.StartupFragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri; // Import if you're planning to handle Uri for posters.
import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.Settings;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class input_info_fragment extends Fragment {
    private EditText editTextOrganizerName, editTextEventName, editTextEventDescription;
    private RadioGroup radioGroupQRCode;
    private AppDatabase appDatabase;
    private String organizerId;
    private String eventId;
    private String deviceId;
    private ImageView posterPreview;
    private Uri posterUri;

    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    posterUri = result.getData().getData();
                    posterPreview.setImageURI(posterUri);
                    posterPreview.setVisibility(View.VISIBLE);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_info_fragment, container, false);

        // Initialization code remains unchanged
        editTextOrganizerName = view.findViewById(R.id.EnterOrganizerName);
        editTextEventName = view.findViewById(R.id.EnterEventName);
        editTextEventDescription = view.findViewById(R.id.EnterEventDescription);
        radioGroupQRCode = view.findViewById(R.id.read_status);

        posterPreview = view.findViewById(R.id.PosterPreview);

        Button uploadPosterButton = view.findViewById(R.id.uploadPosterButton);
        uploadPosterButton.setOnClickListener(v -> selectImage());

        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            boolean isNewQRCode = radioGroupQRCode.getCheckedRadioButtonId() == R.id.readRadioButton;
            if(isNewQRCode)
                saveEventToFirestore(view); // creating a new event and save it to Firestore with a new QR code
            else{
                getNumberOfReusableQrCode(view);
            }

        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_checkin_createEvent_fragment2));

        return view;
    }

    private void saveEventToFirestore(View view) {
        String organizerName = editTextOrganizerName.getText().toString().trim();
        String eventName = editTextEventName.getText().toString().trim();
        String eventDescription = editTextEventDescription.getText().toString().trim();


        if (!organizerName.isEmpty() && !eventName.isEmpty() && !eventDescription.isEmpty()) {
            appDatabase.saveOrganizer(organizerName, deviceId,getContext(), new AppDatabase.FirestoreCallback() {
                @Override
                public void onCallback(String documentId) {
                    organizerId = deviceId;
                    appDatabase.saveEvent(organizerId, eventName, eventDescription, posterUri, getContext(), new AppDatabase.FirestoreCallback() {
                        @Override
                        public void onCallback(String documentId) {
                            eventId = documentId;


                            if(eventId != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("eventId", eventId);
                                bundle.putString("organizerId", organizerId);
                                Log.d(TAG, "Navigation working fine.");
                                Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_displayQrCodeFragment, bundle);
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    public void getNumberOfReusableQrCode(View view) {
        appDatabase.fetchOrganizedEventIdsLength(deviceId, getContext(), new AppDatabase.FirestoreEventArrayLengthCallback() {
            @Override
            public void onCallback(int arrayLength) {
                numberOfReusableQrCode = arrayLength;
                if(numberOfReusableQrCode > 0) {
                    // Navigate to ReusableQRCodeFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("organizerName", editTextOrganizerName.getText().toString().trim());
                    if(organizerId == null)
                        organizerId = deviceId;
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

