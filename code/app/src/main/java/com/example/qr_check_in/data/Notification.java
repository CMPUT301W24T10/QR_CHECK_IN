package com.example.qr_check_in.data;

import java.util.Objects;
import java.util.Date;

public class Notification {

    private String notificationTitle;
    private String notification;
    private String eventId;
    private String DateandTime;

    public Notification(String notificationTitle, String notification, String selectedeventidrequired, String DateandTime) {
        this.notificationTitle = notificationTitle;
        this.notification = notification;
        this.eventId = selectedeventidrequired;
        this.DateandTime = DateandTime;
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

    public String getDateandTime(){
        return DateandTime;
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
        return Objects.hash(notificationTitle, notification, eventId, DateandTime);
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

