package com.example.qr_check_in;

import static com.example.qr_check_in.constants.SELECTEDEVENTIDREQUIRED;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.adapters.ReuseQrAdapter;
import com.example.qr_check_in.data.AppDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ReuseQRcodeFragment extends Fragment {
    private Map<String, Object> eventDetails;
    private ListView listView;

    private ArrayAdapter<Event> reuseQrAdapter;
    private ArrayList<Event> events;

    private AppDatabase db;
    private String selectedEventId; // QR code is the eventId
    private Event selectedEvent;
    private Uri posterUri;


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

        // setting up the list view with adapter and list of events
        events= new ArrayList<Event>();
        reuseQrAdapter = new ReuseQrAdapter(getContext(),events);
        listView = view.findViewById(R.id.ListOfQRCodes);
        listView.setAdapter(reuseQrAdapter);

        ImageButton backButton = view.findViewById(R.id.backButton);
        // Set an OnClickListener to the ImageButton
        backButton.setOnClickListener(v -> {
            // Navigate back using NavController or simply pop the back stack
            if (getActivity() != null) {
                getActivity().onBackPressed();
                // If using NavController, you would navigate back like this:
                // Navigation.findNavController(view).navigateUp();
            }
        });


        if (requireArguments().getString("organizerId") == null) {
            Log.e("OrganizerIdError", "Organizer ID is null");
        }
        if (requireArguments().getString("eventName") == null) {
            Log.e("EventNameError", "Event name is null");
        }
        eventDetails.put("organizerId",requireArguments().getString("organizerId"));
        eventDetails.put("eventName",requireArguments().getString("eventName"));
        eventDetails.put("eventDescription",requireArguments().getString("eventDescription"));
        eventDetails.put("eventLocation",requireArguments().getString("eventLocation"));
        String posterUriString = requireArguments().getString("posterUri");
        if (posterUriString != null && !posterUriString.isEmpty()) {
            posterUri = Uri.parse(posterUriString);
        } else {
            posterUri = null; // Explicitly set to null if not provided or empty
        }
        getEvents(view);

        Button confirmButton = view.findViewById(R.id.buttonConfirmSelectedQRcode); // Assuming the button's ID is confirmButton
        confirmButton.setOnClickListener(v -> {
            if (selectedEventId != null) {
                // Perform the action with the selected eventId
                updateEventDetails();
                // provide navigation to the next activity
                Bundle bundle = new Bundle();
                bundle.putString("eventId", selectedEventId);
                bundle.putString("organizerId", (String)eventDetails.get("organizerId"));
                bundle.putString("UserType", "Organizer");
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
            public void onCallback(Map<String, String> eventIdss) {
                events.clear();
                for (Map.Entry<String, String> entry : eventIdss.entrySet()) {
                    events.add(new Event(entry.getValue(), null,null,entry.getKey(), null));
                }
                reuseQrAdapter.notifyDataSetChanged();
                getActivity().runOnUiThread(() -> {
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        // Navigate to the DisplayQrCodeFragment
                        selectedEvent = events.get(position);
                        selectedEventId = selectedEvent.getEventID();
                        SELECTEDEVENTIDREQUIRED = selectedEvent.getEventID();
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

    public void updateEventDetails() {
        // Only parse the Uri if the string is not null, otherwise, pass null to updateEvent
        Uri posterUriToPass = (posterUri != null) ? Uri.parse(posterUri.toString()) : null;

        db.updateEvent(selectedEventId, (String) eventDetails.get("eventName"),
                (String) eventDetails.get("eventDescription"),
                (String) eventDetails.get("eventLocation"),
                posterUriToPass, getContext());

        db.updateOrganizerWithEvent((String) eventDetails.get("organizerId"),
                selectedEventId, (String) eventDetails.get("eventName"), getContext());
    }

}