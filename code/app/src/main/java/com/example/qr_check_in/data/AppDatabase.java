package com.example.qr_check_in.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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
                    // Update the organizer document with the event ID
                    updateOrganizerWithEvent(organizerId, documentReference.getId(), context);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding event", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding event", e);
                });

    }
    public interface FirestoreCallback {
        void onCallback(String documentId);
    }
    public interface FirestoreDocumentCallback {
        void onCallback(Map<String, Object> data);
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
                    Log.e("FirestoreError", "Error adding organizer", e);
                });

    }
    private void updateOrganizerWithEvent(String organizerId, String eventId, Context context) {
        DocumentReference organizerRef = db.collection("organizers").document(organizerId);

        // Add the event ID to an array of eventIds. If the array doesn't exist, it will be created.
        organizerRef.update("eventIds", FieldValue.arrayUnion(eventId))
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Organizer updated with event ID", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error updating organizer with event ID", Toast.LENGTH_SHORT).show());
    }
    // fetch the user details from database
    public void fetchUserDetails(String userId, FirestoreDocumentCallback firestoreDocumentCallback) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    firestoreDocumentCallback.onCallback(documentSnapshot.getData());
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching user details", e);
                });
    }

    // fetch the event details from database
    public void fetchEventDetails(String eventId, FirestoreDocumentCallback firestoreDocumentCallback) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    firestoreDocumentCallback.onCallback(documentSnapshot.getData());
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching event details", e);
                });
    }
}
