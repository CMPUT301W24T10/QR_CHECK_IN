package com.example.qr_check_in.StartupFragments;

import static java.sql.DriverManager.println;

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
import com.example.qr_check_in.data.NotificationData;
import com.example.qr_check_in.data.PushNotification;
import com.example.qr_check_in.databinding.FragmentNotificationBinding;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * notification fragment created to send notification
 */
public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding mBinding;


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

        mBinding.sendButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = mBinding.NotificationTitle.getText().toString();
                String notification = mBinding.NotificationText.getText().toString();
                String topic = mBinding.topicName.getText().toString();
                sendNewMessage(title, notification, topic);

            }
        }));

    }

    private void sendNewMessage(String title, String notification, String topic) {
        //TODO explain we don't actually send a notification, we send a message, that is displayed as a notification
        PushNotification push = new PushNotification(
                new NotificationData(title, notification),
                "/topics/event"
        );
        retrofit2.Call<ResponseBody> responseBodyCall = RetrofitInstance.getApi().postNotification(push);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                    if (response.isSuccessful()){
                        Toast.makeText(requireContext(),"Sending data was successful - notification recipient: ${notification.to}",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(requireContext(),"Error sending the data",Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}