package com.example.qr_check_in.StartupFragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AppDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    private static final String ARG_DEVICE_ID = "device_id";
    private String deviceID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String fetchedName = "";
    private String fetchedAddress = "";
    private String fetchedPhoneNumber = "";

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided device ID.
     *
     * @param deviceId The device ID.
     * @return A new instance of fragment ProfilePageFragment.
     */
    public static ProfilePageFragment newInstance(String deviceId) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            deviceID = getArguments().getString(ARG_DEVICE_ID, "");
        }
        setActionBarTitle("Edit Profile");
    }

    private void fetchUserDetails(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        fetchedName = documentSnapshot.getString("name");
                        fetchedAddress = documentSnapshot.getString("address");
                        fetchedPhoneNumber = documentSnapshot.getString("phoneNumber");

                        updateUI(fetchedName, fetchedAddress, fetchedPhoneNumber);
                    }
                });
    }

    private void updateUI(String name, String address, String phoneNumber) {
        // Update your UI elements here
        // For example:
        View view = getView();
        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);

        if (name != null && !name.isEmpty()) {
            nameEditView.setText(name);
        }

        if (address != null && !address.isEmpty()) {
            addressEditView.setText(address);
        }

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            phoneNumberEditView.setText(phoneNumber);
        }
    }

    private void setActionBarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserDetails(deviceID);

        EditText nameEditView = view.findViewById(R.id.editTextName);
        EditText addressEditView = view.findViewById(R.id.editTextAddress);
        EditText phoneNumberEditView = view.findViewById(R.id.editTextPhone);
        Button saveButton = view.findViewById(R.id.buttonSave);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = getContext();
                if (context != null) {
                    saveButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.app_buttons)));
                    saveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        nameEditView.addTextChangedListener(textWatcher);
        addressEditView.addTextChangedListener(textWatcher);
        phoneNumberEditView.addTextChangedListener(textWatcher);
    }
}