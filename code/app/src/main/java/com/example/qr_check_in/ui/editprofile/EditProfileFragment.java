package com.example.qr_check_in.ui.editprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.qr_check_in.R;

public class EditProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button buttonSave;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        buttonSave = root.findViewById(R.id.buttonSave);

        // Handle save button click
        buttonSave.setOnClickListener(v -> saveProfile());

        // Load existing user profile data
        loadUserProfile();

        return root;
    }

    private void loadUserProfile() {
        // Load profile data from your data source (e.g., Firebase)
        // For demonstration, I'm assuming some hardcoded data here
        String name = "John Doe";
        String phone = "1234567890";
        String email = "johndoe@example.com";

        // Set the loaded profile data to EditText fields
        editTextName.setText(name);
        editTextPhone.setText(phone);
        editTextEmail.setText(email);
    }

    private void saveProfile() {
        // Get the edited profile information
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Validate the edited information if needed

        // Display the edited profile information
        String message = "Name: " + name + "\nPhone: " + phone + "\nEmail: " + email;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
