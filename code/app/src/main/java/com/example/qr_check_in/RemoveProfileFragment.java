package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.data.AdminData;
import com.example.qr_check_in.data.EventNameIdPair;
import com.example.qr_check_in.data.ProfileIdPair;

import java.util.ArrayList;
import java.util.List;

public class RemoveProfileFragment extends Fragment {
    private AdminData adminData;
    private ListView profileListView;
    private ArrayList<String> profiles;
    private ArrayAdapter<String> profileAdapter;
    private List<ProfileIdPair> profileIdPairList;
    private int selectedPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_remove_profile, container, false);
        adminData = new AdminData();
        profiles = new ArrayList<>();
        profileIdPairList = new ArrayList<>();
        selectedPosition = -1;
        profileListView = root.findViewById(R.id.list_of_profiles);
        profileAdapter = new ArrayAdapter<>(getContext(), R.layout.attendee_list_element, profiles);
        profileListView.setAdapter(profileAdapter);

        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Save the clicked position in the member variable
                selectedPosition = position;

            }
        });

        root.findViewById(R.id.buttonConfirmSelectedDeleteProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition == -1) {
                    // toast a message to select a profile
                    Toast.makeText(getContext(), "Please select a profile to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get the selected profile
                ProfileIdPair selectedProfile = profileIdPairList.get(selectedPosition);
                // Delete the profile from the database
                adminData.deleteProfile(selectedProfile.getDocumentId(), new AdminData.ProfileDeletionListener() {
                    @Override
                    public void onProfileDeletionSuccess(String documentId) {
                        Log.e("EventDeletion", "Event with document ID " + documentId + " deleted successfully");
                        // Remove the event from the list
                        profiles.remove(selectedPosition);
                        selectedPosition = -1;
                        profileAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        adminData.fetchProfileNames(new AdminData.ProfileFetchListener() {
            @Override
            public void onProfileListFetched(List<ProfileIdPair> profileList) {
                profileIdPairList = profileList;
                Log.e("EventList",profileList.toString());
                for (ProfileIdPair profile : profileList) {
                    profiles.add(profile.getEventName());
                }
                profileAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}