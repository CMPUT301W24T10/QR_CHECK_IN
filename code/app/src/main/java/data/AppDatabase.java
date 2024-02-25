package data;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AppDatabase {

    private final FirebaseFirestore db;

    public AppDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void saveEvent(String organizerName, String eventName, String eventDescription, boolean isNewQRCode, Context context, Runnable onSuccess, Runnable onFailure) {
        Map<String, Object> event = new HashMap<>();
        event.put("organizerName", organizerName);
        event.put("eventName", eventName);
        event.put("eventDescription", eventDescription);
        event.put("isNewQRCode", isNewQRCode);

        db.collection("events").add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding event", Toast.LENGTH_SHORT).show();
                    if (onFailure != null) {
                        onFailure.run();
                    }
                });
    }
}
