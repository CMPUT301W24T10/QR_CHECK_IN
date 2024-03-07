package com.example.qr_check_in.ModelClasses;

/**
 * @author sarao
 * Organizer class is a subclass of User class. It is used to create Organizer objects.
 * It has a constructor that takes in a username and sets the role of the user to "organizer".
 * It has two methods that return boolean values to check if the user is an organizer or an attendee.
 */
public class Organizer extends User{
    public Organizer(String username, String UID) {
        super(username, UID);
    }
    public boolean isOrganizer() {
        return true;
    }
    public boolean isAttendee() {
        return false;
    }
}
