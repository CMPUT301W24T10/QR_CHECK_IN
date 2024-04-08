package com.example.qr_check_in.ui.attendeeSignUps;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AttendeeInfo;
import com.example.qr_check_in.data.EventDetailsFetcher;
import com.example.qr_check_in.databinding.FragmentListOfAttendeesBinding;
import com.example.qr_check_in.ui.listOfAttendee.CheckInCount;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeeAdapter;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendees;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeesViewModel;

import java.util.ArrayList;
import java.util.Map;

public class AttendeeSignUpList extends Fragment {
    private EventDetailsFetcher eventDetailsFetcher;
    private ListOfAttendeesViewModel mViewModel;
    private FragmentListOfAttendeesBinding binding;
    private ListView attendeeList;
    private ArrayList<String> attendees;
    private ArrayAdapter<String> attendeeAdapter;

    public static ListOfAttendees newInstance() {
        return new ListOfAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_of_attendees, container, false);
        eventDetailsFetcher = new EventDetailsFetcher();
        attendees = new ArrayList<>();
        ListOfAttendeesViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(ListOfAttendeesViewModel.class);
        String eventId = (sharedViewModel.getEventId());
        eventId = sharedViewModel.getEventId();

        attendeeList = root.findViewById(R.id.list_of_attendees);
        // Create an ArrayAdapter from List of String
        attendeeAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.fragment_event_signup_list_item, attendees);
        // DataBind ListView with items from ArrayAdapter
        attendeeList.setAdapter(attendeeAdapter);

        eventDetailsFetcher.fetchEventSignups(eventId, new EventDetailsFetcher.OnEventSignupsReceivedListener() {
            @Override
            public void onEventSignupsReceived(ArrayList<String> attendee) {
                Log.d("gotAttendees", "Got attendeesMap");
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Initialize attendeesList as a new ArrayList
                            attendees.clear();

                            // Add all the values from attendeesMap directly to attendees
                            attendees.addAll(attendee);
                            Log.e("attendees", attendees.toString());
                            // Notify the adapter about the data change
                            attendeeAdapter.notifyDataSetChanged();
                            TextView textView = root.findViewById(R.id.header_list_of_attendees);
                            textView.setText("Current SignUps: " + attendees.size());
                        }
                    });
                }
            }
        });
        return root;
    }
}
