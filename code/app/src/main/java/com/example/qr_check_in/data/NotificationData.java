package com.example.qr_check_in.data;

public class NotificationData {
    private String message;

    public NotificationData(String title, String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

