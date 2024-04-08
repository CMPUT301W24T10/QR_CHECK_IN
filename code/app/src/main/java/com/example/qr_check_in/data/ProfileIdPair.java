package com.example.qr_check_in.data;
/**
 * Class representing a pair of user profile document ID and username.
 */
public class ProfileIdPair {
    private String documentId; // Document ID of the user profile
    private String userName; // Username associated with the profile

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
