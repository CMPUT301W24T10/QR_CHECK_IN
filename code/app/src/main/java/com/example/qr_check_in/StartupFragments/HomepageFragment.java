package com.example.qr_check_in.StartupFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;

import java.util.ArrayList;
import java.util.List;

import com.example.qr_check_in.EventDetailsFragment;

public class HomepageFragment extends Fragment {

    private ListView eventListView;
    private Button profileButton;
    private Switch notificationSwitch;
    private CheckBox locationCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment, container, false);

        // Initialize UI elements
        eventListView = view.findViewById(R.id.eventList);
        profileButton = view.findViewById(R.id.editprofileButton);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        locationCheckBox = view.findViewById(R.id.locationCheckBox);

        // Populate event list
        List<String> events = new ArrayList<>();
        events.add("Event 1: Description 1");
        events.add("Event 2: Description 2");
        events.add("Event 3: Description 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, events);
        eventListView.setAdapter(adapter);

        // Handle item click on event list
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Redirect to EventDetailsFragment
                EventDetailsFragment fragment = new EventDetailsFragment();
                // Pass any necessary data to the fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(view.getId(), fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Set onClickListener for Profile Button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle profile button click
                // Redirect to EditProfileFragment or any relevant profile page
            }
        });

        // Enable/Disable notifications
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle notification switch change
                // Implement enabling/disabling notifications
            }
        });

        // Enable/Disable geolocation tracking
        locationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle location checkbox change
                // Implement enabling/disabling geolocation tracking
            }
        });

        return view;
    }
}
