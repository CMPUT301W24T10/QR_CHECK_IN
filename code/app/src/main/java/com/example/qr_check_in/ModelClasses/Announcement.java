/**
 * This class represents the Announcement model in the QR Check-In application.
 * It encapsulates the data related to an announcement.
 */
package com.example.qr_check_in.ModelClasses;

public class Announcement {
    private String message;
    private String time;

    public Announcement() {
        // Default constructor required for Firebase
    }

    public Announcement(String message, String time) {
        this.message = message;
        this.time = time;
    }

    /**
     * Get the message of the announcement.
     * @return The message of the announcement.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message of the announcement.
     * @param message The message of the announcement.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the time of the announcement.
     * @return The time of the announcement.
     */
    public String getTime() {
        return time;
    }

    /**
     * Set the time of the announcement.
     * @param time The time of the announcement.
     */
    public void setTime(String time) {
        this.time = time;
    }
}
