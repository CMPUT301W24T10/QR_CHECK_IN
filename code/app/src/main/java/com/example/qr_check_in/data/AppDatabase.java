package com.example.qr_check_in.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                    updateOrganizerWithEvent(organizerId, documentReference.getId(), context);
                    firestoreCallback.onCallback(documentReference.getId());
                    // Update the organizer document with the event ID

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding event", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding event", e);
                });

    }
    public void updateEvent(String eventId, String eventName, String eventDescription, Context context) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);

        db.collection("events").document(eventId).update(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error updating event", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error updating event", e);
                });
    }
    public interface FirestoreCallback {
        void onCallback(String documentId);
    }
    public interface FirestoreDocumentCallback {
        void onCallback(Map<String, Object> data);
    }
    public void saveOrganizer(String organizerName, String deviceId,Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> organizer = new HashMap<>();
        organizer.put("Name", organizerName);

        db.collection("users").document(deviceId).set(organizer, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Organizer added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(deviceId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding organizer", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding organizer", e);
                });
    }
    private void updateOrganizerWithEvent(String organizerId, String eventId, Context context) {
        DocumentReference organizerRef = db.collection("users").document(organizerId);

        // Add the event ID to an array of organized eventIds. If the array doesn't exist, it will be created.
        organizerRef.update("organizedEventIds", FieldValue.arrayUnion(eventId))
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

    public void saveAttendee(String deviceId, Context context, String uniqueID, FirestoreCallback firestoreCallback) {
        Map<String, Object> attendeeData = new HashMap<>();
        attendeeData.put("Name", "guest");

        DocumentReference documentReference = db.collection("events").document(uniqueID);


        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    if(documentSnapshot.contains("attendees")){
                        List<Map<String, Object>> existingAttendees = (List<Map<String, Object>>) documentSnapshot.get("attendees");
                        existingAttendees.add(attendeeData);
                        documentReference.update("attendees", existingAttendees)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Attendee added successfully", Toast.LENGTH_SHORT).show();
                                    firestoreCallback.onCallback(deviceId);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error adding attendee", Toast.LENGTH_SHORT).show();
                                    Log.e("FirestoreError", "Error adding attendee", e);
                                });
                    } else{
                        List<Map<String, Object>> newAttendees = new ArrayList<>();
                        newAttendees.add(attendeeData);

                        documentReference.update("attendees", newAttendees)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Attendee added successfully", Toast.LENGTH_SHORT).show();
                                    firestoreCallback.onCallback(deviceId);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error adding attendee", Toast.LENGTH_SHORT).show();
                                    Log.e("FirestoreError", "Error adding attendee", e);
                                });
                    }
                } else{
                    Log.e("FirestoreError", "Document does not exist");
                }
            } else {
                Log.e("FirestoreError","Error getting document", task.getException());
            }
        });

    }
}
