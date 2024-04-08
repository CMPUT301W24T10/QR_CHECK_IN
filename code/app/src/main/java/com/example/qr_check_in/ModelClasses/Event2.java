package com.example.qr_check_in.ModelClasses;
/**
 * The Event2 class represents an event in the QR Check-In application.
 * It encapsulates the data related to an event, including its name and description.
 */
public class Event2 {
    private String eventName;
    private String eventDescription;
    /**
     * Constructs an Event2 object with the specified name and description.
     *
     * @param eventName the name of the event
     * @param eventDescription the description of the event
     */
    public Event2(String eventName, String eventDescription) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }
    /**
     * Returns the name of the event.
     *
     * @return the name of the event
     */
    public String getEventName() {
        return eventName;
    }
    /**
     * Returns the description of the event.
     *
     * @return the description of the event
     */
    public String getEventDescription() {
        return eventDescription;
    }
}
