package com.example.qr_check_in.geolocation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Before creating the view, set the user agent to prevent getting banned from the OSM servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize and configure the map
        initializeMap(view);
        fetchLocationsAndAddMarkers();

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
            map.onResume(); // Existing code

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause(); // Necessary for some map features such as compass and location overlay
        }
    }

    private void fetchLocationsAndAddMarkers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events") // Replace with your actual collection path
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot eventDoc : task.getResult()) {
                            if (eventDoc.contains("checkIns")) {
                                Map<String, Object> checkInsMap = (Map<String, Object>) eventDoc.get("checkIns");
                                for (Map.Entry<String, Object> checkInEntry : checkInsMap.entrySet()) {
                                    Object checkInObj = checkInEntry.getValue();
                                    if (checkInObj instanceof ArrayList) {
                                        ArrayList<Map<String, Object>> checkInList = (ArrayList<Map<String, Object>>) checkInObj;
                                        for (Map<String, Object> locationMap : checkInList) {
                                            Double latitude = null;
                                            Double longitude = null;
                                            if (locationMap.containsKey("latitude") && locationMap.containsKey("longitude")) {
                                                latitude = (Double) locationMap.get("latitude");
                                                longitude = (Double) locationMap.get("longitude");
                                            }
                                            if (latitude != null && longitude != null) {
                                                // Add a marker for each checked-in location
                                                addMarkerToMap(latitude, longitude);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (map != null) {
                            map.invalidate(); // Refresh the map to display the new markers
                        }
                    } else {
                        Log.w("MapsFragment", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void addMarkerToMap(double latitude, double longitude) {
        if (map == null) return;

        GeoPoint location = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(map);
        marker.setPosition(location);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // Customize the marker icon if you wish
        // Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.custom_marker_icon);
        // marker.setIcon(icon);

        map.getOverlays().add(marker);
    }

}
