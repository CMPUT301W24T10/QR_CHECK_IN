package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qr_check_in.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class checkin_createEvent_fragment extends Fragment {

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

        return view;
    }
}

