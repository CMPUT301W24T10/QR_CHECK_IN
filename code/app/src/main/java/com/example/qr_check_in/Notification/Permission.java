package com.example.qr_check_in.Notification;
import android.os.Build;

import androidx.annotation.RequiresApi;

public abstract class Permission {
    private String[] permissions;

    private Permission(String... permissions) {
        this.permissions = permissions;
    }

    public static final Permission Camera = new Permission("CAMERA") {};
    public static final Permission MandatoryForFeatureOne = new Permission("WRITE_EXTERNAL_STORAGE", "ACCESS_FINE_LOCATION") {};
    public static final Permission Location = new Permission("ACCESS_FINE_LOCATION", "ACCESS_COARSE_LOCATION") {};
    public static final Permission Storage = new Permission("WRITE_EXTERNAL_STORAGE", "READ_EXTERNAL_STORAGE") {};

    @RequiresApi(33)
    public static final Permission Notification = new Permission("POST_NOTIFICATIONS") {};

    public static Permission from(String permission) {
        switch (permission) {
            case "ACCESS_FINE_LOCATION":
            case "ACCESS_COARSE_LOCATION":
                return Location;
            case "WRITE_EXTERNAL_STORAGE":
            case "READ_EXTERNAL_STORAGE":
                return Storage;
            case "POST_NOTIFICATIONS":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    return Notification;
                }
            case "CAMERA":
                return Camera;
            default:
                throw new IllegalArgumentException("Unknown permission: " + permission);
        }
    }
}
