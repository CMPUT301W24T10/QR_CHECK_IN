package com.example.qr_check_in.data;

/**
 * NotificationData class for notification
 */

public class NotificationData {
    private final String title;
    private final String message;
    public NotificationData(String title, String message) {
        this.title = title;
        this.message = message;
    }



    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }





}

