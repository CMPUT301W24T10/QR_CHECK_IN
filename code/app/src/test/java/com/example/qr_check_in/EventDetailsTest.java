package com.example.qr_check_in;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.EventDetails;

public class EventDetailsTest {

    private EventDetails eventDetails;

    @Before
    public void setUp() {
        // Initialize your EventDetails object here with some test data
        eventDetails = new EventDetails("Tech Conference", "A conference on the latest in tech.", "2024-04-08 09:00", "2024-04-08 17:00", "Convention Center", "http://example.com/poster.png");
    }

    @Test
    public void testEventNameSetterAndGetter() {
        // Test the setter
        eventDetails.setEventName("Updated Event Name");
        // Verify the getter
        assertEquals("Updated Event Name", eventDetails.getEventName());
    }

    @Test
    public void testEventDescriptionSetterAndGetter() {
        // Test the setter
        eventDetails.setEventDescription("Updated Description");
        // Verify the getter
        assertEquals("Updated Description", eventDetails.getEventDescription());
    }

    @Test
    public void testStartTimeSetterAndGetter() {
        // Test the setter
        eventDetails.setStartTime("2024-04-08 10:00");
        // Verify the getter
        assertEquals("2024-04-08 10:00", eventDetails.getStartTime());
    }

    @Test
    public void testEndTimeSetterAndGetter() {
        // Test the setter
        eventDetails.setEndTime("2024-04-08 18:00");
        // Verify the getter
        assertEquals("2024-04-08 18:00", eventDetails.getEndTime());
    }

    @Test
    public void testLocationSetterAndGetter() {
        // Test the setter
        eventDetails.setLocation("Updated Location");
        // Verify the getter
        assertEquals("Updated Location", eventDetails.getLocation());
    }

    @Test
    public void testPosterUrlSetterAndGetter() {
        // Test the setter
        eventDetails.setPosterUrl("http://example.com/updated_poster.png");
        // Verify the getter
        assertEquals("http://example.com/updated_poster.png", eventDetails.getPosterUrl());
    }

}
