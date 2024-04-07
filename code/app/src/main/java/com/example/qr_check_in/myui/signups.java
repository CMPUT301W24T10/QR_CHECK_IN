package com.example.qr_check_in.myui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.EventAdapter;
import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.FirestoreEvent;
import com.example.qr_check_in.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class signups extends Fragment {
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;

    private List<FirestoreEvent> eventList = new ArrayList<>();

    public signups() {
        // Required empty public constructor
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

    private void fetchSignedUpEvents() {
        String deviceId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<FirestoreEvent> tempEventList = new ArrayList<>(); // Temporary list to hold fetched events

        db.collection("users").document(deviceId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Boolean> signedUpEventIdsMap = (Map<String, Boolean>) documentSnapshot.get("eventsSignedUpFor");
            if (signedUpEventIdsMap != null) {
                int totalEvents = signedUpEventIdsMap.size();
                int[] eventsFetched = {0}; // Array used to count fetched events, as we need a final or effectively final variable in the lambda expression

                for (String eventId : signedUpEventIdsMap.keySet()) {
                    db.collection("events").document(eventId).get().addOnSuccessListener(eventSnapshot -> {
                        FirestoreEvent firestoreEvent = eventSnapshot.toObject(FirestoreEvent.class);
                        if (firestoreEvent != null) {
                            tempEventList.add(firestoreEvent);
                        }
                        eventsFetched[0]++;
                        if (eventsFetched[0] == totalEvents) { // All events fetched
                            new Handler(Looper.getMainLooper()).post(() -> {
                                eventList.addAll(tempEventList); // Add all at once
                                eventAdapter.notifyDataSetChanged(); // Notify dataset changed
                            });
                        }
                    }).addOnFailureListener(e -> {
                        eventsFetched[0]++;
                        if (eventsFetched[0] == totalEvents && eventsFetched[0] == 1) { // Check if this was the only event and it failed
                            new Handler(Looper.getMainLooper()).post(eventAdapter::notifyDataSetChanged); // Still need to update in case of UI placeholders, etc.
                        }
                        // Log or handle the error in fetching event details
                    });
                }
            }
        }).addOnFailureListener(e -> {
            // Log or handle the error in fetching user's signed-up events
        });
    }



}
