package com.example.qr_check_in.data;

import android.util.Log;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AdminData {
    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // Initialize Firebase Storage

    public AdminData() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    public interface EventFetchListener {
        void onEventListFetched(ArrayList<Event> eventList);
    }

    public void fetchEventNames(EventFetchListener listener) {
        ArrayList<Event> eventList = new ArrayList<>();

        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    String eventName = document.getString("eventName");
                    String eventLocation = document.getString("location");
                    String eventDescription = document.getString("eventDescription");
                    if (eventName != null) { // Making sure eventName is not null
                        eventList.add(new Event(eventName,null,eventDescription,docId, eventLocation));
                    }
//                    if (eventList.size()>10){
//                        break;
//                    }

                }
                listener.onEventListFetched(eventList);
                // Here, eventList contains all pairs of document ID and eventName.
            } else {
                // Handle the error
                Log.d("AdminData", "Error getting documents: ", task.getException());
            }
        });
    }

    public interface EventDeletionListener {
        void onEventDeletionSuccess(String documentId);
    }


    public void deleteEvent(String documentId, EventDeletionListener listener) {
        db.collection("events").document(documentId).delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onEventDeletionSuccess(documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "deleteEvent: "+ e.getMessage() );
                });
    }
    public interface ProfileFetchListener {
        void onProfileListFetched(ArrayList<User> profileList);
    }
    public void fetchProfileNames(ProfileFetchListener listener) {
        ArrayList<User> profileList = new ArrayList<>();

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    String profileName = document.getString("Name");
                    String profileEmail = document.getString("Email Address");
                    String profilePhone = document.getString("Phone Number");
                    if (profileName != null) { // Making sure profileName is not null
                        profileList.add(new User(profileName, docId, profileEmail, profilePhone) {
                            @Override
                            public boolean isOrganizer() {
                                return false;
                            }

                            @Override
                            public boolean isAttendee() {
                                return false;
                            }
                        });
                    }

                }
                listener.onProfileListFetched(profileList);
                // Here, profileList contains all pairs of document ID and profileName.
            } else {
                // Handle the error
                Log.d("AdminData", "Error getting documents: ", task.getException());
            }
        });
    }

    public interface ProfileDeletionListener {
        void onProfileDeletionSuccess(String documentId);
    }

    public void deleteProfile(String documentId, ProfileDeletionListener listener) {
        db.collection("users").document(documentId).delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onProfileDeletionSuccess(documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteProfile", "deleteProfile: "+ e.getMessage() );
                });
    }
}
