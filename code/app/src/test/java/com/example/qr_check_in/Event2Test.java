package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.Event2;

public class Event2Test {
    private Event2 event;

    @Before
    public void setUp() {
        // Initialize an Event2 object before each test
        event = new Event2("Sample Event", "This is a sample description.");
    }

    @Test
    public void eventNameIsCorrect() {
        // Assert that the event name is set and retrieved correctly
        assertEquals("Event name should be 'Sample Event'", "Sample Event", event.getEventName());
    }

    @Test
    public void eventDescriptionIsCorrect() {
        // Assert that the event description is set and retrieved correctly
        assertEquals("Event description should be 'This is a sample description.'", "This is a sample description.", event.getEventDescription());
    }
}
