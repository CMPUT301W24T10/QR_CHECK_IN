package com.example.qr_check_in;

import static com.example.qr_check_in.constants.COUNTNUMBEROFTIMELOGIN;
import static com.example.qr_check_in.constants.DEVICEID;
import static com.example.qr_check_in.constants.PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qr_check_in.ModelClasses.AttendeeCount;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {

    private static final String ATTENDEELIST = "LIST";
    private SharedPreferences prefs;

    public ArrayList<AttendeeCount> list = new  ArrayList();

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


    public void saveDeviceId(String deviceID) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(DEVICEID, deviceID);
        editor.apply();
    }

    public String getDeviceID(){
        return prefs.getString(DEVICEID, null);
    }

    public  void saveList(ArrayList<AttendeeCount> list) {
        android.content.SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(ATTENDEELIST, json);
        editor.apply();
    }


    public void loadList() {
        Gson gson = new Gson();
        String json = prefs.getString(ATTENDEELIST, null);
        Type type = new TypeToken<ArrayList<AttendeeCount>>() {}.getType();
        if (json == null) {
            list = new ArrayList<>();
        } else {
            list = gson.fromJson(json, type);
        }
    }







}
