package com.example.qr_check_in.data;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AppDatabase {

    private final FirebaseFirestore db;

    public AppDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void saveEvent(String organizerId, String eventName, String eventDescription, boolean isNewQRCode, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> event = new HashMap<>();
        event.put("organizerId", organizerId);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);
        event.put("isNewQRCode", isNewQRCode);

        db.collection("events").add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding event", Toast.LENGTH_SHORT).show();
                });

    }
    public interface FirestoreCallback {
        void onCallback(String documentId);
    }
    public void saveOrganizer(String organizerName, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> organizer = new HashMap<>();
        organizer.put("organizerName", organizerName);

        db.collection("organizers").add(organizer)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Organizer added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding organizer", Toast.LENGTH_SHORT).show();
                });

    }
}
