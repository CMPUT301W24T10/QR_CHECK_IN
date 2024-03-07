package landingPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnnouncementsFragment extends Fragment {

    private LinearLayout announcementsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        announcementsLayout = view.findViewById(R.id.announcements_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch announcements and update UI
        fetchAnnouncements();
    }

    private void fetchAnnouncements() {
        // This is where you would fetch announcements from your data source
        // For demonstration, we'll just create dummy announcements
        for (int i = 0; i < 5; i++) {
            // Create a new LinearLayout for each announcement item
            LinearLayout announcementItemLayout = new LinearLayout(getContext());
            announcementItemLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 16); // Add some bottom margin for spacing
            announcementItemLayout.setLayoutParams(layoutParams);

            // Create a TextView for the time of the announcement
            TextView timeTextView = new TextView(getContext());
            timeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            timeTextView.setText("Time of Announcement: " + currentTime);

            // Create a TextView for the content of the announcement
            TextView contentTextView = new TextView(getContext());
            contentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            contentTextView.setText("Announcement details for announcement " + (i + 1));

            // Add the TextViews to the announcement item layout
            announcementItemLayout.addView(timeTextView);
            announcementItemLayout.addView(contentTextView);

            // Add the announcement item layout to the main announcements layout
            announcementsLayout.addView(announcementItemLayout);
        }
    }
}
