package com.example.qr_check_in.data;

import android.util.Log;

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
        void onEventListFetched(List<EventNameIdPair> eventList);
    }

    public void fetchEventNames() {
        List<EventNameIdPair> eventList = new ArrayList<>();

        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    String eventName = document.getString("eventName");
                    if (eventName != null) { // Making sure eventName is not null
                        eventList.add(new EventNameIdPair(docId, eventName));
                    }
                }
                // Here, eventList contains all pairs of document ID and eventName.
                // You can proceed with using eventList as needed.
            } else {
                // Handle the error
                Log.d("AdminData", "Error getting documents: ", task.getException());
            }
        });
    }
}
