package com.example.qr_check_in.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.R;
import com.example.qr_check_in.adapters.UserListAdapter;
import com.example.qr_check_in.data.AdminData;

import java.util.ArrayList;

public class RemoveProfileFragment extends Fragment {
    private AdminData adminData; // Reference to the AdminData class for interacting with user profiles
    private ListView profileListView; // ListView to display user profiles
    private ArrayList<User> profiles; // List of user profiles
    private ArrayAdapter<User> profileAdapter; // Adapter for the profileListView
    private int selectedPosition; // Position of the selected profile in the list


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_remove_profile, container, false);
        adminData = new AdminData(); // Initialize AdminData instance
        profiles = new ArrayList<User>();// Initialize list of profiles and set selected position to -1

        selectedPosition = -1;
        profileListView = root.findViewById(R.id.list_of_profiles);
        profileAdapter = new UserListAdapter(getContext(), profiles);
        profileListView.setAdapter(profileAdapter);

        ImageButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the fragment is attached to an activity
                if (isAdded() && getActivity() != null) {
                    // Use FragmentManager to pop back stack
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });


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
                User selectedProfile = profiles.get(selectedPosition);
                // Delete the profile from the database
                adminData.deleteProfile(selectedProfile.getUID(), new AdminData.ProfileDeletionListener() {
                    @Override
                    public void onProfileDeletionSuccess(String documentId) {
                        // Remove the event from the list
                        profiles.remove(selectedPosition);
                        selectedPosition = -1;
                        profileAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        adminData.fetchProfileNames(new AdminData.ProfileFetchListener() { // Fetch user profiles from the database and update the list
            @Override
            public void onProfileListFetched(ArrayList<User> profileList) {
                profiles.clear();
                profiles.addAll(profileList);
                profileAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}