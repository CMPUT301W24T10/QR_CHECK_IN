package com.example.qr_check_in.geolocation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qr_check_in.R;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeesViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Map;

public class MapsFragment extends Fragment {

    private MapView map = null;

    private String eventId;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Before creating the view, set the user agent to prevent getting banned from the OSM servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ListOfAttendeesViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(ListOfAttendeesViewModel.class);
        eventId = sharedViewModel.getEventId();

        // Initialize and configure the map
        initializeMap(view);
        fetchLocationsAndAddMarkers(eventId);

        return view;
    }

    private void initializeMap(View view) {
        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // Use the default OpenStreetMap tile source

        // Configure initial map state
        map.getController().setZoom(9.5);
        //Start point set to University of Alberta
        GeoPoint startPoint = new GeoPoint(53.5232, -113.5263);
        map.getController().setCenter(startPoint);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.getOverlayManager().clear();
            map.invalidate(); // Force the map to refresh and redraw overlays
            fetchLocationsAndAddMarkers(eventId); // Fetch and display markers again
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause(); // Necessary for some map features such as compass and location overlay
        }
    }

    private void fetchLocationsAndAddMarkers(String currentEventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(currentEventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDoc = task.getResult();
                        if (eventDoc != null && eventDoc.exists() && eventDoc.contains("checkIns")) {
                            // Clear any existing markers
                            map.getOverlays().clear();

                            Object checkInsObj = eventDoc.get("checkIns");
                            if (checkInsObj instanceof Map) {
                                Map<String, Object> checkInsMap = (Map<String, Object>) checkInsObj;
                                for (Map.Entry<String, Object> entry : checkInsMap.entrySet()) {
                                    ArrayList<Map<String, Double>> checkInsList = (ArrayList<Map<String, Double>>) entry.getValue();
                                    for (Map<String, Double> checkIn : checkInsList) {
                                        Double latitude = checkIn.get("latitude");
                                        Double longitude = checkIn.get("longitude");
                                        if (latitude != null && longitude != null) {
                                            // Add marker to map
                                            addMarkerToMap(latitude, longitude);
                                        }
                                    }
                                }
                            }

                            map.invalidate(); // Refresh the map to display new markers
                        } else {
                            Log.d("MapsFragment", "No check-ins for the current event or event not found.");
                        }
                    } else {
                        Log.w("MapsFragment", "Error getting event document:", task.getException());
                    }
                });
    }
    private void addMarkerToMap(double latitude, double longitude) {
        if (map == null) return;

        if (latitude < -85.05112877980658 || latitude > 85.05112877980658) {
            Log.e("MapsFragment", "Invalid latitude value: " + latitude);
            return;
        }
        if (longitude < -180 || longitude > 180) {
            Log.e("MapsFragment", "Invalid longitude value: " + longitude);
            return;
        }



        GeoPoint location = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(map);
        marker.setPosition(location);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        map.getOverlays().add(marker);
    }
}
