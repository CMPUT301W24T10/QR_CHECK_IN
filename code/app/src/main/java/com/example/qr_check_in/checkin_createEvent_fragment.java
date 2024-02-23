package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        view.findViewById(R.id.organizeEventButton).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_checkin_createEvent_fragment_to_input_info_fragment2);
        });
        return view;
    }
}