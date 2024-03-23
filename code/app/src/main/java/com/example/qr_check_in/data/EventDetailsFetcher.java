package com.example.qr_check_in.data;

import android.content.Context;
import android.provider.Settings.Secure;
import androidx.annotation.NonNull;
import com.example.qr_check_in.ModelClasses.EventDetails;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailsFetcher {

    private final FirebaseFirestore db;
    private final String deviceId;

    public EventDetailsFetcher(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public interface EventDetailsCallback {
        void onEventDetailsFetched(EventDetails eventDetails);
        void onError(Exception e);
    }

    public void fetchEventDetails(final EventDetailsCallback callback) {
        db.collection("users")
                .document(deviceId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot userSnapshot = task.getResult();
                        String currentEventID = userSnapshot.getString("currentEventID");
                        if (currentEventID != null && !currentEventID.isEmpty()) {
                            fetchEvent(currentEventID, callback);
                        } else {
                            callback.onError(new Exception("No currentEventID found for device."));
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    private void fetchEvent(String eventId, final EventDetailsCallback callback) {
        db.collection("events").document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot eventSnapshot = task.getResult();
                        EventDetails eventDetails = parseEvent(eventSnapshot);
                        callback.onEventDetailsFetched(eventDetails);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    private EventDetails parseEvent(DocumentSnapshot eventSnapshot) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Timestamp startTimeStamp = eventSnapshot.getTimestamp("startTime");
        Timestamp endTimeStamp = eventSnapshot.getTimestamp("endTime");

        String eventName = eventSnapshot.getString("eventName") != null ? eventSnapshot.getString("eventName") : "----";
        String eventDescription = eventSnapshot.getString("eventDescription") != null ? eventSnapshot.getString("eventDescription") : "----";
        String startTime = startTimeStamp != null ? dateFormat.format(startTimeStamp.toDate()) : "";
        String endTime = endTimeStamp != null ? dateFormat.format(endTimeStamp.toDate()) : "";
        String location = eventSnapshot.getString("location") != null ? eventSnapshot.getString("location") : "TBD";
        // Assuming there's a field for the posterUrl in the Firestore document
        String posterUrl = eventSnapshot.getString("posterUrl") != null ? eventSnapshot.getString("posterUrl") : "default_poster_url";

        return new EventDetails(eventName, eventDescription, startTime, endTime, location, posterUrl);
    }
}
