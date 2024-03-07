package com.example.qr_check_in.StartupFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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

        // generating QR code from eventId and set image view to QR code
        qrId.setText(eventId);
        Bitmap qrCode = generateQRCode(eventId);
        ImageView qrCodeImage = view.findViewById(R.id.ShowQRCode);
        qrCodeImage.setImageBitmap(qrCode);

        // navigation to the event activity when user presses Ok button
        view.findViewById(R.id.openEventActivityButton).setOnClickListener(v -> {
            navigateToEventActivity();
            requireActivity().finish(); // remove the current activity from the back stack
        });

        return view;
    }

    public void navigateToEventActivity() {
        Intent intent = new Intent(getActivity(), EventActivity.class);

        intent.putExtra("eventId", eventId);
        intent.putExtra("organizerId", organizerId);

        startActivity(intent);
    }

    // This method generates a QR code from a given string
    public Bitmap generateQRCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}