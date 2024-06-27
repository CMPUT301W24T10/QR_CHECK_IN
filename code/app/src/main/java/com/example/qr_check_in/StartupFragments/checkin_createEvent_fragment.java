package com.example.qr_check_in.StartupFragments;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.MyEventActivity;
import com.example.qr_check_in.Notification.RetrofitInstance;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.NotificationData;
import com.example.qr_check_in.data.PushNotification;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class checkin_createEvent_fragment extends Fragment {

    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkin_create_event_fragment, container, false);

        // Navigation to create new event fragment on pressing organize event button
        view.findViewById(R.id.organizeEventButton).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.input_info_fragment);
        });



        view.findViewById(R.id.checkInButton).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.QRCheckIn_fragment);
        });

        view.findViewById(R.id.checkInButton).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.QRCheckIn_fragment);
        });
        view.findViewById(R.id.button_settings).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.adminLoginFragment);
        });

        view.findViewById(R.id.signUpButton).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.fragment_Sign_up);
        });

        view.findViewById(R.id.Myevents).setOnClickListener(v -> {
            navigateToEventActivity();
            requireActivity().finish();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRequiredPermissions();


    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

            });

    public void getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);

            } else {
                // repeat the permission or open app details
            }
        }


    }

    public void navigateToEventActivity() {
        Intent intent = new Intent(getActivity(), MyEventActivity.class);
        userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        intent.putExtra("userId",userId);
        startActivity(intent);
    }

}

