package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.User;

public class UserTest {
    private User user;

    // Dummy subclass for testing the abstract User class
    private static class DummyUser extends User {
        DummyUser(String username, String UID) {
            super(username, UID);
        }

        @Override
        public boolean isOrganizer() {
            return false; // Default implementation for testing purposes
        }

        @Override
        public boolean isAttendee() {
            return false; // Default implementation for testing purposes
        }
    }

    @Before
    public void setUp() {
        user = new DummyUser("testUser", "UID123");
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testGetUID() {
        assertEquals("UID123", user.getUID());
    }
}

