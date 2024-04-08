package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class AttendeeInfo {
    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // Initialize Firebase Storage

    public AttendeeInfo() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    public interface getAttendeesMapCallback {
        void onCallback(Map<String, String> attendeesMap, Map<String, Long> checkInCountMap);
    }

    public void getAttendeesMap(String eventId, getAttendeesMapCallback callback){
        // Get the document reference for the event using its ID
        DocumentReference eventDocRef = db.collection("events").document(eventId);

        // Fetch the document data
        eventDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Check if the 'attendees' field exists in the document
                            if (documentSnapshot.contains("attendees")) {
                                // Retrieve the 'attendees' field as a map
                                Map<String, String> attendeesMap = (Map<String, String>) documentSnapshot.get("attendees");
                                Map<String, Long> checkInCountMap = (Map<String, Long>) documentSnapshot.get("checkInCount");
                                    callback.onCallback(attendeesMap, checkInCountMap);
                                // Now you can work with the attendeesMap
                            } else {
                                // The 'attendees' field does not exist in the document
                                // Handle this case accordingly
                            }
                        } else {
                            // The document does not exist
                            // Handle this case accordingly
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                    }
                });
    }
}
