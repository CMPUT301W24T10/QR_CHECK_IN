package com.example.qr_check_in.StartupFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qr_check_in.EventActivity;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.AppDatabase;
import com.example.qr_check_in.geolocation.UserLocationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class QRCheckIn_fragment extends Fragment {

    private Button btnScan;
    private AppDatabase appDatabase; // Use AppDatabase for database interactions
    private UserLocationManager userLocationManager;

    private String deviceId, eventTitle, eventDescription;

    private FirebaseFirestore db;

    boolean found;


    Context thisContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_check_in, container, false);
        thisContext = container.getContext();
        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        btnScan = view.findViewById(R.id.scanButton);
        // Instantiate UserLocationManager with fragment's context
        userLocationManager = new UserLocationManager(getContext());


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

    private void navigateToEventActivity(String eventId,String userId) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("userId", userId);
        intent.putExtra("userType", "Attendee");
        startActivity(intent);
    }
    private void getNameFromFirestore(String deviceId, ExistingAttendeesCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("Name");
                        if (name != null && !name.isEmpty()) {
                            callback.onNameReceived(name);
                        } else {
                            callback.onNameReceived("Guest"); // Set default name if Name field is empty
                        }
                    } else {
                        callback.onNameReceived("Guest"); // Set default name if document doesn't exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    callback.onNameReceived("Guest"); // Set default name on failure
                });
    }

    // Define an interface for callback
    interface ExistingAttendeesCallback {
        void onNameReceived(String name);
    }



    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {

        if (result != null && result.getContents() != null) {

            String uniqueId = result.getContents();
            Log.d("uniqueId", ": " + uniqueId);

            CollectionReference collectionReference = db.collection("events");
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            //this will check if the qr code that is being scanned is actually a qr code for a valid event
                            if (documentSnapshot.getId().equals(uniqueId)) {

                                nameStuff(uniqueId);
                                //checking if attendess field exists in event
                                if (documentSnapshot.contains("attendees")) {
                                    Map<String, String> existingAttendees = (Map<String, String>) documentSnapshot.get("attendees");
                                    //checking if device is already registered into the event
                                    assert existingAttendees != null;

                                    getNameFromFirestore(deviceId, name -> {
                                        // Use the retrieved name to update existingAttendees map
                                        if(name == "Guest"){
                                            showCustomDialog(uniqueId, deviceId);
                                        }else {
                                            existingAttendees.put(deviceId, name);

                                            // Update Firestore document with the new attendees map
                                            documentSnapshot.getReference().update("attendees", existingAttendees)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("CheckIn", "Attendee updated successfully");
                                                        subscribeToNewTopic(uniqueId);
                                                        navigateToEventActivity(uniqueId, deviceId);
                                                    })
                                                    .addOnFailureListener(e -> Log.d("CheckIn", "Error updating attendee", e));
                                        }
                                    });
                                    subscribeToNewTopic(uniqueId);

                                } else {
                                    // If the "attendees" field doesn't exist, create it and add the current device as the first attendee
                                    Map<String, Object> newAttendee = new HashMap<>();
                                    getNameFromFirestore(deviceId, name -> {
                                        if(name == "Guest"){
                                            showCustomDialog(uniqueId, deviceId);
                                        }else {
                                            // Use the retrieved name to update existingAttendees map
                                            newAttendee.put(deviceId, name);

                                            // Update Firestore document with the new attendees map
                                            documentSnapshot.getReference().update("attendees", newAttendee)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("CheckIn", "Attendee updated successfully");
                                                        subscribeToNewTopic(uniqueId);
                                                        navigateToEventActivity(uniqueId, deviceId);
                                                    })
                                                    .addOnFailureListener(e -> Log.d("CheckIn", "Error updating attendee", e));
                                        }
                                    });

                                    subscribeToNewTopic(uniqueId);


                                }


                            }

                        }
                    }

                }
            });


        }

    });

    public void subscribeToNewTopic(String topicInput) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicInput)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("successfully subscribed to the topic");
                    } else {
                        System.out.println("failed to subscribe to the topic");
                    }
                })
                .addOnFailureListener(e -> System.out.println("failed to subscribe to the topic : " + e.getMessage()));
    }

    void updateAttendingEvents(String userId, String eventId) {
        DocumentReference eventRef = db.collection("events").document(eventId);
        DocumentReference userRef = db.collection("users").document(userId);

        eventRef.get().addOnSuccessListener(eventSnapshot -> {
            if (eventSnapshot.exists()) {
                // Assume "eventName" is a field in your event documents
                String eventName = eventSnapshot.getString("eventName");

                userRef.get().addOnSuccessListener(userSnapshot -> {
                    Map<String, Object> attendingEvents;
                    if (userSnapshot.contains("AttendingEvents")) {
                        // If AttendingEvents map exists, retrieve and update it
                        attendingEvents = (Map<String, Object>) userSnapshot.get("AttendingEvents");
                    } else {
                        // Otherwise, create a new map
                        attendingEvents = new HashMap<>();
                    }

                    // Update or add the event ID and name to the map
                    attendingEvents.put(eventId, eventName);

                    // Update the user's document
                    userRef.update("AttendingEvents", attendingEvents)
                            .addOnSuccessListener(aVoid -> Log.d("UpdateUser", "User AttendingEvents updated successfully"))
                            .addOnFailureListener(e -> Log.d("UpdateUser", "Error updating User AttendingEvents", e));
                });
            } else {
                Log.d("UpdateUser", "Event not found with ID: " + eventId);
            }
        }).addOnFailureListener(e -> Log.d("UpdateUser", "Error fetching event details", e));
    }


    void showCustomDialog(String eventId, String deviceId) {

        //method for asking the user for their information and using it to create a new user in firebase
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
            //ensuring that the user enters something in the name field
            if (name.isEmpty()) {
                nameEt.setError("Name is required");
                dialog.dismiss();
                Toast.makeText(thisContext, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

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

            appDatabase.saveAttendee(deviceId, name, getContext(), eventId, new AppDatabase.FirestoreCallback() {
                @Override
                public void onCallback(String documentId) {

                    // Your callback logic, if needed
                }
            });
            updateAttendingEvents(deviceId, eventId);

            subscribeToNewTopic(deviceId);
            dialog.dismiss();
            navigateToEventActivity(eventId, deviceId);


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
        //method to check if the user and their info already exists in firebase.
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
            } else {
                nameCallback.isNameExist("");
            }

        });



    }

    interface nameCallback {
        void isNameExist(String name);

    }

    interface nameDialogCallback {
        void nameExist(String name);
    }



    void nameStuff(String uniqueId) {
        //this method will call on nameCheck and if the user exists will not prompt user for info. if the user doesnt exist will prompt user for info.
        nameCheck(deviceId, new nameCallback() {
            @Override
            public void isNameExist(String name) {
                if (name != "") {
                    appDatabase.saveAttendee(deviceId, name, getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                        @Override
                        public void onCallback(String documentId) {

                            // Your callback logic, if needed
                        }
                    });
                } else {
                    showCustomDialog(uniqueId, deviceId);
                }
                userLocationManager.checkInUser(uniqueId,deviceId);

            }

        });

        updateAttendingEvents(deviceId, uniqueId);
    }
}



