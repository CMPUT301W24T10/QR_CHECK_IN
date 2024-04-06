package com.example.qr_check_in;

import static com.example.qr_check_in.constants.COUNTNUMBEROFTIMELOGIN;
import static com.example.qr_check_in.constants.PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private SharedPreferences prefs;

    public SharedPreference(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveCountAttendeeLogin(Integer count) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(COUNTNUMBEROFTIMELOGIN, count);
        editor.apply();
    }

    public Integer getCountAttendeeLogin(){
        return prefs.getInt(COUNTNUMBEROFTIMELOGIN, 0);
    }



}
