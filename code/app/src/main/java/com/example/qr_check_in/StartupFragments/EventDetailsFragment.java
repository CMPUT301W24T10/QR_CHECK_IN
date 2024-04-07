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
    import com.bumptech.glide.Glide;
    import com.example.qr_check_in.R;
    import com.example.qr_check_in.data.EventFetcher;
    import com.example.qr_check_in.ui.home.HomeFragment;
    import com.google.firebase.Timestamp;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.Locale;

    public class EventDetailsFragment extends Fragment {

        private TextView eventNameTextView, eventDescriptionTextView, startTimeTextView, endTimeTextView, locationTextView;
        private ImageView eventPosterImageView;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event_details, container, false);
            bindViews(view);
            fetchEventDetails();
            return view;
        }

        private void bindViews(View view) {
            eventNameTextView = view.findViewById(R.id.eventName);
            eventDescriptionTextView = view.findViewById(R.id.eventDescription);
            startTimeTextView = view.findViewById(R.id.startTime);
            endTimeTextView = view.findViewById(R.id.endTime);
            locationTextView = view.findViewById(R.id.location);
            eventPosterImageView = view.findViewById(R.id.eventPoster);
        }

        private void fetchEventDetails() {
            new EventFetcher(requireContext()).fetchCurrentEventId(new EventFetcher.OnCurrentEventIdFetchedListener() {
                @Override
                public void onCurrentEventIdFetched(String currentEventId) {
                    FirebaseFirestore.getInstance().collection("events").document(currentEventId)
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

                @Override
                public void onError(String error) {
                    Log.d("EventDetailsFragment", "Error fetching event ID: " + error);
                }
            });
        }

        private void updateUIWithEventDetails(DocumentSnapshot document) {
            getActivity().runOnUiThread(() -> {
                String eventName = document.getString("eventName");
                String eventDescription = document.getString("eventDescription");
                Timestamp startTimeStamp = document.getTimestamp("startTime");
                Timestamp endTimeStamp = document.getTimestamp("endTime");
                String location = document.getString("location");
                String posterUrl = document.getString("posterUrl");

                DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

                String startTime = startTimeStamp != null ? dateFormat.format(startTimeStamp.toDate()) : "TBD";
                String endTime = endTimeStamp != null ? dateFormat.format(endTimeStamp.toDate()) : "TBD";

                eventNameTextView.setText(eventName);
                eventDescriptionTextView.setText(eventDescription);
                startTimeTextView.setText(startTime);
                endTimeTextView.setText(endTime);
                locationTextView.setText(location);

                // Only load image if posterUrl is not null and not empty
                if (posterUrl != null && !posterUrl.isEmpty()) {
                    Glide.with(EventDetailsFragment.this)
                            .load(posterUrl)
                            .placeholder(R.drawable.default_poster)
                            .into(eventPosterImageView);
                }
            });
        }
    }
