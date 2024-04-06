package com.example.qr_check_in.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.AttendeeCount;
import com.example.qr_check_in.ModelClasses.Event;
import com.google.firebase.firestore.CollectionReference;
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

    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // Initialize Firebase Storage

    /**
     * Constructor for AppDatabase. It initializes instances of FirebaseFirestore and FirebaseStorage
     * for later use in database and storage operations.
     */
    public AppDatabase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    /**
     * Saves an event to Firestore and optionally uploads an associated poster image.
     *
     * @param organizerId       Unique ID of the organizer creating the event.
     * @param eventName         Name of the event to be saved.
     * @param eventDescription  Description of the event.
     * @param posterUri         Uri of the event's poster image. Can be null if there is no poster.
     * @param context           Android context, required for making Toasts.
     * @param firestoreCallback Callback to be called after operation is complete or if there's an error.
     */
    public void saveEvent(String organizerId, String eventName, String eventDescription, Uri posterUri, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> event = new HashMap<>(); // Create a new HashMap to hold the event details.
        event.put("organizerId", organizerId);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);


        db.collection("events").add(event) // Add the event details to the "events" collection in Firestore.
                .addOnSuccessListener(documentReference ->{
                    if (posterUri != null) { // If a poster URI is provided, upload the poster image using the document ID as the reference.
                        uploadPosterImage(posterUri,documentReference.getId(),organizerId, context, firestoreCallback);
                    } else {
                        Toast.makeText(context, "Event added successfully without poster", Toast.LENGTH_SHORT).show();
                        updateOrganizerWithEvent(organizerId, documentReference.getId(), context);
                        firestoreCallback.onCallback(documentReference.getId());
                    }
                })
                .addOnFailureListener(e -> { // If there's an error adding the event
                    Toast.makeText(context, "Error adding event", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding event", e);
                });
    }





    /**
     * Updates the details of an existing event in Firestore.
     *
     * @param eventId           The unique ID of the event to update.
     * @param eventName         The new name for the event.
     * @param eventDescription  The new description for the event.
     * @param context           Android context, required for displaying Toast messages.
     */
    public void updateEvent(String eventId, String eventName, String eventDescription, Context context) {
        Map<String, Object> event = new HashMap<>(); // Create a new HashMap to hold the updated event details.
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);

        db.collection("events").document(eventId).update(event)  // Locate the document by its ID in the "events" collection and update it with the new values.
                .addOnSuccessListener(documentReference -> {  // Inform the user that the event has been updated successfully
                    Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {  // If there's an error during the update
                    Toast.makeText(context, "Error updating event", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error updating event", e);
                });
    }

    private void uploadPosterImage(Uri imageUri, String eventId,String organizerId, Context context, FirestoreCallback firestoreCallback) {
        StorageReference posterRef = storage.getReference().child("event_posters/" + eventId); // reference to the location where we'll store our photos
        posterRef.putFile(imageUri) // Upload the photo at the Uri to Firebase storage
                .addOnSuccessListener(taskSnapshot -> posterRef.getDownloadUrl()  // Once the image has been uploaded, we get its download URL
                        .addOnSuccessListener(uri -> {
                            String posterUrl = uri.toString();
                            db.collection("events").document(eventId) // Updating the event document with the new poster URL
                                    .update("posterUrl", posterUrl)
                                    .addOnSuccessListener(aVoid -> { //event was added with the poster
                                        Toast.makeText(context, "Event added successfully with poster", Toast.LENGTH_SHORT).show();
                                        firestoreCallback.onCallback(eventId);  // Invoke the callback function passing the eventId
                                        updateOrganizerWithEvent(organizerId, eventId, context); // Update the organizer document with the new event ID
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Error updating event with poster URL", Toast.LENGTH_SHORT).show()); //updating the event with the poster URL failed
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Error getting poster download URL", Toast.LENGTH_SHORT).show())) //getting the download URL failed
                .addOnFailureListener(e -> Toast.makeText(context, "Error uploading event poster", Toast.LENGTH_SHORT).show()); //upload failed
    }

    public interface FirestoreCallback { // An interface for callback methods when a Firestore operation returns a document ID
        void onCallback(String documentId);
    }
    public interface FirestoreDocumentCallback {  //An interface for callback methods when Firestore operations return a document's data
        void onCallback(Map<String, Object> data);
    }
    public void saveOrganizer(String organizerName, String deviceId, Context context, FirestoreCallback firestoreCallback) {  // Method to save an organizer's information in Firestore.
        if (deviceId == null || deviceId.isEmpty()) { // Check if the provided device ID is valid.
            Log.e("FirestoreError", "Device ID is null or empty");
            Toast.makeText(context, "Error: Device ID is required", Toast.LENGTH_SHORT).show();
            return; // Exit the method early
        }

        Map<String, Object> organizer = new HashMap<>();
        organizer.put("Name", organizerName);
// map to hold the organizer's data with their name.
        db.collection("users").document(deviceId).set(organizer, SetOptions.merge())  // Add or merge the organizer data into the 'users' collection in Firestore, using the device ID as the document ID.
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Organizer added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(deviceId); // This callback now safely uses deviceId
                })
                .addOnFailureListener(e -> { // If the database operation fails, display an error message and log the error.
                    Toast.makeText(context, "Error adding organizer", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding organizer", e);
                });
    }

    private void updateOrganizerWithEvent(String organizerId, String eventId, Context context) {  // Method to update an organizer's document in Firestore by adding an event ID to their list of organized events.
        if (organizerId == null || eventId == null) { // Check if both organizer ID and event ID are provided.
            Log.e("FirestoreError", "Organizer ID or Event ID is null, cannot update organizer with event");
            Toast.makeText(context, "null updating organizer with event ID", Toast.LENGTH_SHORT).show();
            return;
        }
        DocumentReference organizerRef = db.collection("users").document(organizerId);  // Get a reference to the organizer's document in Firestore using the organizer ID.

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


    public void saveAttendee(Integer NumberOfTimesLogin,String deviceId, String attendeeName, Context context, String uniqueID, FirestoreCallback firestoreCallback) {
        DocumentReference documentReference = db.collection("events").document(uniqueID);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Check if the 'attendees' field exists
                    if (documentSnapshot.contains("attendees")) {
                        // Get the existing attendees map
                        Map<String, AttendeeCount> existingAttendees = (Map<String, AttendeeCount>) documentSnapshot.get("attendees");

                        // Check if the attendee with the specified deviceId already exists
                        if (existingAttendees.containsKey(deviceId)) {
                            AttendeeCount values = new AttendeeCount(NumberOfTimesLogin,attendeeName);
                            existingAttendees.put(deviceId, values);
                            documentReference.update("attendees", existingAttendees)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Attendee added successfully", Toast.LENGTH_SHORT).show();
                                        firestoreCallback.onCallback(deviceId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error adding attendee", Toast.LENGTH_SHORT).show();
                                        Log.e("FirestoreError", "Error adding attendee", e);
                                    });


                        } else {
                            // If the 'attendees' field exists but the deviceId is not present, put new device id into field
                            AttendeeCount values = new AttendeeCount(NumberOfTimesLogin,attendeeName);

                            existingAttendees.put(deviceId, values);

                            documentReference.update("attendees", existingAttendees)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Attendee added successfully", Toast.LENGTH_SHORT).show();
                                        firestoreCallback.onCallback(deviceId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error adding attendee", Toast.LENGTH_SHORT).show();
                                        Log.e("FirestoreError", "Error adding attendee", e);
                                    });
                        }
                    } else {
                        // If the 'attendees' field does not exist, create a new map with a list and add the new string
                        Map<String, AttendeeCount> newAttendees = new HashMap<>();
                        AttendeeCount values = new AttendeeCount(NumberOfTimesLogin,attendeeName);

                        newAttendees.put(deviceId, values);

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
                } else {
                    Log.e("FirestoreError", "Document does not exist");
                }
            } else {
                Log.e("FirestoreError", "Error getting document", task.getException());
            }
        });
    }
    //asldfosjogfijsdipgjsdlkfjsldfhoiwehoilkcvnlkjnvcoihfgoihroiptwjpefojqwpofj;dskjlksdnvlknxlvhjoishfogijwepifjopiwejoiwhetuohriuoghowrhgierjgojsifjoiwejfpiwejfp
    public void saveUser(String deviceId, String userName, String userPhone, String emailAddress, String address, String event, Context context, FirestoreCallback firestoreCallback) {
        Map<String, Object> info = new HashMap<>(); // Create a new HashMap to hold the user info
        info.put("Name", userName);
        info.put("Phone", userPhone);
        info.put("Email", emailAddress);
        info.put("Address",address);
        info.put("currentEventID", event);


        db.collection("users").document(deviceId).set(info, SetOptions.merge())  // Add or merge the organizer data into the 'users' collection in Firestore, using the device ID as the document ID.
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show();
                    firestoreCallback.onCallback(deviceId); // This callback now safely uses deviceId
                })
                .addOnFailureListener(e -> { // If the database operation fails, display an error message and log the error.
                    Toast.makeText(context, "Error adding user", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error adding user", e);
                });
    }

    public interface FirestoreEventArrayLengthCallback { //Interface definition for a callback to be invoked when the length of an array of events is retrieved from Firestore.
        void onCallback(int arrayLength);
        void onError(String message);
    }
    /**
     * Fetches the length of the array of organized event IDs associated with a given device ID from Firestore.
     *
     * @param deviceId The device ID of the user whose organized event IDs are to be fetched.
     * @param context  The context of the application.
     * @param callback The callback to be invoked when the length of the array is retrieved or when an error occurs.
     */
    public void fetchOrganizedEventIdsLength(String deviceId, Context context, FirestoreEventArrayLengthCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(deviceId);

        docRef.get().addOnCompleteListener(task -> {   // Retrieve the document asynchronously
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {  // Check if the document exists and contains data
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
    public interface FirestoreFetchArrayCallback {  //interface definition for a callback to be invoked when fetching an array from Firestore.
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
