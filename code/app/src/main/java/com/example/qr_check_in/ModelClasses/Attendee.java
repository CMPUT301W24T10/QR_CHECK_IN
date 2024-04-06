package com.example.qr_check_in.ModelClasses;

/**
 * The Attendee class is a subclass of the User class, representing attendees within the system.
 * It inherits the username and UID attributes from the User class.
 */
public class Attendee extends User {

    /**
     * Constructs a new Attendee object with the specified username and UID.
     *
     * @param username The username of the attendee.
     * @param UID      The unique identifier of the attendee.
     */
    public Attendee(String username, String UID) {
        super(username, UID);
    }

    /**
     * Checks if the user is an organizer.
     *
     * @return Always returns false since this is an Attendee object.
     */
    public boolean isOrganizer() {
        return false;
    }

    /**
     * Checks if the user is an attendee.
     *
     * @return Always returns true since this is an Attendee object.
     */
    public boolean isAttendee() {
        return true;
    }
}
