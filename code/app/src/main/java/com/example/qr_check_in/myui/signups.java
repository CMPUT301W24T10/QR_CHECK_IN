package com.example.qr_check_in.myui;

import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.adapters.EventAdapter;
import com.example.qr_check_in.ModelClasses.FirestoreEvent;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class signups extends Fragment {
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;

    private String deviceID;

    private List<FirestoreEvent> eventList = new ArrayList<>();

    public signups() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static signups newInstance() {
        return new signups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signups, container, false);
        eventsRecyclerView = view.findViewById(R.id.recycler_view_events); // Make sure your RecyclerView's ID is correctly set here
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(eventList);
        eventsRecyclerView.setAdapter(eventAdapter);

        fetchSignedUpEvents();
        return view;
    }

    private void findDeviceID() {
        deviceID = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void fetchSignedUpEvents() {
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
                        List<FirestoreEvent> tempEventList = new ArrayList<>(); // Temporary list to store events

                        for (String eventId : eventIDList) {
                            db.collection("events").document(eventId).get().addOnSuccessListener(eventDocumentSnapshot -> {
                                if (eventDocumentSnapshot.exists()) {
                                    Map<String, Object> eventData = eventDocumentSnapshot.getData();
                                    String eventName = eventData.getOrDefault("eventName", "").toString();
                                    String eventDescription = eventData.getOrDefault("eventDescription", "").toString();
                                    String location = eventData.getOrDefault("location", "").toString();
                                    tempEventList.add(new FirestoreEvent(eventId, eventName, eventDescription, location));
                                }

                                if (eventsProcessed.incrementAndGet() == eventIDList.size()) {
                                    // Ensure UI updates are performed on the main thread
                                    getActivity().runOnUiThread(() -> {
                                        eventList.clear();
                                        eventList.addAll(tempEventList);
                                        eventAdapter.notifyDataSetChanged();
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