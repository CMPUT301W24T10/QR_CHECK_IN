package com.example.qr_check_in.data;

import java.util.Objects;

public class Notification {

    private String notificationTitle;
    private String notification;
    private String eventId;

    public Notification(String notificationTitle, String notification, String selectedeventidrequired) {
        this.notificationTitle = notificationTitle;
        this.notification = notification;
        this.eventId = selectedeventidrequired;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(notificationTitle, that.notificationTitle) &&
                Objects.equals(notification, that.notification) &&
                Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationTitle, notification, eventId);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationTitle='" + notificationTitle + '\'' +
                ", notification='" + notification + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}

