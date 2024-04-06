package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.Announcement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 * AnnouncementFetcher.java
 * This class fetches announcements from the Firestore database and provides a callback mechanism for handling fetched data or errors.
 */

public class AnnouncementFetcher {

    private FirebaseFirestore db;

    // Constructor to initialize Firestore instance
    public AnnouncementFetcher() {
        db = FirebaseFirestore.getInstance();
    }

    // Interface to handle announcement list retrieval or errors
    public interface OnAnnouncementListReceivedListener {
        void onAnnouncementListReceived(List<Announcement> announcementList);
        void onError(String message);
    }

    // Method to fetch announcements from Firestore
    public void fetchAnnouncements(final OnAnnouncementListReceivedListener listener) {
        db.collection("announcements").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // If query is successful, parse documents and send to listener
                            List<Announcement> announcementList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String message = document.getString("message");
                                Timestamp time = document.getTimestamp("time");
                                announcementList.add(new Announcement(message, time));
                            }
                            listener.onAnnouncementListReceived(announcementList);
                        } else {
                            // If query fails, log error and notify listener
                            Log.e("AnnouncementFetcher", "Error getting documents: ", task.getException());
                            listener.onError(task.getException().toString());
                        }
                    }
                });
    }
}
