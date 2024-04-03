package com.example.qr_check_in.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Shubham retorifit instance to call API
 */

public class RetrofitInstance {
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static Retrofit retrofit = null;
    private static NotificationAPI api = null;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(NotificationAPI.class);
    }

    public static NotificationAPI getApi() {
        return api;
    }
}
