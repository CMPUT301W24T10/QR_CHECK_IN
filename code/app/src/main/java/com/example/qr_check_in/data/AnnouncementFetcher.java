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

    public interface OnAnnouncementListReceivedListener {
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
                    if (document.exists()) {
                        Map<String, String> attendingEvents = (Map<String, String>) document.get("AttendingEvents");
                        if (attendingEvents != null) {
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

    private void fetchNotificationsForEvents(List<String> eventIds, final OnAnnouncementListReceivedListener listener) {
        List<Announcement> announcements = new ArrayList<>();
        for (String eventId : eventIds) {
            db.collection("notifications").whereEqualTo("eventId", eventId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String dateandTime = document.getString("dateandTime");
                            String notificationTitle = document.getString("notificationTitle");
                            String notification = document.getString("notification");
                            announcements.add(new Announcement(notificationTitle, dateandTime, notification));
                        }
                        listener.onAnnouncementListReceived(announcements);
                    } else {
                        Log.e("AnnouncementFetcher", "Error getting notifications: ", task.getException());
                        listener.onError(task.getException().toString());
                    }
                }
            });
        }
    }
}
