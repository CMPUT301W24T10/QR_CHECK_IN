package com.example.qr_check_in.data;

public class EventNameIdPair {
    private String documentId;
    private String eventName;

    public EventNameIdPair(String documentId, String eventName) {
        this.documentId = documentId;
        this.eventName = eventName;
    }
    public String getDocumentId() {
        return documentId;
    }
    public String getEventName() {
        return eventName;
    }
}
