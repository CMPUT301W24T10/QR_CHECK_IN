package com.example.qr_check_in;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qr_check_in.data.AppDatabase;

import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventNameTextView;
    private TextView eventStartTimeTextView;
    private TextView eventEndTimeTextView;
    private TextView eventCostTextView;
    private TextView eventLocationTextView;
    private TextView eventDescriptionTextView;
    private AppDatabase AppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_events_detail);

        eventNameTextView = findViewById(R.id.tvEventName);
        eventStartTimeTextView = findViewById(R.id.tvStartTime);
        eventEndTimeTextView = findViewById(R.id.tvEndTime);
        eventCostTextView = findViewById(R.id.tvCost);
        eventLocationTextView = findViewById(R.id.btnLocation); // Assuming this is a TextView in your actual layout
        eventDescriptionTextView = findViewById(R.id.tvDescription);

        AppDatabase = new AppDatabase();

        // Assuming you have the event ID from somewhere (e.g., intent, static variable, etc.)
        String eventId = "yourEventId"; // Replace with actual event ID

        AppDatabase.fetchEventDetails(eventId, new AppDatabase.FirestoreDocumentCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                if (data != null) {
                    // Assuming the data map contains keys that match the Event model's field names
                    String eventName = (String) data.get("eventName");
                    String eventStartTime = (String) data.get("startTime"); // You might need to format date and time
                    String eventEndTime = (String) data.get("endTime");
                    String eventCost = (String) data.get("cost");
                    String eventLocation = (String) data.get("location");
                    String eventDescription = (String) data.get("description");

                    // Populate the TextViews with the retrieved event details
                    eventNameTextView.setText(eventName);
                    eventStartTimeTextView.setText(eventStartTime);
                    eventEndTimeTextView.setText(eventEndTime);
                    eventCostTextView.setText(eventCost);
                    eventLocationTextView.setText(eventLocation);
                    eventDescriptionTextView.setText(eventDescription);
                }
            }
        });
    }
}
