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

    /**
     * Constructs an Announcement object with the specified title, date and time, and content.
     *
     * @param notificationTitle the title of the announcement
     * @param dateandTime the date and time of the announcement
     * @param notification the content of the announcement
     */
    public Announcement(String notificationTitle, String dateandTime, String notification) {
        this.notificationTitle = notificationTitle;
        this.dateandTime = dateandTime;
        this.notification = notification;
    }

    /**
     * Returns the title of the announcement.
     *
     * @return the title of the announcement
     */
    public String getNotificationTitle() {
        return notificationTitle;
    }

    /**
     * Returns the date and time of the announcement.
     *
     * @return the date and time of the announcement
     */
    public String getDateandTime() {
        return dateandTime;
    }

    /**
     * Returns the content of the announcement.
     *
     * @return the content of the announcement
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Sets the title of the announcement.
     *
     * @param notificationTitle the title of the announcement
     */
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    /**
     * Sets the date and time of the announcement.
     *
     * @param dateandTime the date and time of the announcement
     */
    public void setDateandTime(String dateandTime) {
        this.dateandTime = dateandTime;
    }

    /**
     * Sets the content of the announcement.
     *
     * @param notification the content of the announcement
     */
    public void setNotification(String notification) {
        this.notification = notification;
    }
}
