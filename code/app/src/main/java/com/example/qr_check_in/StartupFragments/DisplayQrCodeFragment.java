package com.example.qr_check_in.StartupFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.QRCodeGenerator; // Adjust this import to match your package structure

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DisplayQrCodeFragment extends Fragment {
    private String eventId;
    private String organizerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_qr_code, container, false);

        eventId = requireArguments().getString("eventId");
        organizerId = requireArguments().getString("organizerId");

        TextView qrId = view.findViewById(R.id.QRcodeString);
        qrId.setText(eventId);

        // Use QRCodeGenerator to generate QR code
        Bitmap qrCode = QRCodeGenerator.generateQRCodeImage(eventId, 512, 512);
        ImageView qrCodeImage = view.findViewById(R.id.ShowQRCode);
        if (qrCode != null) {
            qrCodeImage.setImageBitmap(qrCode);
        } else {
            Toast.makeText(getContext(), "Failed to generate QR code. Please try again.", Toast.LENGTH_LONG).show();
        }

        // Navigation to the event activity when user presses the Ok button
        view.findViewById(R.id.openEventActivityButton).setOnClickListener(v -> {
            navigateToEventActivity();
            requireActivity().finish(); // Remove the current activity from the back stack
        });

        return view;
    }

    public void navigateToEventActivity() {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("organizerId", organizerId);
        startActivity(intent);
    }
}
