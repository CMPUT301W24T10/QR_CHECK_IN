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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
