package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;
import com.example.qr_check_in.ui.home.HomeViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailsFragment extends Fragment {

    private TextView eventNameTextView, eventDescriptionTextView, startTimeTextView, endTimeTextView, locationTextView;
    private ImageView eventPosterImageView;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        bindViews(view);
        // Initialize HomeViewModel
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Now use the viewModel to observe eventId changes
        observeEventId();

        return view;
    }

    private void observeEventId() {
        homeViewModel.getEventId().observe(getViewLifecycleOwner(), eventId -> {
            if (eventId != null && !eventId.isEmpty()) {
                fetchEventDetails(eventId); // Adjust fetchEventDetails to accept eventId as a parameter
            }
        });
    }

    private void bindViews(View view) {
        eventNameTextView = view.findViewById(R.id.eventName);
        eventDescriptionTextView = view.findViewById(R.id.eventDescription);
        locationTextView = view.findViewById(R.id.location);
        eventPosterImageView = view.findViewById(R.id.eventPoster);
    }

    // Adjusted to directly use the eventId parameter
    private void fetchEventDetails(String eventId) {
        FirebaseFirestore.getInstance().collection("events").document(eventId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            updateUIWithEventDetails(document);
                        } else {
                            Log.d("EventDetailsFragment", "No such document");
                        }
                    } else {
                        Log.d("EventDetailsFragment", "get failed with ", task.getException());
                    }
                });
    }

    private void updateUIWithEventDetails(DocumentSnapshot document) {
        getActivity().runOnUiThread(() -> {
            String eventName = "Event Name: " + document.getString("eventName");
            String eventDescription = "Event Description: " + document.getString("eventDescription");

            // Check if location is null or empty and set as "TBD" if it is
            String location = document.getString("location");
            if (location == null || location.isEmpty()) {
                location = "TBD"; // Set to "TBD" if the location is null or empty
            }
            location = "Location: " + location;

            String posterUrl = document.getString("posterUrl");

            eventNameTextView.setText(eventName);
            eventDescriptionTextView.setText(eventDescription);
            locationTextView.setText(location);

            // Only load the image if the posterUrl is not null and not empty
            if (posterUrl != null && !posterUrl.isEmpty()) {
                Glide.with(EventDetailsFragment.this)
                        .load(posterUrl)
                        .placeholder(R.drawable.default_poster)
                        .into(eventPosterImageView);
            }
        });
    }


}
