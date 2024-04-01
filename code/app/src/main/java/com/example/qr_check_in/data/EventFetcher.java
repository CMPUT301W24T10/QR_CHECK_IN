package com.example.qr_check_in.data;

import android.content.Context;
import android.provider.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventFetcher {
    private Context context;

    public EventFetcher(Context context) {
        this.context = context;
    }

    public void fetchCurrentEventId(OnCurrentEventIdFetchedListener listener) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(deviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String currentEventId = document.getString("currentEventID");
                        listener.onCurrentEventIdFetched(currentEventId);
                    } else {
                        listener.onError("Document does not exist!");
                    }
                } else {
                    listener.onError(task.getException().getMessage());
                }
            }
        });
    }

    public interface OnCurrentEventIdFetchedListener {
        void onCurrentEventIdFetched(String currentEventId);
        void onError(String error);
    }
}
