package com.example.qr_check_in.StartupFragments;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qr_check_in.R;

import java.util.ArrayList;
import java.util.List;


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
                // Redirect to ProfileActivity
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

// Enable/Disable notifications
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Implement enabling/disabling notifications
                if (isChecked) {
                    // Enable notifications
                    // Implement enabling notifications
                    Toast.makeText(getContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable notifications
                    // Implement disabling notifications
                    Toast.makeText(getContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

// Enable/Disable geolocation tracking
        locationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Implement enabling/disabling geolocation tracking
                if (isChecked) {
                    // Enable geolocation tracking
                    // Implement enabling geolocation tracking
                    Toast.makeText(getContext(), "Geolocation Tracking Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable geolocation tracking
                    // Implement disabling geolocation tracking
                    Toast.makeText(getContext(), "Geolocation Tracking Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ProfilePageFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class ProfilePageFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public ProfilePageFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static ProfilePageFragment newInstance(String param1, String param2) {
            ProfilePageFragment fragment = new ProfilePageFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_profile_page, container, false);
        }
    }
}
