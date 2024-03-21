package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.ModelClasses.EventDetails;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.EventsFetcher;

import java.util.List;

/**
 * A fragment to display a list of events fetched from the server.
 */
public class EventsFragment extends Fragment implements EventsFetcher.OnEventsReceivedListener {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = root.findViewById(R.id.recyclerView_events);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL));

        adapter = new EventListAdapter();
        recyclerView.setAdapter(adapter);

        EventsFetcher eventsFetcher = new EventsFetcher();
        eventsFetcher.fetchEvents(this);

        return root;
    }

    @Override
    public void onEventsReceived(List<EventDetails> eventDetailsList) {
        adapter.setEvents(eventDetailsList);
    }

    @Override
    public void onError(String message) {
        // Handle error
    }

    private static class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

        private List<EventDetails> events;
        private OnEventClickListener eventClickListener;

        public void setEvents(List<EventDetails> events) {
            this.events = events;
            notifyDataSetChanged();
        }

        public void setOnEventClickListener(OnEventClickListener listener) {
            this.eventClickListener = listener;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            EventDetails event = events.get(position);
            holder.eventNameTextView.setText("Event: " + event.getEventName());
            holder.eventDescriptionTextView.setText("Description: " + event.getEventDescription());
            holder.startTimeTextView.setText("Start Time: " + event.getStartTime());
            holder.endTimeTextView.setText("End Time: " + event.getEndTime());
            holder.locationTextView.setText("Location: " + event.getLocation());

            if (!TextUtils.isEmpty(event.getPosterUrl())) {
                Glide.with(holder.itemView)
                        .load(event.getPosterUrl())
                        .placeholder(R.drawable.default_poster)
                        .into(holder.posterImageView);
            } else {
                holder.posterImageView.setImageResource(R.drawable.default_poster);
            }

            holder.itemView.setOnClickListener(v -> {
                if (eventClickListener != null) {
                    eventClickListener.onEventClick(event);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events == null ? 0 : events.size();
        }

        static class EventViewHolder extends RecyclerView.ViewHolder {
            TextView eventNameTextView;
            TextView eventDescriptionTextView;
            TextView startTimeTextView;
            TextView endTimeTextView;
            TextView locationTextView;
            ImageView posterImageView;

            EventViewHolder(@NonNull View itemView) {
                super(itemView);
                eventNameTextView = itemView.findViewById(R.id.tv_event_name);
                eventDescriptionTextView = itemView.findViewById(R.id.tv_event_description);
                startTimeTextView = itemView.findViewById(R.id.tv_start_time);
                endTimeTextView = itemView.findViewById(R.id.tv_end_time);
                locationTextView = itemView.findViewById(R.id.tv_location);
                posterImageView = itemView.findViewById(R.id.iv_poster);
            }
        }
    }

    private interface OnEventClickListener {
        void onEventClick(EventDetails event);
    }
}
