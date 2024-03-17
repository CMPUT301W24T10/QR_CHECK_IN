package com.example.qr_check_in.ModelClasses;

public class Event2 {
    private String eventName;
    private String eventDescription;

    public Event2(String eventName, String eventDescription) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }
}
