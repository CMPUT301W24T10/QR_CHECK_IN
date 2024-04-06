package com.example.qr_check_in.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.EventFetcher;
import com.example.qr_check_in.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getEventName().observe(getViewLifecycleOwner(), name -> {
            binding.textEventName.setText(name);
        });

        homeViewModel.getEventDescription().observe(getViewLifecycleOwner(), description -> {
            binding.textEventDescription.setText(description);
        });

        binding.buttonMoreDetails.setOnClickListener(view -> navigateToEventDetailsFragment());

        fetchEventDetails();

        return root;
    }

    private void navigateToEventDetailsFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_home_to_event_details);
    }

    private void fetchEventDetails() {
        new EventFetcher(requireContext()).fetchCurrentEventId(new EventFetcher.OnCurrentEventIdFetchedListener() {
            @Override
            public void onCurrentEventIdFetched(String currentEventId) {
                FirebaseFirestore.getInstance().collection("events").document(currentEventId)
                        .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String eventName = document.getString("eventName");
                                    String eventDescription = document.getString("eventDescription");
                                    String posterUrl = document.getString("posterUrl");

                                    binding.textEventName.setText(eventName);
                                    binding.textEventDescription.setText(eventDescription);

                                    // Only load image if posterUrl is not null and not empty
                                    if (posterUrl != null && !posterUrl.isEmpty()) {
                                        Glide.with(requireContext())
                                                .load(posterUrl)
                                                .placeholder(R.drawable.default_poster)
                                                .into(binding.imageEvent);
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onError(String error) {
                // Handle errors, e.g., by showing a toast
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
