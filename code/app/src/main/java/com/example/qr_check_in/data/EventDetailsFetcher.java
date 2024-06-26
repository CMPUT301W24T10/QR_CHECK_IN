package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.EventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Class responsible for fetching event details from Firebase Firestore.
 */
public class EventDetailsFetcher {

    private FirebaseFirestore db;

    /**
     * Constructor initializing the Firestore database instance.
     */
    public EventDetailsFetcher() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Fetches event details from the Firestore database.
     *
     * @param listener Listener to handle event details received and errors.
     */
    public void fetchEventDetails(final OnEventDetailsReceivedListener listener) {
        db.collection("events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EventDetails> eventDetailsList = new ArrayList<>();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            for (QueryDocumentSnapshot document : task.getResult()) {  // Extract event details from Firestore document
                                String name = document.getString("eventName") != null ? document.getString("eventName") : "----";
                                String description = document.getString("eventDescription") != null ? document.getString("eventDescription") : "----";
                                Timestamp startTimeStamp = document.getTimestamp("startTime");
                                Timestamp endTimeStamp = document.getTimestamp("endTime");
                                String startTime = startTimeStamp != null ? dateFormat.format(startTimeStamp.toDate()) : "TBD";
                                String endTime = endTimeStamp != null ? dateFormat.format(endTimeStamp.toDate()) : "TBD";
                                String location = document.getString("location") != null ? document.getString("location") : "TBD";
                                String posterUrl = document.getString("posterUrl") != null ? document.getString("posterUrl") : "default_poster_url"; // default poster URL or handle null case
                                eventDetailsList.add(new EventDetails(name, description, startTime, endTime, location, posterUrl));
                            }
                            listener.onEventDetailsReceived(eventDetailsList); // Pass event details list to the listener
                        } else { //null case
                            Log.d("EventDetailsFetcher", "Error getting documents: ", task.getException());
                            listener.onError(task.getException().toString());
                        }
                    }
                });
    }

    /**
     * Interface for receiving event details and error messages.
     */
    public interface OnEventDetailsReceivedListener {
        /**
         * Called when event details are received successfully.
         *
         * @param eventDetailsList List of EventDetails objects.
         */
        void onEventDetailsReceived(List<EventDetails> eventDetailsList);

        /**
         * Called when an error occurs during the fetching process.
         *
         * @param message Error message.
         */
        void onError(String message);
    }

    // add a method to fetch event signups Map
    public void fetchEventSignups(String eventId, OnEventSignupsReceivedListener listener) {
        db.collection("events").document(eventId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                if (document.contains("signups")) {
                                    Map<String, String> attendeesList = (Map<String,String>) document.get("signups");
                                    ArrayList<String> attendees = new ArrayList<>();
                                    for (Map.Entry<String, String> entry : attendeesList.entrySet()) {
                                        attendees.add(entry.getValue());
                                    }
                                    listener.onEventSignupsReceived(attendees);
                                } else {
                                    listener.onEventSignupsReceived(new ArrayList<>());
                                }
                            } else {
                            }
                        } else {
                        }
                    }
                });
    }

    public interface OnEventSignupsReceivedListener {
        void onEventSignupsReceived(ArrayList<String> attendeesList);

    }

}
