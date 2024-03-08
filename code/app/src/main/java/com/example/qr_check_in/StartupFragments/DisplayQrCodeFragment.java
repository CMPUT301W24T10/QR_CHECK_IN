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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_qr_code, container, false);

        eventId = requireArguments().getString("eventId");
        organizerId = requireArguments().getString("organizerId");

        qrCode = QRCodeGenerator.generateQRCodeImage(eventId, 512, 512);
        ImageView qrCodeImage = view.findViewById(R.id.ShowQRCode);

        if (qrCode != null) {
            qrCodeImage.setImageBitmap(qrCode);
            view.findViewById(R.id.shareViaEmailButton).setOnClickListener(v -> shareQrCodeViaEmail());
        } else {
            Toast.makeText(getContext(), "Failed to generate QR code. Please try again.", Toast.LENGTH_LONG).show();
        }

        view.findViewById(R.id.openEventActivityButton).setOnClickListener(v -> {
            navigateToEventActivity();
            requireActivity().finish(); // remove the current activity from the back stack
        });

        return view;
    }

    private void shareQrCodeViaEmail() {
        File qrCodeFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "QR_Code.png");
        try (FileOutputStream out = new FileOutputStream(qrCodeFile)) {
            qrCode.compress(Bitmap.CompressFormat.PNG, 100, out);
            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.qr_check_in.provider", qrCodeFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share QR Code via"));
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error sharing QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToEventActivity() {
        Intent intent = new Intent(getActivity(), EventActivity.class);

        intent.putExtra("eventId", eventId);
        intent.putExtra("userId", organizerId);

        startActivity(intent);
    }
}
