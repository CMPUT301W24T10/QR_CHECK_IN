package com.example.qr_check_in;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EventDetailsActivity extends Activity {
    private TextView eventNameTextView;
    private TextView eventDateTimeTextView;
    private TextView eventLocationTextView;
    private TextView eventDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_events_detail);

        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventDateTimeTextView = findViewById(R.id.eventDateTimeTextView);
        eventLocationTextView = findViewById(R.id.eventLocationTextView);
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);

        // Retrieve event details from Firebase or any other data source
        String eventName = "Tech Conference";
        String eventDateTime = "March 10, 2024 - 9:00 AM";
        String eventLocation = "Conference Center";
        String eventDescription = "Join us for a day of tech talks and networking.";

        // Populate the TextViews with the retrieved event details
        eventNameTextView.setText(eventName);
        eventDateTimeTextView.setText(eventDateTime);
        eventLocationTextView.setText(eventLocation);
        eventDescriptionTextView.setText(eventDescription);
    }
}
