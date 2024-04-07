package com.example.qr_check_in.geolocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventLocationFetcher extends ViewModel {

    private final MutableLiveData<List<GeoPoint>> locationsLiveData = new MutableLiveData<>();

    public LiveData<List<GeoPoint>> getEventLocations(String eventID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<GeoPoint> locationsList = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> checkIns = (Map<String, Object>) documentSnapshot.get("checkIns");
                        if (checkIns != null) {
                            for (Map.Entry<String, Object> entry : checkIns.entrySet()) {
                                ArrayList<Map<String, Double>> userCheckIns = (ArrayList<Map<String, Double>>) entry.getValue();
                                for (Map<String, Double> checkIn : userCheckIns) {
                                    Double lat = checkIn.get("latitude");
                                    Double lon = checkIn.get("longitude");
                                    if (lat != null && lon != null) {
                                        locationsList.add(new GeoPoint(lat, lon));
                                    }
                                }
                            }
                        }
                        locationsLiveData.setValue(locationsList);
                    } else {
                        locationsLiveData.setValue(new ArrayList<>()); // Empty list on failure
                    }
                });
        return locationsLiveData;
    }
}
