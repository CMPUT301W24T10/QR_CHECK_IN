package com.example.qr_check_in.data;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSignUpHandler {

    private FirebaseFirestore db;

    public EventSignUpHandler() {
        db = FirebaseFirestore.getInstance();
    }

    public interface SignUpCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public void signUpUserForEvent(String deviceId, String userName, EventNameIdPair selectedEvent, SignUpCallback callback) {
        // Reference to the user's document in the 'users' collection
        DocumentReference userDocRef = db.collection("users").document(deviceId);

        // Transaction to update both the event and user atomically
        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    // Read from the user's document first
                    DocumentSnapshot userSnapshot = transaction.get(userDocRef);

                    // Prepare the event info Map
                    Map<String, String> eventInfo = new HashMap<>();
                    eventInfo.put("id", selectedEvent.getDocumentId());
                    eventInfo.put("name", selectedEvent.getEventName());

                    // Add the event to the user's 'eventsSignedUpFor'
                    List<Map<String, String>> eventsSignedUpFor = new ArrayList<>();

                    if (userSnapshot.contains("eventsSignedUpFor")) {
                        eventsSignedUpFor = (List<Map<String, String>>) userSnapshot.get("eventsSignedUpFor");
                    }

                    // Check if the event is already in the list
                    boolean alreadySignedUp = false;
                    for (Map<String, String> signedUpEvent : eventsSignedUpFor) {
                        if (signedUpEvent.get("id").equals(selectedEvent.getDocumentId())) {
                            alreadySignedUp = true;
                            break;
                        }
                    }

                    if (alreadySignedUp) {
                        // User has already signed up for this event, handle this case appropriately
                        try {
                            throw new Exception("User has already signed up for this event.");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // User has not signed up for this event, add it to the list
                        eventsSignedUpFor.add(eventInfo);
                        List<Map<String, String>> finalEventsSignedUpFor = eventsSignedUpFor;
                        transaction.set(userDocRef, new HashMap<String, Object>() {{
                            put("eventsSignedUpFor", finalEventsSignedUpFor);
                        }}, SetOptions.merge());

                        // Reference to the event's document in the 'events' collection
                        DocumentReference eventDocRef = db.collection("events").document(selectedEvent.getDocumentId());
                        // Add user to the event's 'signups', this is a write operation
                        transaction.update(eventDocRef, "signups." + deviceId, userName);
                    }

                    // Success, return null
                    return null;
                }).addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e));
    }
}


