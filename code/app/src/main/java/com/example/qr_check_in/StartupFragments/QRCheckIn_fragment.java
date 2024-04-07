package com.example.qr_check_in.StartupFragments;




import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qr_check_in.ModelClasses.AttendeeCount;
import com.example.qr_check_in.R;
import com.example.qr_check_in.SharedPreference;
import com.example.qr_check_in.data.AppDatabase;
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

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


public class QRCheckIn_fragment extends Fragment {

    private Button btnScan;
    private AppDatabase appDatabase; // Use AppDatabase for database interactions

    private String deviceId, eventTitle, eventDescription;

    private FirebaseFirestore db;

    boolean found;
    Integer Count = 0;


    Context thisContext;

    private SharedPreference sharedPreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_check_in, container, false);
        thisContext = container.getContext();
        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        btnScan = view.findViewById(R.id.scanButton);

        appDatabase = new AppDatabase();
        db = FirebaseFirestore.getInstance();

        sharedPreference = new SharedPreference(requireContext());
        sharedPreference.saveDeviceId(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnScan.setOnClickListener(v -> {
            scanCode();
        });
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

            String uniqueId = result.getContents();
            Log.d("uniqueId", ": " + uniqueId);

            CollectionReference collectionReference = db.collection("events");
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            Integer count = 0;
                            count = sharedPreference.getCountAttendeeLogin();

                            Log.d("count", "onComplete: "+count);

                            //this will check if the qr code that is being scanned is actually a qr code for a valid event
                            if (documentSnapshot.getId().equals(uniqueId)) {

                                //checking if attendess field exists in event
                                if (documentSnapshot.contains("attendees")) {
                                    Map<String, String> existingAttendees = (Map<String, String>) documentSnapshot.get("attendees");

                                    //checking if device is already registered into the event
                                    if (existingAttendees.containsKey(deviceId)) {
                                        count++;
                                        sharedPreference.saveCountAttendeeLogin(count);
                                        if (sharedPreference.list.contains(deviceId))
                                        {
                                            for (int i=0;i<sharedPreference.list.size();i++)
                                            {
                                                if (sharedPreference.list.get(i).getdeviceId()==deviceId)
                                                {
                                                    sharedPreference.list.get(i).setNumberOfTimesLogin(count);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            sharedPreference.list.add(new AttendeeCount(deviceId,count));


                                        }
                                        sharedPreference.saveList(sharedPreference.list);
                                        subscribeToNewTopic(uniqueId);
                                        nameStuff(uniqueId);
                                        //Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                                    } else {

                                        //if device is not registered will make the alert dialog

                                        eventTitle = documentSnapshot.getString("eventName");
                                        eventDescription = documentSnapshot.getString("eventDescription");
                                        found = true;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                                        builder.setTitle(eventTitle);
                                        builder.setMessage(eventDescription);
                                        count++;
                                        sharedPreference.saveCountAttendeeLogin(count);
                                        if (sharedPreference.list.contains(deviceId))
                                        {
                                            for (int i=0;i<sharedPreference.list.size();i++)
                                            {
                                                if (sharedPreference.list.get(i).getdeviceId()==deviceId)
                                                {
                                                    sharedPreference.list.get(i).setNumberOfTimesLogin(count);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            sharedPreference.list.add(new AttendeeCount(deviceId,count));


                                        }
                                        sharedPreference.saveList(sharedPreference.list);


                                        builder.setPositiveButton("Check In", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            //this method is called to determine whether we need to ask for name or not

                                            sharedPreference.saveList(sharedPreference.list);

                                            subscribeToNewTopic(uniqueId);

                                        });

                                        builder.setNegativeButton("Back", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            // If user click no then dialog box is canceled.
                                            dialog.cancel();
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();



                                        nameStuff(uniqueId);

                                    }
                                    //if attendees field doesnt exist will make the alert dialog box
                                } else {

                                    eventTitle = documentSnapshot.getString("eventName");

                                    eventDescription = documentSnapshot.getString("eventDescription");
                                    found = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                                    builder.setTitle(eventTitle);
                                    builder.setMessage(eventDescription);
                                    count++;
                                    sharedPreference.saveCountAttendeeLogin(count);
                                    if (sharedPreference.list.contains(deviceId))
                                    {
                                        for (int i=0;i<sharedPreference.list.size();i++)
                                        {
                                            if (sharedPreference.list.get(i).getdeviceId()==deviceId)
                                            {
                                                sharedPreference.list.get(i).setNumberOfTimesLogin(count);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        sharedPreference.list.add(new AttendeeCount(deviceId,count));


                                    }
                                    sharedPreference.saveList(sharedPreference.list);

                                    builder.setPositiveButton("Check In", (DialogInterface.OnClickListener) (dialog, which) -> {

                                        nameStuff(uniqueId);
                                        subscribeToNewTopic(uniqueId);

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


    void showCustomDialog(String eventId, nameDialogCallback nameDialogCallback) {

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

    void currentEventIdUpdater(String deviceId, String eventId) {
        DocumentReference docReference = db.collection("users").document(deviceId);

        docReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot docSnapshot = task.getResult();
                if (docSnapshot.exists()) {
                    Map<String, Object> map = docSnapshot.getData();
                    map.put("currentEventID", eventId);

                    docReference.set(map);

                }
            }

        });
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
                    currentEventIdUpdater(deviceId, uniqueId);
                    Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                } else {
                    showCustomDialog(uniqueId, new nameDialogCallback() {
                        @Override
                        public void nameExist(String name) {
                            if (name != "") {
                                appDatabase.saveAttendee(deviceId, name, getContext(), uniqueId, new AppDatabase.FirestoreCallback() {
                                    @Override
                                    public void onCallback(String documentId) {
                                        // Your callback logic, if needed
                                    }
                                });
                                Navigation.findNavController(requireView()).navigate(R.id.action_QRCheckIn_fragment_to_attendeeSelection_fragment);
                            } else {
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



