package com.example.qr_check_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link input_info_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class input_info_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public input_info_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment input_info_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static input_info_fragment newInstance(String param1, String param2) {
        input_info_fragment fragment = new input_info_fragment();
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
        View view=  inflater.inflate(R.layout.fragment_input_info_fragment, container, false);

        view.findViewById(R.id.confirm_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_displayQrCodeFragment);
        });

        view.findViewById(R.id.cancel_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_checkin_createEvent_fragment2);
        });

        return view;
    }
}