package com.example.qr_check_in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class PromotionalFragment extends Fragment {
    private TextView tvEventName, tvEventDescription;
    private ImageView ivEventPoster;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotional, container, false);

        db = FirebaseFirestore.getInstance();
        tvEventName = view.findViewById(R.id.tvEventName);
        tvEventDescription = view.findViewById(R.id.tvEventDescription);
        ivEventPoster = view.findViewById(R.id.ivEventPoster);

        // Retrieve the event ID passed from the QRCheckIn_fragment using Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String eventId = bundle.getString("eventId", ""); // Use an empty string as a default value
            fetchEventDetails(eventId);
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> goBack());

        return view;
    }

    private void fetchEventDetails(String eventId) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    tvEventName.setText(document.getString("eventName"));
                    tvEventDescription.setText(document.getString("eventDescription"));

                    String posterUrl = document.getString("posterUrl");
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        Glide.with(this).load(posterUrl)
                                .placeholder(R.drawable.default_poster_placeholder)
                                .into(ivEventPoster);
                    } else {
                        ivEventPoster.setImageResource(R.drawable.default_poster_placeholder);
                    }
                } else {
                    showDataNotFound();
                }
            } else {
                showError();
            }
        });
    }

    private void goBack() {
        NavHostFragment.findNavController(PromotionalFragment.this).popBackStack();
    }

    private void showDataNotFound() {
        tvEventName.setText("Event not found");
        tvEventDescription.setVisibility(View.GONE);
        ivEventPoster.setImageResource(R.drawable.default_poster_placeholder);
    }

    private void showError() {
        tvEventName.setText("Error loading event");
        tvEventDescription.setVisibility(View.GONE);
        ivEventPoster.setImageResource(R.drawable.default_poster_placeholder);
    }
}
