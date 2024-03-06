package landingPage;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qr_check_in.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class input_info_fragment extends Fragment {
    private EditText editTextOrganizerName, editTextEventName, editTextEventDescription;
    private RadioGroup radioGroupQRCode;
    private FirebaseFirestore db;

    public input_info_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_info_fragment, container, false);

        editTextOrganizerName = view.findViewById(R.id.EnterOrganizerName);
        editTextEventName = view.findViewById(R.id.EnterEventName);
        editTextEventDescription = view.findViewById(R.id.EnterEventDescription);
        radioGroupQRCode = view.findViewById(R.id.read_status);

        /* Navigation to QR code display fragment and creating event on the database(pushing event details on database)
        *  on pressing confirm button
        */
        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            saveEventToFirestore(view);
        });

        // back Navigation to home fragment on pressing organize event button
        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_checkin_createEvent_fragment2));

        return view;
    }

    private void saveEventToFirestore(View view) {
        String organizerName = editTextOrganizerName.getText().toString().trim();
        String eventName = editTextEventName.getText().toString().trim();
        String eventDescription = editTextEventDescription.getText().toString().trim();
        boolean isNewQRCode = radioGroupQRCode.getCheckedRadioButtonId() == R.id.readRadioButton;

        if (!organizerName.isEmpty() && !eventName.isEmpty() && !eventDescription.isEmpty()) {
            Map<String, Object> event = new HashMap<>();
            event.put("organizerName", organizerName);
            event.put("eventName", eventName);
            event.put("eventDescription", eventDescription);
            event.put("isNewQRCode", isNewQRCode);

            db.collection("events").add(event)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Event added successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_input_info_fragment_to_displayQrCodeFragment);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding event", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }

    }
}

