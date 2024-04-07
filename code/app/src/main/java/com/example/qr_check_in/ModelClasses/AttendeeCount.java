package com.example.qr_check_in.ModelClasses;

public class AttendeeCount {

    private int NumberOfTimesLogin;
    private String deviceId;

    public AttendeeCount(String deviceId,int NumberOfTimesLogin ) {

        this.deviceId = deviceId;
        this.NumberOfTimesLogin = NumberOfTimesLogin;
    }



    public int getNumberOfTimesLogin() {
        return NumberOfTimesLogin;
    }

    public void setNumberOfTimesLogin(int NumberOfTimesLogin) {
        this.NumberOfTimesLogin = NumberOfTimesLogin;
    }

    public String getdeviceId() {
        return deviceId;
    }


    public void setAttendee(String attendee) {
        this.deviceId = deviceId;
    }
}


