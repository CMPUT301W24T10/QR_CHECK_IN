package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.qr_check_in.ModelClasses.FirestoreEvent;

public class FirestoreEventTest {

    @Test
    public void testEventConstructorAndGetterMethods() {
        // Given
        String expectedEventId = "event123";
        String expectedTitle = "Tech Talk";
        String expectedDescription = "An informative session on the latest in technology.";
        String expectedLocation = "Conference Room 1";

        // When
        FirestoreEvent event = new FirestoreEvent(expectedEventId, expectedTitle, expectedDescription, expectedLocation);

        // Then
        assertEquals(expectedEventId, event.getEventId());
        assertEquals(expectedTitle, event.getTitle());
        assertEquals(expectedDescription, event.getDescription());
        assertEquals(expectedLocation, event.getLocation());
    }

    @Test
    public void testNoArgConstructor() {
        // When
        FirestoreEvent event = new FirestoreEvent();

        // Then
        // Asserting that all the fields are null or default values since no arguments were passed
        assertEquals(null, event.getEventId());
        assertEquals(null, event.getTitle());
        assertEquals(null, event.getDescription());
        assertEquals(null, event.getLocation());
    }
}