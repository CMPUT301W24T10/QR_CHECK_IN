package com.example.qr_check_in.ModelClasses;

/**
 * The Organizer class is a subclass of the User class, representing organizers within the system.
 * It inherits the username and UID attributes from the User class.
 */
public class Organizer extends User {

    /**
     * Constructs a new Organizer object with the specified username and UID.
     *
     * @param username The username of the organizer.
     * @param UID      The unique identifier of the organizer.
     */
    public Organizer(String username, String UID) {
        super(username, UID);
    }

    /**
     * Checks if the user is an organizer.
     *
     * @return Always returns true since this is an Organizer object.
     */
    public boolean isOrganizer() {
        return true;
    }

    /**
     * Checks if the user is an attendee.
     *
     * @return Always returns false since this is an Organizer object.
     */
    public boolean isAttendee() {
        return false;
    }
}
