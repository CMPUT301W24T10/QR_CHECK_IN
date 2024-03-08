package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.Attendee;

/**
 * Test class for Attendee.
 */
public class AttendeeTest {

    private Attendee attendee;

    @Before
    public void setUp() {
        // Initialize an Attendee object before each test
        attendee = new Attendee("testUser", "UID123");
    }

    @Test
    public void testIsOrganizer() {
        // Verify that isOrganizer() method returns false for Attendee
        assertFalse(attendee.isOrganizer());
    }

    @Test
    public void testIsAttendee() {
        // Verify that isAttendee() method returns true for Attendee
        assertTrue(attendee.isAttendee());
    }

    // Additional tests can be added here to test the inherited functionality from User
    @Test
    public void testGetUsername() {
        assertEquals("testUser", attendee.getUsername());
    }

    @Test
    public void testGetUID() {
        assertEquals("UID123", attendee.getUID());
    }
}
