package com.example.qr_check_in.ModelClasses;

/*
 * Announcement.java
 * This class represents the Announcement model in the QR Check-In application.
 * It encapsulates the data related to an announcement.
 */

public class Announcement {
    private String notificationTitle;
    private String dateandTime;
    private String notification;

    // Constructor
    public Announcement(String notificationTitle, String dateandTime, String notification) {
        this.notificationTitle = notificationTitle;
        this.dateandTime = dateandTime;
        this.notification = notification;
    }

    // Getter for notificationTitle
    public String getNotificationTitle() {
        return notificationTitle;
    }

    // Getter for dateandTime
    public String getDateandTime() {
        return dateandTime;
    }

    // Getter for notification
    public String getNotification() {
        return notification;
    }

    // Setter for notificationTitle
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    // Setter for dateandTime
    public void setDateandTime(String dateandTime) {
        this.dateandTime = dateandTime;
    }

    // Setter for notification
    public void setNotification(String notification) {
        this.notification = notification;
    }
}
