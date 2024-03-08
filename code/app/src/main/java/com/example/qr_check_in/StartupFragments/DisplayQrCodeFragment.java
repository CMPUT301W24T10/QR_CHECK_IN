package com.example.qr_check_in.StartupFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.QRCodeGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DisplayQrCodeFragment extends Fragment {
    private String eventId;
    private String organizerId;
    private Bitmap qrCode;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    /**
     * Called when the fragment is being created.
     * This method is called after the fragment instance is created but before it is added to the activity.
     * It is typically used to initialize fragment-specific data or resources.
     *
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     *                            This argument may be null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * This method is responsible for inflating the fragment's layout, initializing views, and setting up event listeners.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     *                            This argument may be null.
     * @return The root View of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_qr_code, container, false);
// Retrieve eventId and organizerId from fragment arguments
        eventId = requireArguments().getString("eventId");
        organizerId = requireArguments().getString("organizerId");
// Generate QR code image using the eventId
        qrCode = QRCodeGenerator.generateQRCodeImage(eventId, 512, 512);
        ImageView qrCodeImage = view.findViewById(R.id.ShowQRCode);

        if (qrCode != null) { // Display the QR code image
            qrCodeImage.setImageBitmap(qrCode);
            view.findViewById(R.id.shareViaEmailButton).setOnClickListener(v -> shareQrCodeViaEmail());
        } else { //QR code generation failed
            Toast.makeText(getContext(), "Failed to generate QR code. Please try again.", Toast.LENGTH_LONG).show();
        }

        view.findViewById(R.id.openEventActivityButton).setOnClickListener(v -> {
            navigateToEventActivity();
            requireActivity().finish(); // remove the current activity from the back stack
        });

        return view;
    }
    /**
     * Shares the generated QR code image via email.
     * The QR code image is saved locally and then shared via an intent.
     * This method compresses the QR code bitmap, saves it to a file, creates a content URI for the file,
     * and launches an ACTION_SEND intent to share the image.
     */
    private void shareQrCodeViaEmail() {
        // Create a file to store the QR code image in the app's external storage directory
        File qrCodeFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "QR_Code.png");
        try (FileOutputStream out = new FileOutputStream(qrCodeFile)) {
            qrCode.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress the QR code bitmap and write it to the file
            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.qr_check_in.provider", qrCodeFile); // Get a content URI for the file using a FileProvider
            Intent shareIntent = new Intent(Intent.ACTION_SEND);  // Create an intent to share the QR code image via email
            shareIntent.setType("image/png");  // Set the MIME type of the shared content
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri); // Attach the content URI of the image to the intent
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  // Grant read permission to the receiving app
            startActivity(Intent.createChooser(shareIntent, "Share QR Code via")); // Start an activity to choose an client for sharing the QR code image.(Shows a lot of options but only gmail works for now)
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error sharing QR Code", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Navigates to the EventActivity to display details of the event associated with the QR code.
     * Passes the event ID and organizer ID as extras in the intent to the EventActivity.
     */
    public void navigateToEventActivity() {
        Intent intent = new Intent(getActivity(), EventActivity.class);

        intent.putExtra("eventId", eventId);
        intent.putExtra("userId", organizerId);

        startActivity(intent);
    }
}
