package com.example.qr_check_in.StartupFragments;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.qr_check_in.R;



public class AttendeeSelection_fragment extends Fragment {

    private Button geolocation;
    private Button editProfile;
    private static final String ARG_DEVICE_ID_KEY = "device_id";

    // Method to create a new instance of AttendeeSelection_fragment with device ID
    public static AttendeeSelection_fragment newInstance(String deviceId) {
        AttendeeSelection_fragment fragment = new AttendeeSelection_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID_KEY, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_selection, container, false);

        String deviceId = getArguments().getString(ARG_DEVICE_ID_KEY, "");

        setActionBarTitle("User Options");
        geolocation = view.findViewById(R.id.geoTracking);
        editProfile = view.findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("deviceId", deviceId);
                Navigation.findNavController(requireView()).navigate(R.id.action_attendeeSelection_fragment_to_profilePageFragment, bundle);
            }
        });

        geolocation.setOnClickListener(v -> requestLocationPermissions());
        return view;
    }

    private void setActionBarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    private void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationPermissionRequest.launch(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}
            );
        } else {
            // Permissions not needed for older Android versions.
            // Perform your location-related tasks here.
        }
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                        } else {
                            // No location access granted.
                        }
                    }
            );
}
