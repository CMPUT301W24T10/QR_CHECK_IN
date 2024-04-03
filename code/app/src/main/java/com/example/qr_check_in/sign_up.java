package com.example.qr_check_in;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.data.AdminData;
import com.example.qr_check_in.data.EventNameIdPair;
import com.example.qr_check_in.data.EventSignUpHandler;
import com.example.qr_check_in.data.User_name;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class sign_up extends Fragment {

    private AdminData adminData;
    private ListView eventListView;
    private ArrayList<String> events;
    private ArrayAdapter<String> eventAdapter;
    private List<EventNameIdPair> eventIdPairList;
    private int selectedPosition;
    private User_name userNameFetcher; // Assuming User_name is your UserNameFetcher class
    private EventSignUpHandler eventSignUpHandler; // Ensure this class is implemented as described

    private String deviceId;

    public sign_up() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventSignUpHandler = new EventSignUpHandler(); // Initialize the EventSignUpHandler
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        adminData = new AdminData();
        events = new ArrayList<>();
        eventIdPairList = new ArrayList<>();
        selectedPosition = -1;
        userNameFetcher = new User_name(); // Assuming this fetches or creates a user

        eventListView = root.findViewById(R.id.list_of_events);
        eventAdapter = new ArrayAdapter<>(getContext(), R.layout.attendee_list_element, events);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }
        });

        adminData.fetchEventNames(new AdminData.EventFetchListener() {
            @Override
            public void onEventListFetched(List<EventNameIdPair> eventList) {
                eventIdPairList = eventList;
                Log.e("EventList", eventList.toString());
                for (EventNameIdPair event : eventList) {
                    events.add(event.getEventName());
                }
                eventAdapter.notifyDataSetChanged();
            }
        });

        Button signUpButton = root.findViewById(R.id.sg_button);
        signUpButton.setOnClickListener(v -> {
            if (selectedPosition < 0 || selectedPosition >= eventIdPairList.size()) {
                Toast.makeText(getContext(), "Please select an event to sign up for.", Toast.LENGTH_SHORT).show();
                return;
            }
            EventNameIdPair selectedEvent = eventIdPairList.get(selectedPosition);

            deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            userNameFetcher.getUserNameOrCreateGuest(deviceId, new User_name.UserNameFetchListener() {
                @Override
                public void onUserNameFetched(String userName) {
                    // Use the eventSignUpHandler here
                    eventSignUpHandler.signUpUserForEvent(deviceId, userName, selectedEvent, new EventSignUpHandler.SignUpCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Signed up successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(), "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return root;
    }
}
