package com.example.qr_check_in.ModelClasses;

public class AttendeeCount {
    private int NumberOfTimesLogin;
    private String attendee;

    public AttendeeCount(int NumberOfTimesLogin, String attendee) {
        this.NumberOfTimesLogin = NumberOfTimesLogin;
        this.attendee = attendee;
    }

    public int getNumberOfTimesLogin() {
        return NumberOfTimesLogin;
    }

    public void setNumberOfTimesLogin(int NumberOfTimesLogin) {
        this.NumberOfTimesLogin = NumberOfTimesLogin;
    }

    public String getAttendee() {
        return attendee;
    }


    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }
}


