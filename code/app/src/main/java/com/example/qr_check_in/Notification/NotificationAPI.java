package com.example.qr_check_in.Notification;

import com.example.qr_check_in.data.PushNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Notification api to push notification
 */

public interface NotificationAPI {
    String CONTENT_TYPE = "application/json";
    String API_KEY = "AAAAkjXSgiI:APA91bHDulGmrg9T5uLuWTufU3cvH6az-pKWpmGJAy7mcYQgkhoXGeEVCd4ZeFAkQXnIiQ2ArhsiVSX2DaHMWXDjupPUSY7sK7g2AVW9OaE843LDIrzvQR-b6HUnnxGPoRPxsNdUENPK";

    @Headers({"Authorization: key=" + API_KEY, "Content-Type: " + CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> postNotification(
            @Body PushNotification notification
    );
}
