package com.example.qr_check_in.ModelClasses;

/**
 * The User class is an abstract class that serves as the base class for creating Organizer and Attendee objects.
 * It encapsulates common attributes and behaviors shared by both types of users.
 */
public abstract class User {
    private String username; // The username of the user
    private String UID; // The unique identifier of the user

    /**
     * Constructs a new User object with the specified username and UID.
     *
     * @param username The username of the user.
     * @param UID      The unique identifier of the user.
     */
    public User(String username, String UID) {
        this.username = username;
        this.UID = UID;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the UID of the user.
     *
     * @return The UID of the user.
     */
    public String getUID() {
        return UID;
    }

    /**
     * Checks if the user is an organizer.
     *
     * @return True if the user is an organizer, false otherwise.
     */
    public abstract boolean isOrganizer();

    /**
     * Checks if the user is an attendee.
     *
     * @return True if the user is an attendee, false otherwise.
     */
    public abstract boolean isAttendee();
}