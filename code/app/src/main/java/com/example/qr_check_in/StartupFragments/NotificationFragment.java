package com.example.qr_check_in.StartupFragments;

import static com.example.qr_check_in.constants.SELECTEDEVENTIDREQUIRED;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qr_check_in.Notification.RetrofitInstance;
import com.example.qr_check_in.data.AppDatabase;
import com.example.qr_check_in.data.Notification;
import com.example.qr_check_in.data.NotificationData;
import com.example.qr_check_in.data.PushNotification;
import com.example.qr_check_in.databinding.FragmentNotificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.N;

import java.lang.ref.Reference;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * notification fragment created to send notification
 */
public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding mBinding;
    private FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentNotificationBinding.inflate(inflater, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        mBinding.sendButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = mBinding.NotificationTitle.getText().toString();
                String notification = mBinding.NotificationText.getText().toString();
                if (title.isEmpty()) {
                    mBinding.NotificationTitle.setError("Notification title should not be empty");
                } else if (notification.isEmpty()) {
                    mBinding.NotificationText.setError("Notification should not be empty");

                } else {
                    sendNewMessage(title, notification);
                }

            }
        }));

        mBinding.backButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (requireActivity().getOnBackPressedDispatcher().hasEnabledCallbacks()) {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }


            }
        }));

    }

    private void sendNewMessage(String title, String notification) {
        String eventName = "/topics/" + SELECTEDEVENTIDREQUIRED;
        //TODO explain we don't actually send a notification, we send a message, that is displayed as a notification
        PushNotification push = new PushNotification(
                new NotificationData(title, notification),
                eventName
        );
        submitData(title, notification, SELECTEDEVENTIDREQUIRED);

        retrofit2.Call<ResponseBody> responseBodyCall = RetrofitInstance.getApi().postNotification(push);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Notification sent successful to all the attendees registered to this event", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error sending the notification", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void submitData(String title, String notification, String selectedeventidrequired) {
        // Notification notificationData = new Notification(title,notification);


    }
}