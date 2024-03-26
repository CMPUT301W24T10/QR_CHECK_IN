package com.example.qr_check_in.StartupFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AppDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Map;


public class QRCheckIn_fragment extends Fragment {

    private Button btnScan;
    private AppDatabase appDatabase; // Use AppDatabase for database interactions

    private String deviceId, eventTitle, eventDescription;

    private FirebaseFirestore db;

    boolean found;


    Button checkIn, back;

    Context thisContext;

    TextView title, description;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_check_in, container, false);
        thisContext = container.getContext();
        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        btnScan = view.findViewById(R.id.scanButton);

        appDatabase = new AppDatabase();
        db = FirebaseFirestore.getInstance();
        btnScan.setOnClickListener(v -> {
            scanCode();
        });

        return view;
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }


    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {

        if (result != null && result.getContents() != null) {
            String attendeeName = "guest";
            String uniqueId = result.getContents();

            CollectionReference collectionReference = db.collection("events");
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if (documentSnapshot.getId().equals(uniqueId)) {
                                if (documentSnapshot.contains("attendees")) {
                                    Map<String, String> existingAttendees = (Map<String, String>) documentSnapshot.get("attendees");
                                    if (existingAttendees.containsKey(deviceId)) {
                                        Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                                    }else{
                                        nameStuff(uniqueId);
                                    }
                                } else {

                                    eventTitle = documentSnapshot.getString("eventName");
                                    eventDescription = documentSnapshot.getString("eventDescription");
                                    found = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                                    builder.setTitle(eventTitle);
                                    builder.setMessage(eventDescription);

                                    builder.setPositiveButton("Check In", (DialogInterface.OnClickListener) (dialog, which) -> {

                                        nameStuff(uniqueId);
                                    });

                                    builder.setNegativeButton("Back", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        // If user click no then dialog box is canceled.
                                        dialog.cancel();
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }


                            }

                        }
                    }

                }
            });


        }

    });


    void showCustomDialog(String eventId, nameDialogCallback nameDialogCallback) {


        final Dialog dialog = new Dialog(thisContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_name_dialog_box);

        final EditText nameEt = dialog.findViewById(R.id.dialogName);
        final EditText emailAddressEt = dialog.findViewById(R.id.dialogEmailAddress);
        final EditText phoneNumberEt = dialog.findViewById(R.id.dialogPhoneNumber);
        final EditText addressEt = dialog.findViewById(R.id.dialogAddress);
        Button cancel = dialog.findViewById(R.id.cancelButton);
        Button next = dialog.findViewById(R.id.nextButton);
        next.setOnClickListener((v -> {
            final String name = nameEt.getText().toString();
            nameDialogCallback.nameExist(name);
            String emailAddress = emailAddressEt.getText().toString();
            if (emailAddress.equals("")) {
                emailAddress = "Blank";
            }
            String phoneNumber = phoneNumberEt.getText().toString();
            if (phoneNumber.equals("")) {
                phoneNumber = "Blank";
            }
            String address = addressEt.getText().toString();
            if (address.equals("")) {
                address = "Blank";
            }


            appDatabase.saveUser(deviceId, name, phoneNumber, emailAddress, address, eventId, thisContext, new AppDatabase.FirestoreCallback() {
                @Override
               public void onCallback(String documentId) {

                }
            });
            dialog.dismiss();


        }));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    void nameCheck(String unique, nameCallback nameCallback) {
        CollectionReference userReference = db.collection("users");

        userReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    if (documentSnapshot.getId().equals(unique)) {


                        final String name = documentSnapshot.get("Name").toString();
                        nameCallback.isNameExist(name);
                        return;

                    }

                }
                nameCallback.isNameExist("");
            } else{
                nameCallback.isNameExist("");
            }

        });


    }

    interface nameCallback {
        void isNameExist(String name);

    }

    interface nameDialogCallback{
        void nameExist(String name);
    }

    void currentEventIdUpdater(String deviceId, String eventId){
        DocumentReference docReference = db.collection("users").document(deviceId);

        docReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot docSnapshot = task.getResult();
                if(docSnapshot.exists()){
                    Map<String, Object> map = docSnapshot.getData();
                    map.put("currentEventID", eventId);
                    docReference.set(map);

                }
            }

        });
    }

    void nameStuff(String uniqueId){
        nameCheck(deviceId, new nameCallback() {
            @Override
            public void isNameExist(String name) {
                if(name != ""){
                    appDatabase.saveAttendee(deviceId, name, getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                        @Override
                        public void onCallback(String documentId) {
                            // Your callback logic, if needed
                        }
                    });
                    currentEventIdUpdater(deviceId, uniqueId);
                    Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                }else{
                    showCustomDialog(uniqueId, new nameDialogCallback() {
                        @Override
                        public void nameExist(String name){
                            if(name != ""){
                                appDatabase.saveAttendee(deviceId, name, getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                                    @Override
                                    public void onCallback(String documentId) {
                                        // Your callback logic, if needed
                                    }
                                });
                                Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                            }else{
                                appDatabase.saveAttendee(deviceId, "Guest", getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                                    @Override
                                    public void onCallback(String documentId) {
                                        // Your callback logic, if needed
                                    }
                                });
                                Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                            }
                        }
                    });
                }
            }
        });
    }
}



