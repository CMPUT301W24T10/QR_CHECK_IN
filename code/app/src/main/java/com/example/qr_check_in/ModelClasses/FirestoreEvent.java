package com.example.qr_check_in.ModelClasses;
/**
 * The FirestoreEvent class represents an event stored in Firestore within the QR Check-In application.
 * It encapsulates the data related to an event, including its title, ID, description, and location.
 */
public class FirestoreEvent {
    private String title; // Ensure the field names match with Firestore document
    private String eventId;
    private String description;
    private String location;
    /**
     * Constructs an empty FirestoreEvent object.
     * This constructor is typically used for Firestore deserialization.
     */
    public FirestoreEvent() {
    }

    /**
     * Constructs a FirestoreEvent object with the specified ID, title, description, and location.
     *
     * @param eventId the ID of the event
     * @param eventName the title of the event
     * @param eventDescription the description of the event
     * @param eventLocation the location of the event
     */
    public FirestoreEvent(String eventId, String eventName, String eventDescription, String eventLocation) {
        this.eventId = eventId;
        this.title = eventName;
        this.description = eventDescription;
        this.location = eventLocation;
    }

    /**
     * Returns the ID of the event.
     *
     * @return the ID of the event
     */
    public String getEventId() { return eventId; }
    /**
     * Returns the title of the event.
     *
     * @return the title of the event
     */
    public String getTitle() { return title; }
    /**
     * Returns the description of the event.
     *
     * @return the description of the event
     */
    public String getDescription() { return description; }
    /**
     * Returns the location of the event.
     *
     * @return the location of the event
     */
    public String getLocation() { return location; }
}