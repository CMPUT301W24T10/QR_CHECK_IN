package com.example.qr_check_in.ModelClasses;

/*
 * Announcement.java
 * This class represents the Announcement model in the QR Check-In application.
 * It encapsulates the data related to an announcement.
 */

import com.google.firebase.Timestamp;

public class Announcement {
    private String message;
    private Timestamp time;

    // Constructor
    public Announcement(String message, Timestamp time) {
        this.message = message;
        this.time = time;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Getter for time
    public Timestamp getTime() {
        return time;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Setter for time
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
