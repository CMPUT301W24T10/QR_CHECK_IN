package com.example.qr_check_in.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
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

    public void saveEvent(String organizerId, String eventName, String eventDescription, boolean isNewQRCode, Uri posterUri, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> event = new HashMap<>();
        event.put("organizerId", organizerId);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);
        event.put("isNewQRCode", isNewQRCode);

        db.collection("events").add(event)
                .addOnSuccessListener(documentReference -> {
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
            Toast.makeText(context, "Error updating organizer with event ID", Toast.LENGTH_SHORT).show();
            return;
        }
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
