package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.Announcement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnnouncementFetcher {

    private FirebaseFirestore db;

    public AnnouncementFetcher() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnAnnouncementListReceivedListener { // Interface for receiving announcement list or error message
        void onAnnouncementListReceived(List<Announcement> announcementList);
        void onError(String message);
    }

    // Function to fetch announcements based on AttendingEvents from a user's document
    public void fetchAnnouncementsByDeviceId(String deviceId, final OnAnnouncementListReceivedListener listener) {
        db.collection("users").document(deviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  // Get AttendingEvents map from user document
                        Map<String, String> attendingEvents = (Map<String, String>) document.get("AttendingEvents");
                        if (attendingEvents != null) {   // Fetch notifications for events
                            fetchNotificationsForEvents(new ArrayList<>(attendingEvents.keySet()), listener);
                        } else {
                            listener.onError("No attending events found for user.");
                        }
                    } else {
                        listener.onError("User document not found.");
                    }
                } else {
                    Log.e("AnnouncementFetcher", "Error getting user document: ", task.getException());
                    listener.onError(task.getException().toString());
                }
            }
        });
    }

    private void fetchNotificationsForEvents(List<String> eventIds, final OnAnnouncementListReceivedListener listener) {  // Function to fetch notifications for events
        List<Announcement> announcements = new ArrayList<>();
        for (String eventId : eventIds) {
            db.collection("notifications").whereEqualTo("eventId", eventId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {   // Get notification details from Firestore document
                            String dateandTime = document.getString("dateandTime");
                            String notificationTitle = document.getString("notificationTitle");
                            String notification = document.getString("notification");
                            announcements.add(new Announcement(notificationTitle, dateandTime, notification));  // Create Announcement object and add to the list
                        }
                        listener.onAnnouncementListReceived(announcements);   // Pass the list of announcements to the listener
                    } else {
                        Log.e("AnnouncementFetcher", "Error getting notifications: ", task.getException());
                        listener.onError(task.getException().toString());
                    }
                }
            });
        }
    }
}
