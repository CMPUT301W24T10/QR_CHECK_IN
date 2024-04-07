package com.example.qr_check_in.ModelClasses;

import java.util.HashMap;

/**
 * The Event class represents an event within the system.
 * It contains information such as title, date, time, location, description, organizer,
 * attendees, and the number of attendees.
 */
public class Event {
    private String title; // The title of the event
    private String date; // The date of the event
    private String time; // The time of the event
    private String location; // The location of the event
    private String description; // The description of the event
    private Organizer organizer; // The organizer of the event
    public HashMap<String, Attendee> attendees; // The attendees of the event
    public int numAttendees; // The number of attendees
    public int maxAttendees; // The maximum number of attendees allowed
    public boolean isFull; // Indicates if the event is full
    public String eventID; // The unique identifier of the event

    /**
     * Constructs a new Event object with the specified title, organizer, description, and eventID.
     *
     * @param title       The title of the event.
     * @param organizer   The organizer of the event.
     * @param description The description of the event.
     * @param eventID     The unique identifier of the event.
     */
    public Event(String title, Organizer organizer, String description, String eventID) {
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.attendees = new HashMap<>();
        this.numAttendees = 0;
        this.eventID = eventID;
    }

    /**
     * Gets the title of the event.
     *
     * @return The title of the event.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the organizer of the event.
     *
     * @return The organizer of the event.
     */
    public Organizer getOrganizer() {
        return organizer;
    }

    /**
     * Gets the description of the event.
     *
     * @return The description of the event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the attendee with the specified UID.
     *
     * @param UID The unique identifier of the attendee.
     * @return The attendee with the specified UID.
     */
    public Attendee getAttendee(int UID) {
        return attendees.get(UID);
    }

    /**
     * Adds an attendee to the event.
     *
     * @param attendee The attendee to add.
     * @return True if the attendee was successfully added, false otherwise.
     */
    public boolean addAttendee(Attendee attendee) {
        if (numAttendees < maxAttendees) {
            attendees.put(attendee.getUID(), attendee);
            numAttendees++;
            return true;
        } else if (numAttendees >= maxAttendees) {
            isFull = true;
        }
        return false;
    }
}
