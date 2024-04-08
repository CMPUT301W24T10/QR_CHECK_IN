package com.example.qr_check_in.data;

import static com.example.qr_check_in.constants.SELECTEDEVENTIDREQUIRED;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.Notification.RetrofitInstance;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class EventSignUpHandler {

    private FirebaseFirestore db;

    public EventSignUpHandler() {
        db = FirebaseFirestore.getInstance();
    }

    public interface SignUpCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public void signUpUserForEvent(String deviceId, String userName, Event selectedEvent, SignUpCallback callback) {
        // Reference to the user's document in the 'users' collection
        DocumentReference userDocRef = db.collection("users").document(deviceId);

        // Transaction to update both the event and user atomically
        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    // Read from the user's document first
                    DocumentSnapshot userSnapshot = transaction.get(userDocRef);

                    // Prepare the event info Map
                    Map<String, String> eventInfo = new HashMap<>();
                    eventInfo.put("id", selectedEvent.getEventID());
                    eventInfo.put("name", selectedEvent.getTitle());



                    if (userSnapshot.contains("eventsSignedUpFor")) {

                        transaction.update(userDocRef, "eventsSignedUpFor."+ selectedEvent.getEventID(),selectedEvent.getTitle());

                        // Reference to the event's document in the 'events' collection
                        DocumentReference eventDocRef = db.collection("events").document(selectedEvent.getEventID());
                        // Add user to the event's 'signups', this is a write operation
                        transaction.update(eventDocRef, "signups." + deviceId, userName);
                    }

                    // Success, return null
                    return null;
                }).addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface CheckSpace {
        void onSpaceAvailable();
        void onSpaceUnavailable();
    }

    public void checkSpace(String deviceId, String userName, Event selectedEvent, CheckSpace callback) {
        DocumentReference eventDocRef = db.collection("events").document(selectedEvent.getEventID());
        DocumentReference userDocRef = db.collection("users").document(deviceId);
        eventDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    long maxCapacity = document.getLong("signUpLimit");
                    long currentCount = document.getLong("signUps");
                    if (currentCount < maxCapacity || maxCapacity==-1) {
                        // Space is available run the transaction to update event and user
                        // get the map of signups
                        Map<String, String> signups = (Map<String, String>) document.get("signups");
                        if (signups== null || !signups.containsKey(deviceId)) {
                            // User has not already signed up for this event
                            db.runTransaction((Transaction.Function<Void>) transaction -> {
                                // Update the event's 'signUps' count
                                transaction.update(userDocRef, "eventsSignedUpFor."+ selectedEvent.getEventID(),selectedEvent.getTitle());
                                transaction.update(eventDocRef, "signUps", currentCount + 1);
                                // Add user to the event's 'signups'
                                transaction.update(eventDocRef, "signups." + deviceId, userName);

                                if( (double) (currentCount + 1) /maxCapacity > 0.8){
                                    // send notification to the topic: selectedEvent.getEventID()
                                    sendNotificationTriggerToServer(selectedEvent.getEventID(), "Event: "+selectedEvent.getTitle(),"Event is 80% full");

                                } else if (currentCount+1 == maxCapacity){
                                    sendNotificationTriggerToServer(selectedEvent.getEventID(), "Event: "+selectedEvent.getTitle(),"Event is full");


                                } else if ( maxCapacity!= -1 && (double) (currentCount + 1) /maxCapacity > 0.4 && (double) (currentCount + 1) /maxCapacity < 0.6){
                                    sendNotificationTriggerToServer(selectedEvent.getEventID(), "Event: "+selectedEvent.getTitle(),"Event is half filled");
                                }
                                sendNotificationTriggerToServer(selectedEvent.getEventID(), "Event: "+selectedEvent.getTitle(),"Event is filling up");

                                return null;
                            }).addOnSuccessListener(aVoid -> {
                                callback.onSpaceAvailable();
                            }).addOnFailureListener(e -> {
                                callback.onSpaceUnavailable();
                            });

                        }
                        callback.onSpaceAvailable();
                    } else {
                        callback.onSpaceUnavailable();
                    }
                } else {
                    callback.onSpaceUnavailable();
                }
            } else {
                callback.onSpaceUnavailable();
            }
        });
    }

    private void sendNotificationTriggerToServer(String eventId, String title, String notification) {
        String eventName = "/topics/event" + eventId;
        //TODO explain we don't actually send a notification, we send a message, that is displayed as a notification
        PushNotification push = new PushNotification(
                new NotificationData(title, notification),
                eventName
        );


        retrofit2.Call<ResponseBody> responseBodyCall = RetrofitInstance.getApi().postNotification(push);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                if (response.isSuccessful()) {
//                    Toast.makeText(requireContext(), "Notification sent successful to all the attendees registered to this event", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(requireContext(), "Error sending the notification", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }



}


