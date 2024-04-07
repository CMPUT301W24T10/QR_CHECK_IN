package com.example.qr_check_in.data;

import android.graphics.Bitmap;

public class PromotionalQRCodeGenerator extends QRCodeGenerator {

    private static final String PROMO_PREFIX = "PROMO_"; // A unique prefix to identify promotional QR codes

    /**
     * Generates a promotional QR code image containing the event ID and a prefix.
     *
     * @param eventId The event ID to be encoded into the QR code.
     * @param width   The desired width of the QR code image.
     * @param height  The desired height of the QR code image.
     * @return A Bitmap image of the promotional QR code, or null if an error occurs.
     */
    public static Bitmap generatePromotionalQRCodeImage(String eventId, int width, int height) {
        // Combine the promotional prefix with the event ID to create the text to encode
        String textToEncode = PROMO_PREFIX + eventId;

        // Use the parent class's method to generate the QR code image
        return generateQRCodeImage(textToEncode, width, height);
    }

}

