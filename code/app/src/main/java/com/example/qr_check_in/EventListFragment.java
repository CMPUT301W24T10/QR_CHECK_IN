package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.admin.EventListAdapter;
import com.example.qr_check_in.data.AdminData;
import com.example.qr_check_in.data.EventNameIdPair;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {
    private AdminData adminData;
    private ListView eventListView;
    private ArrayList<Event> events;
    private ArrayAdapter<Event> eventAdapter;

    private int selectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_list, container, false);
        adminData = new AdminData();
        events = new ArrayList<Event>();

        selectedPosition = -1;
        eventListView = root.findViewById(R.id.list_of_events);
        eventAdapter = new EventListAdapter(getContext(), events);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Save the clicked position in the member variable
                selectedPosition = position;

            }
        });
        ImageButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the hosting Activity's onBackPressed method
                getActivity().onBackPressed();
            }
        });

        root.findViewById(R.id.buttonConfirmSelectedDeleteEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition == -1) {
                    // toast a message to select an event
                    Toast.makeText(getContext(), "Please select an event to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                // get the selected event
                Event selectedEvent = events.get(selectedPosition);
                // Delete the event from the database
                adminData.deleteEvent(selectedEvent.getEventID(), new AdminData.EventDeletionListener() {
                    @Override
                    public void onEventDeletionSuccess(String documentId) {
                        Log.e("EventDeletion", "Event with document ID " + documentId + " deleted successfully");
                        // Remove the event from the list
                        events.remove(selectedPosition);
                        selectedPosition = -1;
                        eventAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });

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



        return root;
    }
}