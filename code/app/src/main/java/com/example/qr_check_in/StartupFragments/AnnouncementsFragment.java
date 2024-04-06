package com.example.qr_check_in.StartupFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.qr_check_in.ModelClasses.Announcement;
import com.example.qr_check_in.data.AnnouncementFetcher;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsFragment extends Fragment {
    private ListView announcementListView;
    private AnnouncementAdapter announcementAdapter;
    private String deviceId;
    private List<Announcement> announcements;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_announcements, container, false);

        announcements = new ArrayList<>();
        announcementListView = root.findViewById(R.id.list_of_announcements);
        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        announcementListView.setAdapter(announcementAdapter);
        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        new AnnouncementFetcher().fetchAnnouncementsByDeviceId(deviceId, new AnnouncementFetcher.OnAnnouncementListReceivedListener() {
            @Override
            public void onAnnouncementListReceived(List<Announcement> announcementList) {
                announcements.clear();
                announcements.addAll(announcementList);
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error fetching announcements: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private class AnnouncementAdapter extends ArrayAdapter<Announcement> {
        public AnnouncementAdapter(@NonNull Context context, List<Announcement> announcements) {
            super(context, 0, announcements);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_item, parent, false);
            }

            TextView tvTime = convertView.findViewById(R.id.tv_announcement_time);
            TextView tvTitle = convertView.findViewById(R.id.tv_announcement_title);
            TextView tvMessage = convertView.findViewById(R.id.tv_announcement_message);

            Announcement announcement = getItem(position);
            if (announcement != null) {
                tvTime.setText(announcement.getDateandTime());
                tvTitle.setText(announcement.getNotificationTitle());
                tvMessage.setText(announcement.getNotification());
            }

            return convertView;
        }
    }
}
