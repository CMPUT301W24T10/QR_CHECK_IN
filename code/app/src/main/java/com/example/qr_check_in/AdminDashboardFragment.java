package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDashboardFragment newInstance(String param1, String param2) {
        AdminDashboardFragment fragment = new AdminDashboardFragment();
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
        View view= inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        view.findViewById(R.id.btnRemoveEvents).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.removeEventFragment);
        });
        view.findViewById(R.id.btnRemoveProfiles).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.removeProfileFragment);
        });
        view.findViewById(R.id.btnRemoveImages).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.removeImageFragment);
        });
        view.findViewById(R.id.btnBrowseEvents).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.eventListFragment);
        });
        view.findViewById(R.id.btnBrowseProfiles).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.profileListFragment);
        });
        view.findViewById(R.id.btnBrowseImages).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.imageListFragment);
        });
        return view;
    }
}