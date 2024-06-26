package com.example.qr_check_in.ui.shareQRcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.PromotionalQRCodeGenerator;
import com.example.qr_check_in.data.QRCodeGenerator;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeesViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareQRcode extends Fragment {
    private String eventId;
    private String organizerId;
    private Bitmap qrCode;
    private Bitmap promotionalQrCode; // Variable to hold the promotional QR code
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
        ListOfAttendeesViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(ListOfAttendeesViewModel.class);
        eventId = sharedViewModel.getEventId();
        organizerId = sharedViewModel.getUserId();
        // Generate QR code image using the eventId
        qrCode = QRCodeGenerator.generateQRCodeImage(eventId, 512, 512);
        ImageView qrCodeImage = view.findViewById(R.id.ShowQRCode);

        if (qrCode != null) { // Display the QR code image
            qrCodeImage.setImageBitmap(qrCode);
            view.findViewById(R.id.shareViaEmailButton).setOnClickListener(v -> shareQrCodeViaEmail());
        } else { //QR code generation failed
            Toast.makeText(getContext(), "Failed to generate QR code. Please try again.", Toast.LENGTH_LONG).show();
        }

        promotionalQrCode = PromotionalQRCodeGenerator.generatePromotionalQRCodeImage(eventId, 512, 512);
        ImageView promoQrCodeImage = view.findViewById(R.id.ShowPromotionalQRCode);

        if (promotionalQrCode != null) {
            promoQrCodeImage.setImageBitmap(promotionalQrCode);
            view.findViewById(R.id.sharePromotionalViaEmailButton).setOnClickListener(v -> sharePromotionalQrCodeViaEmail());
        } else {
            Toast.makeText(getContext(), "Failed to generate promotional QR code. Please try again.", Toast.LENGTH_LONG).show();
        }

        view.findViewById(R.id.openEventActivityButton).setVisibility(View.INVISIBLE);

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

    private void sharePromotionalQrCodeViaEmail() {
        // Code to save and share the promotional QR code
        File promoQrCodeFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Promotional_QR_Code.png");

        try (FileOutputStream out = new FileOutputStream(promoQrCodeFile)) {
            promotionalQrCode.compress(Bitmap.CompressFormat.PNG, 100, out);
            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.qr_check_in.provider", promoQrCodeFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Promotional QR Code via"));
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error sharing Promotional QR Code", Toast.LENGTH_SHORT).show();
        }
    }
}
