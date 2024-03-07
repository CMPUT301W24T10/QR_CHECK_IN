package com.example.qr_check_in.ModelClasses;
/**
 * @author sarao
 * Attendee class is a subclass of User class. It is used to create Attendee objects.
 * Attendee objects are used to store information about attendees of the Event.
 */

public class Attendee extends User{
    public Attendee(String username, String UID) {
        super(username, UID);
    }
    public boolean isOrganizer() {
        return false;
    }
    public boolean isAttendee() {
        return true;
    }
}
