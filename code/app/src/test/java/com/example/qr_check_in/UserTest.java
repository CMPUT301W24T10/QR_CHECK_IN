package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.ModelClasses.User;

public class UserTest {

    private User testUser;

    @Before
    public void setUp() throws Exception {
        // Since User is abstract, we instantiate an anonymous subclass for testing.
        testUser = new User("testUsername", "testUID", "testEmail@example.com", "1234567890") {
            @Override
            public boolean isOrganizer() {
                return false; // Example implementation
            }

            @Override
            public boolean isAttendee() {
                return true; // Example implementation
            }
        };
    }

    @Test
    public void getUsername_ShouldReturnCorrectUsername() {
        assertEquals("testUsername", testUser.getUsername());
    }

    @Test
    public void getUID_ShouldReturnCorrectUID() {
        assertEquals("testUID", testUser.getUID());
    }

    @Test
    public void getEmail_ShouldReturnCorrectEmail() {
        assertEquals("testEmail@example.com", testUser.getEmail());
    }

    @Test
    public void getPhoneNumber_ShouldReturnCorrectPhoneNumber() {
        assertEquals("1234567890", testUser.getPhoneNumber());
    }

    @Test
    public void isOrganizer_ShouldReturnFalse() {
        assertFalse(testUser.isOrganizer());
    }

    @Test
    public void isAttendee_ShouldReturnTrue() {
        assertTrue(testUser.isAttendee());
    }
}
