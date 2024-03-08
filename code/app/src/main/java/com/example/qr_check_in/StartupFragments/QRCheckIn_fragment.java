package com.example.qr_check_in.StartupFragments;



import android.os.Bundle;
import android.provider.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;
import androidx.activity.result.ActivityResultLauncher;

import androidx.fragment.app.Fragment;
import com.example.qr_check_in.data.AppDatabase;


import com.example.qr_check_in.R;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



public class QRCheckIn_fragment extends Fragment {

    private Button btnScan;
    private AppDatabase appDatabase; // Use AppDatabase for database interactions

    private String deviceId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_check_in, container, false);

        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        btnScan = view.findViewById(R.id.scanButton);
        appDatabase = new AppDatabase();
        btnScan.setOnClickListener(v -> {
            scanCode();


        });

        return view;
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {

        if (result != null && result.getContents() != null) {
            String uniqueId = result.getContents();

            appDatabase.saveAttendee(deviceId, getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                @Override
                public void onCallback(String documentId) {
                    Bundle bundle = new Bundle();
                    bundle.putString("device_id", deviceId);
                    bundle.putString("event_id", uniqueId);
                    Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment, bundle);
                }
            });
        }
    });
}

