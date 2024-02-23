package appMembers;

import java.util.HashMap;

public class Event {
    private String title;
    private String date;
    private String time;
    private String location;
    private String description;
    private Organizer organizer;
    private HashMap<Integer, Attendee> attendees;
    private int numAttendees;
    private int maxAttendees;
    private boolean isFull;
    public Event(String title, Organizer organizer, String description){
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.attendees = new HashMap<Integer, Attendee>();
    }

    public String getTitle() {
        return title;
    }
    public Organizer getOrganizer() {
        return organizer;
    }
    public String getDescription() {
        return description;
    }
    public Attendee getAttendee(int UID) {
        return attendees.get(UID);
    }
    public boolean addAttendee(Attendee attendee) {
        if (numAttendees < maxAttendees) {
            attendees.put(attendee.getUID(), attendee);
            numAttendees++;
            return true;
        }
        else if (numAttendees >= maxAttendees) {
            isFull = true;
        }
        return false;
    }


}
