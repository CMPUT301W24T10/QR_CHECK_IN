package com.example.qr_check_in.ModelClasses;

import java.util.HashMap;
/**
 *  @author sarao
 *  Event class is used to create an event object that contains the title, date, time, location,
 *  description, organizer, attendees, and the number of attendees.
 */

public class Event {
    private String title;
    private String date;
    private String time;
    private String location;
    private String description;
    private Organizer organizer;
    private HashMap<String, Attendee> attendees;
    private int numAttendees;
    private int maxAttendees;
    private boolean isFull;
    private String eventID;
    public Event(String title, Organizer organizer, String description, String eventID){
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.attendees = new HashMap<String, Attendee>();
        this.numAttendees = 0;
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }
    public Organizer getOrganizer() {
        return organizer;
    }
    public String getDescription() {
        return description;
    }
    public Attendee getAttendee(int UID) {
        return attendees.get(UID);
    }
    public boolean addAttendee(Attendee attendee) {
        if (numAttendees < maxAttendees) {
            attendees.put(attendee.getUID(), attendee);
            numAttendees++;
            return true;
        }
        else if (numAttendees >= maxAttendees) {
            isFull = true;
        }
        return false;
    }


}
