package com.example.qr_check_in.geolocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLocationManager {

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserLocationManager(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }


    @SuppressLint("MissingPermission")
    // Handle permissions appropriately in your activity before calling this
    public void checkInUser(String eventId, String userId) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                Map<String, Object> checkInLocation = new HashMap<>();
                checkInLocation.put("latitude", location.getLatitude());
                checkInLocation.put("longitude", location.getLongitude());

                DocumentReference eventDocRef = db.collection("events").document(eventId);

                // Approach to append the location to the specific userID's list of check-ins
                eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> allCheckIns = (Map<String, Object>) documentSnapshot.get("checkIns");
                    if (allCheckIns == null) {
                        allCheckIns = new HashMap<>();
                    }
                    List<Map<String, Object>> userCheckIns = (List<Map<String, Object>>) allCheckIns.get(userId);
                    if (userCheckIns == null) {
                        userCheckIns = new ArrayList<>();
                    }
                    userCheckIns.add(checkInLocation);
                    allCheckIns.put(userId, userCheckIns);

                    eventDocRef.update("checkIns", allCheckIns)
                            .addOnSuccessListener(aVoid -> System.out.println("Check-in location added successfully."))
                            .addOnFailureListener(e -> System.out.println("Error adding check-in location."));
                });

                fusedLocationClient.removeLocationUpdates(this);
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}