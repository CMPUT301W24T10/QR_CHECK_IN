package com.example.qr_check_in.data;

import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class AppDatabase {

    private final FirebaseFirestore db;
    private final FirebaseStorage storage; // Initialize Firebase Storage

    public AppDatabase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void saveEvent(String organizerId, String eventName, String eventDescription, Uri posterUri, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> event = new HashMap<>();
        event.put("organizerId", organizerId);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);


        db.collection("events").add(event)
                .addOnSuccessListener(documentReference ->{
                    if (posterUri != null) {
                        uploadPosterImage(posterUri,documentReference.getId(),organizerId, context, firestoreCallback);
                    } else {
                        Toast.makeText(context, "Event added successfully without poster", Toast.LENGTH_SHORT).show();
                        updateOrganizerWithEvent(organizerId, documentReference.getId(), context);
                        firestoreCallback.onCallback(documentReference.getId());
                    }
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

    private void uploadPosterImage(Uri imageUri, String eventId,String organizerId, Context context, FirestoreCallback firestoreCallback) {
        StorageReference posterRef = storage.getReference().child("event_posters/" + eventId);
        posterRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> posterRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String posterUrl = uri.toString();
                            db.collection("events").document(eventId)
                                    .update("posterUrl", posterUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Event added successfully with poster", Toast.LENGTH_SHORT).show();
                                        firestoreCallback.onCallback(eventId);
                                        updateOrganizerWithEvent(organizerId, eventId, context);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error updating event with poster URL", Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Error getting poster download URL", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(context, "Error uploading event poster", Toast.LENGTH_SHORT).show());
    }

    public interface FirestoreCallback {
        void onCallback(String documentId);
    }
    public interface FirestoreDocumentCallback {
        void onCallback(Map<String, Object> data);
    }
    public void saveOrganizer(String organizerName, String deviceId, Context context, FirestoreCallback firestoreCallback) {
        if (deviceId == null || deviceId.isEmpty()) {
            Log.e("FirestoreError", "Device ID is null or empty");
            Toast.makeText(context, "Error: Device ID is required", Toast.LENGTH_SHORT).show();
            return; // Exit the method early
        }

        Map<String, Object> organizer = new HashMap<>();
        organizer.put("Name", organizerName);

        db.collection("users").document(deviceId).set(organizer, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Organizer added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(deviceId); // This callback now safely uses deviceId
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding organizer", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding organizer", e);
                });
    }

    private void updateOrganizerWithEvent(String organizerId, String eventId, Context context) {
        if (organizerId == null || eventId == null) {
            Log.e("FirestoreError", "Organizer ID or Event ID is null, cannot update organizer with event");
            Toast.makeText(context, "null updating organizer with event ID", Toast.LENGTH_SHORT).show();
            return;
        }
        DocumentReference organizerRef = db.collection("users").document(organizerId);

        // Add the event ID to an array of organized eventIds. If the array doesn't exist, it will be created.
        organizerRef.update("organizedEventIds", FieldValue.arrayUnion(eventId))
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Organizer updated with event ID", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error updating organizer with event ID"+e.getMessage(), Toast.LENGTH_SHORT).show());
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
