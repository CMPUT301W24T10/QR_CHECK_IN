package com.example.qr_check_in.data;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
public class QRCodeGenerator {
    /**
     * Generates a QR code image from the given text.
     *
     * @param text   The string to be encoded into the QR code.
     * @param width  The desired width of the QR code image.
     * @param height The desired height of the QR code image.
     * @return A Bitmap image of the QR code, or null if an error occurs.
     */
    public static Bitmap generateQRCodeImage(String text, int width, int height) {
        try {
            // Create a QR Code writer to encode the text into a QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // Encode the text into a BitMatrix which represents the QR code
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            // Create a Bitmap of the defined width and height to hold the QR code
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // Iterate through the BitMatrix to draw the QR code onto the Bitmap
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Return the Bitmap containing the generated QR code
            return bitmap;
        } catch (WriterException e) {
            //exception if there was an error during QR code generation
            e.printStackTrace();
            return null;
        }
    }

}
