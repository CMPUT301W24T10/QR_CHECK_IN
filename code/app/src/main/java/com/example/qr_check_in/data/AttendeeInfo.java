package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendeeInfo {
    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // Initialize Firebase Storage

    public AttendeeInfo() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    public interface getAttendeesMapCallback {
        void onCallback(Map<String, String> attendeesMap);
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
                                Log.e("contains", "attendees");
                                Map<String, Object> rawMap = (Map<String, Object>) documentSnapshot.get("attendees");
                                if (rawMap != null) {
                                    Map<String, String> attendeesMap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                                        if (entry.getValue() instanceof String) {
                                            attendeesMap.put(entry.getKey(), (String) entry.getValue());
                                        }
                                        // Optionally handle or log cases where values are not Strings
                                    }
//                                    Log.e("AttendeesMap", attendeesMap.toString());
                                    callback.onCallback(attendeesMap);
                                } else {
                                    // Handle case where attendees is null or not a map
                                }
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

    public void getAttendeeCheckInCount(String eventId, String userId, OnCompleteListener<Integer> onCompleteListener) {
        DocumentReference eventDocRef = db.collection("events").document(eventId);

        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("checkIns")) {
                Map<String, Object> allCheckIns = (Map<String, Object>) documentSnapshot.get("checkIns");
                if (allCheckIns != null && allCheckIns.containsKey(userId)) {
                    List<Map<String, Object>> userCheckIns = (List<Map<String, Object>>) allCheckIns.get(userId);
                    if (userCheckIns != null) {
                        onCompleteListener.onComplete(userCheckIns.size());
                    } else {
                        onCompleteListener.onComplete(0);
                    }
                } else {
                    onCompleteListener.onComplete(0);
                }
            } else {
                onCompleteListener.onComplete(0);
            }
        }).addOnFailureListener(e -> onCompleteListener.onComplete(0));
    }
}
