package com.example.qr_check_in.StartupFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AnnouncementFetcher;
import com.example.qr_check_in.ModelClasses.Announcement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * AnnouncementsFragment.java
 * This fragment displays a list of announcements fetched from a server.
 * It utilizes AnnouncementFetcher to fetch announcements asynchronously.
 * It contains an AnnouncementAdapter for populating the list view with announcement data.
 * Outstanding issue: None known.
 */

public class AnnouncementsFragment extends Fragment {
    private ListView announcementListView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcements;

    // Fragment lifecycle method for creating the view
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_announcements, container, false);

        // Initialize views and adapters
        announcements = new ArrayList<>();
        announcementListView = root.findViewById(R.id.list_of_announcements);
        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        announcementListView.setAdapter(announcementAdapter);

        // Fetch announcements asynchronously
        new AnnouncementFetcher().fetchAnnouncements(new AnnouncementFetcher.OnAnnouncementListReceivedListener() {
            @Override
            public void onAnnouncementListReceived(List<Announcement> announcementList) {
                // Update UI with fetched announcements
                announcements.clear();
                announcements.addAll(announcementList);
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                // Show error message if fetching fails
                Toast.makeText(getContext(), "Error fetching announcements: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    // Custom adapter for displaying announcements in a list view
    private class AnnouncementAdapter extends ArrayAdapter<Announcement> {
        public AnnouncementAdapter(@NonNull Context context, List<Announcement> announcements) {
            super(context, 0, announcements);
        }

        // Method to populate the view for each announcement item
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_item, parent, false);
            }

            // Get references to view elements
            TextView tvTime = convertView.findViewById(R.id.tv_announcement_time);
            TextView tvMessage = convertView.findViewById(R.id.tv_announcement_message);

            // Get the current announcement object
            Announcement announcement = getItem(position);
            if (announcement != null) {
                // Format the time and set it to the time text view
                String formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(announcement.getTime().toDate());
                tvTime.setText(formattedTime);
                // Set the announcement message to the message text view
                tvMessage.setText(announcement.getMessage());
            }

            return convertView;
        }
    }
}
