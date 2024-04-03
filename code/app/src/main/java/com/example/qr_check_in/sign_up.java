package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.data.AdminData;
import com.example.qr_check_in.data.EventNameIdPair;

import java.util.ArrayList;
import java.util.List;


public class sign_up extends Fragment {

    private AdminData adminData;
    private ListView eventListView;
    private ArrayList<String> events;
    private ArrayAdapter<String> eventAdapter;
    private List<EventNameIdPair> eventIdPairList;
    private int selectedPosition;

    public sign_up() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        adminData = new AdminData();
        events = new ArrayList<>();
        eventIdPairList = new ArrayList<>();
        selectedPosition = -1;
        eventListView = root.findViewById(R.id.list_of_events);
        eventAdapter = new ArrayAdapter<>(getContext(), R.layout.attendee_list_element, events);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Save the clicked position in the member variable
                selectedPosition = position;

            }
        });



        adminData.fetchEventNames(new AdminData.EventFetchListener() {
            @Override
            public void onEventListFetched(List<EventNameIdPair> eventList) {
                eventIdPairList = eventList;
                Log.e("EventList",eventList.toString());
                for (EventNameIdPair event : eventList) {
                    events.add(event.getEventName());
                }
                eventAdapter.notifyDataSetChanged();
            }
        });



        return root;
    }
}