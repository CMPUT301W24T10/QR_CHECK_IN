package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsFragment extends Fragment {

    private ListView announcementListView;
    private ArrayAdapter<String> adapter;
    private List<String> announcementsList;
    private DatabaseReference announcementsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);

        announcementListView = view.findViewById(R.id.announcementListView);
        announcementsList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.announcement_item, R.id.messageTextView, announcementsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.announcement_item, parent, false);
                }

                TextView timeTextView = convertView.findViewById(R.id.timeTextView);
                TextView messageTextView = convertView.findViewById(R.id.messageTextView);

                String announcement = announcementsList.get(position);
                String[] parts = announcement.split(" - ");
                String time = parts[0];
                String message = parts[1];

                timeTextView.setText(time);
                messageTextView.setText(message);

                return convertView;
            }
        };
        announcementListView.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            announcementsRef = FirebaseDatabase.getInstance().getReference().child("announcements");

            announcementsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    announcementsList.clear();
                    for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                        String message = announcementSnapshot.child("message").getValue(String.class);
                        String time = announcementSnapshot.child("time").getValue(String.class);
                        announcementsList.add(time + " - " + message);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }

        return view;
    }
}
