package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qr_check_in.R;

import com.example.qr_check_in.data.AppDatabase;

public class input_info_fragment extends Fragment {
    private EditText editTextOrganizerName, editTextEventName, editTextEventDescription;
    private RadioGroup radioGroupQRCode;
    private AppDatabase appDatabase; // Use AppDatabase for database interactions
    private String organizerId;
    private String eventId;

    public input_info_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = new AppDatabase(); // Initialize AppDatabase
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

        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            saveEventToFirestore(view);
        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_checkin_createEvent_fragment2));

        return view;
    }

    private void saveEventToFirestore(View view) {
        String organizerName = editTextOrganizerName.getText().toString().trim();
        String eventName = editTextEventName.getText().toString().trim();
        String eventDescription = editTextEventDescription.getText().toString().trim();
        boolean isNewQRCode = radioGroupQRCode.getCheckedRadioButtonId() == R.id.readRadioButton;

        if (!organizerName.isEmpty() && !eventName.isEmpty() && !eventDescription.isEmpty()) {
            appDatabase.saveOrganizer(organizerName, getContext(), new AppDatabase.FirestoreCallback() {
                @Override
                public void onCallback(String documentId) {
                    organizerId = documentId;
                    appDatabase.saveEvent(organizerId, eventName, eventDescription, isNewQRCode, getContext(), new AppDatabase.FirestoreCallback() {
                        @Override
                        public void onCallback(String documentId) {
                            eventId = documentId;

                            if(eventId != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("eventId", eventId);
                                bundle.putString("organizerId", organizerId);
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
}
