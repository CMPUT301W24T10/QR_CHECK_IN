package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.Attendee;
import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.Organizer;

public class EventTest {
    private Event event;
    private Organizer organizer;
    private Attendee attendee;

    @Before
    public void setUp() {
        organizer = new Organizer("organizerUsername", "organizerUID");
        event = new Event("Test Event", organizer, "This is a test event description.", "eventID123");

        // Assume maximum attendees is a public field or can be set via a method
        event.maxAttendees = 10;
    }

    @Test
    public void eventInitializationTest() {
        assertEquals("Test Event", event.getTitle());
        assertEquals("This is a test event description.", event.getDescription());
        assertSame(organizer, event.getOrganizer());
        assertNotNull(event.attendees);
        assertEquals(0, event.numAttendees);
        assertEquals("eventID123", event.eventID);
    }

    @Test
    public void addingAttendeeIncreasesCountTest() {
        attendee = new Attendee("attendeeUsername", "attendeeUID");
        assertTrue(event.addAttendee(attendee));
        assertEquals(1, event.numAttendees);
        assertFalse(event.isFull);
    }

    @Test
    public void addingAttendeeWhenFullReturnsFalseTest() {
        // Fill the event to its max capacity
        for (int i = 0; i < event.maxAttendees; i++) {
            attendee = new Attendee("attendeeUsername" + i, "attendeeUID" + i);
            event.addAttendee(attendee);
        }

        // Try to add one more attendee
        attendee = new Attendee("extraAttendee", "extraUID");
        assertFalse(event.addAttendee(attendee));
        assertTrue(event.isFull);
    }

}

