package com.example.qr_check_in.ui.listOfAttendee;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qr_check_in.AttendeesAdapter;
import com.example.qr_check_in.NotificationAdapter;
import com.example.qr_check_in.R;
import com.example.qr_check_in.SharedPreference;
import com.example.qr_check_in.data.AttendeeInfo;
import com.example.qr_check_in.databinding.FragmentListOfAttendeesBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ListOfAttendees extends Fragment {
    private AttendeeInfo attendeeInfo;
    private ListOfAttendeesViewModel mViewModel;
    private FragmentListOfAttendeesBinding binding;
    private ListView attendeeList;
    private ArrayList<String> attendees;
    private ArrayList<String> deviceId;
    private String eventId;
    private AttendeesAdapter adapter;
    private SharedPreference sharedPreference;

    public static ListOfAttendees newInstance() {
        return new ListOfAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       binding = FragmentListOfAttendeesBinding.inflate(inflater, container, false);
        attendeeInfo = new AttendeeInfo();
        attendees = new ArrayList<>();
        deviceId = new ArrayList<>();
        ListOfAttendeesViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(ListOfAttendeesViewModel.class);
        eventId = (sharedViewModel.getEventId());
        sharedPreference = new SharedPreference(requireContext());
        sharedPreference.loadList();
        Log.d("listData", "onCreateView: "+sharedPreference.list.size());

        // Create an ArrayAdapter from List of String

        // DataBind ListView with items from ArrayAdapter

        attendeeInfo.getAttendeesMap(eventId, new AttendeeInfo.getAttendeesMapCallback() {
            @Override
            public void onCallback(Map<String, String> data) {

                            // Initialize attendeesList as a new ArrayList

                if (data.values()!=null)
                {

                    attendees.addAll(data.values());
                    deviceId.addAll(data.keySet());
                    binding.listRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                    adapter = new AttendeesAdapter(attendees,deviceId, requireContext(),sharedPreference.getCountAttendeeLogin(),sharedPreference.getDeviceID(),sharedPreference.list);
                    binding.listRecycler.setAdapter(adapter);

                }


                            // Add all the values from attendeesMap directly to attendees




                            // Notify the adapter about the data change
                        }




        });



        return binding.getRoot();
    }


}