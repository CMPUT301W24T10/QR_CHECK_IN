package com.example.qr_check_in.ModelClasses;

/**
 * Represents details of an event, including its name, description, start and end times, location, and poster URL.
 */
public class EventDetails {
    private String eventName;
    private String eventDescription;
    private String startTime;
    private String endTime;
    private String location;
    private String posterUrl; // New field for poster URL

    /**
     * Constructs a new EventDetails object with the specified details.
     *
     * @param eventName       The name of the event.
     * @param eventDescription The description of the event.
     * @param startTime       The start time of the event.
     * @param endTime         The end time of the event.
     * @param location        The location of the event.
     * @param posterUrl       The URL of the poster for the event.
     */
    public EventDetails(String eventName, String eventDescription, String startTime, String endTime, String location, String posterUrl) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.posterUrl = posterUrl;
    }

    /**
     * Gets the name of the event.
     *
     * @return The name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     *
     * @param eventName The name of the event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the description of the event.
     *
     * @return The description of the event.
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the description of the event.
     *
     * @param eventDescription The description of the event.
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time of the event.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime The start time of the event.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time of the event.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime The end time of the event.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the location of the event.
     *
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location The location of the event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the URL of the poster for the event.
     *
     * @return The URL of the poster for the event.
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Sets the URL of the poster for the event.
     *
     * @param posterUrl The URL of the poster for the event.
     */
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
