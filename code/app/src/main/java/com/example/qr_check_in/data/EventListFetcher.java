package com.example.qr_check_in.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qr_check_in.ModelClasses.Event2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListFetcher {

    private FirebaseFirestore db;

    public EventListFetcher() {
        db = FirebaseFirestore.getInstance();
    }

    public void fetchEvents(final OnEventListReceivedListener listener) {
        db.collection("events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Event2> eventList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("eventName");
                                String description = document.getString("eventDescription");
                                eventList.add(new Event2(name, description));
                            }
                            listener.onEventListReceived(eventList);
                        } else {
                            Log.d("EventListFetcher", "Error getting documents: ", task.getException());
                            listener.onError(task.getException().toString());
                        }
                    }
                });
    }

    public interface OnEventListReceivedListener {
        void onEventListReceived(List<Event2> eventList);
        void onError(String message);
    }
}
