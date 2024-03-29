package com.example.qr_check_in;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_check_in.data.EventNameIdPair;

public class EventNameIdPairTest {
    private EventNameIdPair eventNameIdPair;
    private final String testDocumentId = "testDocumentId";
    private final String testEventName = "testEventName";

    @Before
    public void setUp() {
        eventNameIdPair = new EventNameIdPair(testDocumentId, testEventName);
    }

    @Test
    public void testGetDocumentId() {
        assertEquals("Document ID should match the constructor argument", testDocumentId, eventNameIdPair.getDocumentId());
    }

    @Test
    public void testGetEventName() {
        assertEquals("Event name should match the constructor argument", testEventName, eventNameIdPair.getEventName());
    }
}

