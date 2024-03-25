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
import com.example.qr_check_in.data.EventDetailsFetcher;

import java.util.List;

/**
 * A fragment to display a list of events fetched from the server.
 */
public class EventsFragment extends Fragment implements EventDetailsFetcher.OnEventDetailsReceivedListener {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return the root View of the fragment's layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = root.findViewById(R.id.recyclerView_events);

        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new EventListAdapter();
        recyclerView.setAdapter(adapter);

        // Fetch events details
        EventDetailsFetcher eventDetailsFetcher = new EventDetailsFetcher();
        eventDetailsFetcher.fetchEventDetails(this);

        return root;
    }

    /**
     * Callback method to receive the fetched event details from the server.
     *
     * @param eventDetailsList List of EventDetails objects containing information about events.
     */
    @Override
    public void onEventDetailsReceived(List<EventDetails> eventDetailsList) {
        adapter.setEvents(eventDetailsList);
    }

    /**
     * Callback method to handle errors occurred during the fetching process.
     *
     * @param message Error message describing the issue.
     */
    @Override
    public void onError(String message) {
        // Handle error
    }

    /**
     * Adapter for displaying events in RecyclerView.
     */
    private class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

        private List<EventDetails> events;
        private OnEventClickListener eventClickListener;

        /**
         * Sets the list of events to be displayed.
         *
         * @param events List of EventDetails objects.
         */
        public void setEvents(List<EventDetails> events) {
            this.events = events;
            notifyDataSetChanged();
        }

        /**
         * Sets a listener for event clicks.
         *
         * @param listener OnEventClickListener instance.
         */
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

            // Load poster image using Glide
            if (!TextUtils.isEmpty(event.getPosterUrl())) {
                Glide.with(holder.itemView)
                        .load(event.getPosterUrl())
                        .placeholder(R.drawable.default_poster)
                        .into(holder.posterImageView);
            } else {
                holder.posterImageView.setImageResource(R.drawable.default_poster); // Set default poster if no URL provided
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

        /**
         * ViewHolder class for events.
         */
        class EventViewHolder extends RecyclerView.ViewHolder {
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

    /**
     * Interface for handling event clicks.
     */
    private interface OnEventClickListener {
        /**
         * Called when an event is clicked.
         *
         * @param event Clicked EventDetails object.
         */
        void onEventClick(EventDetails event);
    }
}
