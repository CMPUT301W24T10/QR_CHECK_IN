package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.Organizer;

public class OrganizerTest {
    private Organizer organizer;

    @Before
    public void setUp() {
        // Initialize an Organizer object before each test
        organizer = new Organizer("organizerUsername", "organizerUID");
    }

    @Test
    public void testIsOrganizer() {
        // Verify that isOrganizer() method returns true for an Organizer
        assertTrue("Organizer should return true for isOrganizer", organizer.isOrganizer());
    }

    @Test
    public void testIsAttendee() {
        // Verify that isAttendee() method returns false for an Organizer
        assertFalse("Organizer should return false for isAttendee", organizer.isAttendee());
    }

    // You can also test the inherited functionality if necessary
    @Test
    public void testGetUsername() {
        assertEquals("The username should match the one passed in constructor", "organizerUsername", organizer.getUsername());
    }

    @Test
    public void testGetUID() {
        assertEquals("The UID should match the one passed in constructor", "organizerUID", organizer.getUID());
    }
}
