package com.example.qr_check_in.geolocation;

// Required imports:
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String eventId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false); // Replace with your fragment layout file
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String eventId = bundle.getString("eventId");
            if (eventId != null) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); // Replace with your map fragment ID
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadCheckIns(eventId, mMap);
    }

    // Method to load check-ins and display them on the map:
    public void loadCheckIns(String eventId, GoogleMap googleMap) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Map<String, Object> checkIns = (Map<String, Object>) documentSnapshot.get("checkIns");
                        if (checkIns != null) {
                            for (Map.Entry<String, Object> entry : checkIns.entrySet()) {
                                List<Map<String, Double>> deviceCheckIns = (List<Map<String, Double>>) entry.getValue();
                                for (Map<String, Double> checkIn : deviceCheckIns) {
                                    Double latitude = checkIn.get("latitude");
                                    Double longitude = checkIn.get("longitude");
                                    if (latitude != null && longitude != null) {
                                        LatLng location = new LatLng(latitude, longitude);
                                        googleMap.addMarker(new MarkerOptions().position(location));
                                    }
                                }
                            }
                        }
                    } else {
                        // Document does not exist
                        // Handle the case appropriately
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    // Log the error or notify the user
                });
    }
}
