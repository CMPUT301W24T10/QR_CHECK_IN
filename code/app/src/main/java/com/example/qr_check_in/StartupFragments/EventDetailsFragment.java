package com.example.qr_check_in.StartupFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class EventDetailsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Find TextViews
        TextView eventNameTextView = view.findViewById(R.id.event_name);
        TextView descriptionTextView = view.findViewById(R.id.description);
        TextView locationTextView = view.findViewById(R.id.location);
        TextView startTimeTextView = view.findViewById(R.id.start_time);
        TextView endTimeTextView = view.findViewById(R.id.end_time);

        // Fetch event details from Firestore
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                // Get data from Firestore document
                                String eventName = Objects.requireNonNull(document.getString("eventName"));
                                String eventDescription = Objects.requireNonNull(document.getString("eventDescription"));
                                String location = document.getString("location");
                                String startTime = document.getString("startTime");
                                String endTime = document.getString("endTime");

                                // Handle null values for location, startTime, and endTime
                                if (location == null) {
                                    location = ""; // Set default value or leave blank
                                }
                                if (startTime == null) {
                                    startTime = ""; // Set default value or leave blank
                                }
                                if (endTime == null) {
                                    endTime = ""; // Set default value or leave blank
                                }

                                // Set event details to TextViews
                                eventNameTextView.setText("Event Name: " + eventName);
                                descriptionTextView.setText("Description: " + eventDescription);
                                locationTextView.setText("Location: " + location);
                                startTimeTextView.setText("Start Time: " + startTime);
                                endTimeTextView.setText("End Time: " + endTime);

                                // Break the loop after processing the first event
                                break;
                            }
                        }
                    } else {
                        // Handle error
                    }
                });

        return view;
    }
}
