package com.example.qr_check_in.ui.EditProfile;

import android.content.Context;
import android.provider.Settings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileImageGenerator {

    private Context context;

    public ProfileImageGenerator(Context context) {
        this.context = context;
    }

    public void checkAndUpdateProfileImage() {
        String documentId = getDeviceId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(documentId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String profileImageURL = document.getString("profileImageUrl");
                            if (profileImageURL == null || profileImageURL.trim().isEmpty()) {
                                // Profile image URL is blank or not present, generate from name
                                String name = document.getString("Name");
                                if (name != null && !name.trim().isEmpty()) {
                                    String generatedImageUrl = generateImageUrlFromName(name);
                                    // Update Firestore document with generated image URL
                                    document.getReference().update("profileImageUrl", generatedImageUrl);
                                    // Create and update defaultProfileImage field
                                    document.getReference().update("defaultProfileImage", true);
                                }
                            }
                        } else {
                            // Handle the case where the document does not exist
                            System.out.println("No such document with ID: " + documentId);
                        }
                    } else {
                        // Handle the failure
                        System.err.println("get failed with " + task.getException());
                    }
                });
    }


    private String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String generateImageUrlFromName(String name) {
        // Extract initials from name
        String initials = getInitials(name);

        // Generate the URL for the image using an external service
        String imageUrl = String.format("https://ui-avatars.com/api/?name=%s&size=256&background=random&color=fff", initials);

        return imageUrl;
    }

    private static String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        StringBuilder initials = new StringBuilder();
        for (String part : name.split("\\s+")) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }

        return initials.toString().toUpperCase();
    }

    private static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}