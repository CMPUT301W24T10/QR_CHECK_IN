package com.example.qr_check_in;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.ModelClasses.FirestoreEvent; // Ensure this import is correct

import java.util.List;

// EventAdapter.java
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<FirestoreEvent> events; // Now using FirestoreEvent objects

    public EventAdapter(List<FirestoreEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        FirestoreEvent event = events.get(position);
        holder.textViewEventDescription.setText("Event Name: " + event.getTitle());
        holder.textViewEventName.setText("Event Name: " + event.getTitle());
        holder.textViewEventLocation.setText("Location: " + event.getLocation());

        // You can set other fields similarly if they exist in FirestoreEvent
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventName;
        TextView textViewEventDescription;
        TextView textViewEventLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEventName = itemView.findViewById(R.id.text_view_event_name);
            textViewEventDescription = itemView.findViewById(R.id.text_view_event_description);
            textViewEventLocation = itemView.findViewById(R.id.text_view_event_location);
            // Initialize other views if necessary
        }
    }
}
