package com.example.qr_check_in.ModelClasses;

public class FirestoreEvent {
    private String title; // Ensure the field names match with Firestore document
    private String description;
    private String location;

    public FirestoreEvent() {
    }

    // No-argument constructor required for Firestore deserialization
    public FirestoreEvent(String eventName, String eventDescription, String eventLocation) {
        this.title = eventName;
        this.description = eventDescription;
        this.location = eventLocation;
    }

    // Getters (Setters are not strictly necessary for Firestore if we're only reading data)
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
}