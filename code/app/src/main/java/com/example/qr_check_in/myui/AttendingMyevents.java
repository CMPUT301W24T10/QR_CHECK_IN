package com.example.qr_check_in.myui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qr_check_in.EventAdapter;
import com.example.qr_check_in.ModelClasses.FirestoreEvent;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class AttendingMyevents extends Fragment {

    private RecyclerView AttendingEventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<FirestoreEvent> AttendingEventList = new ArrayList<>();

    public AttendingMyevents() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendingevents, container, false);
        AttendingEventsRecyclerView = view.findViewById(R.id.recycler_view_attending_events);
        AttendingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(AttendingEventList);
        AttendingEventsRecyclerView.setAdapter(eventAdapter);

        fetchAttendingEvents();
        return view;
    }

    private void fetchAttendingEvents() {
        String deviceId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(deviceId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null && data.containsKey("AttendingEvents")) {
                    Object organizedEventIdsObj = data.get("AttendingEvents");
                    if (organizedEventIdsObj instanceof Map) {
                        Map<String, String> organizedEventIds = (Map<String, String>) organizedEventIdsObj;
                        List<String> eventIDList = new ArrayList<>(organizedEventIds.keySet());
                        AtomicInteger eventsProcessed = new AtomicInteger(0);
                        List<FirestoreEvent> tempEventList = new ArrayList<>();

                        for (String eventId : eventIDList) {
                            db.collection("events").document(eventId).get().addOnSuccessListener(eventDocumentSnapshot -> {
                                if (eventDocumentSnapshot.exists()) {
                                    Map<String, Object> eventData = eventDocumentSnapshot.getData();
                                    String eventName = eventData.getOrDefault("eventName", "").toString();
                                    String eventDescription = eventData.getOrDefault("eventDescription", "").toString();
                                    String location = eventData.getOrDefault("location", "").toString();
                                    tempEventList.add(new FirestoreEvent(eventName, eventDescription, location));
                                }
                                if (eventsProcessed.incrementAndGet() == eventIDList.size()) {
                                    // Ensure UI updates are performed on the main thread
                                    getActivity().runOnUiThread(() -> {
                                        AttendingEventList.clear();
                                        AttendingEventList.addAll(tempEventList);
                                        eventAdapter.notifyDataSetChanged();
                                    });
                                }
                            }).addOnFailureListener(e -> {
                                // Handle the error
                            });
                        }
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // Handle the error
        });
    }



}