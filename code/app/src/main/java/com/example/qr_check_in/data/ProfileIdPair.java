package com.example.qr_check_in.data;

public class ProfileIdPair {
    private String documentId;
    private String userName;

    public ProfileIdPair(String documentId, String userName) {
        this.documentId = documentId;
        this.userName = userName;
    }
    public String getDocumentId() {
        return documentId;
    }
    public String getEventName() {
        return userName;
    }

}
