package com.example.qr_check_in.geolocation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapsFragment extends Fragment {

    private MapView map = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Before creating the view, set the user agent to prevent getting banned from the OSM servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize and configure the map
        initializeMap(view);

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
            map.onResume(); // Necessary for some map features such as compass and location overlay
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause(); // Necessary for some map features such as compass and location overlay
        }
    }
}
