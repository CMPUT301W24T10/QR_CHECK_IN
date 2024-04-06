package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.beust.ah.A;
import com.example.qr_check_in.ModelClasses.AttendeeCount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AttendeeInfo {
    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // Initialize Firebase Storage

    public AttendeeInfo() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    public interface getAttendeesMapCallback {
        void onCallback(Map<String, AttendeeCount> listresult);
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
                                // Get the existing attendees map
                                Map<String, AttendeeCount> attendeesList = (Map<String, AttendeeCount>) documentSnapshot.get("attendees");
                                callback.onCallback(attendeesList);
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
