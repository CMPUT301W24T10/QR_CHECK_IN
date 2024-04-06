package com.example.qr_check_in.ui.EventList;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.ModelClasses.Event2;
import com.example.qr_check_in.ModelClasses.Event3;
import com.example.qr_check_in.adapters.EventAdapter;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EventListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private String deviceID;
    private List<Event3> eventList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        // Populate your event list here
        populateEventList();

        return rootView;
    }

    private void findDeviceID() {
        deviceID = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void populateEventList() {
        findDeviceID();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(deviceID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null && data.containsKey("eventsSignedUpFor")) {
                    Object eventsSignedUpForObj = data.get("eventsSignedUpFor");
                    if (eventsSignedUpForObj instanceof Map) {
                        Map<String, String> eventsSignedUpFor = (Map<String, String>) eventsSignedUpForObj;
                        List<String> eventIDList = new ArrayList<>(eventsSignedUpFor.keySet());
                        AtomicInteger eventsProcessed = new AtomicInteger(0);
                        List<Event3> tempEventList = new ArrayList<>(); // Temporary list to store events

                        for (String eventId : eventIDList) {
                            db.collection("events").document(eventId).get().addOnSuccessListener(eventDocumentSnapshot -> {
                                if (eventDocumentSnapshot.exists()) {
                                    Map<String, Object> eventData = eventDocumentSnapshot.getData();
                                    String eventName = eventData.getOrDefault("eventName", "").toString();
                                    String eventDescription = eventData.getOrDefault("eventDescription", "").toString();
                                    String location = eventData.getOrDefault("location", "").toString();
                                    String posterURL = eventData.getOrDefault("posterURL", "").toString();
                                    tempEventList.add(new Event3(eventName, eventDescription, location, posterURL));
                                }

                                if (eventsProcessed.incrementAndGet() == eventIDList.size()) {
                                    // Ensure UI updates are performed on the main thread
                                    getActivity().runOnUiThread(() -> {
                                        eventList.clear();
                                        eventList.addAll(tempEventList);
                                        adapter.notifyDataSetChanged();
                                    });
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}