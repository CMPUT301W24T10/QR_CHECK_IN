package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.Event2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListFetcher {

    private FirebaseFirestore db;
    /**
     * Constructor for the EventListFetcher class.
     * Initializes the FirebaseFirestore instance for database operations.
     */
    public EventListFetcher() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Fetches a list of events from the Firestore database.
     *
     * @param listener The listener to be notified when the event list is received or if an error occurs.
     */
    public void fetchEvents(final OnEventListReceivedListener listener) {
        db.collection("events").get()   // Access the 'events' collection in Firestore and retrieve all documents
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {  // If the task is successful, iterate through the result documents
                            List<Event2> eventList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("eventName");
                                String description = document.getString("eventDescription");
                                eventList.add(new Event2(name, description)); // Create an Event2 object with the retrieved details and add it to the list
                            }
                            listener.onEventListReceived(eventList);
                        } else {
                            Log.d("EventListFetcher", "Error getting documents: ", task.getException());
                            listener.onError(task.getException().toString());
                        }
                    }
                });
    }
    /**
     * Listener interface for receiving the event list or error message from EventListFetcher.
     */
    public interface OnEventListReceivedListener {
        void onEventListReceived(List<Event2> eventList);
        void onError(String message);
    }
}
