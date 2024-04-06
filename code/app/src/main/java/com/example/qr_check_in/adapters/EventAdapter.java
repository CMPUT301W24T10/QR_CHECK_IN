package com.example.qr_check_in.adapters;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.qr_check_in.ModelClasses.Event2;
import com.example.qr_check_in.ModelClasses.Event3;
import com.example.qr_check_in.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

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

        Glide.with(holder.poster.getContext())
                .load(event.getEventPoster())
                .override(100, 100)
                .placeholder(R.drawable.default_poster)
                .into(holder.poster);
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
