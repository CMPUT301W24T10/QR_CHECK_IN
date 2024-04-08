package com.example.qr_check_in;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationUpdater {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore firestore;

    public LocationUpdater(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        firestore = FirebaseFirestore.getInstance();
    }

    public void fetchAndUpdateLocation(String eventId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, handle it accordingly
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // Update interval
        locationRequest.setFastestInterval(5000); // Fastest interval

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                updateLocationInFirestore(eventId, location);
                fusedLocationClient.removeLocationUpdates(this); // Stop location updates
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void updateLocationInFirestore(String eventId, Location location) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("latitude", location.getLatitude());
        locationData.put("longitude", location.getLongitude());

        firestore.collection("events").document(eventId)
                .update("userLocations." + deviceId, locationData)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated the location
                })
                .addOnFailureListener(e -> {
                    // Handle the failure of location update
                });
    }
}

