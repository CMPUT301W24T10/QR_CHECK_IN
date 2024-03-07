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

    public void saveEvent(String organizerId, String eventName, String eventDescription, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> event = new HashMap<>();
        event.put("organizerId", organizerId);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);

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
    public interface FirestoreEventArrayLengthCallback {
        void onCallback(int arrayLength);
        void onError(String message);
    }
    // fetch the length of the organizedEventIds array from the user document
    public void fetchOrganizedEventIdsLength(String deviceId, Context context, FirestoreEventArrayLengthCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(deviceId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    List<String> organizedEventIds = (List<String>) document.get("organizedEventIds");
                    if (organizedEventIds != null) {
                        // If the array exists, pass its length to the callback
                        callback.onCallback(organizedEventIds.size());
                    } else {
                        // If the array does not exist, pass a length of 0 or indicate absence as needed
                        callback.onCallback(0);
                    }
                } else {
                    // Document does not exist
                    callback.onError("Document does not exist");
                }
            } else {
                // Task failed
                callback.onError("Failed to fetch document: " + task.getException().getMessage());
            }
        });
    }
    public interface FirestoreFetchArrayCallback {
        void onCallback(List<String> array);
        void onError(String message);
    }

    public void fetchOrganizedEventIds(String deviceId, FirestoreFetchArrayCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(deviceId == null) {
            Log.e("FirestoreError", "Device ID is null");
            return;
        }
        DocumentReference docRef = db.collection("users").document(deviceId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Object organizedEventIdsObject = document.get("organizedEventIds");
                    if (organizedEventIdsObject instanceof List<?>) {
                        // This is a safe cast because we checked the instance type beforehand
                        @SuppressWarnings("unchecked")
                        List<String> organizedEventIds = (List<String>) organizedEventIdsObject;
                        // Now you can use organizedEventIds as a List<String>
                        if (organizedEventIds != null) {
                            // If the array exists, pass it to the callback
                            callback.onCallback(organizedEventIds);
                        } else {
                            // If the array does not exist, pass an empty list or null as needed
                            callback.onCallback(new ArrayList<>()); // or callback.onCallback(null);
                        }
                    } else {
                        // Handle the case where it's not a List or is null
                    }
                } else {
                    // Document does not exist
                    callback.onError("Document does not exist");
                }
            } else {
                // Task failed
                callback.onError("Failed to fetch document: " + task.getException().getMessage());
            }
        });
    }

}
