package com.example.qr_check_in;

import static com.example.qr_check_in.constants.SELECTEDEVENTIDREQUIRED;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.data.AppDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ReuseQRcodeFragment extends Fragment {
    private Map<String, Object> eventDetails;
    private List<String> eventIds;

    private AppDatabase db;
    private String selectedEventId; // QR code is the eventId


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new AppDatabase();
        eventDetails = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reuse_q_rcode, container, false);

        if (requireArguments().getString("organizerId") == null) {
            Log.e("OrganizerIdError", "Organizer ID is null");
        }
        if (requireArguments().getString("eventName") == null) {
            Log.e("EventNameError", "Event name is null");
        }
        Log.e("EventDescription", "onCreateView: " + requireArguments().getString("eventDescription"));
        eventDetails.put("organizerId",requireArguments().getString("organizerId"));
        eventDetails.put("eventName",requireArguments().getString("eventName"));
        eventDetails.put("eventDescription",requireArguments().getString("eventDescription"));

        // Call getEvents to fetch eventIds and setup ListView
        getEvents(view);   // this is making the app crash

        Button confirmButton = view.findViewById(R.id.buttonConfirmSelectedQRcode); // Assuming the button's ID is confirmButton
        confirmButton.setOnClickListener(v -> {
            if (selectedEventId != null) {
                // Perform the action with the selected eventId
                updateEventDetails(selectedEventId);
                // provide navigation to the next activity
                Bundle bundle = new Bundle();
                bundle.putString("eventId", selectedEventId);
                bundle.putString("organizerId", (String)eventDetails.get("organizerId"));
                Navigation.findNavController(view).navigate(R.id.displayQrCodeFragment, bundle);
            } else {
                Toast.makeText(getContext(), "Please select an event first", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    public void getEvents(View view) {
        ListView listView = view.findViewById(R.id.ListOfQRCodes); // Find ListView by ID

        db.fetchOrganizedEventIds((String) eventDetails.get("organizerId"), new AppDatabase.FirestoreFetchArrayCallback() {
            @Override
            public void onCallback(List<String> eventIdss) {
                // Update the UI (ListView) with the fetched eventIds
                // Must be run on UI thread
                eventIds = eventIdss;
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, eventIds);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        String eventId = eventIds.get(position);
                        // Navigate to the DisplayQrCodeFragment
                        selectedEventId = eventIds.get(position); // Save the selected eventId
                        SELECTEDEVENTIDREQUIRED = eventIds.get(position);
                        // Optionally, you can give visual feedback here or log the selected item
                        Log.d("SelectedEventId", "Selected event ID: " + selectedEventId);
                    });
                });
            }

            @Override
            public void onError(String message) {
                Log.e("FirestoreError", message);
            }
        });
    }

    public void updateEventDetails(String eventId) {
        db.updateEvent(eventId, (String)eventDetails.get("eventName"), (String)eventDetails.get("eventDescription"), getContext());
    }
}