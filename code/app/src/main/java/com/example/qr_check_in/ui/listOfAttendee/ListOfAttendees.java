package com.example.qr_check_in.ui.listOfAttendee;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AttendeeInfo;
import com.example.qr_check_in.databinding.FragmentGalleryBinding;
import com.example.qr_check_in.databinding.FragmentListOfAttendeesBinding;
import com.example.qr_check_in.ui.gallery.GalleryViewModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListOfAttendees extends Fragment {
    private AttendeeInfo attendeeInfo;
    private ListOfAttendeesViewModel mViewModel;
    private FragmentListOfAttendeesBinding binding;
    private ListView attendeeList;
    private ArrayList<CheckInCount> attendees;
    private ArrayAdapter<CheckInCount> attendeeAdapter;
    private String eventId;

    public static ListOfAttendees newInstance() {
        return new ListOfAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_of_attendees, container, false);
        attendeeInfo = new AttendeeInfo();
        attendees = new ArrayList<>();
        ListOfAttendeesViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(ListOfAttendeesViewModel.class);
        eventId = sharedViewModel.getEventId();

        attendeeList = root.findViewById(R.id.list_of_attendees);
        // Create an ArrayAdapter from List of String
        attendeeAdapter = new ListOfAttendeeAdapter(getContext(), attendees);
        // DataBind ListView with items from ArrayAdapter
        attendeeList.setAdapter(attendeeAdapter);

        attendeeInfo.getAttendeesMap(eventId, new AttendeeInfo.getAttendeesMapCallback() {
            @Override
            public void onCallback(Map<String, String> attendeesMap, Map<String, Long> checkInCountMap) {
                Log.d("gotAttendees", "Got attendeesMap");
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Initialize attendeesList as a new ArrayList
                            attendees.clear();

                            // Add all the values from attendeesMap directly to attendees
//                            attendees.addAll(attendeesMap.values());
                            // create a new CheckInCount object for each attendee and add it to the attendees list
                            Log.d("attendeeMap", ""+ attendeesMap);
                            Log.d("countMap", ""+ checkInCountMap);
                            for (Map.Entry<String, String> entry : attendeesMap.entrySet()) {
                                Log.d("forLoop", ""+ checkInCountMap.get(entry.getKey()).getClass().getName() + " " + entry.getValue());
                                Log.d("objCreation", "success");
                                attendees.add(new CheckInCount(checkInCountMap.get(entry.getKey()), entry.getValue()));
                            }

                            Log.d("attendeesSet", "attendeesSet");

                            // Notify the adapter about the data change
                            attendeeAdapter.notifyDataSetChanged();
                            TextView textView = root.findViewById(R.id.header_list_of_attendees);
                            textView.setText("Current Attendees Count: " + attendees.size());
                        }
                    });
                }

            }
        });



        return root;
    }


}