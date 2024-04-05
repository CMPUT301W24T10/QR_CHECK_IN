package com.example.qr_check_in.Notification;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.qr_check_in.MainActivity;
import com.example.qr_check_in.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Firebase messaging class to receive notification
 */


public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "defaultChannelName";

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        System.out.println("This is the token: " + newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);



        /*----------------------- Show a notification based on the message -------------------- */
        if (tiramisuPermissionsCheck()) {
            // Create a notification
            Intent intent = new Intent(this, MainActivity.class);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = new Random().nextInt();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager);
            }
            // Clears other activities until our MainActivity opens up
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // So when we click the notification we are going to open the main activity
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            // Create the actual notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(message.getData().get("title"))
                    .setContentText(message.getData().get("message"))
                    .setColor(Color.GREEN)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            // Showing the notification
            notificationManager.notify(notificationID, builder.build());
        }
    }

    private boolean tiramisuPermissionsCheck() {
        // If we are above level 33, check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "QR Check-in";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Our notification channel");
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);

        notificationManager.createNotificationChannel(channel);
    }
}