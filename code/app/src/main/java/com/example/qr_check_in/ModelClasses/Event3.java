package com.example.qr_check_in.ModelClasses;

public class Event3 {
    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private String eventPoster;

    public Event3(String eventName, String eventDescription, String eventLocation, String eventPoster) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventPoster = eventPoster;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventPoster() {
        return eventPoster;
    }


}
