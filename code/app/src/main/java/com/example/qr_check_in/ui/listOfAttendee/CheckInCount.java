package com.example.qr_check_in.ui.listOfAttendee;

public class CheckInCount {
    private long checkInCount;
    private String attendeeName;

    public CheckInCount( long checkInCount, String attendeeName) {
        this.checkInCount = checkInCount;
        this.attendeeName = attendeeName;
    }

    public long getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(long checkInCount) {
        this.checkInCount = checkInCount;
    }
    public String getAttendeeName() {
        return attendeeName;
    }
    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }
}
