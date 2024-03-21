package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.qr_check_in.ModelClasses.EventDetails;
import com.example.qr_check_in.R;

public class EventDetailsFragment extends Fragment {

    private EventDetails eventDetails;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Assuming EventDetails object is passed as a serialized argument
            eventDetails = (EventDetails) getArguments().getSerializable("eventDetails");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (eventDetails != null) {
            updateUI(view);
        }
    }

    private void updateUI(View view) {
        ((TextView) view.findViewById(R.id.eventNameTextView)).setText(eventDetails.getEventName());
        ((TextView) view.findViewById(R.id.eventDescriptionTextView)).setText(eventDetails.getEventDescription());
        ((TextView) view.findViewById(R.id.startTimeTextView)).setText(eventDetails.getStartTime());
        ((TextView) view.findViewById(R.id.endTimeTextView)).setText(eventDetails.getEndTime());
        ((TextView) view.findViewById(R.id.locationTextView)).setText(eventDetails.getLocation());

        ImageView poster = view.findViewById(R.id.posterImageView);
        Glide.with(this)
                .load(eventDetails.getPosterUrl())
                .placeholder(R.drawable.default_poster) // Assuming there's a default placeholder in your drawables
                .into(poster);
    }
}
