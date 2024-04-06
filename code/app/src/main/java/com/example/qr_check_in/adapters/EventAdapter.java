package com.example.qr_check_in.adapters;
import com.bumptech.glide.Glide;
import com.example.qr_check_in.ModelClasses.Event2;
import com.example.qr_check_in.ModelClasses.Event3;
import com.example.qr_check_in.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event3> eventList;

    public EventAdapter(List<Event3> eventList) {
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event3 event = eventList.get(position);
        holder.name.setText("Event Name: " + event.getEventName());
        holder.description.setText("Description: " + event.getEventDescription());
        holder.location.setText("Location: " + event.getEventLocation());

        if (!event.getEventPoster().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("event_posters/" + event.getEventPoster());

            Glide.with(holder.itemView.getContext())
                    .load(storageReference)
                    .into(holder.poster);
        } else {
            holder.poster.setImageResource(R.drawable.default_poster);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, location;
        public ImageView poster;

        public EventViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            location = view.findViewById(R.id.location);
            poster = view.findViewById(R.id.event_image);
        }
    }
}
