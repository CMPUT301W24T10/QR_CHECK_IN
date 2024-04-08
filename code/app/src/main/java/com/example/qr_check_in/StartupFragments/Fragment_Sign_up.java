package com.example.qr_check_in.StartupFragments;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.R;
import com.example.qr_check_in.admin.EventListAdapter;
import com.example.qr_check_in.data.AdminData;
import com.example.qr_check_in.data.EventNameIdPair;
import com.example.qr_check_in.data.EventSignUpHandler;
import com.example.qr_check_in.data.User_name;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Sign_up extends Fragment {

    private AdminData adminData;
    private ListView eventListView;
    private ArrayList<Event> events;
    private ArrayAdapter<Event> eventAdapter;
    private int selectedPosition;
    private User_name userNameFetcher; // Assuming User_name is your UserNameFetcher class
    private EventSignUpHandler eventSignUpHandler; // Ensure this class is implemented as described

    private String deviceId;

    public Fragment_Sign_up() {
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

        selectedPosition = -1;
        userNameFetcher = new User_name(); // Assuming this fetches or creates a user

        eventListView = root.findViewById(R.id.list_of_events);
        eventAdapter = new EventListAdapter(getContext(), events);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }
        });

        adminData.fetchEventNames(new AdminData.EventFetchListener() {
            @Override
            public void onEventListFetched(ArrayList<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                eventAdapter.notifyDataSetChanged();
            }
        });

        ImageButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        Button signUpButton = root.findViewById(R.id.sg_button);
        signUpButton.setOnClickListener(v -> {
            if (selectedPosition < 0 || selectedPosition >= events.size()) {
                Toast.makeText(getContext(), "Please select an event to sign up for.", Toast.LENGTH_SHORT).show();
                return;
            }
            Event selectedEvent = events.get(selectedPosition);

            deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            userNameFetcher.getUserNameOrCreateGuest(deviceId, new User_name.UserNameFetchListener() {
                @Override
                public void onUserNameFetched(String userName) {
                    // Use the eventSignUpHandler here
//                    eventSignUpHandler.signUpUserForEvent(deviceId, userName, selectedEvent, new EventSignUpHandler.SignUpCallback() {
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(getContext(), "Signed up successfully!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            Toast.makeText(getContext(), "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    eventSignUpHandler.checkSpace(deviceId, userName, selectedEvent, new EventSignUpHandler.CheckSpace() {
                        @Override
                        public void onSpaceAvailable() {
                            Toast.makeText(getContext(), "Signed up successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSpaceUnavailable() {
                            Toast.makeText(getContext(), "Sign Up fail, Event full", Toast.LENGTH_SHORT).show();
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
